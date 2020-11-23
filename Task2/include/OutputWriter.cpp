//
// Created by Joe on 23/11/2020.
//

#include "OutputWriter.h"

OutputWriter::OutputWriter(char *fileName) {
    this->fileName = fileName;
}

void OutputWriter::writeFile(double *ans, unordered_map<int, vector<int>> *adj) {
    ofstream answerFile;

    answerFile.open(fileName, ios::trunc);
    answerFile << std::setprecision(2) << std::fixed << "[";

    bool first = false;

    for (int i = 0; i < adj->size(); i++) {
        if (!adj->at(i).empty()) {
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
