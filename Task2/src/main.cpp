//
// Created by Joe on 22/11/2020.
//

#include "../include/Bag.h"
#include "../include/InputReader.h"

#define PARAM_IN 1
#define PARAM_OUT 2
#define PARAM_THREADS 3

#define PARAM_NUMBER 4

using namespace std;

void printGraph(const unordered_map<int, vector<int>> &adj) {
    for (const auto &i: adj) {
        cout << i.first << " -> ";
        for (int j : i.second) {
            cout << j << " ";
        }
        cout << endl;
    }
}

int main(int argc, char *argv[]) {
    if (argc != PARAM_NUMBER) {
        exit(-1);
    }

    char *inputFileName = argv[PARAM_IN];
    char *outputFileName = argv[PARAM_OUT];
    int numThreads = atoi(argv[PARAM_THREADS]); // NOLINT(cert-err34-c)

    unordered_map<int, vector<int>> adj;
    unordered_map<int, pair<bool, bool>> visited;

    auto inputReader = InputReader(inputFileName);

    inputReader.readFile(&adj, &visited);

//    output_file(output_file_na    me);

    printGraph(adj);

    return 0;
}