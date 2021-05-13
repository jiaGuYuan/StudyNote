#include <algorithm>
#include <vector>
#include <list>
#include <functional>
#include <iostream>
#include <random>

template <typename T>
void print_t(T vec) {
    for (auto x : vec) {
        std::cout << x << ", ";
    }
    std::cout << std::endl << "==========" << std::endl;
}

template <typename T>
void print_iter_value(T vec_iter) {
    for (auto x : vec_iter) {
        std::cout << *x << ", ";
    }
    std::cout << std::endl << "==========" << std::endl;
}

template <typename T>
void print_seq_iter(const T &vec, const std::vector<typename T::iterator> &vec_iter) {
    uint i = 0;
    for (auto iter=vec.begin(); iter!=vec.end() && i < vec_iter.size(); ++iter) {
        if (vec_iter[i++] == iter) {
            std::cout << "eq ";
        }
        else {
            std::cout << "not_eq ";
        }
    }
    std::cout << std::endl << "==========" << std::endl;
}


void sort_test_002() {
    std::vector<int> vec;
    for(int i=0; i<10; i++) {
        vec.push_back(random());
    }
    print_t<std::vector<int> >(vec);

    // 保存迭代器
    std::vector<std::vector<int>::iterator > vec_iter;
    for (auto iter=vec.begin(); iter!=vec.end(); iter++) {
        vec_iter.push_back(iter);
    }

    print_iter_value<std::vector<std::vector<int>::iterator> >(vec_iter); // 元素比较
    print_seq_iter<std::vector<int> >(vec, vec_iter); // 比较保存的迭代器与真实的迭代器

    
    //使用小于运算符进行比较
    std::sort(vec.begin(), vec.end()); //默认行为就是使用小于运算符
    print_t<std::vector<int> >(vec);

    // 迭代器是跟元素绑定的(而不下跟位置绑定的); 即使元素的位置顺序变化了, 保存的迭代器也能访问到调整位置后的原元素
    print_iter_value<std::vector<std::vector<int>::iterator> >(vec_iter);
    // 保存的迭代器与排序后的迭代器一致
    print_seq_iter<std::vector<int> >(vec, vec_iter);

    // std::vector resize之后原来保存的迭代器无效了 ！！！
    std::cout << vec.capacity() << ", " << vec.size() << std::endl;
    vec.resize(100); //增删元素也会导致resize
    std::cout << vec.capacity() << ", " << vec.size() << std::endl;

    print_iter_value<std::vector<std::vector<int>::iterator> >(vec_iter);
    print_seq_iter<std::vector<int> >(vec, vec_iter); // 全部输出 "not_eq"
}


void sort_test_005() {
    std::list<int> lst;
    for(int i=0; i<10; i++) {
        lst.push_back(random());
    }
    print_t<std::list<int> >(lst);

    // 保存迭代器
    std::vector<std::list<int>::iterator > vec_iter;
    for (auto iter=lst.begin(); iter!=lst.end(); iter++) {
        vec_iter.push_back(iter);
    }

    print_iter_value<std::vector<std::list<int>::iterator> >(vec_iter);
    print_seq_iter<std::list<int> >(lst, vec_iter);

    // std::list resize之后原来保存的迭代器依然有效 ！！！
    std::cout <<  lst.size() << std::endl;
    lst.resize(100000);
    std::cout <<  lst.size() << std::endl;
    print_iter_value<std::vector<std::list<int>::iterator> >(vec_iter);
    print_seq_iter<std::list<int> >(lst, vec_iter);
    lst.resize(11);
    print_seq_iter<std::list<int> >(lst, vec_iter); // 全部输出 "eq"

    // const_iterator与iterator相同; 只是一个有修改权限,一个没有修改权限。
    const std::list<int>::const_iterator const_iter = lst.begin();
    std::list<int>::iterator iter = lst.begin();
    // *const_iter = 10; // error
    // *iter = 10; // ok
    std::cout << (const_iter == iter) << std::endl << "==========" << std::endl; 
    print_t<std::list<int> >(lst); 
}


void sort_test() {
    
    sort_test_002();
    sort_test_005();
}

