import numpy as np

def generate_owner_vectors(num_agents, num_items):
    """
    An allocation is an (num_items)-length number in (num_agents)-ary, with
    j^th entry denoting owner of j^th item. Get owner vectors by represeting
    each allocation as an integer in 0 to (num_agents**num_items) - 1.
    """
    max_alloc_num = num_agents ** num_items
    for alloc_num in range(max_alloc_num):
        owner_vector = []
        for j in range(num_items):
            owner_vector.append(alloc_num % num_agents)
            alloc_num = alloc_num // num_agents
        yield owner_vector


def generate_all_allocations(num_agents, num_items):
    """Get allocations in tuple-of-bundles representation from owner vectors"""
    for owner_vector in generate_owner_vectors(num_agents, num_items):
        alloc = [[] for i in range(num_agents)]
        for j in range(num_items):
            alloc[owner_vector[j]].append(j)
        yield alloc


def get_max_nash_welfare(num_agents, num_items, valuation_matrix):
    """Return the allocation with the maximum Nash welfare"""
    opt_nash_welfare = -1
    opt_alloc = []

    for alloc in generate_all_allocations(num_agents, num_items):
        values = [sum([valuation_matrix[i][j] for j in alloc[i]]) for i in range(num_agents)]
        nash_welfare = np.prod(values)

        if nash_welfare > opt_nash_welfare:
            opt_nash_welfare = nash_welfare
            opt_alloc = alloc

    return opt_alloc, opt_nash_welfare


def main(num_agents, num_items, valuation_matrix):
#     num_agents = 2
#     num_items = 3
#     valuation_matrix = np.array(
#         [[2,4,1],
#          [10,2,3]])

    mnw_alloc, mnw = get_max_nash_welfare(num_agents, num_items, valuation_matrix)

    print("MNW allocation: ", mnw_alloc)
    print("MNW: ", mnw)
    return mnw_alloc

# if __name__ == '__main__':
#     main()
