# Welcome to Cloud Functions for Firebase for Python!
# To get started, simply uncomment the below code or create your own.
# Deploy with `firebase deploy`

import scipy as sp
from pip._internal import req
from scipy.optimize import linprog
from scipy.optimize import Bounds
import numpy as np
from scipy.sparse.csgraph import min_weight_full_bipartite_matching
import scipy.sparse as spsp
from firebase_functions import https_fn
from firebase_admin import initialize_app

app = initialize_app()
#
#

def mnw_ilp_scipy(num_agents, num_items, valuation_matrix):
    N = range(0, num_agents)
    M = range(0, num_items)
    v = valuation_matrix

    # [W1....WN, x11, x12, ...x1M, x21, x22...]
    c = -np.concatenate((np.ones(num_agents), np.zeros(num_agents * num_items)))

    A_ub = []
    b_u = []

    # Wi <= logk + (log(k+1)-log(k))[SIG(xig*vig)-k]
    # rearranged to Wi + (log(k)-log(k+1))SIG(xig*vig) <= logk - k(log(k+1)-log(k))
    for i in N:
        for k in range(1, 101, 2):
            new_A = [0] * len(c)
            new_A[i] = 1  # WI
            for g in M:
                new_A[num_agents + i * num_items + g] = (np.log(k) - np.log(k + 1)) * v[i][g]  # coefficient of xig:

            A_ub.append(new_A)
            b_u.append(np.log(k) - (k * (np.log(k + 1) - np.log(k))))

    # print("A_ub = ",A_ub)
    # print("b_u = ", b_u)

    # -sig(xig*vig) <= -1
    for i in N:
        new_A = [0] * len(c)
        for g in M:
            new_A[num_agents + i * num_items + g] = -v[i][g]
        A_ub.append(new_A)
        b_u.append(-1)

    # print("after adding -sig(xig*vig) <= -1")
    # print("A_ub = ",A_ub)
    # print("b_u = ", b_u)

    # sig(xig) = 1, for all goods
    A_eq = []
    b_eq = []

    for g in M:
        new_A = [0] * len(c)
        for i in N:
            new_A[num_agents + i * num_items + g] = 1
        A_eq.append(new_A)
        b_eq.append(1)

    # print("A_eq = ", A_eq)
    # print("b_eq = ", b_eq)

    # all Ws are continuous, x values must be integers between 0 and 1
    Integrality = np.concatenate((np.zeros(num_agents), np.ones(num_agents * num_items)))
    bounds = []
    for i in N:
        bounds.append((0, None))
    for i in range(num_agents * num_items):
        bounds.append((0, 1))

    res = linprog(c, A_ub=A_ub, b_ub=b_u, A_eq=A_eq, b_eq=b_eq, bounds=bounds, integrality=Integrality)

    # formatting res.x for our app
    alloc = []
    for i in N:
        cur_alloc = []
        for j in M:
            if round(res.x[num_agents + i * num_items + j], 3) == 1:
                cur_alloc.append(j)
        alloc.append(cur_alloc)
    return alloc

def max_weight_matching(num_agents,utilities):
    sparse_utilities = spsp.csr_matrix(utilities)
    sparse_utilities = -sparse_utilities

    matching = min_weight_full_bipartite_matching(sparse_utilities)

    agents = matching[0]
    goods = matching[1]

    alloc = [[] for i in range(num_agents)]
    # print(agents)
    # print(goods)
    for agent, good in zip(agents, goods):
        alloc[agent].append(good)
        # print(f"Resource {good} is allocated to Player {agent}")
    return alloc

@https_fn.on_call()
def mnw(req: https_fn.CallableRequest):
    n_agents = req.data['agents']
    n_items = req.data['items']
    val_matrix = req.data['values']
    if n_agents > n_items:
        return {"alloc": max_weight_matching(n_agents, val_matrix)}
    else:
        return {'alloc': mnw_ilp_scipy(n_agents, n_items, val_matrix)}


class Object:
    def __init__(self, a1_util, a2_util, id):
        self.a1_util = a1_util
        self.a2_util = a2_util
        self.id = id

    def __str__(self):
        return f"Object {self.id}: {self.a1_util}, {self.a2_util}"

@https_fn.on_call()
def chores_2agents(req: https_fn.CallableRequest):
    num_items = req.data['chores']
    utils = req.data['values']


    u_w = utils[0]  # denoting one agent as the winner and the other as loser (winner is agent 1)
    u_l = utils[1]

    winner_allocation = []
    loser_allocation = []

    # filling loser allocation because all chores are given to loser
    for i in range(num_items):
        loser_allocation.append(Object(u_w[i], u_l[i], i))

    # sorting the list
    def sortingKey(ob1):
        if ob1.a1_util == 0:  # prevetns division by zero
            return abs(ob1.a2_util) * 2  # this is so that it puts it at the start of the list
        return abs(ob1.a2_util) / abs(ob1.a1_util)

    loser_allocation.sort(reverse=True, key=sortingKey)  # loser is a2, winner is a1

    # defining ef1
    def IS_EF1(l_a, w_a):
        total_loser_util = 0  # what the loser thinks of their bundle
        most_disliked_chore = 0  # figuring out the chore with the least amount of desire

        for obj in l_a:
            total_loser_util += obj.a2_util
            most_disliked_chore = min(most_disliked_chore, obj.a2_util)

        loser_valuation_of_winner = sum(obj.a2_util for obj in w_a)  # what the loser thinks of the other person's bundle

        # this works because in the case where the loser can drop one of their chores and no longer be envious, it makes the most sense for them to drop the chore they like the least.
        return total_loser_util - most_disliked_chore >= loser_valuation_of_winner

    # switching over each item until EF1 is satisfied
    is_ef1 = False
    while is_ef1 == False:
        if(len(loser_allocation) == 0):
            break
        winner_allocation.append(loser_allocation.pop(0))

        # see if allocation is "fair" by ef1 now
        is_ef1 = IS_EF1(loser_allocation, winner_allocation)


    # making 2d array representing the allocation
    # return [winner_allocation, loser_allocation] (defines as objects)

    winner = [obj.id for obj in winner_allocation]
    loser = [obj.id for obj in loser_allocation]

    return {'alloc': [winner, loser]} # (only gives object names)


@https_fn.on_call()
def get_rr_allocation(req: https_fn.CallableRequest):
    num_agents = req.data['agents']
    num_items = req.data['chores']
    valuation = req.data['values']
    val_matrix = valuation
    items_allocated = 0
    alloc = [[] for i in range(num_agents)]
    while items_allocated < num_items:
        print("items allocated",items_allocated)
        for i in range(num_agents):
            #get index of item to be allocated
            index = val_matrix[i].index(min(val_matrix[i]))
            print("agent turn",i,"index",index)
            alloc[i].append(index)
            #remove that item from everyones valuation matrix, by setting to 200
            for j in range(num_agents):
                val_matrix[j][index] = 11*num_items
                # print("valuation",val_matrix)
            items_allocated += 1
            #break if all items have been allocated, else go to next agent's turn
            if items_allocated == num_items:
                break
    return {'alloc': alloc}
