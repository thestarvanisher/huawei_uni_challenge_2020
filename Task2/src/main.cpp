//
// Created by Ivo and Joe on 22/11/2020.
//

#include <queue>
#include <stack>
#include <algorithm>
#include <cmath>
#include <cstring>
#include "../include/OutputWriter.h"
#include "../include/Bag.h"
#include "../include/InputReader.h"

#define PARAM_IN 1
#define PARAM_OUT 2
#define PARAM_THREADS 3

#define PARAM_NUMBER 4

using namespace std;

void printGraph(unordered_map<int, LinkedList> *adj) {
    /*for (const auto &i: adj) {
        cout << i.first << " -> ";
        for (int j : i.second) {
            cout << j << " ";
        }
        cout << endl;
    }*/
    for(auto i: *adj) {
        cout << i.first << " -> ";
        Node *itr = (*adj)[i.first].beginIter();
        while(itr != NULL) {
            cout << (*itr).value << " ";
        }
        cout << endl;
        itr = itr->next;
    }
}

double *doGraphShit(unordered_map<int, LinkedList> *adj) {
    vector<int> *P;
    int *sigma;
    int *d;
    double *delta;
    double *ans;

    stack<int> S;
    queue<int> Q;

    int maxNode = adj->size();

    d = new int[maxNode];
    delta = new double[maxNode];
    sigma = new int[maxNode];
    P = new vector<int>[maxNode];
    ans = new double[maxNode];

    cout << "Starting" << endl;

    for (int i = maxNode - 1; i > -1; i--) {
        int currentNode = i;

        memset(sigma, 0, sizeof(int) * maxNode);
        memset(d, -1, sizeof(int) * maxNode);
        memset(delta, 0, sizeof(double) * maxNode);
        for (int j = 0; j < maxNode; j++) {
            P[j].clear();
        }

        sigma[currentNode] = 1;
        d[currentNode] = 1;
        Q.push(currentNode);

        while (!Q.empty()) {
            int v = Q.front();
            Q.pop();

            S.emplace(v);

            Node *itr = (*adj)[v].beginIter();
            while(itr != NULL) {
                if(d[(*itr).value] < 0) {
                    Q.push((*itr).value);
                    d[(*itr).value] = d[(*itr).value] + 1;
                }

                if(d[(*itr).value] == d[v] + 1) {
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

            for (const int &v: P[w]) {
                double toAdd = (double) ((double) sigma[v] / (double) sigma[w]) * (double) (1 + delta[w]);

                delta[v] += toAdd;
            }

            if (w != currentNode) {
                ans[w] += delta[w];
            }
        }
    }

    double maxNum = 0;

    for (int i = 0; i < maxNode; i++) {
        maxNum = max(ans[i], maxNum);
    }

    for (int i = 0; i < maxNode; i++) {
        ans[i] /= maxNum;
    }

    return ans;
}

void printAnswer(const double *ans, unordered_map<int, LinkedList> *adj) {
    /*for (int i = 0; i < adj.size(); i++) {
        if (!adj.at(i).empty()) {
            cout << i << " " << floor(ans[i] * 100) / 100 << endl;
        }
    }*/
    for(auto i: *adj) {
        if((*adj)[i.first].isEmpty() == false) {
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
    char *outputFileName = argv[PARAM_OUT];
    int numThreads = atoi(argv[PARAM_THREADS]); // NOLINT(cert-err34-c)

    unordered_map<int, LinkedList> adj;
    unordered_map<int, pair<bool, bool>> visited;
    auto inputReader = InputReader(inputFileName);
    inputReader.readFile(&adj, &visited);

//    printGraph(&adj);

    double *ans = doGraphShit(&adj);

//    printAnswer(ans, &adj);

    auto outputWriter = OutputWriter(outputFileName);
    outputWriter.writeFile(ans, &adj);

    return 0;
}
