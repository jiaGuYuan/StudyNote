/*********************客户端的主程序*************************************/
#define  _CRT_SECURE_NO_WARNINGS	//为了使用某些CRT函数
#pragma comment(lib,"ws2_32.lib")   //加载动态链接库ws2_32.lib
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

	if (argc != 3) //执行的参数格式
	{
		fprintf(stderr, "usage: %s <host> <port>\n", argv[0]);
		exit(0);
	}
	
	host = argv[1];		/*  第一个参数为主机地址(参数个数从0开始算)  */
	port = atoi(argv[2]);  /*  第二个参数为主机端口号(参数个数从0开始算)  */

	//printf("%d %s   %d\n", argc, host, port);

	char sendBuf[1024] = "", recvBuf[1024] = "";

	while (1) //发送数据，输入quit停止发送
	{
		//与运行在主机hostnameh上的服务器建立一个连接，并向知名监听端口port发连接请求
		if ((clientfd = open_clientfd(host, port)) < 0) 
		{
			printf("ERROR: open_clientfd()\n");
			system("pause");
			return -1;
		}
			
		//发送数据
		printf("输入要发送到server的数据:");
		fgets(sendBuf, 1024, stdin);	//从流中读一行或指定个字符(包括行尾的'\n',并自动加上字符串结束符'\0')
		
		if (strcmp(sendBuf, "quit\n")==0)
			break;

		printf("haha:%s,%d\n", sendBuf,strcmp(sendBuf, "quit"));

		send(clientfd, sendBuf, strlen(sendBuf) + 1, 0);

		printf("server返回的数据:");
		recv(clientfd, recvBuf, 1024, 0);
		printf("%s\n", recvBuf);
		WSACleanup();

	}

	closesocket(clientfd);
	
	system("pause");
}

/*	函数名：open_clientfd(char *hostname, int port)
功能： 与运行在主机hostnameh上的服务器建立一个连接，并向知名监听端口port发连接请求
参数： hostname：要访问的服务器IP的点分十进制表示或域名表示， port：要访问的服务器的端口
返回： 一个打开的套接字描述符	(非负)， 出错返回-1
*/
int open_clientfd(char *hostname, int port)
{
	struct WSAData wsaData;
	if (WSAStartup(MAKEWORD(1, 1), &wsaData) != 0) //加载套接字库并协商版本
	{
		printf("Error: 加载套接字库\n");
		return -1;
	}
	//判断版本设置是否正确
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1)
	{
		WSACleanup(); //释放所有由Winsock分配的资源
		printf("版本设置不正确\n");
		return -1;
	}

	SOCKET clientfd;
	if ((clientfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		printf("ERROR: socket()\n");
		return -1;
	}
	//printf("clientfd = %d\n", clientfd);

	struct hostent *hp;	//主机条目
	unsigned int addr;
	if ((addr = inet_addr(hostname))  != INADDR_NONE) //若是点分十进制表示的IP
	{
		hp = gethostbyaddr((const char *)&addr, sizeof(addr), AF_INET);
		if (NULL == hp)
		{
			printf("ERROR:IP不正确\n");
			return -1;
		}
	}
	else  if ((hp = gethostbyname(hostname)) == NULL)//若是域名， 获取域名对应的主机条目
	{
		printf("ERROR:IP不正确\n");
		return -1;
	}
	
	struct sockaddr_in serveraddr; //要访问的服务器套接字地址结构
	memset(&serveraddr, 0, sizeof(serveraddr));
	memcpy(&serveraddr.sin_addr.S_un.S_addr, hp->h_addr_list[0], sizeof(IN_ADDR));
	serveraddr.sin_family = AF_INET;
	serveraddr.sin_port = htons(789); //主机字节序-->网络字节序

	// 将客户端套接字（端口由内核临时分配）与 地址为serveraddr的服务器套接字建立一个因特网连接
	if (connect(clientfd, (SOCKADDR *)&serveraddr, sizeof(serveraddr)) != 0)
	{
		printf("ERROR: connect()\n");
		return -1;
	}
	return clientfd;
}


