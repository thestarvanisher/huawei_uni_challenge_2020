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

#define NUM_THREADS 8

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

void doGraphShit(unordered_map<int, LinkedList> *adj, vector<int> *ranges, int starting, int ending) {
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

    //for (auto i: *adj) {
    //cout<< ">> "<<starting << " "<<ending<<endl;
    for(int i = starting; i < ending; i++) {

        int currentNode = (*ranges)[i];
        memset(sigma, 0, sizeof(int) * maxNode);
        memset(d, -1, sizeof(int) * maxNode);
        memset(delta, 0, sizeof(double) * maxNode);
        //auto t11 = chrono::high_resolution_clock::now();
        for (int j = 0; j < maxNode; j++) {
            P[j].clear();
        }
        //auto t12 = chrono::high_resolution_clock::now();
        //cout << "Clearing takes: " << chrono::duration_cast<chrono::milliseconds>(t12 - t11).count() << endl;

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

            /*for (const int &w: adj->at(v)) {
                if (d[w] < 0) {
                    Q.push(w);
                    d[w] = d[v] + 1;
                }

                if (d[w] == d[v] + 1) {
                    sigma[w] += sigma[v];
                    P[w].push_back(v);
                }
            }*/
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
                //lock_guard<mutex> lock(mtx[w]);
                //ANS[w] += delta[w];
            }
        }
    }

    /*double maxNum = 0;
    double minNum = numeric_limits<double>::max();

    for (int i = 0; i < maxNode; i++) {
        maxNum = max(ans[i], maxNum);
        minNum = min(ans[i], minNum);
    }

    for (int i = 0; i < maxNode; i++) {
        ans[i] = ((ans[i] - minNum) / (maxNum - minNum));
    }

    return ans;
    */

    {
        lock_guard<mutex> lock(gg);
        aa.push_back(ans);
    }
}

void printAnswer(const double *ans, unordered_map<int, LinkedList> *adj) {
    /*for (int i = 0; i < adj.size(); i++) {
        if (!adj.at(i).empty()) {
            cout << i << " " << floor(ans
            [i] * 100) / 100 << endl;
        }
    }*/
    for (auto i: *adj) {
        if (!(*adj)[i.first].isEmpty()) {
            cout << i.first << " " << floor(ans[i.first] * 100) / 100 << endl;
        }
    }
}

int main(int argc, char *argv[]) {
    ios_base::sync_with_stdio(false);

    if (argc != PARAM_NUMBER) {
        exit(-1);
    }

    char *inputFileName = argv[PARAM_IN];

    unordered_map<int, LinkedList> adj;
    unordered_map<int, pair<bool, bool>> visited;
    auto inputReader = InputReader(inputFileName);
    inputReader.readFile(&adj, &MAX_NODE);

    //cout<<MAX_NODE<<endl;
    //cout << adj.size() << " nodes." << endl;
    ANS = new double[MAX_NODE + 1];
    mtx = new mutex[MAX_NODE + 1];
    vector<int>ranges;
    //int ranges[adj.size()];
    //int j = 0;
    for(auto i: adj) {
        //ranges[j] = i.first;
        ranges.push_back(i.first);
        //j++;
    }
    //auto st = chrono::high_resolution_clock::now();
    vector<thread> threads;
    // for(int i = 0; i < MAX_NODE + 1; i++) {
    //     if(adj.find(i) != adj.end()) {
    //         thread t (doGraphShit, &adj, i);
    //         threads.push_back(move(t));
    //     }
    // }
    //auto st = chrono::high_resolution_clock::now();

    //vector<double*> answers;

    for(int i = 0; i < (NUM_THREADS - 1); i++) {
        int s = (int)((adj.size()*i)/NUM_THREADS);
        int e = (int)((adj.size()*(i + 1))/NUM_THREADS);
        //double *answer;
        //answers.push_back(answer);
        thread t(doGraphShit, &adj, &ranges, s, e);
        threads.push_back(move(t));
    }
    //double *answer2;
    //answers.push_back(answer2);
    int ss = (int)((adj.size()*(NUM_THREADS - 1))/NUM_THREADS);
    thread g(doGraphShit, &adj, &ranges, ss, adj.size());
    threads.push_back(move(g));

    


    // for(auto i: adj) {
    //     thread t (doGraphShit, ref(adj), (int)i.first);
    //     threads.push_back(move(t));
    // }
    for(int i = 0; i <threads.size(); i++) {
        threads[i].join();
    }
    for(int i = 0; i < aa.size(); i++) {
        for(int j = 0; j < MAX_NODE + 1; j++) {
            //cout<<"ans: "<<i<<" " <<j<< " "<< aa[i][j]<<endl; 
            ANS[j] += aa[i][j];
        }
    }
    //auto en = chrono::high_resolution_clock::now();
    //cout<<"TIME: " << chrono::duration_cast<chrono::milliseconds>(en - st).count() << endl;
    
    double maxNum = 0;
    double minNum = numeric_limits<double>::max();

    for (int i = 0; i < MAX_NODE + 1; i++) {
        maxNum = max(ANS[i], maxNum);
        minNum = min(ANS[i], minNum);
    }

    for (int i = 0; i < MAX_NODE + 1; i++) {
        ANS[i] = ((ANS[i] - minNum) / (maxNum - minNum));
    }
    
    //cout<<"DONE"<<endl;

    //double *ans = doGraphShit(&adj);

    //auto st = chrono::high_resolution_clock::now();
    auto outputWriter = OutputWriter();
    outputWriter.writeFile(ANS, &adj, MAX_NODE + 1);
    //auto en = chrono::high_resolution_clock::now();
    //cout<<"TIME: " << chrono::duration_cast<chrono::milliseconds>(en - st).count() << endl;

    return 0;
}
