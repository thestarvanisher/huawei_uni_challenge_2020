//
// Created by Ivo and Joe on 22/11/2020.
//

#include <iostream>
#include <sys/stat.h>
#include <sys/mman.h>
#include <errno.h>
#include <string>
#include <stdarg.h>
#include <fcntl.h>
#include <algorithm>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <chrono>
#include <unordered_map>
#include <vector>
#include <utility>
#include <thread>
#include <exception>
#include "LinkedList.h"

using namespace std;

class InputReader {
private:
    char *fileName;

public:
    explicit InputReader(char *fileName);

    void readFile(unordered_map<int, LinkedList> *adj, unordered_map<int, pair<bool, bool>> *visited, int *MAX_NODE);
};