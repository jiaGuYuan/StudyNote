//一个生产者、一个消费者，一个缓冲区时：这种情况下好像并不需要对缓冲区的操作进行互斥 （不需要使用临界区来进行互斥）

#include <iostream>
#include <windows.h>

HANDLE g_hEventBufferEmpty, g_hEventBufferFull;	//事件用于同步

int g_Buffer; //缓冲区

DWORD WINAPI producer(LPVOID lpPara) //生产者
{
	std::cout << "   创建生产者线程: \n" << std::endl;

	const int yield = *(int *)lpPara;
	for (int i = 0; i < yield; i++)
	{
		WaitForSingleObject(g_hEventBufferEmpty, INFINITE);	//等待空闲缓冲区
		Sleep(0); //模拟时间片切换
		g_Buffer = i;
		printf("生产者向缓冲区中放产品%d\n", g_Buffer);

		SetEvent(g_hEventBufferFull); //生成可用产品
	}
	std::cout << "   结束生产者线程: \n" << std::endl;

	return 0;
}

DWORD WINAPI consumer(LPVOID lpPara) //消费者
{
	std::cout << " 创建消费者线程: \n" << std::endl;

	const int yield = *(int *)lpPara;
	
	for (int i = 0; i < yield; i++)
	{
		WaitForSingleObject(g_hEventBufferFull, INFINITE);	//等待可用产品

		printf("\t\t消费者从缓冲区中取产品%d\n", g_Buffer);

		SetEvent(g_hEventBufferEmpty); //释放缓冲区
		Sleep(10); //some other work should to do  
	}
	std::cout << " 结束消费者线程: \n" << std::endl;
	
	return 0;
}


int main()
{
	std::cout << "      生产者消费者问题:   1生产者 1消费者 1缓冲区\n" << std::endl;
	int yield = 0; //产量
	std::cout << "输入生产者的产量: yield = ";
	std::cin >> yield;

	g_hEventBufferEmpty = CreateEvent(NULL,	FALSE, TRUE, NULL); //创建一个自动复位事件，表示缓冲区是否为空 （触发）
	g_hEventBufferFull = CreateEvent(NULL, FALSE, FALSE, NULL); //创建一个自动复位事件，表示缓冲区无可用产品 （未触发）
	 
	const int THREAD_NUM = 2;
	HANDLE thread_handle[THREAD_NUM];
	
	//内核对象的安全属性为默认设置、线程空间为默认大小、线程入口为函数为producer，向线程函数传递参数&yield，不返回该线程ID号
	thread_handle[0] = CreateThread(NULL, 0, producer, &yield, 0, NULL);
	//内核对象的安全属性为默认设置、线程空间为默认大小、线程入口为函数为producer，向线程函数传递参数&yield，不返回该线程ID号
	thread_handle[1] = CreateThread(NULL, 0, consumer, &yield, 0, NULL);
	
	WaitForMultipleObjects(THREAD_NUM, thread_handle, TRUE, INFINITE); //等待thread_handle中的所有线程结束
	CloseHandle(thread_handle[0]); //撤消线程句柄
	CloseHandle(thread_handle[1]);

	CloseHandle(g_hEventBufferEmpty); //销毁事件句柄
	CloseHandle(g_hEventBufferFull);

	system("pause"); //暂停以查看显示窗口
	return 0;
}
