//
// Created by Joe on 22/11/2020.
//

#include "InputReader.h"

InputReader::InputReader(char *fileName) {
    this->fileName = fileName;
}

void InputReader::readFile(unordered_map<int, LinkedList> *adj, unordered_map<int, pair<bool, bool>> *visited) {
    int fileDescriptor;

    struct stat s;
    int status;
    size_t size;

    const char *mapped;

    fileDescriptor = open(fileName, O_RDONLY);

    if (fileDescriptor < 0) {
        cout << "There was an error opening the file!   " << endl;
        exit(EXIT_FAILURE);
    }

    status = fstat(fileDescriptor, &s);

    if (status < 0) {
        cout << "Error obtaining the size of the file!" << endl;
        exit(EXIT_FAILURE);
    }

    size = s.st_size;

    mapped = (char *) mmap(0, size, PROT_READ, MAP_PRIVATE, fileDescriptor, 0);

    close(fileDescriptor);

    if (mapped == MAP_FAILED) {
        cout << "Mapping failed" << endl;
        exit(EXIT_FAILURE);
    }

    bool left_n = false, right_n = false;
    int l = 0, r = 0;

    int maxNode = 0;

    for (int i = 0; i < size; i++) {
        char c = mapped[i];
        if (c == '(') {
            left_n = true;
        } else if (c == ')') {
            left_n = false;
            right_n = false;

            /*if (adj->find(l) == adj->end()) {
                adj->insert(make_pair(l, vector<int>()));
            }

            if (adj->find(r) == adj->end()) {
                adj->insert(make_pair(r, vector<int>()));
            }
            */

            //adj->at(l).push_back(r);
            Node *p = new Node(r);
            (*adj)[l].addElement(p);

            //maxNode = max(max(l, maxNode), r);

            l = 0;
            r = 0;
        } else if (c == ',') {
            if (!left_n) continue;
            right_n = true;
        } else if (c >= '0' && c <= '9') {
            if (!right_n) {
                l = l * 10 + (c - '0');
            } else {
                r = r * 10 + (c - '0');
            }
        }
    }

    /*for (int i = 0; i < maxNode; i++) {
        if (adj->find(i) == adj->end()) {
            adj->insert(make_pair(i, vector<int>()));
        }
    }*/

    for(auto i: *adj) {
        //visited->insert(make_pair(i.first, make_pair(false, false)));
        (*visited)[i.first] = make_pair(false, false);
    }

    cout << "Graph Size: " << adj->size() << endl;
}
