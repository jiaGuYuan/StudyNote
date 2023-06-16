#include <stdlib.h>
#include <stdio.h>
#include "queue.h"

/* 说明: 该文件用于测试使用queue.h中定义的尾队列.
   执行:gcc queue.h tailQueue_used_gjy.c  -o test_gjy.out
 */

//定义一个BusinessNode_TatlQ_Head类型的尾队列类型
TAILQ_HEAD(BusinessNode_TatlQ_Head, BusinessNode); 

//定义一个业务节点
struct BusinessNode{

  int data;

  TAILQ_ENTRY(BusinessNode) tailQueuefield; //定义一个尾队列节点对象.BusinessNode对象通过该字段加入尾队列中
};

int main()
{
    struct BusinessNode_TatlQ_Head business_tqhead; //定义尾队列头节点
    TAILQ_INIT(&business_tqhead); //初始化尾队列头节点
    
    struct BusinessNode *ptmp = NULL;
    
    // 1.队尾插入节点测试: 执行后队列中的业务节点数据依次为 0 1 2
    #define N 3
    struct BusinessNode *pBsnsNodeAry[N] = {NULL, NULL, NULL}; //用于记录将插入节点的地址
    for(int i = 0; i < N; ++i)
    {
        pBsnsNodeAry[i] = (struct BusinessNode*)malloc(sizeof(struct BusinessNode));
        pBsnsNodeAry[i]->data = i;
 
        //将业务节点pBsnsNodeAry[i]插入队列尾部.
        //参数分别为:队列头节点, 要插入的业务节点, 队列节点在业务节点中的字段名
        TAILQ_INSERT_TAIL(&business_tqhead, pBsnsNodeAry[i], tailQueuefield);
    }

    // 2.队列首部插入节点测试: 执行后队列中的业务节点数据依次为 10 0 1 2
    struct BusinessNode *pBsnsNode;
    pBsnsNode = (struct BusinessNode*)malloc(sizeof(struct BusinessNode));
    pBsnsNode->data = 10;
    //将业务节点pBsnsNode插入队列首部.
    //参数分别为:队列头节点, 要插入的业务节点, 队列节点在业务节点中的字段名
    TAILQ_INSERT_HEAD(&business_tqhead, pBsnsNode, tailQueuefield);
    
    
    // 3.队列中指定节点前后插入节点测试: 执行后队列中的业务节点数据依次为 10 20 0 1 30 2
    /* 现在pBsnsNode指向队列首元素, pBsnsNodeAry[N-1]指向队尾元素 */
    ptmp = (struct BusinessNode*)malloc(sizeof(struct BusinessNode));
    ptmp->data = 20;
    //在队列节点pBsnsNode的后面插入元素
    TAILQ_INSERT_AFTER(&business_tqhead, pBsnsNode, ptmp, tailQueuefield);
 
    ptmp = (struct BusinessNode*)malloc(sizeof(struct BusinessNode));
    ptmp->data = 30;
    //在队列节点pBsnsNodeAry[N-1]的前面插入元素
    TAILQ_INSERT_BEFORE(pBsnsNodeAry[N-1], ptmp, tailQueuefield);
    
    //4.获取队列首节点测试, 应输出:10
    ptmp = TAILQ_FIRST(&business_tqhead);
    printf("the first entry is %d\n", ptmp->data);
    
    //5.1.获取下一节点测试, 应输出:20
    //获取指定业务节点的下一个节点.
    //参数分别为:用于定位的节点, 队列节点在业务节点中的字段名
    ptmp = TAILQ_NEXT(ptmp, tailQueuefield);
    printf("the second entry is %d\n", ptmp->data);
    
    //5.2.获取上一节点测试, 应输出:10
    //获取指定业务节点的下一个节点.
    //参数分别为:用于定位的节点, 头节点类型, 队列节点在业务节点中的字段名
    ptmp = TAILQ_PREV(ptmp, BusinessNode_TatlQ_Head, tailQueuefield);
    printf("the first entry is %d\n", ptmp->data);
    
    //6.正向迭代测试, 应输出:10 20 0 1 30 2
    // 正向迭代; 参数分别为:用于换代的临时业务节点指针, 队列头节点指针, 队列节点在业务节点中的字段名
    TAILQ_FOREACH(ptmp, &business_tqhead, tailQueuefield)
    {
        printf("%d\t", ptmp->data);
    }
    printf("\n");
    
    //7.反向迭代测试, 应输出: 2 30 1 0 20 10
    // 反向迭代; 参数分别为:用于换代的临时业务节点指针, 队列头节点指针, 头节点类型, 队列节点在业务节点中的字段名
    TAILQ_FOREACH_REVERSE(ptmp, &business_tqhead, BusinessNode_TatlQ_Head, tailQueuefield)
    {
        printf("%d\t", ptmp->data);
    }
    printf("\n");
    
    //8.移除业务节点pBsnsNodeAry[1]测试, 应输出:10 20 0 30 2
    //移除业务节点(移除节点的内存由调用都释放)
    TAILQ_REMOVE(&business_tqhead, pBsnsNodeAry[1], tailQueuefield);
    free(pBsnsNodeAry[1]);
    pBsnsNodeAry[1] = NULL;
    TAILQ_FOREACH(ptmp, &business_tqhead, tailQueuefield)
    {
        printf("%d\t", ptmp->data);
    }
    printf("\n");
    
    //9.替换业务节点pBsnsNodeAry[1]测试, 应输出:10 20 100 30 2
    ptmp = (struct BusinessNode*)malloc(sizeof(struct BusinessNode));
    ptmp->data = 100;
    //用new_iten替换第一个元素
    //替换队列的业务节点
    //参数分别为:队列头节点指针, 被替换的业务节点指针, 用于替换的业务节点, 队列节点在业务节点中的字段名
    TAILQ_REPLACE(&business_tqhead, pBsnsNodeAry[0], ptmp, tailQueuefield);
    TAILQ_FOREACH(ptmp, &business_tqhead, tailQueuefield)
    {
        printf("%d\t", ptmp->data);
    }
    printf("\n");

    //10.释放所有业务节点, 应输出:business_tqhead is empty
    //释放所有业务节点
    while(!TAILQ_EMPTY(&business_tqhead)) //每次取出首节点来释放,直到队列为空
    {
        ptmp = TAILQ_FIRST(&business_tqhead);
        TAILQ_REMOVE(&business_tqhead, ptmp, tailQueuefield);
        free(ptmp);
    }
    if(TAILQ_EMPTY(&business_tqhead))
    {
        printf("business_tqhead is empty\n");
    }
    
    return 0;
}






