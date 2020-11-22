#include <iostream>
#include <thread>
#include <ctime>
#include <vector>
#include <stack>
#include <fstream>
#include <unordered_set>
#include <map>
#include <mutex>
#include <chrono>
#include <cstring>
#include <queue>
#include <algorithm>

#define PARAM_NUMBER 4
#define PARAM_IN 1
#define PARAM_OUT 2
#define PARAM_THREADS 3

using namespace std;


std::mutex *t;

class Node {
public:
    int mNumber;
    int mProjected;
    vector<Node *> mNeighbors;
    double BC;

    Node(int number, int projected) : mNumber(number), mProjected(projected), BC(0) {};
};

/* Kind of ThreadPool, the only task for this class
    is giving work for objects, that want work
*/
class NodePool {
    int counter;
    std::mutex mMutex;
public:

    explicit NodePool(int size) {
        counter = size - 1;
    }

    int getNode() {
        std::lock_guard <std::mutex> lock(mMutex);
        int x = counter--;
        return x;
    }
};

// Unit of computation
class NodeThread {

    vector<int> *P; // Predecessors
    int *sigma;     // Numbers of shortest paths
    int *d;         // Distance
    double *delta;

    stack<int> S;
    queue<int> Q;

    NodePool *mPool;
    vector <Node> *mGraph;
    size_t mLen;

public:
    NodeThread(NodePool *pool, vector <Node> *graph, int id) {
        mLen = graph->size();
        d = new int[mLen];
        delta = new double[mLen];
        sigma = new int[mLen];
        P = new vector<int>[mLen];

        clear_computations();
        mPool = pool;
        mGraph = graph;
    }

    void operator()() {
        // Operator overloading allows to execute class object as thread
        int number;
        while ((number = mPool->getNode()) >= 0) {// NodePool gives as work to do
            clear_computations();
            sigma[number] = 1;
            d[number] = 1;
            Q.push(number);
            // Nothing interesting to comment, everything like in algorithm from task
            while (!Q.empty()) {

                int v = Q.front();
                Q.pop();

                S.emplace(v);

                for (int i = 0; i < mGraph->at(v).mNeighbors.size(); ++i) {
                    int w = mGraph->at(v).mNeighbors.at(i)->mProjected;
                    if (d[w] < 0) {
                        Q.push(w);
                        d[w] = d[v] + 1;
                    }

                    if (d[w] == d[v] + 1) {
                        sigma[w] += sigma[v];
                        P[w].push_back(v);
                    }
                }
            }
            while (!S.empty()) {
                int w = S.top();
                S.pop();
                for (int i = 0; i < P[w].size(); ++i) {
                    int v = P[w].at(i);
                    delta[v] += (double) ((double) sigma[v] /
                                          (double) sigma[w]) * (double) (1 + delta[w]);
                }
                if (w != number) {
                    // Update BC value as atomic operation (for float we need own lock)
//                    std::lock_guard <std::mutex> lock(t[w]);
                    mGraph->at(w).BC += delta[w];
                }
            }
        }
    }

    void clear_computations() {
        memset(sigma, 0, sizeof(int) * mLen);
        memset(d, -1, sizeof(int) * mLen);
        memset(delta, 0, sizeof(double) * mLen);
        for (int i = 0; i < mLen; ++i)
            P[i].clear();
    }
};


// Custom comparator, establishing order on nodes
struct cmpNode {
    bool operator()(const Node &a, const Node &b) const {
        return a.mNumber < b.mNumber;
    }
} cmp;


void create_graph_from_input(char *file_name, vector <Node> &graph,
                             map<int, int> &projection, vector <vector<int>> &neighbours) {
    std::ifstream ifs;
    ifs.open(file_name, std::ifstream::in);
    int x;
    int which = 0;
    int parent = 0;
    int number = 0;
    while (ifs >> x) {
        if (projection.find(x) == projection.end()) {
            projection.insert(std::pair<int, int>(x, number++));
            graph.emplace_back(x, number - 1);// Add  node to graph
            neighbours.emplace_back();
        }

        int found = projection.find(x)->second;
        // Add connection between nodes
        if (which == 1)// Second in pair
        {
            neighbours.at(parent).push_back(found);
            //first->mNeighbors.push_back(&graph.at(found));
            which = 0;
        } else {
            parent = found;
            which++;
        }
    }
    ifs.close();
}

void create_mutexes(vector <Node> &graph, vector <vector<int>> &neighbours) {
    // Mutexes for BC updates
    size_t graph_size = graph.size();
    t = new std::mutex[graph_size];
    for (int i = 0; i < graph_size; ++i)
        for (int j = 0; j < neighbours.at(i).size(); j++)
            graph.at(i).mNeighbors.push_back(&graph.at(neighbours.at(i).at(j)));
}

void create_threads(int number_of_threads, vector <Node> &graph, NodePool &boss, vector <thread> &vec,
                    vector <NodeThread> &threads) {
    for (int i = 0; i < number_of_threads; ++i) {
        threads.emplace_back(&boss, &graph, i);
        vec.emplace_back(threads.at(i));
    }

    for (int i = 0; i < number_of_threads; ++i)
        vec.at(i).join();
}

void write_output(char *file_name, vector <Node> &graph) {
    std::ofstream ofs;
    ofs.open(file_name, std::ifstream::trunc);

    for (auto &i : graph)
        if (!i.mNeighbors.empty())
            ofs << i.mNumber << " " << i.BC << endl;
    ofs.close();
}


int main(int argc, char *argv[]) {
    if (argc != PARAM_NUMBER)
        exit(-1);

    vector <Node> graph;
    map<int, int> projection;
    vector <vector<int>> neighbours;

    create_graph_from_input(argv[PARAM_IN], graph, projection, neighbours);

    create_mutexes(graph, neighbours);

    NodePool boss(graph.size());

    vector <thread> vec;
    vector <NodeThread> threads;

    create_threads(atoi(argv[PARAM_THREADS]), graph, boss, vec, threads);

    // Computation finished
    std::sort(graph.begin(), graph.begin() + graph.size(), cmp);

    char *test = argv[PARAM_OUT];

    cout << test << endl;

    write_output(argv[PARAM_OUT], graph);
}