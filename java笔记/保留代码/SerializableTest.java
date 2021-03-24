import java.io.*;
import java.util.*;


//恢复序列化时报EOF异常---为什么 
public class SerializableTest implements Serializable {
	public static final int ONE = 1, TWO = 2, TREE = 3;
	private int num_0; //会被正常序列化--恢复为保存时的值
	private static int num_1 = ONE; //定义时初始化 --static不会被序列化--在恢复时初始化为定义时的值
	private static int num_2; //在构造中初始化
	
	public SerializableTest() {
		System.out.println("call SerializableTest()");
		num_0 = TREE;
		num_1 = TREE;
		num_2 = TREE;
	}

	//因为static成员不会被自动序列化, 如果需要,得我们自己动手
	public static void serializeStaticState(ObjectOutputStream os)
	throws IOException {
		os.writeInt(num_1);
		os.writeInt(num_2);
	}
	public static void deserializeStaticState(ObjectInputStream is)
	throws IOException {
		//读的顺序应与写的顺序一致
		num_1 = is.readInt();
		num_2 = is.readInt();
	}

	public String toString() {
		return getClass().toString() +" num_0["+this.num_0 +"] num_1[" +this.num_1+ "] num_2[" +this.num_2 +"] \n";
	}

	public static void serializeTest(String[] args) throws Exception{
		SerializableTest serial = null; 
		if(args.length == 0) {
			serial = new SerializableTest();
			ObjectOutputStream out = new ObjectOutputStream(
										new FileOutputStream("SerializableTest.out"));
			out.writeObject(serial);
			SerializableTest.serializeStaticState(out);
			out.close();//注释这句,看看会发生什么? --why(原因见下)
		}else { 
			ObjectInputStream in = new ObjectInputStream(
									new FileInputStream(args[0]));
			// 按照写序列化对象的顺序读出序列化对象
			serial = (SerializableTest)in.readObject(); //不会调用构造,直接基于二进制恢复
			SerializableTest.deserializeStaticState(in);
			in.close();
		}

		System.out.println(serial);
	}

	public static void main(String[] args) throws Exception {
		//serializeTest(args);
		//
		ExternalizableTest.main(args);
	}
}



class ExternalizableTest implements Externalizable {
	public static final int ONE = 1, TWO = 2;
	private int x;
	
	public ExternalizableTest() {
		System.out.println("call ExternalizableTest()");
		x = TWO;
	}

	public String toString() {
		return getClass().toString() +" x["+this.x +"] \n";
	}

	public void writeExternal(ObjectOutput out)
    throws IOException{
    	System.out.println("call writeExternal()");
		out.writeInt(x);    	
    }

    public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException{
    	System.out.println("call readExternal()");
    	x = in.readInt();
    }

	public static void main(String[] args) throws Exception {
		ExternalizableTest serial = null; 
		if(args.length == 0) {
			serial = new ExternalizableTest();
			ObjectOutputStream out = new ObjectOutputStream(
										new FileOutputStream("ExternalizableTest.out"));
			out.writeObject(serial); //会调用writeExternal()
			out.close();
		}else { 
			ObjectInputStream in = new ObjectInputStream(
									new FileInputStream(args[0]));
			// 按照写序列化对象的顺序读出序列化对象
			serial = (ExternalizableTest)in.readObject(); //调用构造,并调用readExternal()
			in.close();
		}

		System.out.println(serial);
	}
}


/*
writeObject(),只要调用，系统会自己就去将这个对象写入流并存入文件。
readInt()这种写入基本数据的则不是这样
基本数据以块数据记录的形式写入 ObjectOutputStream 中。块数据记录由头部和数据组成。
块数据部分包括标记和跟在部分后面的字节数。连续的基本写入数据被合并在一个块数据记录中。
块数据记录的分块因子为 1024 字节。每个块数据记录都将填满 1024 字节，才会被写入。
简单的说，只写一个100，是不够1024字节的，所以要手动调用close()才会写入。

另外，还有一种情况，就是在终止块数据模式时也可以写入基本数据。
当调用writeObject()是可以终止现在块数据记录的。
所以楼主把oos.writeInt(100);放到任一个oos.writeObject()之前，也是可以的。
（当然readInt()的位置也相应调一下） 

 */