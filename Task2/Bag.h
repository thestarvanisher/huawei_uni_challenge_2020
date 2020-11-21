#include<iostream>
#include<vector>
#include<map>
#include "Pennant.h"
using namespace std; 

class Bag {
    
    private:
        int size;
        vector<Pennant*> *items;

    public:
        Bag() {
            this->size = 0;
            this->items = new vector<Pennant*>;
            this->items->push_back(NULL);
        }
        Bag(int size) {
            this->size = size;
            this->items = new vector<Pennant*>;
            for(int i = 0; i < size; i++) {
                this->items->push_back(NULL);
            }
        }

        void addPennant(int position, Pennant *p) {
            this->items->at(position) = p;
        }

        vector<Pennant*>* getItems() {
            return this->items;
        }

        void addElement(Pennant *p) {
            bool success = false;

            int i = 0;
            while(success == false) {
                if(i >= this->items->size()) {
                    break;
                }

                if(this->items->at(i) == NULL) {
                    this->items->at(i) = p;
                    success = true;
                    break;
                }
                else {
                    this->items->at(i)->joinPennant(p);
                    p = this->items->at(i);
                    this->items->at(i) = NULL;
                    i++;
                }
            }

            if(success == false) {
                this->items->push_back(p);
            }
        }

        Bag* splitBag() {
            Bag *bag_left = new Bag(this->items->size() - 1);
            Bag *bag_right = new Bag(this->items->size() - 1);

            for(int i = this->items->size() - 1; i > 0; i--) {
                if(this->items->at(i) == NULL) {
                    continue;
                }
                
                Pennant* p_right = this->items->at(i)->splitPennant();
                bag_left->addPennant(i-1, this->items->at(i));
                bag_right->addPennant(i-1, p_right);
            }

            if(this->items->at(0) != NULL) {
                bag_right->addElement(this->items->at(0));
            }

            this->items = bag_left->getItems();

            return bag_right;
        }

        void printBag() {
            for(int i = 0; i < this->items->size(); i++) {
                if(this->items->at(i) == NULL) {
                    cout<<"No Element"<<endl;
                }
                else {
                    this->items->at(i)->printPennant();
                }
                cout<<"-------"<<endl;
            }
        }
};