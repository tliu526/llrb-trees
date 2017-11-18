This is a readme outlining the experimental setup of the tests ran.

### Add, contains, remove:
1. A vector of integers data is initialized with n values, ascending order.
2. data is shuffled, using Collections.shuffle()
3. a structs vector of orderedStructures is initialized, both data and structs passed to runTests()
4. runTests calls populate() on all structures in struct
5. populate() adds data to all structures, using System.currentTimeMillis() for time
6. after all structures are populated, data is shuffled, and contains is called on each structure,
again timed using Sys.currentTimeMillis()
7. data is shuffled again, and the same process is run for delete on each structure

Note that data is shuffled after all add/contains method calls are completed on each data structure,
ensuring that each data structure receives the same shuffled data as input.

### Height:

Ugly function because OrderedStructure doesn't contain a height() method.

1. A vector of integers data is passed, and each tree has data added to it
2. the height of each tree is then stored in a corresponding array of heights
3. data is shuffled, and the trees are re-initialized with data in the new shuffled order
4. Height is then recorded, and the process is continued n times
5. The average height of each tree is the output
