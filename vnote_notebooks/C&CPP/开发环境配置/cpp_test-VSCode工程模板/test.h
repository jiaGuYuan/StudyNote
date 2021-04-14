#include <vector>
#include <iostream>
#include <iomanip>

using std::vector;

class Grid {

public:
    Grid(int w, int h){
        // 初始化二维数组
        for (int i=0; i<h; i++) {
            std::vector<int> v;
            for (int j=0; j<w; j++) {
                v.push_back(i*w+j);
            }
            this->grid.push_back(v);
        }
    }

    void print(){
        for (vector<vector<int> >::iterator it=this->grid.begin(); it!=this->grid.end(); it++) {
            vector<int> row = *it;

            for (vector<int>::iterator it2=row.begin(); it2!=row.end(); it2++) {
                std::cout << std::setw(3) << *it2;
            }
            std::cout << std::endl;
        } 
    }

private:
    vector<vector<int> > grid;
};

