/********************服务器的主程序*************************************/
#define  _CRT_SECURE_NO_WARNINGS	 //为了使用某些CRT函数
#pragma    comment(lib,"ws2_32.lib") //加载动态链接库ws2_32.lib
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
		// 等待客户端连接请求，并返回一个连接套接字
		if ((connfd = accept(listenfd, (SOCKADDR *)&clientaddr, &len)) < 0)
		{
			printf("ERROR: accept()\n");
			continue;
		}
		//printf("connfd = %d\n", connfd);

		recv(connfd, recvBuf, 1024, 0);
		printf("服务器收到：%s\n", recvBuf);

		//sprintf(sendBuf, "hello worclientaddr = %s", inet_ntoa(clientaddr.sin_addr));
		printf("服务器发送给client的数据：Hello client\n");
		sprintf(sendBuf, "Hello client\n ");
		send(connfd, sendBuf, strlen(sendBuf) + 1, 0);

		closesocket(connfd);
	}

	closesocket(listenfd);
	WSACleanup(); //释放所有由Winsock分配的资源
	system("pause");
}

/*	函数名：open_listenfd(int port)
功能： 打开和返回一个监听描述符，这个描述符准备好在知名端口prot上接收连接请求
参数： port：用于监听的知名端口
返回： 返回一个监听描述符(非负)， 出错返回-1
*/
int open_listenfd(int port)
{
	struct WSAData wsaData;
	if (WSAStartup(MAKEWORD(1, 1), &wsaData) != 0) //加载套接字库并协商版本
	{
		printf("Error: 加载套接字库\n");
		return -1;
	}
	//判断版本设置是否正确
	if (LOBYTE(wsaData.wVersion) != 1 || HIBYTE(wsaData.wVersion) != 1) //LOBYTE、HIBYTE宏是作用在网络字节序下的
	{
		WSACleanup(); //释放所有由Winsock分配的资源
		printf("版本设置不正确\n");
		return -1;
	}

	SOCKET listenfd; //监听套接字描述符

	// 创建套接字， 基于TCP（SOCK_STREAM）
	if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		printf("ERROR: socket()\n");
		return -1;
	}
	//printf("listenfd = %d\n", listenfd);

	struct sockaddr_in serveraddr; //要访问的服务器套接字地址结构
	memset(&serveraddr, 0, sizeof(serveraddr));
	serveraddr.sin_family = AF_INET;  //sin_family不是网络字节序的
	serveraddr.sin_addr.S_un.S_addr = htonl(INADDR_ANY); //htonl:将主机字节序转为网络字节序(long)
	serveraddr.sin_port = htons((unsigned short)port); //指定端口号：主机字节序-->网络字节序

	//将套接字绑定到一个本地地址和端口上
	if (bind(listenfd, (SOCKADDR *)&serveraddr, sizeof(serveraddr)) < 0)
	{
		printf("ERROR: bind()\n");
		return -1;
	}

	//将套接字设置为监听模式;等待连接队列的最大值为5
	if (listen(listenfd, 5) < 0)
	{
		printf("ERROR: listen()\n");
		return -1;
	}

	return listenfd;
}

