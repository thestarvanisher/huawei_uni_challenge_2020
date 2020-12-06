//
// Created by Ivo and Joe on 22/11/2020.
//

#include <queue>
#include <stack>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <limits>
#include "../include/OutputWriter.h"
#include "../include/Bag.h"
#include "../include/InputReader.h"
#include <thread>
#include <mutex>
#include <iostream>
#include <chrono>
#define PARAM_IN 1

#define PARAM_NUMBER 2

#define NUM_THREADS 96

using namespace std;

int MAX_NODE;
double *ANS;
mutex *mtx;
mutex gg;
vector<double*> aa;

void printGraph(unordered_map<int, LinkedList> *adj) {
    for (auto i: *adj) {
        cout << i.first << " -> ";
        Node *itr = (*adj)[i.first].beginIter();
        while (itr != nullptr) {
            cout << (*itr).value << " ";
        }
        cout << endl;
        itr = itr->next;
    }
}

/*
    Run the Brandes' algorithm on the set of nodes
*/
void doGraphOperation(unordered_map<int, LinkedList> *adj, vector<int> *ranges, int starting, int ending) {
    vector<int> *P;
    int *sigma;
    int *d;
    double *delta;
    double *ans;

    stack<int> S;
    queue<int> Q;
    int maxNode = MAX_NODE;
    maxNode += 1;

    d = new int[maxNode];
    delta = new double[maxNode];
    sigma = new int[maxNode];
    P = new vector<int>[maxNode];
    ans = new double[maxNode];

    for(int i = starting; i < ending; i++) {

        int currentNode = (*ranges)[i];
        memset(sigma, 0, sizeof(int) * maxNode);
        memset(d, -1, sizeof(int) * maxNode);
        memset(delta, 0, sizeof(double) * maxNode);
        
        for (int j = 0; j < maxNode; j++) {
            P[j].clear();
        }

        sigma[currentNode] = 1;
        d[currentNode] = 0;
        Q.push(currentNode);

        while (!Q.empty()) {
            int v = Q.front();
            Q.pop();

            S.push(v);

            Node *itr = (*adj)[v].beginIter();
            while (itr != nullptr) {
                if (d[(*itr).value] < 0) {
                    Q.push((*itr).value);
                    d[(*itr).value] = d[v] + 1;
                }

                if (d[(*itr).value] == d[v] + 1) {
                    sigma[(*itr).value] += sigma[v];
                    P[(*itr).value].push_back(v);
                }
                itr = itr->next;
            }
        }

        while (!S.empty()) {
            int w = S.top();
            S.pop();

            for (const int &vv: P[w]) {
                double toAdd = (double) ((double) sigma[vv] / (double) sigma[w]) * (double) (1 + delta[w]);
                delta[vv] += toAdd;
            }

            if (w != currentNode) {
                ans[w] += delta[w];
            }
        }
    }

    {
        lock_guard<mutex> lock(gg);
        aa.push_back(ans);
    }
}

void printAnswer(const double *ans, unordered_map<int, LinkedList> *adj) {
    for (auto i: *adj) {
        if (!(*adj)[i.first].isEmpty()) {
            cout << i.first << " " << floor(ans[i.first] * 100) / 100 << endl;
        }
    }
}

/*
    Run the algorithm
*/
int main(int argc, char *argv[]) {
    ios_base::sync_with_stdio(false);

    if (argc != PARAM_NUMBER) {
        exit(-1);
    }

    char *inputFileName = argv[PARAM_IN];

    // Create the adjacency list and read the file
    unordered_map<int, LinkedList> adj;
    unordered_map<int, pair<bool, bool>> visited;
    auto inputReader = InputReader(inputFileName);
    inputReader.readFile(&adj, &MAX_NODE);

    ANS = new double[MAX_NODE + 1];
    mtx = new mutex[MAX_NODE + 1];

    // Create the ranges
    vector<int>ranges;

    for(auto i: adj) {
        ranges.push_back(i.first);
    }

    vector<thread> threads;

    // Create the threads and run them. Give them the adjacency list, the nodes, start and finish
    // node for the range
    for(int i = 0; i < (NUM_THREADS - 1); i++) {
        int s = (int)((adj.size()*i)/NUM_THREADS);
        int e = (int)((adj.size()*(i + 1))/NUM_THREADS);

        thread t(doGraphOperation, &adj, &ranges, s, e);
        threads.push_back(move(t));
    }

    int ss = (int)((adj.size()*(NUM_THREADS - 1))/NUM_THREADS);
    thread g(doGraphOperation, &adj, &ranges, ss, adj.size());
    threads.push_back(move(g));

    // Wait for the threads to finish
    for(int i = 0; i <threads.size(); i++) {
        threads[i].join();
    }

    // Combine the results from all threads
    for(int i = 0; i < aa.size(); i++) {
        for(int j = 0; j < MAX_NODE + 1; j++) { 
            ANS[j] += aa[i][j];
        }
    }
    
    double maxNum = 0;
    double minNum = numeric_limits<double>::max();

    for (int i = 0; i < MAX_NODE + 1; i++) {
        maxNum = max(ANS[i], maxNum);
        minNum = min(ANS[i], minNum);
    }

    // Normalise the answers
    if(minNum == maxNum) {
        for (int i = 0; i < MAX_NODE + 1; i++) {
            ANS[i] = (double)0.0;
        }   
    }
    else {
        for (int i = 0; i < MAX_NODE + 1; i++) {
            ANS[i] = ((ANS[i] - minNum) / (maxNum - minNum));
        }   
    }
    
    // Save the result to a file
    auto outputWriter = OutputWriter();
    outputWriter.writeFile(ANS, &adj, MAX_NODE + 1);
    return 0;
}

