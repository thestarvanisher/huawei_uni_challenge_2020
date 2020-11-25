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

#define PARAM_IN 1

#define PARAM_NUMBER 2

using namespace std;

int MAX_NODE;

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

double *doGraphShit(unordered_map<int, LinkedList> *adj) {
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

    for (auto i: *adj) {
        int currentNode = i.first;
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
            }
        }
    }

    double maxNum = 0;
    double minNum = numeric_limits<double>::max();

    for (int i = 0; i < maxNode; i++) {
        maxNum = max(ans[i], maxNum);
        minNum = min(ans[i], minNum);
    }

    for (int i = 0; i < maxNode; i++) {
        ans[i] = ((ans[i] - minNum) / (maxNum - minNum));
    }

    return ans;
}

void printAnswer(const double *ans, unordered_map<int, LinkedList> *adj) {
    /*for (int i = 0; i < adj.size(); i++) {
        if (!adj.at(i).empty()) {
            cout << i << " " << floor(ans[i] * 100) / 100 << endl;
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
    inputReader.readFile(&adj, &visited, &MAX_NODE);
    
    cout << adj.size() << " nodes." << endl;

    double *ans = doGraphShit(&adj);

    auto outputWriter = OutputWriter();
    outputWriter.writeFile(ans, &adj, MAX_NODE + 1);

    return 0;
}
