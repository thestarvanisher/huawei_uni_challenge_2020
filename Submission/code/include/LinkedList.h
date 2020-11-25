/*
    Created by Ivo
*/
#ifndef LINKED_LIST_H
#define LINKED_LIST_H


#include<iostream>
using namespace std;

class Node {
    public:
        int value;
        Node *next;

        Node(int value) {
            this->value = value;
            this->next = NULL;
        }
};

class LinkedList {
    private:
        Node *begin;
        Node *end;
        int sv;
    
    public:
        LinkedList() {
            this->begin = NULL;
            this->end = NULL;
        }

        void addElement(Node *element) {
            if(this->begin == NULL) {
                this->sv = (*element).value;
                this->begin = element;
                this->end = element;
                return;
            }
            this->end->next = element;
            this->end = element;
        }
        Node* beginIter() {
            return this->begin;
        }

        Node* endIter() {
            return this->end;
        }

        void appendList(LinkedList *ls) {
            if(this->begin == NULL) {
                this->begin = ls->beginIter();
                this->end = ls->endIter();
            }
            else {
                this->end->next = ls->beginIter();
                this->end = ls->endIter();
            }
        }

        bool isEmpty() {
            return this->begin == NULL && this->end == NULL;
        }
};

#endif