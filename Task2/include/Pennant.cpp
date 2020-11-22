//
// Created by Joe on 22/11/2020.
//

#include "Pennant.h"

Pennant::Pennant(int n) {
    this->node = n;
    this->left = NULL;
    this->right = NULL;
}

Pennant *Pennant::getLeft() {
    return this->left;
}

Pennant *Pennant::getRight() {
    return this->right;
}

void Pennant::setLeft(Pennant *p) {
    this->left = p;
}

void Pennant::setRight(Pennant *p) {
    this->right = p;
}

void Pennant::joinPennant(Pennant *p) {
    p->setRight(this->getLeft());
    this->setLeft(p);
}

int Pennant::getNodeValue() {
    return this->node;
}

Pennant *Pennant::splitPennant() {
    Pennant *p = this->left;
    this->left = p->getRight();
    p->setRight(NULL);
    return p;
}

void Pennant::printNode() {
    cout << this->node << endl;
}

void Pennant::printPennant() {
    vector<Pennant *> level;
    level.push_back(this);

    while (!level.empty()) {
        vector<Pennant *> next_level;
        while (!level.empty()) {
            if (level.at(0)->getLeft() != NULL) {
                next_level.push_back(level.at(0)->getLeft());
            }
            if (level.at(0)->getRight() != NULL) {
                next_level.push_back(level.at(0)->getRight());
            }
            cout << "" << level.at(0)->getNodeValue() << " ";
            level.erase(level.begin());
        }
        level = next_level;
        cout << endl;
    }
}
