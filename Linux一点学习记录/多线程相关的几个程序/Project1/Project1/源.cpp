#include <stdio.h>
#include <stdlib.h>
int main()
{
	char *p1 = "hello";
	char *p2 = "hello";

	if (p1 == p2)
	{
		printf("YES\n");
	}
	else
	{
		printf("NO\n");
	}
	
	system("pause");
	return 0;
}