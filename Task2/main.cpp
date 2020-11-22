//
// Created by Joe on 22/11/2020.
//

#include <string>

#define PARAM_IN = 1;
#define PARAM_OUT = 2;
#define PARAM_THREADS = 3;

#define PARAM_NUMBER = 4;

int main(int argc, char *argv[]) {
    if (argc != PARAM_NUMBER) {
        exit(-1);
    }

    string input_file_name = argv[PARAM_IN];
    string output_file_name = argv[PARAM_OUT];
    int num_threads = argv[PARAM_THREADS];

    input_graph(input_file_name);

//    output_file(output_file_name);
}

void input_graph(char *file_name) {
    std::ifstream ifs;
    ifs.open(file_name, std::ifstream::in);


}