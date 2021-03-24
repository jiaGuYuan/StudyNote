#include <winsock2.h>
#pragma comment(lib,"ws2_32.lib") //加载动态链接库ws2_32.lib

//函数功能：创建线程
//函数返回值：成功返回新线程的句柄，失败返回NULL。
HANDLE WINAPI CreateThread(
LPSECURITY_ATTRIBUTES lpThreadAttributes,	//表示线程内核对象的安全属性，
 							//一般传入NULL表示使用默认设置
SIZE_T dwStackSize,	//参数表示线程栈空间大小。传入0表示使用默认大小（1MB）
LPTHREAD_START_ROUTINE lpStartAddress,	//表示新线程所执行的线程函数地址，
							//多个线程可以使用同一个函数地址。
LPVOID lpParameter,	//传给线程函数的参数
DWORD dwCreationFlags,	//指定额外的标志来控制线程的创建，为0表示线程创建之后
				//立即就可以进行调度，如果为CREATE_SUSPENDED则表示线程
				//创建后暂停运行，这样它就无法调度，直到调用ResumeThread()。
LPDWORD lpThreadId		//返回线程的ID号，传入NULL表示不需要返回该线程ID号。
);

//如果在代码中有使用标准C运行库中的函数时，尽量使用_beginthreadex()来代替CreateThread()。

_MCRTIMP uintptr_t __cdecl _beginthreadex(
	void *security,  //安全属性
	unsigned stacksize,  //栈空间大小
	unsigned (__CLR_OR_STD_CALL * initialcode) (void *),  //新线程所执行的线程函数的地址
	void * argument,  //线程函数的参数
	unsigned createflag,
	unsigned *thrdaddr  //线程的ID号
)



//函数功能(等待单个事件用：WaitForSingleObjects)：等待函数 – 使线程进入等待状态，
//	直到指定的内核对象被触发、或超时 。当我们调用该函数请求一个互斥对象时，会判断请求
//	互斥对象的线程ID 号与拥有互斥对象的线程ID号是否相等，若相等既使互斥对象处于
//	未触发状态 WaitForSingleObject 也能请求到互斥对象 
//函数返回值：在指定的时间内对象被触发，函数返回WAIT_OBJECT_0。 超过最长等待时间对象
//		  仍未被触发返回WAIT_TIMEOUT。传入参数有错误将返回WAIT_FAILED
DWORD WINAPI WaitForSingleObject(
HANDLEhHandle,		//为要等待的内核对象
DWORDdwMilliseconds	//为最长等待的时间，以毫秒为单位，如传入5000就表示5秒，
				//传入0就立即返回，传入INFINITE表示无限等待。
);

//等待多个事件用：WaitForMultipleObjects
DWORD WaitForMultipleObjects(
  DWORD nCount,             // number of handles in the handle array
  CONST HANDLE *lpHandles,  // pointer to the object-handle array
  BOOL fWaitAll,            // wait flag
  DWORD dwMilliseconds      // time-out interval in milliseconds
);
/*
其中参数

nCount 句柄的数量 最大值为MAXIMUM_WAIT_OBJECTS（64）

HANDLE 句柄数组的指针。

HANDLE 类型可以为（Event，Mutex，Process，Thread，Semaphore ）数组

BOOL bWaitAll 	等待的类型，如果为TRUE 则等待所有信号量有效在往下执行，
			FALSE 当有其中一个信号量有效时就向下执行

DWORD dwMilliseconds 超时时间 超时后向执行。 如果为WSA_INFINITE 永不超时。
			   如果没有信号量就会在这死等。
*/





//下面列出一些常用的原子操作Interlocked系列函数：

//1.增减操作
LONG__cdecl InterlockedIncrement(LONG volatile* Addend);
LONG__cdecl InterlockedDecrement(LONG volatile* Addend);
//返回变量执行增减操作之后的值。
LONG__cdec InterlockedExchangeAdd(LONG volatile* Addend, LONGValue);
//返回运算后的值，注意！加个负数就是减。

//2.赋值操作
LONG__cdeclInterlockedExchange(LONG volatile* Target, LONGValue);
//Value就是新值，函数会返回原先的值。





/*关键段CRITICAL_SECTION一共就四个函数，使用很是方便。
 下面是这四个函数的原型和使用说明。
函数功能：初始化
函数原型：*/
void InitializeCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//函数说明：定义关键段变量后必须初始化。

//函数功能：销毁
//函数原型：
void DeleteCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//函数说明：用完之后记得销毁。

//函数功能：进入关键区域：等待指定临界区对象的所有权 
//等待到指定的对象时返回，否则一直等待 
void EnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//函数说明：系统保证各线程互斥的进入关键区域。

//函数功能：离开关关键区域
//函数原型：
void LeaveCriticalSection(LPCRITICAL_SECTION lpCriticalSection);


//关键段CRITICAL_SECTION的定义吧，它在WinBase.h中被定义成RTL_CRITICAL_SECTION。
//而RTL_CRITICAL_SECTION在WinNT.h中声明，它其实是个结构体：
typedef struct _RTL_CRITICAL_SECTION {
PRTL_CRITICAL_SECTION_DEBUG DebugInfo; //调试信息
LONG LockCount;	//
LONG RecursionCount; //
HANDLE OwningThread; // from the thread's ClientId->UniqueThread
HANDLE LockSemaphore;  //
DWORD SpinCount;  //
} RTL_CRITICAL_SECTION, *PRTL_CRITICAL_SECTION;
 /*	各个参数的解释如下：
	第一个参数：PRTL_CRITICAL_SECTION_DEBUG DebugInfo;
		调试用的。
	第二个参数：LONG LockCount;
		初始化为-1，n表示有n个线程在等待。
	第三个参数：LONG RecursionCount;
		表示该关键段的拥有线程对此资源获得关键段次数，初为0。
	第四个参数：HANDLE OwningThread;
		即拥有该关键段的线程句柄，
		微软对其注释为——from the thread's ClientId->UniqueThread
	第五个参数：HANDLE LockSemaphore;
		实际上是一个自复位事件。
	第六个参数：DWORD SpinCount;
		旋转锁的设置，单CPU下忽略 */

：
HANDLE CreateEvent(
LPSECURITY_ATTRIBUTESl pEventAttributes,
BOOL bManualReset,
BOOL bInitialState,
LPCTSTR lpName
);
/*	函数说明：
	第一个参数LPSECURITY_ATTRIBUTESl pEventAttributes表示安全控制，
		一般直接传入NULL。
	第二个参数BOOL bManualReset确定事件是手动置位还是自动置位，
		传入TRUE表示手动置位，传入FALSE表示自动置位。
		如果为自动置位，则对该事件调用WaitForSingleObject()后会自动
		调用ResetEvent()使事件变成未触发状态。
	第三个参数BOOL bInitialState表示事件的初始状态，传入TRUE表示已触发。
	第四个参数LPCTSTR lpName表示事件的名称，传入NULL表示匿名事件。 */


HANDLE OpenEvent(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName     //名称
);
/*	函数说明：
	第一个参数DWORD dwDesiredAccess表示访问权限，对事件一般传入EVENT_ALL_ACCESS。
		详细解释可以查看MSDN文档。
	第二个参数BOOL bInheritHandle表示事件句柄继承性，一般传入TRUE即可。
	第三个参数LPCTSTR lpName表示名称，不同进程中的各线程可以通过名称来确保
		它们访问同一个事件。*/

BOOL SetEvent(HANDLE hEvent);
/*第三个SetEvent
函数功能：触发事件
函数原型：BOOLSetEvent(HANDLE hEvent);
函数说明：每次触发后，必有一个或多个处于等待状态下的线程变成可调度状态。*/

BOOL ResetEvent(HANDLE hEvent);
/*第四个ResetEvent
函数功能：将事件设为末触发
函数原型：BOOLResetEvent(HANDLE hEvent);

最后一个事件的清理与销毁
由于事件是内核对象，因此使用CloseHandle()就可以完成清理与销毁了。*/


PulseEvent();
/*	函数功能：在事件触发后立即将事件设置为未触发，相当于触发一个事件脉冲。
	函数原型：BOOLPulseEvent(HANDLE hEvent);
	函数说明：这是一个不常用的事件函数，此函数相当于SetEvent()
		   后立即调用ResetEvent();此时情况可以分为两种：
	1.对于手动置位事件，所有正处于等待状态下线程都变成可调度状态。
	2.对于自动置位事件，所有正处于等待状态下线程只有一个变成可调度状态。
	此后事件是末触发的。该函数不稳定，因为无法预知在调用PulseEvent ()时
	哪些线程正处于等待状态。


	最后总结下事件Event
	1．事件是内核对象，事件分为手动置位事件和自动置位事件。
         事件Event内部它包含一个使用计数（所有内核对象都有），
	   一个布尔值表示是手动置位事件还是自动置位事件，
	   另一个布尔值用来表示事件有无触发。
	2．事件可以由SetEvent()来触发，由ResetEvent()来设成未触发。
	   还可以由PulseEvent()来发出一个事件脉冲。
	3．事件可以解决线程间同步问题，因此也能解决互斥问题。 */


/*互斥量Mutex主要将用到四个函数
第一个 CreateMutex
函数功能：创建互斥量（注意与事件Event的创建函数对比）
函数原型：*/
HANDLE CreateMutex(
LPSECURITY_ATTRIBUTES lpMutexAttributes,
BOOL bInitialOwner,
LPCTSTR lpName
);
/*	函数说明：
	第一个参数LPSECURITY_ATTRIBUTES lpMutexAttributes表示安全控制，
	         一般直接传入NULL。
	第二个参数BOOL bInitialOwner用来确定互斥量的初始拥有者。
	      如果传入TRUE表示互斥量对象内部会记录创建它的线程的线程ID号并将
		递归计数设置为1，由于该线程ID非零，所以互斥量处于未触发状态。
		如果传入FALSE，那么互斥量对象内部的线程ID号将设置为NULL，
		递归计数设置为0，这意味互斥量不为任何线程占用，处于触发状态。
	第三个参数LPCTSTR lpName用来设置互斥量的名称，在多个进程中的线程就是
	      通过名称来确保它们访问的是同一个互斥量。
	函数返回值：
	成功返回一个表示互斥量的句柄，失败返回NULL。 */

//第二个打开互斥量
HANDLE OpenMutex(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName     //名称
);
/*	函数说明：
	第一个参数DWORD dwDesiredAccess表示访问权限，
	         对互斥量一般传入MUTEX_ALL_ACCESS。详细解释可以查看MSDN文档。
	第二个参数BOOL bInheritHandle表示互斥量句柄继承性，一般传入TRUE即可。
	第三个参数LPCTSTR lpName表示名称。某一个进程中的线程创建互斥量后，
	      其它进程中的线程就可以通过这个函数来找到这个互斥量。
	函数返回值：
	成功返回一个表示互斥量的句柄，失败返回NULL。*/

/* 第三个触发互斥量
函数原型：*/
BOOL ReleaseMutex (HANDLE hMutex);
/*	函数说明：
	访问互斥资源前应该要调用等待函数，结束访问就要调用ReleaseMutex()来
      表示自己已经结束访问，其它线程可以开始访问了。*/

CloseHandle();
/*最后一个清理互斥量
由于互斥量是内核对象，因此使用CloseHandle()就可以（这一点所有内核对象都一样）。*/








/*信号量Semaphore常用
第一个 CreateSemaphore
函数功能：创建信号量
函数原型：*/
HANDLE CreateSemaphore(
LPSECURITY_ATTRIBUTES lpSemaphoreAttributes,
LONG lInitialCount,
LONG lMaximumCount,
LPCTSTR lpName
);
/*	函数说明：
	第一个参数LPSECURITY_ATTRIBUTES lpSemaphoreAttributes表示安全控制，
	         一般直接传入NULL。
	第二个参数LONG lInitialCount表示初始资源数量。
	第三个参数LONG lMaximumCount表示信号最大数量。
	第四个参数LPCTSTR lpName表示信号量的名称，传入NULL表示匿名信号量。*/

/*第二个 OpenSemaphore
函数功能：打开信号量
函数原型：*/
HANDLE OpenSemaphore(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName
);
/*	函数说明：
	第一个参数DWORD dwDesiredAccess表示访问权限，对一般传入SEMAPHORE_ALL_ACCESS。
		    详细解释可以查看MSDN文档。
	第二个参数BOOL bInheritHandle表示信号量句柄继承性，一般传入TRUE即可。
	第三个参数LPCTSTR lpName表示名称，不同进程中的各线程可以通过名称来确保
		它们访问同一个信号量。*/


/*第三个 ReleaseSemaphore
函数功能：递增信号量的当前资源计数
函数原型：*/
BOOL ReleaseSemaphore(
HANDLE hSemaphore,
LONG lReleaseCount,
LPLONG lpPreviousCount
);
/*	函数说明：
	第一个参数HANDLE hSemaphore是信号量的句柄。
	第二个参数LONG lReleaseCount表示增加个数，必须大于0且不超过最大资源数量。
	第三个参数LPLONG lpPreviousCount可以用来传出先前的资源计数，
		设为NULL表示不需要传出。
	注意：当前资源数量大于0，表示信号量处于触发，
		等于0表示资源已经耗尽故信号量处于未触发。
	在对信号量调用等待函数时，等待函数会检查信号量的当前资源计数，
	如果大于0(即信号量处于触发状态)，
	减1后返回让调用线程继续执行。一个线程可以多次调用等待函数来减小信号量。*/

CloseHandle();
/*最后一个 信号量的清理与销毁
由于信号量是内核对象，因此使用CloseHandle()就可以完成清理与销毁了。*/


