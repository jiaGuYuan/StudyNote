#include <list>
#include <set>
#include <iostream>
#include <algorithm>
#include <iterator>

// 使用Set+List构造一个'维持插入顺序的Set'
class OrderedSet {
    using ListOfT = std::list<int>;

    // 集合中存储 pair(value, 以及value在list中的迭代器), 方便对list中元素的快速删除
    using SetItem = typename std::pair<int, ListOfT::iterator>;

    // set中元素的比较只与集合元素的value有关
    struct classcomp {
        bool operator() (const SetItem& lhs, const SetItem& rhs) const
        {return lhs.first < rhs.first;}
    };
    using SetOfListIter = typename std::set<SetItem, classcomp>;

  public:
      OrderedSet() {}
      OrderedSet(const OrderedSet &other){
          for (auto item : other) {
              this->append(item);
          }
      }
      OrderedSet& operator=(const OrderedSet &other) {
          if (this == &other) {
            return *this;
          }
          m_set.clear();
          m_list.clear();
          for (auto item : other) {
              this->append(item);
          }
          return *this;
      }

      void append(int val) { 
          // 元素已存在
          if (m_set.find(SetItem(val, m_list.end())) != m_set.end()) { 
            return;
          }

          // 元素未存在. 加入到list和set中
          auto iter = m_list.insert(m_list.end(), val);
          m_set.insert(SetItem(val, iter));
      }

      void remove(int val) {
          auto iter = m_set.find(SetItem(val, m_list.end()));
          if (iter == m_set.end()) { // 元素不存在
            return;
          }

          // 元素存在. 从list和set中删除
          auto list_iter = iter->second;
          m_list.erase(list_iter); 
          m_set.erase(iter); 
      }

      void clear() {
          m_set.clear();
          m_list.clear();
      }

      ListOfT::iterator begin() { return m_list.begin(); }
      ListOfT::const_iterator begin() const { return m_list.begin(); }
      ListOfT::iterator end() { return {m_list.end()}; } 
      ListOfT::const_iterator end() const { return m_list.end(); } 

      // 交集
      void intersection_set(const OrderedSet &other, OrderedSet &out_oset) {
          SetOfListIter inter_set;
          std::set_intersection(m_set.begin(), m_set.end(), 
              other.m_set.begin(), other.m_set.end(),
              std::inserter(inter_set, inter_set.begin()), classcomp());

          // 为了维护插入顺序, 需要从m_list进行操作
          for (auto item : *this) {
              if (inter_set.find(SetItem(item, m_list.end())) != inter_set.end() ) {
                  out_oset.append(item);
              }
          }
      }

      // 差集
      void difference_set(const OrderedSet &other, OrderedSet &out_oset) {
          SetOfListIter diff_set;
          std::set_difference(m_set.begin(), m_set.end(), 
              other.m_set.begin(), other.m_set.end(),
              std::inserter(diff_set, diff_set.begin()), classcomp());

          // 为了维护插入顺序, 需要从m_list进行操作
          for (auto item : *this) {
              if (diff_set.find(SetItem(item, m_list.end())) != diff_set.end() ) {
                  out_oset.append(item);
              }
          }
      }

      // 并集
      void union_set(OrderedSet &other, OrderedSet &out_oset) {
          // 为了维护插入顺序, 使用 "A并B = A + B差A" 来求并集
          SetOfListIter diff_set;
          std::set_difference(other.m_set.begin(), other.m_set.end(),
              m_set.begin(), m_set.end(), 
              std::inserter(diff_set, diff_set.begin()), classcomp());

          for (auto item : *this) {
              out_oset.append(item);
          }

          for (auto item : other) {
              if (diff_set.find(SetItem(item, other.m_list.end())) != diff_set.end() ) {
                  out_oset.append(item);
              }
          }
      }

  // private:
  public:
      ListOfT m_list;
      SetOfListIter m_set;   
};

void orderset_test(){
  OrderedSet oset1;

  oset1.append(5);
  oset1.append(3);
  oset1.append(9);
  oset1.append(1);
  oset1.append(5);
  oset1.append(5);
  for (int val : oset1)
      std::cout << val << " ";
  std::cout << std::endl;

  OrderedSet oset2;
  oset2.append(3);
  oset2.append(1);
  oset2.append(10);
  oset2.append(12);
  oset2.append(10);
  oset2.append(10);
  for (int val : oset2)
      std::cout << val << " ";
  std::cout << std::endl;
  
  OrderedSet out_set;
  oset1.difference_set(oset2, out_set);
  for (int val : out_set)
      std::cout << val << " ";
  std::cout << std::endl;

  out_set.clear();
  oset1.union_set(oset2, out_set);
  for (int val : out_set)
      std::cout << val << " ";
  std::cout << std::endl;

  out_set.clear();
  oset1.intersection_set(oset2, out_set);
  for (int val : out_set)
      std::cout << val << " ";
  std::cout << std::endl;

  out_set.clear();
  for (int i=0; i<1000000; i++) {
     out_set.append(i);
  };
  std::cout << out_set.m_list.size() << ", " << out_set.m_set.size() << std::endl;

  for (int i=0; i<1000000; i++) {
     if (i%8==0) {
      out_set.remove(i);
     }
  };
  std::cout << out_set.m_list.size() << ", " << out_set.m_set.size() << std::endl;

  for (int i=0; i<1000000; i++) {
     out_set.append(i);
  };
  std::cout << out_set.m_list.size() << ", " << out_set.m_set.size() << std::endl;
}