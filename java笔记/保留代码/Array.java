public class Array{
 	public static void main(String[] args){
 		System.out.println("\n--------------基本类型数组自动初始化--------------");
 		int[] array = new int[5];
 		for(int i=0; i<array.length; i++){
 			System.out.print(array[i]+"\t");
 		}
 		


		System.out.println("\n\n--------------未初始化对象数组--------------");
 		Array[] array1 = new Array[5];
 		for(int i=0; i<array1.length; i++){
 			System.out.print(array1[i]+"\t");
 		}
 		// for(int i=0; i<array2.length; i++){
 		// 	System.out.print(array2[i].getStr()+"\t"); //对象数组元素未初始化,所以这里访问会导致空指针异常
 		// }


 		System.out.println("\n\n--------------已初始化对象数组--------------");
 		Array[] array2 = new Array[5];  //创建数组对象, 此时数组的引用元素未初始化--指向空
 		for(int i=0; i<array2.length; i++){
 			array2[i] = new Array("haha_"+i); //初始化数组对象的引用元素
 		}
 		for(int i=0; i<array2.length; i++){
 			System.out.print(array2[i]+"\t");
 		}
 		System.out.println();
 		for(int i=0; i<array2.length; i++){
 			System.out.print(array2[i].getStr()+"\t");
 		}
 	}

 	private String str;

 	public Array(String str){
 		this.str = str;
 	}

 	public String getStr(){
 		return this.str;
 	}
 }


 /*
java中基本类型创建在栈上.
java中所有对象创建在堆上,所有引用创建在栈上,栈上的引用指向堆上的对象.
java中的引用(变量)相当于对C++中的指针的封装,在引用对象实例时自动进行了一次解引用操作.
java中数组(数组名)是一个引用.
java: ClassType value = new ClassType(); --在堆上new出一个ClassType实例, 在栈上创建它的引用(由value来引用它).
类比c++. ClassType *pvalue = new ClassType(); --在堆上new出一个ClassType实例, 在栈上创建一个指针来指向它.
value.member = x; 相当于c++的(*pvalue).member = x

Java的数组引用.
Type array[] = new Type[N]; --在堆上new出一个数组对象实例, 在栈上创建它的引用(由array来引用).**注意: 此时数组中的元素是空指向的. **
当Type是基本类型时,相当于c++的 Type *parray = new Type[N];
当Type是引用类型(类类型)时,相当于C++的 Type **pparray = new *Type[N];

对象数组中存放的并不是对象本身，而是对象的引用.

java中除了基本类型就是引用类型. 


java没有无符号类型.

java中的对象数组中存放的是对象的引用. (数组元素是对象的引用)
如: MyClass[] array = new MyClass[N];  //在栈上创建一个数组引用,在堆上创建一个数组对象. 并使引用指向对象
    for(int i=0; i<array.length; i++)
    {
    	array[i] = new MyClass(); //初始化数组的引用元素
    }
    一个完整的数组需要了三个空间: (1)栈上的数组引用 (2)堆上的数组对象 (3)堆上的MyClass对象
    栈上的array数组引用,指向堆中的数组对象; 而数组对象中的元素是MyClass对象的引用.


  */