//һ�������ߡ�һ�������ߣ�һ��������ʱ����������º��񲢲���Ҫ�Ի������Ĳ������л��� ������Ҫʹ���ٽ��������л��⣩

#include <iostream>
#include <windows.h>

HANDLE g_hEventBufferEmpty, g_hEventBufferFull;	//�¼�����ͬ��

int g_Buffer; //������

DWORD WINAPI producer(LPVOID lpPara) //������
{
	std::cout << "   �����������߳�: \n" << std::endl;

	const int yield = *(int *)lpPara;
	for (int i = 0; i < yield; i++)
	{
		WaitForSingleObject(g_hEventBufferEmpty, INFINITE);	//�ȴ����л�����
		Sleep(0); //ģ��ʱ��Ƭ�л�
		g_Buffer = i;
		printf("�������򻺳����зŲ�Ʒ%d\n", g_Buffer);

		SetEvent(g_hEventBufferFull); //���ɿ��ò�Ʒ
	}
	std::cout << "   �����������߳�: \n" << std::endl;

	return 0;
}

DWORD WINAPI consumer(LPVOID lpPara) //������
{
	std::cout << " �����������߳�: \n" << std::endl;

	const int yield = *(int *)lpPara;
	
	for (int i = 0; i < yield; i++)
	{
		WaitForSingleObject(g_hEventBufferFull, INFINITE);	//�ȴ����ò�Ʒ

		printf("\t\t�����ߴӻ�������ȡ��Ʒ%d\n", g_Buffer);

		SetEvent(g_hEventBufferEmpty); //�ͷŻ�����
		Sleep(10); //some other work should to do  
	}
	std::cout << " �����������߳�: \n" << std::endl;
	
	return 0;
}


int main()
{
	std::cout << "      ����������������:   1������ 1������ 1������\n" << std::endl;
	int yield = 0; //����
	std::cout << "���������ߵĲ���: yield = ";
	std::cin >> yield;

	g_hEventBufferEmpty = CreateEvent(NULL,	FALSE, TRUE, NULL); //����һ���Զ���λ�¼�����ʾ�������Ƿ�Ϊ�� ��������
	g_hEventBufferFull = CreateEvent(NULL, FALSE, FALSE, NULL); //����һ���Զ���λ�¼�����ʾ�������޿��ò�Ʒ ��δ������
	 
	const int THREAD_NUM = 2;
	HANDLE thread_handle[THREAD_NUM];
	
	//�ں˶���İ�ȫ����ΪĬ�����á��߳̿ռ�ΪĬ�ϴ�С���߳����Ϊ����Ϊproducer�����̺߳������ݲ���&yield�������ظ��߳�ID��
	thread_handle[0] = CreateThread(NULL, 0, producer, &yield, 0, NULL);
	//�ں˶���İ�ȫ����ΪĬ�����á��߳̿ռ�ΪĬ�ϴ�С���߳����Ϊ����Ϊproducer�����̺߳������ݲ���&yield�������ظ��߳�ID��
	thread_handle[1] = CreateThread(NULL, 0, consumer, &yield, 0, NULL);
	
	WaitForMultipleObjects(THREAD_NUM, thread_handle, TRUE, INFINITE); //�ȴ�thread_handle�е������߳̽���
	CloseHandle(thread_handle[0]); //�����߳̾��
	CloseHandle(thread_handle[1]);

	CloseHandle(g_hEventBufferEmpty); //�����¼����
	CloseHandle(g_hEventBufferFull);

	system("pause"); //��ͣ�Բ鿴��ʾ����
	return 0;
}
