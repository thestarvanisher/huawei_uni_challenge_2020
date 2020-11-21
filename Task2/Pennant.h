#include<iostream>
#include<vector>
using namespace std;

class Pennant {
    private:
        int node;
        Pennant *left;
        Pennant *right;
    public:
        /*Pennant() {
            this->left = NULL;
            this->right = NULL;
        }*/

        Pennant(int n) {
            this->node = n;
            this->left = NULL;
            this->right = NULL;
        }

        Pennant* getLeft() {
            return this->left;
        }

        Pennant* getRight() {
            return this->right;
        }

        void setLeft(Pennant *p) {
            this->left = p;
        }

        void setRight(Pennant *p) {
            this->right = p;
        }

        void joinPennant(Pennant *p) {
            p->setRight(this->getLeft());
            this->setLeft(p);
        }

        int getNodeValue() {
            return this->node;
        }

        Pennant* splitPennant() {
            Pennant *p = this->left;
            this->left = p->getRight();
            p->setRight(NULL);
            return p;
        }

        void printNode() {
            cout << this->node << endl;
        }

        void printPennant() {
            vector<Pennant*> level;
            level.push_back(this);

            while(level.empty() == false) {
                vector<Pennant*> next_level;
                while(level.empty() == false) {
                    if(level.at(0)->getLeft() != NULL) {
                        next_level.push_back(level.at(0)->getLeft());
                    }
                    if(level.at(0)->getRight() != NULL) {
                        next_level.push_back(level.at(0)->getRight());
                    }
                    cout << "" << level.at(0)->getNodeValue() << " ";
                    level.erase(level.begin());
                }
                level = next_level;
                cout << endl;
            } 
        }
};