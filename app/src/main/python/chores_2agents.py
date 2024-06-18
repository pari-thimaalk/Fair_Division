import numpy as np

class Object:
    def __init__(self, a1_util, a2_util, id):
        self.a1_util = a1_util
        self.a2_util = a2_util
        self.id = id

    def __str__(self):
        return f"Object {self.id}: {self.a1_util}, {self.a2_util}"


def chores_2agents(num_items, val_array):
    utils = val_array.tolist()

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
        winner_allocation.append(loser_allocation.pop(0))

        # see if allocation is "fair" by ef1 now
        is_ef1 = IS_EF1(loser_allocation, winner_allocation)



    # making 2d array representing the allocation
    # return [winner_allocation, loser_allocation] (defines as objects)

    winner = [obj.id for obj in winner_allocation]
    loser = [obj.id for obj in loser_allocation]

    return [winner, loser] # (only gives object names)


def main(num_items, valuation_matrix):
    # agent1_util = [-4, -5, -6, -3, -7, -2, -8]  # utility function for agent 1
    # agent2_util = [-5, -3, -4, -6, -2, -7, -1]  # utility function for agent 2


    # input
    # valuation_matrix = np.array([agent1_util, agent2_util])


    # output (a 2d array of integers, each representing the nth object)
    a = chores_2agents(num_items, valuation_matrix)

    return a

