/********************��������������*************************************/
#define  _CRT_SECURE_NO_WARNINGS	 //Ϊ��ʹ��ĳЩCRT����
#pragma    comment(lib,"ws2_32.lib") //���ض�̬���ӿ�ws2_32.lib
#include <stdio.h>
#include <winsock2.h>
#include <windows.h>

int open_listenfd(int port);
int main()
{
	SOCKET listenfd;
	if ((listenfd = open_listenfd(789)) < 0)
	{
		printf("ERROR: open_lientfd()\n");
		return -1;
	}
	
	char sendBuf[1024] = "", recvBuf[1024] = "";
	SOCKADDR_IN  clientaddr;
	int len = sizeof(SOCKADDR_IN);
	while (1)
	{
		SOCKET connfd;
		// �ȴ��ͻ����������󣬲�����һ�������׽���
		if ((connfd = accept(listenfd, (SOCKADDR *)&clientaddr, &len)) < 0)
		{
			printf("ERROR: accept()\n");
			continue;
		}
		//printf("connfd = %d\n", connfd);

		recv(connfd, recvBuf, 1024, 0);
		printf("�������յ���%s\n", recvBuf);

		//sprintf(sendBuf, "hello worclientaddr = %s", inet_ntoa(clientaddr.sin_addr));
		printf("���������͸�client�����ݣ�Hello client\n");
		sprintf(sendBuf, "Hello client\n ");
		send(connfd, sendBuf, strlen(sendBuf) + 1, 0);

		closesocket(connfd);
	}

	closesocket(listenfd);
	WSACleanup(); //�ͷ�������Winsock�������Դ
	system("pause");
}

/*	��������open_listenfd(int port)
���ܣ� �򿪺ͷ���һ�����������������������׼������֪���˿�prot�Ͻ�����������
������ port�����ڼ�����֪���˿�
���أ� ����һ������������(�Ǹ�)�� ������-1
*/
int open_listenfd(int port)
{
	struct WSAData wsaData;
	if (WSAStartup(MAKEWORD(1, 1), &wsaData) != 0) //�����׽��ֿⲢЭ�̰汾
	{
		printf("Error: �����׽��ֿ�\n");
		return -1;
	}
	//�жϰ汾�����Ƿ���ȷ
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1) //LOBYTE��HIBYTE���������������ֽ����µ�
	{
		WSACleanup(); //�ͷ�������Winsock�������Դ
		printf("�汾���ò���ȷ\n");
		return -1;
	}

	SOCKET listenfd; //�����׽���������

	// �����׽��֣� ����TCP��SOCK_STREAM��
	if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		printf("ERROR: socket()\n");
		return -1;
	}
	//printf("listenfd = %d\n", listenfd);

	struct sockaddr_in serveraddr; //Ҫ���ʵķ������׽��ֵ�ַ�ṹ
	memset(&serveraddr, 0, sizeof(serveraddr));
	serveraddr.sin_family = AF_INET;  //sin_family���������ֽ����
	serveraddr.sin_addr.S_un.S_addr = htonl(INADDR_ANY); //htonl:�������ֽ���תΪ�����ֽ���(long)
	serveraddr.sin_port = htons((unsigned short)port); //ָ���˿ںţ������ֽ���-->�����ֽ���

	//���׽��ְ󶨵�һ�����ص�ַ�Ͷ˿���
	if (bind(listenfd, (SOCKADDR *)&serveraddr, sizeof(serveraddr)) < 0)
	{
		printf("ERROR: bind()\n");
		return -1;
	}

	//���׽�������Ϊ����ģʽ;�ȴ����Ӷ��е����ֵΪ5
	if (listen(listenfd, 5) < 0)
	{
		printf("ERROR: listen()\n");
		return -1;
	}

	return listenfd;
}

