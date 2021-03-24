/* 一个生产者、二个消费者，4个缓冲区时。（使消费者按照生产者生产的顺序取出产品: 这种情况下只需要对生产者与消费者之间同步，消费者相互之间互斥。
   就对缓冲区数据的存取而言不需要在生产者与消费者之间互斥(因为在生产者与消费者中对缓冲区的操作分别是通过g_s和 g_x来实现的),
   但是生产者与消费者之间者在使用屏幕资源printf，所以需要对printf进行互斥操作）*/

#include <iostream>
#include <windows.h>

HANDLE g_hSemaphoreBufferEmpty, g_hSemaphoreBufferFull;     //信号量用于同步
CRITICAL_SECTION g_csThreadCode;    //消费者之间的关键区互斥操作
CRITICAL_SECTION g_csProducer_Consumer;    //生产者与消费者之间的关键区互斥操作
int g_yield = 0; //产量
const int BUFFER_SIZE = 4;          //缓冲区个数  
int g_Buffer[BUFFER_SIZE];          //缓冲池 
int g_s = 0, g_x = 0;    //g_s为生产者向缓冲区放产品的下标，g_x为消费者从缓冲区取产品的下标
int end_consumer = 0; //是否消费完

DWORD WINAPI producer(LPVOID lpPara) //生产者
{
    for (int i = 1; i <= g_yield; i++)
    {
        WaitForSingleObject(g_hSemaphoreBufferEmpty, INFINITE);    //等待空闲缓冲区

        g_Buffer[g_s] = i;

        EnterCriticalSection(&g_csProducer_Consumer); //进入临界区开始操作操作资源
             printf("%生产者向 %d 号缓冲区中放产品%d\n", g_s, g_Buffer[g_s]);
             Sleep(50);
             printf("1aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n"); //用于演示生产者与消费者对屏幕资源的竞争
             printf("2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("3aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("4aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("5aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
        
        LeaveCriticalSection(&g_csProducer_Consumer); //离开临界区     

        g_s = (g_s + 1) % BUFFER_SIZE; //循环使用缓冲区
        

        ReleaseSemaphore(g_hSemaphoreBufferFull, 1, NULL); //生成可用产品数加1
    }

    return 0;
}

DWORD WINAPI consumer(LPVOID lpPara) //消费者
{
    while(1)
    {
        WaitForSingleObject(g_hSemaphoreBufferFull, INFINITE);    //等待可用产品

        EnterCriticalSection(&g_csThreadCode); //进入临界区开始操作操作资源
             if (g_Buffer[g_x] == g_yield)
             {
                 if (!end_consumer)
                 {
                     EnterCriticalSection(&g_csProducer_Consumer); //进入临界区开始操作操作资源
                     printf("\t\t消费者从 %d 缓冲区中取出产品 %d\n", g_x, g_Buffer[g_x]);
                     printf("\t\t1bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");  //用于演示生产者与消费者对屏幕资源的竞争
                     printf("\t\t2bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
                     printf("\t\t3bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n\n");
                     LeaveCriticalSection(&g_csProducer_Consumer); //离开临界区
                 }

                 end_consumer = 1;
                 LeaveCriticalSection(&g_csThreadCode); //离开临界区
                 ReleaseSemaphore(g_hSemaphoreBufferEmpty, 1, NULL); //空缓冲区数加1
                 ReleaseSemaphore(g_hSemaphoreBufferFull, 1, NULL);
                 break;
             }
             EnterCriticalSection(&g_csProducer_Consumer); //进入临界区开始操作操作资源
                 Sleep(50);
                 printf("\t\t消费者从 %d 缓冲区中取出产品 %d\n", g_x, g_Buffer[g_x]);    //应该将这一句与生产者进行互斥
                 printf("\t\t1bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");  //用于演示生产者与消费者对屏幕资源的竞争
                 printf("\t\t2bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
                 printf("\t\t3bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n\n");
            LeaveCriticalSection(&g_csProducer_Consumer); //离开临界区    

             g_x = (g_x + 1) % BUFFER_SIZE; //循环使用缓冲区
        LeaveCriticalSection(&g_csThreadCode); //离开临界区
    
        ReleaseSemaphore(g_hSemaphoreBufferEmpty, 1, NULL); //空缓冲区数加1
    }

    return 0;
}


int main()
{
    std::cout << "      生产者消费者问题:   1生产者 1消费者 1缓冲区\n" << std::endl;
    std::cout << "输入生产者的产量: g_yield = ";
    std::cin >> g_yield;

    InitializeCriticalSection(&g_csThreadCode);    //初始化临界区   
    InitializeCriticalSection(&g_csProducer_Consumer);    //初始化临界区   
    g_hSemaphoreBufferEmpty = CreateSemaphore(NULL, BUFFER_SIZE, BUFFER_SIZE, NULL); //创建一个信号量，表示空缓冲区个数
    g_hSemaphoreBufferFull = CreateSemaphore(NULL, 0, BUFFER_SIZE, NULL); //创建一个信号量，表示有产品的缓冲区个数

    const int THREAD_NUM = 3;
    HANDLE thread_handle[THREAD_NUM];

    //内核对象的安全属性为默认设置、线程空间为默认大小、线程入口为函数为producer，不向线程函数传递参数，不返回该线程ID号
    thread_handle[0] = CreateThread(NULL, 0, producer, NULL, 0, NULL);

    //内核对象的安全属性为默认设置、线程空间为默认大小、线程入口为函数为producer，不向线程函数传递参数，不返回该线程ID号
    thread_handle[1] = CreateThread(NULL, 0, consumer, NULL, 0, NULL);
    thread_handle[2] = CreateThread(NULL, 0, consumer, NULL, 0, NULL);

    WaitForMultipleObjects(THREAD_NUM, thread_handle, TRUE, INFINITE); //等待thread_handle中的所有线程结束
    for (int i = 0; i < THREAD_NUM; i++) //撤消线程句柄
        CloseHandle(thread_handle[i]);

    DeleteCriticalSection(&g_csThreadCode);    //销毁临界区
    DeleteCriticalSection(&g_csProducer_Consumer);    //销毁临界区
    CloseHandle(g_hSemaphoreBufferEmpty); //销毁信号量句柄
    CloseHandle(g_hSemaphoreBufferFull);

    system("pause"); //暂停以查看显示窗口
    return 0;
}
