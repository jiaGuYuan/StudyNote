/*********************�ͻ��˵�������*************************************/
#define  _CRT_SECURE_NO_WARNINGS	//Ϊ��ʹ��ĳЩCRT����
#pragma comment(lib,"ws2_32.lib")   //���ض�̬���ӿ�ws2_32.lib
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <windows.h>
#include <string.h>


int open_clientfd(char *hostname, int port);
int main(int argc, char **argv)
{
	int clientfd, port;
	char *host = NULL;

	if (argc != 3) //ִ�еĲ�����ʽ
	{
		fprintf(stderr, "usage: %s <host> <port>\n", argv[0]);
		exit(0);
	}
	
	host = argv[1];		/*  ��һ������Ϊ������ַ(����������0��ʼ��)  */
	port = atoi(argv[2]);  /*  �ڶ�������Ϊ�����˿ں�(����������0��ʼ��)  */

	//printf("%d %s   %d\n", argc, host, port);

	char sendBuf[1024] = "", recvBuf[1024] = "";

	while (1) //�������ݣ�����quitֹͣ����
	{
		//������������hostnameh�ϵķ���������һ�����ӣ�����֪�������˿�port����������
		if ((clientfd = open_clientfd(host, port)) < 0) 
		{
			printf("ERROR: open_clientfd()\n");
			system("pause");
			return -1;
		}
			
		//��������
		printf("����Ҫ���͵�server������:");
		fgets(sendBuf, 1024, stdin);	//�����ж�һ�л�ָ�����ַ�(������β��'\n',���Զ������ַ���������'\0')
		
		if (strcmp(sendBuf, "quit\n")==0)
			break;

		printf("haha:%s,%d\n", sendBuf,strcmp(sendBuf, "quit"));

		send(clientfd, sendBuf, strlen(sendBuf) + 1, 0);

		printf("server���ص�����:");
		recv(clientfd, recvBuf, 1024, 0);
		printf("%s\n", recvBuf);
		WSACleanup();

	}

	closesocket(clientfd);
	
	system("pause");
}

/*	��������open_clientfd(char *hostname, int port)
���ܣ� ������������hostnameh�ϵķ���������һ�����ӣ�����֪�������˿�port����������
������ hostname��Ҫ���ʵķ�����IP�ĵ��ʮ���Ʊ�ʾ��������ʾ�� port��Ҫ���ʵķ������Ķ˿�
���أ� һ���򿪵��׽���������	(�Ǹ�)�� ������-1
*/
int open_clientfd(char *hostname, int port)
{
	struct WSAData wsaData;
	if (WSAStartup(MAKEWORD(1, 1), &wsaData) != 0) //�����׽��ֿⲢЭ�̰汾
	{
		printf("Error: �����׽��ֿ�\n");
		return -1;
	}
	//�жϰ汾�����Ƿ���ȷ
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1)
	{
		WSACleanup(); //�ͷ�������Winsock�������Դ
		printf("�汾���ò���ȷ\n");
		return -1;
	}

	SOCKET clientfd;
	if ((clientfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		printf("ERROR: socket()\n");
		return -1;
	}
	//printf("clientfd = %d\n", clientfd);

	struct hostent *hp;	//������Ŀ
	unsigned int addr;
	if ((addr = inet_addr(hostname))  != INADDR_NONE) //���ǵ��ʮ���Ʊ�ʾ��IP
	{
		hp = gethostbyaddr((const char *)&addr, sizeof(addr), AF_INET);
		if (NULL == hp)
		{
			printf("ERROR:IP����ȷ\n");
			return -1;
		}
	}
	else  if ((hp = gethostbyname(hostname)) == NULL)//���������� ��ȡ������Ӧ��������Ŀ
	{
		printf("ERROR:IP����ȷ\n");
		return -1;
	}
	
	struct sockaddr_in serveraddr; //Ҫ���ʵķ������׽��ֵ�ַ�ṹ
	memset(&serveraddr, 0, sizeof(serveraddr));
	memcpy(&serveraddr.sin_addr.S_un.S_addr, hp->h_addr_list[0], sizeof(IN_ADDR));
	serveraddr.sin_family = AF_INET;
	serveraddr.sin_port = htons(789); //�����ֽ���-->�����ֽ���

	// ���ͻ����׽��֣��˿����ں���ʱ���䣩�� ��ַΪserveraddr�ķ������׽��ֽ���һ������������
	if (connect(clientfd, (SOCKADDR *)&serveraddr, sizeof(serveraddr)) != 0)
	{
		printf("ERROR: connect()\n");
		return -1;
	}
	return clientfd;
}


