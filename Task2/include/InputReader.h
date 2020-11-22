//
// Created by Joe on 22/11/2020.
//

#include<iostream>
#include<sys/stat.h>
#include<sys/mman.h>
#include <errno.h>
#include <string>
#include <stdarg.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include<chrono>
#include<unordered_map>
#include<vector>
#include <utility>
#include<thread>
#include <exception>

using namespace std;

class InputReader {
private:
    const char *fileName;

public:
    InputReader(char *fileName);

    void readFile(unordered_map<int, vector<int>> *adj, unordered_map<int, pair<bool, bool>> *visited);
};
