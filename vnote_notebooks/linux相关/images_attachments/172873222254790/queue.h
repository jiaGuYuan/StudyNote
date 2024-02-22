/*    $OpenBSD: queue.h,v 1.16 2000/09/07 19:47:59 art Exp $    */
/*    $NetBSD: queue.h,v 1.11 1996/05/16 05:17:14 mycroft Exp $    */

/*
 * Copyright (c) 1991, 1993
 *    The Regents of the University of California.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 *    @(#)queue.h    8.5 (Berkeley) 8/20/94
 */

#ifndef    SYS_QUEUE_H__
#define    SYS_QUEUE_H__

/*
 * This file defines five types of data structures: singly-linked lists,
 * lists, simple queues, tail queues, and circular queues.
 *
 *
 * A singly-linked list is headed by a single forward pointer. The elements
 * are singly linked for minimum space and pointer manipulation overhead at
 * the expense of O(n) removal for arbitrary elements. New elements can be
 * added to the list after an existing element or at the head of the list.
 * Elements being removed from the head of the list should use the explicit
 * macro for this purpose for optimum efficiency. A singly-linked list may
 * only be traversed in the forward direction.  Singly-linked lists are ideal
 * for applications with large datasets and few or no removals or for
 * implementing a LIFO queue.
 *
 * A list is headed by a single forward pointer (or an array of forward
 * pointers for a hash table header). The elements are doubly linked
 * so that an arbitrary element can be removed without a need to
 * traverse the list. New elements can be added to the list before
 * or after an existing element or at the head of the list. A list
 * may only be traversed in the forward direction.
 *
 * A simple queue is headed by a pair of pointers, one the head of the
 * list and the other to the tail of the list. The elements are singly
 * linked to save space, so elements can only be removed from the
 * head of the list. New elements can be added to the list before or after
 * an existing element, at the head of the list, or at the end of the
 * list. A simple queue may only be traversed in the forward direction.
 *
 * A tail queue is headed by a pair of pointers, one to the head of the
 * list and the other to the tail of the list. The elements are doubly
 * linked so that an arbitrary element can be removed without a need to
 * traverse the list. New elements can be added to the list before or
 * after an existing element, at the head of the list, or at the end of
 * the list. A tail queue may be traversed in either direction.
 *
 * A circle queue is headed by a pair of pointers, one to the head of the
 * list and the other to the tail of the list. The elements are doubly
 * linked so that an arbitrary element can be removed without a need to
 * traverse the list. New elements can be added to the list before or after
 * an existing element, at the head of the list, or at the end of the list.
 * A circle queue may be traversed in either direction, but has a more
 * complex end of list detection.
 *
 * For details on the use of these macros, see the queue(3) manual page.
 */

/*
 * 单链表定义
 */
//单链表-链表头. name:要定义的链表类型名; tepe:业务节点的类型
#define SLIST_HEAD(name, type)                                             \
struct name {                                                              \
    struct type *slh_first;    /* first element */                           \
}       
//单链表头初始化       
#define    SLIST_HEAD_INITIALIZER(head)                                    \
    { NULL }        
        
#ifndef _WIN32      
//单链表条目 -- type为业务节点的类型     
#define SLIST_ENTRY(type)                                                  \
struct {                                                                   \
    struct type *sle_next;    /* 指向下一个业务节点 */                       \
}
#endif

/*
 * 访问单链表的方法
 */
//获取单链表第一个业务节点的指针
#define    SLIST_FIRST(head)    ((head)->slh_first)
//是否为链表尾--用于作为判断遍历链表时的结束条件
#define    SLIST_END(head)        NULL
//链表是否为空
#define    SLIST_EMPTY(head)    (SLIST_FIRST(head) == SLIST_END(head))
//获取下一个业务节点的地址
#define    SLIST_NEXT(elm, field)    ((elm)->field.sle_next)

//正向遍历链表
#define    SLIST_FOREACH(var, head, field)                          \
    for((var) = SLIST_FIRST(head);                                  \
        (var) != SLIST_END(head);                                   \
        (var) = SLIST_NEXT(var, field))

/*
 * 初始化单链表
 */
#define    SLIST_INIT(head) {                                       \
    SLIST_FIRST(head) = SLIST_END(head);                            \
}

//在slistelm业务节点之后插入一个业务节点elm. field为业务节点中的单链表节点字段名--类型为SLIST_ENTRY
#define    SLIST_INSERT_AFTER(slistelm, elm, field) do {            \
    (elm)->field.sle_next = (slistelm)->field.sle_next;             \
    (slistelm)->field.sle_next = (elm);                             \
} while (0)

//在单链表头部插入一个业务节点
#define    SLIST_INSERT_HEAD(head, elm, field) do {                 \
    (elm)->field.sle_next = (head)->slh_first;                      \
    (head)->slh_first = (elm);                                      \
} while (0)

//从单链表头部移除一个业务节点
#define    SLIST_REMOVE_HEAD(head, field) do {                      \
    (head)->slh_first = (head)->slh_first->field.sle_next;          \
} while (0)


/************************************************************************/


/*
 * 链表定义.
 */
//链表头. name:要定义的链表类型名; tepe:业务节点的类型
#define LIST_HEAD(name, type)                                       \
struct name {                                                       \
    struct type *lh_first;    /* first element */                     \
}

//链表头初始化
#define LIST_HEAD_INITIALIZER(head)                                 \
    { NULL }

//链表条目 -- type为业务节点的类型
#define LIST_ENTRY(type)                                            \
struct {                                                            \
    struct type *le_next;    /* 指向下一个业务节点 */                 \
                                                                    \
    /* le_prev中存放的上一个业务节点中的链表节点字段的le_next字段的地址--&preBusinessNode.field.le_next;\
       (*tqe_prev)存放的是当前业务节点的地址(因为上一个链表节点的le_next指向当前业务节点)\
    */                                                              \
    struct type **le_prev;                                          \
}

/*
 * 链表访问宏
 */
 //获取链表第一个业务节点的指针
#define    LIST_FIRST(head)        ((head)->lh_first)
//是否为链表尾--用于作为判断遍历链表时的结束条件
#define    LIST_END(head)            NULL
//链表是否为空
#define    LIST_EMPTY(head)        (LIST_FIRST(head) == LIST_END(head))
//获取下一个业务节点的地址
#define    LIST_NEXT(elm, field)        ((elm)->field.le_next)

//正向遍历链表
#define LIST_FOREACH(var, head, field)                              \
    for((var) = LIST_FIRST(head);                                   \
        (var)!= LIST_END(head);                                     \
        (var) = LIST_NEXT(var, field))

/*
 * 初始化单链表
 */
#define    LIST_INIT(head) do {                                     \
    LIST_FIRST(head) = LIST_END(head);                              \
} while (0)

//在listelm业务节点之后插入一个业务节点elm. field为业务节点中的单链表节点字段名--类型为SLIST_ENTRY
#define LIST_INSERT_AFTER(listelm, elm, field) do {                             \
    if (((elm)->field.le_next = (listelm)->field.le_next) != NULL)              \
        (listelm)->field.le_next->field.le_prev = &(elm)->field.le_next;        \
    (listelm)->field.le_next = (elm);                                           \
    (elm)->field.le_prev = &(listelm)->field.le_next;                           \
} while (0)

//在listelm业务节点之前插入一个业务节点elm. field为业务节点中的单链表节点字段名--类型为SLIST_ENTRY
#define    LIST_INSERT_BEFORE(listelm, elm, field) do {                         \
    (elm)->field.le_prev = (listelm)->field.le_prev;                            \
    (elm)->field.le_next = (listelm);                                           \
    *(listelm)->field.le_prev = (elm);                                          \
    (listelm)->field.le_prev = &(elm)->field.le_next;                           \
} while (0)

//在单链表头部插入一个业务节点
#define LIST_INSERT_HEAD(head, elm, field) do {                                 \
    if (((elm)->field.le_next = (head)->lh_first) != NULL)                      \
        (head)->lh_first->field.le_prev = &(elm)->field.le_next;                \
    (head)->lh_first = (elm);                                                   \
    (elm)->field.le_prev = &(head)->lh_first;                                   \
} while (0)
//从单链表头部移除一个业务节点
#define LIST_REMOVE(elm, field) do {                                            \
    if ((elm)->field.le_next != NULL)                                           \
        (elm)->field.le_next->field.le_prev =                                   \
            (elm)->field.le_prev;                                               \
    *(elm)->field.le_prev = (elm)->field.le_next;                               \
} while (0)

//将业务节点elm替换为elm2
#define LIST_REPLACE(elm, elm2, field) do {                                     \
    if (((elm2)->field.le_next = (elm)->field.le_next) != NULL)                 \
        (elm2)->field.le_next->field.le_prev =  &(elm2)->field.le_next;         \
    (elm2)->field.le_prev = (elm)->field.le_prev;                               \
    *(elm2)->field.le_prev = (elm2);                                            \
} while (0)


/*****************************************************************************/


/*
 * 简单的队列定义 -- name:要定义的链表类型名; tepe:业务节点的类型
 */
#define SIMPLEQ_HEAD(name, type)                                                \
struct name {                                                                   \
    struct type *sqh_first;    /* 指向第一个业务节点 */                           \
                                                                                \
    /* sql_last指向最后一个节点的sqe_next -- 即sqh_last ==         &lastNode->sqe_next; \
       (*sql_last)指向NULL(因为最后一个节点的sqe_next为NULL)                            \
     */                                                                          \
    struct type **sqh_last;                                                     \
}

#define SIMPLEQ_HEAD_INITIALIZER(head)                                          \
    { NULL, &(head).sqh_first }

//队列条目 -- type为业务节点的类型
#define SIMPLEQ_ENTRY(type)                                                     \
struct {                                                                        \
    struct type *sqe_next;    /* 指向下一个业务节点 */                            \
}

/*
 * 队列访问宏
 */
 //获取队列第一个业务节点的指针
#define    SIMPLEQ_FIRST(head)        ((head)->sqh_first)
//是否为队列尾--用于作为判断遍历队列时的结束条件
#define    SIMPLEQ_END(head)        NULL
//队列是否为空
#define    SIMPLEQ_EMPTY(head)        (SIMPLEQ_FIRST(head) == SIMPLEQ_END(head))
//获取下一个业务节点的地址
#define    SIMPLEQ_NEXT(elm, field)    ((elm)->field.sqe_next)

//正向遍历队列
#define SIMPLEQ_FOREACH(var, head, field)                       \
    for((var) = SIMPLEQ_FIRST(head);                            \
        (var) != SIMPLEQ_END(head);                             \
        (var) = SIMPLEQ_NEXT(var, field))

/*
 * 初始化一个简单队列. 
 */
#define    SIMPLEQ_INIT(head) do {                              \
    (head)->sqh_first = NULL;                                   \
    (head)->sqh_last = &(head)->sqh_first;                      \
} while (0)

/*
 * 在队列头部插入一个(业务)节点(在队列头节点后插入一个节点). 
 */
#define SIMPLEQ_INSERT_HEAD(head, elm, field) do {              \
    if (((elm)->field.sqe_next = (head)->sqh_first) == NULL)    \
        (head)->sqh_last = &(elm)->field.sqe_next;              \
    (head)->sqh_first = (elm);                                  \
} while (0)

/*
 * 在队列尾部插入一个(业务)节点. 
 */
#define SIMPLEQ_INSERT_TAIL(head, elm, field) do {              \
    (elm)->field.sqe_next = NULL;                               \
    *(head)->sqh_last = (elm);                                  \
    (head)->sqh_last = &(elm)->field.sqe_next;                  \
} while (0)

/*
 * 在指定节点之后插入一个节点
 */
#define SIMPLEQ_INSERT_AFTER(head, listelm, elm, field) do {                \
    if (((elm)->field.sqe_next = (listelm)->field.sqe_next) == NULL)        \
        (head)->sqh_last = &(elm)->field.sqe_next;                          \
    (listelm)->field.sqe_next = (elm);                                      \
} while (0)

/*
 * 从队列头部删除一个节点.
 */
#define SIMPLEQ_REMOVE_HEAD(head, elm, field) do {                          \
    if (((head)->sqh_first = (elm)->field.sqe_next) == NULL)                \
        (head)->sqh_last = &(head)->sqh_first;                              \
} while (0)


/**************************************************************************************************/


/*
 * 尾队列定义
 */
 //尾队列头节点. --name:为要定义的尾队列类型名; type--业务节点的类型
#define TAILQ_HEAD(name, type)                                              \
struct name {                                                               \
    struct type *tqh_first;    /* first element */                            \
    struct type **tqh_last;    /* addr of last next element */                 \
}

//尾队列头初始化
#define TAILQ_HEAD_INITIALIZER(head)    { NULL, &(head).tqh_first }

/*
 * 尾队列使用方法:
   通过在业务节点包含一个尾队列节点,来将业务节点加入队列---这儿有点像linux的链表.
 
   该实现中业务节点中需要一个尾队列节点成员变量field(可以取其它变量名).
   如下,定义一种业务节点和一个管理该种节点的尾队列.:
    //定义一个BusinessNode_TatlQ_Head类型的尾队列类型
    TAILQ_HEAD(BusinessNode_TatlQ_Head, BusinessNode); 

    //定义一个业务节点
    struct BusinessNode{
      everyType data;

      TAILQ_ENTRY(BusinessNode) field; //定义一个尾队列节点对象.BusinessNode对象通过该字段加入尾队列中
    }
   
 */
 

//尾队列条目。用于定义一个尾队列条目－－type为业务节点的类型(如BusinessNode)
//该结构体是一个匿名结构体(其结构与尾队列头一致,所以可将其转换为尾队列头进行操作)。
//它一般都是另外一个结构体或者共用体的成员.  
#define TAILQ_ENTRY(type)                                                                       \
struct {                                                                                        \
    struct type *tqe_next;    /* 指向下一个业务节点(注意该指针指向的是业务节点businessNode) */        \
                                                                                                \
    /*指向上一个业务节点的field.tqe_next字段(存放的是上一个业务节点field.tqe_next的地址 -- &preBusinessNode.field.tqe_next).\
      因为tqe_next是TAILQ_ENTRY的第一个元素, 所以field.tqe_next的地址值等于field的地址.                                                 \
      由于当前节点的tqe_prev存放的是上一个节点的tqe_next的地址,而上一个tqe_next中存放的是下一个(当前)业务节点的地址;                    \
      所以当前节点的*tqe_prev中存放的是当前业务的地址..                                                                                 \
      */                                                                                                                    \
    struct type **tqe_prev;                                                                                                \
}

 //获取队列head的首元素(的地址)
#define    TAILQ_FIRST(head)        ((head)->tqh_first)
#define    TAILQ_END(head)            NULL
//根据elm元素的field字段来获取它的下一个节点(的地址),field字段是TAILQ_ENTRY(type)类型的
#define    TAILQ_NEXT(elm, field)        ((elm)->field.tqe_next)

/*获取队列的最后一个业务节点(的地址),head为指向队列的头结点的指针,headname是队列头结点的类型名
  这里利用了TAILQ_HEAD与TAILQ_ENTRY结构的一致性,当前业务节点的地址可通过上一个节点的tqe_next来获取(也可通过当前节点的*tqe_prev来获取).
  */
#define TAILQ_LAST(head, headname)                                      \
    (*(((struct headname *)((head)->tqh_last))->tqh_last))

/*获取队列elm的前一个元素的地址*/
#define TAILQ_PREV(elm, headname, field)                                \
    (*(((struct headname *)((elm)->field.tqe_prev))->tqh_last))
//判断队列是否为空
#define    TAILQ_EMPTY(head)                                            \
    (TAILQ_FIRST(head) == TAILQ_END(head))

//遍历队列. --遍历过程不是安全的--不能在遍历过程中释放业务节点
#define TAILQ_FOREACH(var, head, field)                                 \
    for((var) = TAILQ_FIRST(head);                                      \
        (var) != TAILQ_END(head);                                       \
        (var) = TAILQ_NEXT(var, field))
//反向遍历队列 --遍历过程不是安全的--不能在遍历过程中释放业务节点
#define TAILQ_FOREACH_REVERSE(var, head, headname, field)               \
    for((var) = TAILQ_LAST(head, headname);                             \
        (var) != TAILQ_END(head);                                       \
        (var) = TAILQ_PREV(var, headname, field))

/*
 尾队列函数 .
 */
 //初始化队列头节点,head:指向头节点的指针
#define    TAILQ_INIT(head) do {                                        \
    (head)->tqh_first = NULL;                                           \
    (head)->tqh_last = &(head)->tqh_first;                              \
} while (0)

//在队列的头部插入一个元素,
//head:指向队列头节点的指针,elm:指向要插入的元素,field:elm的插入操作是通过它的这个字段来完成的,它是TAILQ_ENTRY(type)类型的
#define TAILQ_INSERT_HEAD(head, elm, field) do {                        \
    if (((elm)->field.tqe_next = (head)->tqh_first) != NULL)            \
        (head)->tqh_first->field.tqe_prev = &(elm)->field.tqe_next;     \
    else                                                                \
        (head)->tqh_last = &(elm)->field.tqe_next;                      \
    (head)->tqh_first = (elm);                                          \
    (elm)->field.tqe_prev = &(head)->tqh_first;                         \
} while (0)

//在队列的尾部插入一个元素,
//head:指向队列头节点的指针,elm:指向要插入的元素,field:插入操作是通过这个字段来完成的,它是TAILQ_ENTRY(type)类型的
#define TAILQ_INSERT_TAIL(head, elm, field) do {            \
    (elm)->field.tqe_next = NULL;                           \
    (elm)->field.tqe_prev = (head)->tqh_last;               \
    *(head)->tqh_last = (elm);                              \
    (head)->tqh_last = &(elm)->field.tqe_next;              \
} while (0)

//在head队列的listelm节点之后插入一个elm节点, field:是elm中包含TAILQ_ENTRY(type)结构的字段
#define TAILQ_INSERT_AFTER(head, listelm, elm, field) do {                          \
    if (((elm)->field.tqe_next = (listelm)->field.tqe_next) != NULL)                \
        (elm)->field.tqe_next->field.tqe_prev = &(elm)->field.tqe_next;             \
    else                                                                            \
        (head)->tqh_last = &(elm)->field.tqe_next;                                  \
    (listelm)->field.tqe_next = (elm);                                              \
    (elm)->field.tqe_prev = &(listelm)->field.tqe_next;                             \
} while (0)

//在head队列的listelm节点之前插入一个elm节点, field:是elm中包含TAILQ_ENTRY(type)结构的字段
#define    TAILQ_INSERT_BEFORE(listelm, elm, field) do {            \
    (elm)->field.tqe_prev = (listelm)->field.tqe_prev;              \
    (elm)->field.tqe_next = (listelm);                              \
    *(listelm)->field.tqe_prev = (elm);                             \
    (listelm)->field.tqe_prev = &(elm)->field.tqe_next;             \
} while (0)

//从head队列中移除elm节点, field:是elm中包含TAILQ_ENTRY(type)结构的字段
#define TAILQ_REMOVE(head, elm, field) do {                                     \
    if (((elm)->field.tqe_next) != NULL)                                        \
        (elm)->field.tqe_next->field.tqe_prev =  (elm)->field.tqe_prev;         \
    else                                                                        \
        (head)->tqh_last = (elm)->field.tqe_prev;                               \
    *(elm)->field.tqe_prev = (elm)->field.tqe_next;                             \
} while (0)

//将head队列中的elm节点用elm2节点替代, field:是elm中包含TAILQ_ENTRY(type)结构的字段
#define TAILQ_REPLACE(head, elm, elm2, field) do {                          \
    if (((elm2)->field.tqe_next = (elm)->field.tqe_next) != NULL)           \
        (elm2)->field.tqe_next->field.tqe_prev = &(elm2)->field.tqe_next;   \
    else                                                                    \
        (head)->tqh_last = &(elm2)->field.tqe_next;                         \
    (elm2)->field.tqe_prev = (elm)->field.tqe_prev;                         \
    *(elm2)->field.tqe_prev = (elm2);                                       \
} while (0)


/*************************************************************************************/

/*
 *循环队列定义。
 */
 //循环队列头
#define CIRCLEQ_HEAD(name, type)                                            \
struct name {                                                               \
    struct type *cqh_first;        /* first element */                        \
    struct type *cqh_last;        /* last element */                          \
}

//循环初始化队列头
#define CIRCLEQ_HEAD_INITIALIZER(head)                                      \
    { CIRCLEQ_END(&head), CIRCLEQ_END(&head) }

//循环队列条目
#define CIRCLEQ_ENTRY(type)                                                 \
struct {                                                                    \
    struct type *cqe_next;        /* next element */                          \
    struct type *cqe_prev;        /* previous element */                      \
}

/*
 * 循环队列的访问方法
 */
 //第一个节点
#define    CIRCLEQ_FIRST(head)        ((head)->cqh_first)
//最后一个节点
#define    CIRCLEQ_LAST(head)        ((head)->cqh_last)
//结束，用于遍历
#define    CIRCLEQ_END(head)        ((void *)(head))
//获取节点elm的下一个节点,field是elm结构中包含的CIRCLEQ_ENTRY(type)字段,elm的链由field字段构成
#define    CIRCLEQ_NEXT(elm, field)    ((elm)->field.cqe_next)
//获取节点elm的前一个节点
#define    CIRCLEQ_PREV(elm, field)    ((elm)->field.cqe_prev)
//循环队列是否为空
#define    CIRCLEQ_EMPTY(head)                              \
    (CIRCLEQ_FIRST(head) == CIRCLEQ_END(head))

//遍历循环队列
#define CIRCLEQ_FOREACH(var, head, field)                   \
    for((var) = CIRCLEQ_FIRST(head);                        \
        (var) != CIRCLEQ_END(head);                         \
        (var) = CIRCLEQ_NEXT(var, field))

//反向遍历循环队列
#define CIRCLEQ_FOREACH_REVERSE(var, head, field)           \
    for((var) = CIRCLEQ_LAST(head);                         \
        (var) != CIRCLEQ_END(head);                         \
        (var) = CIRCLEQ_PREV(var, field))

/*
 * 循环队列的功能.
 */
 //初始循环队列,使头节点的cqh_first,cqh_last指向它自身
#define    CIRCLEQ_INIT(head) do {                          \
    (head)->cqh_first = CIRCLEQ_END(head);                  \
    (head)->cqh_last = CIRCLEQ_END(head);                   \
} while (0)

//在listelm之后插入elm。head为指向循环队列头节点的指针,field是elm结构中包含的CIRCLEQ_ENTRY(type)字段,elm的链由field字段构成
#define CIRCLEQ_INSERT_AFTER(head, listelm, elm, field) do {    \
    (elm)->field.cqe_next = (listelm)->field.cqe_next;          \
    (elm)->field.cqe_prev = (listelm);                          \
    if ((listelm)->field.cqe_next == CIRCLEQ_END(head))         \
        (head)->cqh_last = (elm);                               \
    else                                                        \
        (listelm)->field.cqe_next->field.cqe_prev = (elm);      \
    (listelm)->field.cqe_next = (elm);                          \
} while (0)

//在listelm之前插入elm。head为指向循环队列头节点的指针,field是elm结构中包含的CIRCLEQ_ENTRY(type)字段,elm的链由field字段构成
#define CIRCLEQ_INSERT_BEFORE(head, listelm, elm, field) do {       \
    (elm)->field.cqe_next = (listelm);                              \
    (elm)->field.cqe_prev = (listelm)->field.cqe_prev;              \
    if ((listelm)->field.cqe_prev == CIRCLEQ_END(head))             \
        (head)->cqh_first = (elm);                                  \
    else                                                            \
        (listelm)->field.cqe_prev->field.cqe_next = (elm);          \
    (listelm)->field.cqe_prev = (elm);                              \
} while (0)

//在循环队列的头部插入节点elm
#define CIRCLEQ_INSERT_HEAD(head, elm, field) do {                  \
    (elm)->field.cqe_next = (head)->cqh_first;                      \
    (elm)->field.cqe_prev = CIRCLEQ_END(head);                      \
    if ((head)->cqh_last == CIRCLEQ_END(head))                      \
        (head)->cqh_last = (elm);                                   \
    else                                                            \
        (head)->cqh_first->field.cqe_prev = (elm);                  \
    (head)->cqh_first = (elm);                                      \
} while (0)

//在循环队列的尾部插入节点elm
#define CIRCLEQ_INSERT_TAIL(head, elm, field) do {                  \
    (elm)->field.cqe_next = CIRCLEQ_END(head);                      \
    (elm)->field.cqe_prev = (head)->cqh_last;                       \
    if ((head)->cqh_first == CIRCLEQ_END(head))                     \
        (head)->cqh_first = (elm);                                  \
    else                                                            \
        (head)->cqh_last->field.cqe_next = (elm);                   \
    (head)->cqh_last = (elm);                                       \
} while (0)

//从循环队列中移除节点elm
#define    CIRCLEQ_REMOVE(head, elm, field) do {                    \
    if ((elm)->field.cqe_next == CIRCLEQ_END(head))                 \
        (head)->cqh_last = (elm)->field.cqe_prev;                   \
    else                                                            \
        (elm)->field.cqe_next->field.cqe_prev = (elm)->field.cqe_prev;  \
    if ((elm)->field.cqe_prev == CIRCLEQ_END(head))                 \
        (head)->cqh_first = (elm)->field.cqe_next;                  \
    else                                                            \
        (elm)->field.cqe_prev->field.cqe_next =                     \
            (elm)->field.cqe_next;                                  \
} while (0)

//将循环队列中的elm替换为elm2
#define CIRCLEQ_REPLACE(head, elm, elm2, field) do {                            \
        if (((elm2)->field.cqe_next = (elm)->field.cqe_next) == CIRCLEQ_END(head))  \
            (head).cqh_last = (elm2);                                           \
        else                                                                    \
            (elm2)->field.cqe_next->field.cqe_prev = (elm2);                    \
    if (((elm2)->field.cqe_prev = (elm)->field.cqe_prev) == CIRCLEQ_END(head))  \
                                                                                \
        (head).cqh_first = (elm2);                                              \
    else                                                                        \
        (elm2)->field.cqe_prev->field.cqe_next = (elm2);                        \
} while (0)

#endif    /* !SYS_QUEUE_H__ */
