# Huawei University Challenge 2020 UK
The Huawei University Challenge 2020 was an interestesting competition where we had to develop Hight Performance Computing (HPC) algorithms. There were two tasks in the competition, the first one worth 35% of the final score and the second one worth 65% of the final score.

**Our team place:** 11<sup>th</sup> 

## Task 1
For this task we had to write a multithreaded BFS algorithm. We were given a list of edges and had to output the correct traversal order. 

We wrote a level synchronous algorithm that used a tree data structure to represent the nodes in each level. We wrote it in Java. Because of the slower I/O speed of Java, the final algorithm ran for **1.498** seconds on a sparse graph with ~25k nodes.

The full details about the task could be found in the `Guidebook-Task1.pdf` file, located in the `Task1` folder. There, you could also find the implementation of the algorithm.

## Task 2
The second task asked us to write a multithreaded algorithm to compute the normalised node betweeness centrality which is a measurement of centrality in a graph. You can read about that [here](https://en.wikipedia.org/wiki/Betweenness_centrality).

We implemented the Brandes' algorithm for betweeness centrality in C++. For the multithreaded part we split all nodes between the threads and compute the measurement separately. At the end, the computed results are added together. The threads don't have to be synchronised until the last step which speeds up the program, since no mutex locks were needed to be used for that part. For the I/O part we used memory-mapped I/O which helped speed up reading the file. At the end the algorithm ran for **1.197** seconds on a graph with ~13k nodes. 

The full details about the task could be found in the `Guidebook-Task2.pdf` file, located in the `Task2` folder. There, you could also find the implementation of the algorithm. The most efficient implementation could be found in the `parallel_2.cpp` file, located in the `src` folder in `Task2`.

### Possible imporvements
During the competitions couple of improvements were tried for Task 2, however due to the limited time, they weren't successfully implemented. Some of these improvements include:
* Memoization - using a hash table to store computed values which are added to the `delta` array. The hash table must use three keys and store one value under them. We tried using `map` with `pair<int, pair<int, double>>` as a key, however that proved to be slow. If a fast custom map is implemented that would allow us to get the computed value instead of computing it again, which theoretically would speed up the program.
* Making the BFS level synchronous inside of the thread and using a thread pool - if that is done properly, threads which are not used could be used inside the BFS (if there is a sufficiently large number of nodes in the next level).

## The testing server
Detailed information about the testing server could be found in the `Guidebook-Task1.pdf`, located in the `Task1` folder.
Some of the server specs are as follows:
|Component|Details|
|---------|-------|
|Name|Kunpeng 920 4826|
|CPU core count|96|
|CPU base frequency|2600MHz|
|CPU Architecture|ARM|
|RAM|256GB|
|RAM type|DDR4 2933|
|MAX memory bandwidth|331.818 GB/s|
|L3 cache|48MiB|
