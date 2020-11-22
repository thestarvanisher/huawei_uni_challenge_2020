#include<iostream>
#include<vector>

using namespace std;

class Pennant {
private:
    int node;
    Pennant *left;
    Pennant *right;

public:
    Pennant(int n);
s
    Pennant *getLeft();

    Pennant *getRight();

    void setLeft(Pennant *p);

    void setRight(Pennant *p);

    void joinPennant(Pennant *p);

    int getNodeValue();

    Pennant *splitPennant();

    void printNode();

    void printPennant();
};