//
// Created by Joe on 23/11/2020.
//

#include <unordered_map>
#include <iostream>
#include <fstream>
#include <cmath>
#include <vector>
#include <iomanip>

using namespace std;

class OutputWriter {
private:
    char *fileName;

public:
    explicit OutputWriter(char *fileName);

    void writeFile(double *ans, unordered_map<int, vector<int>> *adj);
};
