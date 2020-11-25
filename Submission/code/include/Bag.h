#include <iostream>
#include <vector>
#include "Pennant.h"

using namespace std;

class Bag {
private:
    int size;
    vector<Pennant *> *items;

public:
    Bag();

    Bag(int size);

    void addPennant(int position, Pennant *p);

    vector<Pennant *> *getItems();

    void addElement(Pennant *p);

    Bag *splitBag();

    void printBag();
};