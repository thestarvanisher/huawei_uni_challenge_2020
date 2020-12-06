//
// Created by Ivo and Joe on 23/11/2020.
//

#include "OutputWriter.h"

OutputWriter::OutputWriter() {}

void OutputWriter::writeFile(double *ans, unordered_map<int, LinkedList> *adj, int NUM_NODES) {
    ofstream answerFile;

    answerFile.open("ans.txt", ios::trunc);
    answerFile << std::setprecision(2) << std::fixed << "[";

    bool first = false;

    for (int i = 0; i < NUM_NODES; i++) {
        if (adj->find(i) != adj->end()) {
            if (first) {
                answerFile << ",";
            }
            first = true;

            answerFile << "(" << i << "," << ans[i] << ")";
        }
    }

    answerFile << "]";
    answerFile.close();
}
