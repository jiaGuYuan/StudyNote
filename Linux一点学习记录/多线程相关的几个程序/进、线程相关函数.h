#include <winsock2.h>
#pragma comment(lib,"ws2_32.lib") //���ض�̬���ӿ�ws2_32.lib

//�������ܣ������߳�
//��������ֵ���ɹ��������̵߳ľ����ʧ�ܷ���NULL��
HANDLE WINAPI CreateThread(
LPSECURITY_ATTRIBUTES lpThreadAttributes,	//��ʾ�߳��ں˶���İ�ȫ���ԣ�
 							//һ�㴫��NULL��ʾʹ��Ĭ������
SIZE_T dwStackSize,	//������ʾ�߳�ջ�ռ��С������0��ʾʹ��Ĭ�ϴ�С��1MB��
LPTHREAD_START_ROUTINE lpStartAddress,	//��ʾ���߳���ִ�е��̺߳�����ַ��
							//����߳̿���ʹ��ͬһ��������ַ��
LPVOID lpParameter,	//�����̺߳����Ĳ���
DWORD dwCreationFlags,	//ָ������ı�־�������̵߳Ĵ�����Ϊ0��ʾ�̴߳���֮��
				//�����Ϳ��Խ��е��ȣ����ΪCREATE_SUSPENDED���ʾ�߳�
				//��������ͣ���У����������޷����ȣ�ֱ������ResumeThread()��
LPDWORD lpThreadId		//�����̵߳�ID�ţ�����NULL��ʾ����Ҫ���ظ��߳�ID�š�
);

//����ڴ�������ʹ�ñ�׼C���п��еĺ���ʱ������ʹ��_beginthreadex()������CreateThread()��

_MCRTIMP uintptr_t __cdecl _beginthreadex(
	void *security,  //��ȫ����
	unsigned stacksize,  //ջ�ռ��С
	unsigned (__CLR_OR_STD_CALL * initialcode) (void *),  //���߳���ִ�е��̺߳����ĵ�ַ
	void * argument,  //�̺߳����Ĳ���
	unsigned createflag,
	unsigned *thrdaddr  //�̵߳�ID��
)



//��������(�ȴ������¼��ã�WaitForSingleObjects)���ȴ����� �C ʹ�߳̽���ȴ�״̬��
//	ֱ��ָ�����ں˶��󱻴�������ʱ �������ǵ��øú�������һ���������ʱ�����ж�����
//	���������߳�ID ����ӵ�л��������߳�ID���Ƿ���ȣ�����ȼ�ʹ���������
//	δ����״̬ WaitForSingleObject Ҳ�����󵽻������ 
//��������ֵ����ָ����ʱ���ڶ��󱻴�������������WAIT_OBJECT_0�� ������ȴ�ʱ�����
//		  ��δ����������WAIT_TIMEOUT����������д��󽫷���WAIT_FAILED
DWORD WINAPI WaitForSingleObject(
HANDLEhHandle,		//ΪҪ�ȴ����ں˶���
DWORDdwMilliseconds	//Ϊ��ȴ���ʱ�䣬�Ժ���Ϊ��λ���紫��5000�ͱ�ʾ5�룬
				//����0���������أ�����INFINITE��ʾ���޵ȴ���
);

//�ȴ�����¼��ã�WaitForMultipleObjects
DWORD WaitForMultipleObjects(
  DWORD nCount,             // number of handles in the handle array
  CONST HANDLE *lpHandles,  // pointer to the object-handle array
  BOOL fWaitAll,            // wait flag
  DWORD dwMilliseconds      // time-out interval in milliseconds
);
/*
���в���

nCount ��������� ���ֵΪMAXIMUM_WAIT_OBJECTS��64��

HANDLE ��������ָ�롣

HANDLE ���Ϳ���Ϊ��Event��Mutex��Process��Thread��Semaphore ������

BOOL bWaitAll 	�ȴ������ͣ����ΪTRUE ��ȴ������ź�����Ч������ִ�У�
			FALSE ��������һ���ź�����Чʱ������ִ��

DWORD dwMilliseconds ��ʱʱ�� ��ʱ����ִ�С� ���ΪWSA_INFINITE ������ʱ��
			   ���û���ź����ͻ��������ȡ�
*/





//�����г�һЩ���õ�ԭ�Ӳ���Interlockedϵ�к�����

//1.��������
LONG__cdecl InterlockedIncrement(LONG volatile* Addend);
LONG__cdecl InterlockedDecrement(LONG volatile* Addend);
//���ر���ִ����������֮���ֵ��
LONG__cdec InterlockedExchangeAdd(LONG volatile* Addend, LONGValue);
//����������ֵ��ע�⣡�Ӹ��������Ǽ���

//2.��ֵ����
LONG__cdeclInterlockedExchange(LONG volatile* Target, LONGValue);
//Value������ֵ�������᷵��ԭ�ȵ�ֵ��





/*�ؼ���CRITICAL_SECTIONһ�����ĸ�������ʹ�ú��Ƿ��㡣
 ���������ĸ�������ԭ�ͺ�ʹ��˵����
�������ܣ���ʼ��
����ԭ�ͣ�*/
void InitializeCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//����˵��������ؼ��α���������ʼ����

//�������ܣ�����
//����ԭ�ͣ�
void DeleteCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//����˵��������֮��ǵ����١�

//�������ܣ�����ؼ����򣺵ȴ�ָ���ٽ������������Ȩ 
//�ȴ���ָ���Ķ���ʱ���أ�����һֱ�ȴ� 
void EnterCriticalSection(LPCRITICAL_SECTION lpCriticalSection);
//����˵����ϵͳ��֤���̻߳���Ľ���ؼ�����

//�������ܣ��뿪�عؼ�����
//����ԭ�ͣ�
void LeaveCriticalSection(LPCRITICAL_SECTION lpCriticalSection);


//�ؼ���CRITICAL_SECTION�Ķ���ɣ�����WinBase.h�б������RTL_CRITICAL_SECTION��
//��RTL_CRITICAL_SECTION��WinNT.h������������ʵ�Ǹ��ṹ�壺
typedef struct _RTL_CRITICAL_SECTION {
PRTL_CRITICAL_SECTION_DEBUG DebugInfo; //������Ϣ
LONG LockCount;	//
LONG RecursionCount; //
HANDLE OwningThread; // from the thread's ClientId->UniqueThread
HANDLE LockSemaphore;  //
DWORD SpinCount;  //
} RTL_CRITICAL_SECTION, *PRTL_CRITICAL_SECTION;
 /*	���������Ľ������£�
	��һ��������PRTL_CRITICAL_SECTION_DEBUG DebugInfo;
		�����õġ�
	�ڶ���������LONG LockCount;
		��ʼ��Ϊ-1��n��ʾ��n���߳��ڵȴ���
	������������LONG RecursionCount;
		��ʾ�ùؼ��ε�ӵ���̶߳Դ���Դ��ùؼ��δ�������Ϊ0��
	���ĸ�������HANDLE OwningThread;
		��ӵ�иùؼ��ε��߳̾����
		΢�����ע��Ϊ����from the thread's ClientId->UniqueThread
	�����������HANDLE LockSemaphore;
		ʵ������һ���Ը�λ�¼���
	������������DWORD SpinCount;
		��ת�������ã���CPU�º��� */

��
HANDLE CreateEvent(
LPSECURITY_ATTRIBUTESl pEventAttributes,
BOOL bManualReset,
BOOL bInitialState,
LPCTSTR lpName
);
/*	����˵����
	��һ������LPSECURITY_ATTRIBUTESl pEventAttributes��ʾ��ȫ���ƣ�
		һ��ֱ�Ӵ���NULL��
	�ڶ�������BOOL bManualResetȷ���¼����ֶ���λ�����Զ���λ��
		����TRUE��ʾ�ֶ���λ������FALSE��ʾ�Զ���λ��
		���Ϊ�Զ���λ����Ը��¼�����WaitForSingleObject()����Զ�
		����ResetEvent()ʹ�¼����δ����״̬��
	����������BOOL bInitialState��ʾ�¼��ĳ�ʼ״̬������TRUE��ʾ�Ѵ�����
	���ĸ�����LPCTSTR lpName��ʾ�¼������ƣ�����NULL��ʾ�����¼��� */


HANDLE OpenEvent(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName     //����
);
/*	����˵����
	��һ������DWORD dwDesiredAccess��ʾ����Ȩ�ޣ����¼�һ�㴫��EVENT_ALL_ACCESS��
		��ϸ���Ϳ��Բ鿴MSDN�ĵ���
	�ڶ�������BOOL bInheritHandle��ʾ�¼�����̳��ԣ�һ�㴫��TRUE���ɡ�
	����������LPCTSTR lpName��ʾ���ƣ���ͬ�����еĸ��߳̿���ͨ��������ȷ��
		���Ƿ���ͬһ���¼���*/

BOOL SetEvent(HANDLE hEvent);
/*������SetEvent
�������ܣ������¼�
����ԭ�ͣ�BOOLSetEvent(HANDLE hEvent);
����˵����ÿ�δ����󣬱���һ���������ڵȴ�״̬�µ��̱߳�ɿɵ���״̬��*/

BOOL ResetEvent(HANDLE hEvent);
/*���ĸ�ResetEvent
�������ܣ����¼���Ϊĩ����
����ԭ�ͣ�BOOLResetEvent(HANDLE hEvent);

���һ���¼�������������
�����¼����ں˶������ʹ��CloseHandle()�Ϳ�����������������ˡ�*/


PulseEvent();
/*	�������ܣ����¼��������������¼�����Ϊδ�������൱�ڴ���һ���¼����塣
	����ԭ�ͣ�BOOLPulseEvent(HANDLE hEvent);
	����˵��������һ�������õ��¼��������˺����൱��SetEvent()
		   ����������ResetEvent();��ʱ������Է�Ϊ���֣�
	1.�����ֶ���λ�¼������������ڵȴ�״̬���̶߳���ɿɵ���״̬��
	2.�����Զ���λ�¼������������ڵȴ�״̬���߳�ֻ��һ����ɿɵ���״̬��
	�˺��¼���ĩ�����ġ��ú������ȶ�����Ϊ�޷�Ԥ֪�ڵ���PulseEvent ()ʱ
	��Щ�߳������ڵȴ�״̬��


	����ܽ����¼�Event
	1���¼����ں˶����¼���Ϊ�ֶ���λ�¼����Զ���λ�¼���
         �¼�Event�ڲ�������һ��ʹ�ü����������ں˶����У���
	   һ������ֵ��ʾ���ֶ���λ�¼������Զ���λ�¼���
	   ��һ������ֵ������ʾ�¼����޴�����
	2���¼�������SetEvent()����������ResetEvent()�����δ������
	   ��������PulseEvent()������һ���¼����塣
	3���¼����Խ���̼߳�ͬ�����⣬���Ҳ�ܽ���������⡣ */


/*������Mutex��Ҫ���õ��ĸ�����
��һ�� CreateMutex
�������ܣ�������������ע�����¼�Event�Ĵ��������Աȣ�
����ԭ�ͣ�*/
HANDLE CreateMutex(
LPSECURITY_ATTRIBUTES lpMutexAttributes,
BOOL bInitialOwner,
LPCTSTR lpName
);
/*	����˵����
	��һ������LPSECURITY_ATTRIBUTES lpMutexAttributes��ʾ��ȫ���ƣ�
	         һ��ֱ�Ӵ���NULL��
	�ڶ�������BOOL bInitialOwner����ȷ���������ĳ�ʼӵ���ߡ�
	      �������TRUE��ʾ�����������ڲ����¼���������̵߳��߳�ID�Ų���
		�ݹ��������Ϊ1�����ڸ��߳�ID���㣬���Ի���������δ����״̬��
		�������FALSE����ô�����������ڲ����߳�ID�Ž�����ΪNULL��
		�ݹ��������Ϊ0������ζ��������Ϊ�κ��߳�ռ�ã����ڴ���״̬��
	����������LPCTSTR lpName�������û����������ƣ��ڶ�������е��߳̾���
	      ͨ��������ȷ�����Ƿ��ʵ���ͬһ����������
	��������ֵ��
	�ɹ�����һ����ʾ�������ľ����ʧ�ܷ���NULL�� */

//�ڶ����򿪻�����
HANDLE OpenMutex(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName     //����
);
/*	����˵����
	��һ������DWORD dwDesiredAccess��ʾ����Ȩ�ޣ�
	         �Ի�����һ�㴫��MUTEX_ALL_ACCESS����ϸ���Ϳ��Բ鿴MSDN�ĵ���
	�ڶ�������BOOL bInheritHandle��ʾ����������̳��ԣ�һ�㴫��TRUE���ɡ�
	����������LPCTSTR lpName��ʾ���ơ�ĳһ�������е��̴߳�����������
	      ���������е��߳̾Ϳ���ͨ������������ҵ������������
	��������ֵ��
	�ɹ�����һ����ʾ�������ľ����ʧ�ܷ���NULL��*/

/* ����������������
����ԭ�ͣ�*/
BOOL ReleaseMutex (HANDLE hMutex);
/*	����˵����
	���ʻ�����ԴǰӦ��Ҫ���õȴ��������������ʾ�Ҫ����ReleaseMutex()��
      ��ʾ�Լ��Ѿ��������ʣ������߳̿��Կ�ʼ�����ˡ�*/

CloseHandle();
/*���һ����������
���ڻ��������ں˶������ʹ��CloseHandle()�Ϳ��ԣ���һ�������ں˶���һ������*/








/*�ź���Semaphore����
��һ�� CreateSemaphore
�������ܣ������ź���
����ԭ�ͣ�*/
HANDLE CreateSemaphore(
LPSECURITY_ATTRIBUTES lpSemaphoreAttributes,
LONG lInitialCount,
LONG lMaximumCount,
LPCTSTR lpName
);
/*	����˵����
	��һ������LPSECURITY_ATTRIBUTES lpSemaphoreAttributes��ʾ��ȫ���ƣ�
	         һ��ֱ�Ӵ���NULL��
	�ڶ�������LONG lInitialCount��ʾ��ʼ��Դ������
	����������LONG lMaximumCount��ʾ�ź����������
	���ĸ�����LPCTSTR lpName��ʾ�ź��������ƣ�����NULL��ʾ�����ź�����*/

/*�ڶ��� OpenSemaphore
�������ܣ����ź���
����ԭ�ͣ�*/
HANDLE OpenSemaphore(
DWORD dwDesiredAccess,
BOOL bInheritHandle,
LPCTSTR lpName
);
/*	����˵����
	��һ������DWORD dwDesiredAccess��ʾ����Ȩ�ޣ���һ�㴫��SEMAPHORE_ALL_ACCESS��
		    ��ϸ���Ϳ��Բ鿴MSDN�ĵ���
	�ڶ�������BOOL bInheritHandle��ʾ�ź�������̳��ԣ�һ�㴫��TRUE���ɡ�
	����������LPCTSTR lpName��ʾ���ƣ���ͬ�����еĸ��߳̿���ͨ��������ȷ��
		���Ƿ���ͬһ���ź�����*/


/*������ ReleaseSemaphore
�������ܣ������ź����ĵ�ǰ��Դ����
����ԭ�ͣ�*/
BOOL ReleaseSemaphore(
HANDLE hSemaphore,
LONG lReleaseCount,
LPLONG lpPreviousCount
);
/*	����˵����
	��һ������HANDLE hSemaphore���ź����ľ����
	�ڶ�������LONG lReleaseCount��ʾ���Ӹ������������0�Ҳ����������Դ������
	����������LPLONG lpPreviousCount��������������ǰ����Դ������
		��ΪNULL��ʾ����Ҫ������
	ע�⣺��ǰ��Դ��������0����ʾ�ź������ڴ�����
		����0��ʾ��Դ�Ѿ��ľ����ź�������δ������
	�ڶ��ź������õȴ�����ʱ���ȴ����������ź����ĵ�ǰ��Դ������
	�������0(���ź������ڴ���״̬)��
	��1�󷵻��õ����̼߳���ִ�С�һ���߳̿��Զ�ε��õȴ���������С�ź�����*/

CloseHandle();
/*���һ�� �ź���������������
�����ź������ں˶������ʹ��CloseHandle()�Ϳ�����������������ˡ�*/


