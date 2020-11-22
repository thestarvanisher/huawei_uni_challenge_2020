#include<iostream>
// Gets the file size
#include<sys/stat.h>
// Mapping of the file
#include<sys/mman.h>
#include <errno.h>
#include <string>
#include <stdarg.h>

// Opens the file
#include <fcntl.h>
// Closes the file
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
#include<mutex>
using namespace std;
mutex mx;
struct Node {
    int value;
    bool visited;
    bool level_visited;
};
unordered_map<int, mutex*> locks;
void traversePart(const char *data, unordered_map<int, vector<int>> *adj, int s, int e) {
    bool left_n = false, right_n = false;
    int l = 0, r = 0;
    for(int i = s; i < e; i++) {
        char c = data[i];
        if(c == '(') {
            left_n = true;
        }
        else if(c == ')') {
            left_n = false;
            right_n = false;
            
            if(adj->find(l) == adj->end()) {
                mx.lock();
                adj->insert(make_pair(l, vector<int>()));
                locks.emplace(l, new mutex);
                //locks[l] = t;
                mx.unlock();
                //adj[l] = new vector<int>();
            }
            
            //mx.lock();
            if(adj->find(r) == adj->end()) {
                mx.lock();
                adj->insert(make_pair(r, vector<int>()));
                locks.emplace(l, new mutex);
                mx.unlock();
            }
            //mx.unlock();
            //cout << "Not here1" << endl;
            //mx.lock();
            locks[l]->lock();
            adj->at(l).push_back(r);
            locks[l]->unlock();
            //mx.unlock();
            
            //cout << "Not here2" << endl;

            l = 0;
            r = 0;
        }
        else if(c == ',') {
            if(left_n == true) {
                right_n = true;
            }
        }
        else if(c > '0' && c < '9') {
            if(right_n == true) {
                r = r * 10 + (c - '0');
            }
            else {
                l = l * 10 + (c - '0');
            }
        }
    }
}


vector<int> getRanges(const char *data, int size, int threads_number) {
    vector<int> ranges;
    for(int i = 0; i < threads_number; i++) {
        ranges.push_back((int)((i*size)/threads_number));
    }

    for(int i = 1; i < ranges.size(); i++) {
        int temp = ranges[i];
        //char c = data[ranges[i]];
        //cout << "=> " << c << " " << temp << endl;
        while(data[temp] != '(') {
            temp--;
            if(temp < 0) {
                break;
            }
        }
        ranges[i] = temp;
    }
    /*for(int i = 0; i < ranges.size(); i++) {
        cout << ranges[i] << endl;
    }*/

    return ranges;
}

void inputGraph(const char* fileName, unordered_map<int, vector<int>> *adj, unordered_map<int, pair<bool, bool>> *visited) {

    // File descriptor
    int fileDesriptor;

    // The structure for the file size
    struct stat s;
    int status;
    size_t size;

    const char* mapped;

    fileDesriptor = open(fileName, O_RDONLY);

    if(fileDesriptor < 0) {
        cout << "There was an error opening the file!" << endl;
        exit(EXIT_FAILURE);
    }

    // Get the size of the file
    status = fstat(fileDesriptor, &s);

    if(status < 0) {
        cout << "Error obtaining the size of the file!" << endl;
        exit(EXIT_FAILURE);
    }

    // Size of the file in bytes
    size = s.st_size;

    mapped = (char*)mmap(0, size, PROT_READ, MAP_PRIVATE, fileDesriptor, 0);
    
    // Close the file
    close(fileDesriptor);

    // If mapping failes
    if(mapped == MAP_FAILED) {
        cout << "Mapping failed" << endl;
        exit(EXIT_FAILURE);
    }
    
    // Print the characters

    bool left_n = false, right_n = false;
    int l = 0, r = 0;

    for(int i = 0; i < size; i++) {
        char c = mapped[i];
        if(c == '(') {
            left_n = true;
        }
        else if(c == ')') {
            left_n = false;
            right_n = false;

            if(adj->find(l) == adj->end()) {
                adj->insert(make_pair(l, vector<int>()));
            }
            if(adj->find(r) == adj->end()) {
                adj->insert(make_pair(r, vector<int>()));
            }
            
            adj->at(l).push_back(r);

            l = 0;
            r = 0;
        }
        else if(c == ',') {
            if(left_n == true) {
                right_n = true;
            }
        }
        else if(c > '0' && c < '9') {
            if(right_n == true) {
                r = r * 10 + (c - '0');
            }
            else {
                l = l * 10 + (c - '0');
            }

        }
    }


    for(auto i: *adj) {
        visited->insert(make_pair(i.first, make_pair(false, false)));
    }
    
}


int main() {

    unordered_map<int, vector<int>> adj;
    unordered_map<int, pair<bool, bool>> visited;
    inputGraph("task2_data_for_competition.txt", &adj, &visited);

    /*cout << "ANS: " << endl; 
    for(auto i: adj) {
        cout << i.first << " -> ";
        for(int j = 0; j < i.second.size(); j++) {
            cout << i.second[j] << " ";
        }
        cout << endl;
    }*/


    return 0;
}