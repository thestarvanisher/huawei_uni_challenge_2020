//
// Created by Joe on 22/11/2020.
//

#include <string>

#define PARAM_IN 1
#define PARAM_OUT 2
#define PARAM_THREADS 3

#define PARAM_NUMBER 4

using namespace std;

void inputGraph(const string &fileName);

void inputGraph(const string &fileName) {
    ifstream ifs;
    ifs.open(fileName, ifstream::in);
}

int main(int argc, char *argv[]) {
    if (argc != PARAM_NUMBER) {
        exit(-1);
    }

    string inputFileName = string(argv[PARAM_IN]);
    string outputFileName = string(argv[PARAM_OUT]);
    int numThreads = atoi(argv[PARAM_THREADS]); // NOLINT(cert-err34-c)

    inputGraph(inputFileName);

//    output_file(output_file_name);
}