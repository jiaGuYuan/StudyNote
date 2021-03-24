/* һ�������ߡ����������ߣ�4��������ʱ����ʹ�����߰���������������˳��ȡ����Ʒ: ���������ֻ��Ҫ����������������֮��ͬ�����������໥֮�以�⡣
   �ͶԻ��������ݵĴ�ȡ���Բ���Ҫ����������������֮�以��(��Ϊ�����������������жԻ������Ĳ����ֱ���ͨ��g_s�� g_x��ʵ�ֵ�),
   ������������������֮������ʹ����Ļ��Դprintf��������Ҫ��printf���л��������*/

#include <iostream>
#include <windows.h>

HANDLE g_hSemaphoreBufferEmpty, g_hSemaphoreBufferFull;     //�ź�������ͬ��
CRITICAL_SECTION g_csThreadCode;    //������֮��Ĺؼ����������
CRITICAL_SECTION g_csProducer_Consumer;    //��������������֮��Ĺؼ����������
int g_yield = 0; //����
const int BUFFER_SIZE = 4;          //����������  
int g_Buffer[BUFFER_SIZE];          //����� 
int g_s = 0, g_x = 0;    //g_sΪ�������򻺳����Ų�Ʒ���±꣬g_xΪ�����ߴӻ�����ȡ��Ʒ���±�
int end_consumer = 0; //�Ƿ�������

DWORD WINAPI producer(LPVOID lpPara) //������
{
    for (int i = 1; i <= g_yield; i++)
    {
        WaitForSingleObject(g_hSemaphoreBufferEmpty, INFINITE);    //�ȴ����л�����

        g_Buffer[g_s] = i;

        EnterCriticalSection(&g_csProducer_Consumer); //�����ٽ�����ʼ����������Դ
             printf("%�������� %d �Ż������зŲ�Ʒ%d\n", g_s, g_Buffer[g_s]);
             Sleep(50);
             printf("1aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n"); //������ʾ�������������߶���Ļ��Դ�ľ���
             printf("2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("3aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("4aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
             printf("5aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
        
        LeaveCriticalSection(&g_csProducer_Consumer); //�뿪�ٽ���     

        g_s = (g_s + 1) % BUFFER_SIZE; //ѭ��ʹ�û�����
        

        ReleaseSemaphore(g_hSemaphoreBufferFull, 1, NULL); //���ɿ��ò�Ʒ����1
    }

    return 0;
}

DWORD WINAPI consumer(LPVOID lpPara) //������
{
    while(1)
    {
        WaitForSingleObject(g_hSemaphoreBufferFull, INFINITE);    //�ȴ����ò�Ʒ

        EnterCriticalSection(&g_csThreadCode); //�����ٽ�����ʼ����������Դ
             if (g_Buffer[g_x] == g_yield)
             {
                 if (!end_consumer)
                 {
                     EnterCriticalSection(&g_csProducer_Consumer); //�����ٽ�����ʼ����������Դ
                     printf("\t\t�����ߴ� %d ��������ȡ����Ʒ %d\n", g_x, g_Buffer[g_x]);
                     printf("\t\t1bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");  //������ʾ�������������߶���Ļ��Դ�ľ���
                     printf("\t\t2bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
                     printf("\t\t3bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n\n");
                     LeaveCriticalSection(&g_csProducer_Consumer); //�뿪�ٽ���
                 }

                 end_consumer = 1;
                 LeaveCriticalSection(&g_csThreadCode); //�뿪�ٽ���
                 ReleaseSemaphore(g_hSemaphoreBufferEmpty, 1, NULL); //�ջ���������1
                 ReleaseSemaphore(g_hSemaphoreBufferFull, 1, NULL);
                 break;
             }
             EnterCriticalSection(&g_csProducer_Consumer); //�����ٽ�����ʼ����������Դ
                 Sleep(50);
                 printf("\t\t�����ߴ� %d ��������ȡ����Ʒ %d\n", g_x, g_Buffer[g_x]);    //Ӧ�ý���һ���������߽��л���
                 printf("\t\t1bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");  //������ʾ�������������߶���Ļ��Դ�ľ���
                 printf("\t\t2bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
                 printf("\t\t3bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n\n");
            LeaveCriticalSection(&g_csProducer_Consumer); //�뿪�ٽ���    

             g_x = (g_x + 1) % BUFFER_SIZE; //ѭ��ʹ�û�����
        LeaveCriticalSection(&g_csThreadCode); //�뿪�ٽ���
    
        ReleaseSemaphore(g_hSemaphoreBufferEmpty, 1, NULL); //�ջ���������1
    }

    return 0;
}


int main()
{
    std::cout << "      ����������������:   1������ 1������ 1������\n" << std::endl;
    std::cout << "���������ߵĲ���: g_yield = ";
    std::cin >> g_yield;

    InitializeCriticalSection(&g_csThreadCode);    //��ʼ���ٽ���   
    InitializeCriticalSection(&g_csProducer_Consumer);    //��ʼ���ٽ���   
    g_hSemaphoreBufferEmpty = CreateSemaphore(NULL, BUFFER_SIZE, BUFFER_SIZE, NULL); //����һ���ź�������ʾ�ջ���������
    g_hSemaphoreBufferFull = CreateSemaphore(NULL, 0, BUFFER_SIZE, NULL); //����һ���ź�������ʾ�в�Ʒ�Ļ���������

    const int THREAD_NUM = 3;
    HANDLE thread_handle[THREAD_NUM];

    //�ں˶���İ�ȫ����ΪĬ�����á��߳̿ռ�ΪĬ�ϴ�С���߳����Ϊ����Ϊproducer�������̺߳������ݲ����������ظ��߳�ID��
    thread_handle[0] = CreateThread(NULL, 0, producer, NULL, 0, NULL);

    //�ں˶���İ�ȫ����ΪĬ�����á��߳̿ռ�ΪĬ�ϴ�С���߳����Ϊ����Ϊproducer�������̺߳������ݲ����������ظ��߳�ID��
    thread_handle[1] = CreateThread(NULL, 0, consumer, NULL, 0, NULL);
    thread_handle[2] = CreateThread(NULL, 0, consumer, NULL, 0, NULL);

    WaitForMultipleObjects(THREAD_NUM, thread_handle, TRUE, INFINITE); //�ȴ�thread_handle�е������߳̽���
    for (int i = 0; i < THREAD_NUM; i++) //�����߳̾��
        CloseHandle(thread_handle[i]);

    DeleteCriticalSection(&g_csThreadCode);    //�����ٽ���
    DeleteCriticalSection(&g_csProducer_Consumer);    //�����ٽ���
    CloseHandle(g_hSemaphoreBufferEmpty); //�����ź������
    CloseHandle(g_hSemaphoreBufferFull);

    system("pause"); //��ͣ�Բ鿴��ʾ����
    return 0;
}
