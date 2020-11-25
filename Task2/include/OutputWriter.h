//
// Created by Ivo and Joe on 23/11/2020.
//

#include <unordered_map>
#include <iostream>
#include <fstream>
#include <cmath>
#include <vector>
#include <iomanip>
#include "LinkedList.h"

using namespace std;

class OutputWriter {
private:
    char *fileName;

public:
    explicit OutputWriter(char *fileName);

    void writeFile(double *ans, unordered_map<int, LinkedList> *adj, int NUM_NODES);
};
