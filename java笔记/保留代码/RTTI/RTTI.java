import java.lang.reflect.*;
import java.io.*;
import java.util.*;

class StripQualifiers {
	private StreamTokenizer st;
	public StripQualifiers(String qualified) {
		st = new StreamTokenizer(new StringReader(qualified));
		st.ordinaryChar(' '); // Keep the spaces
	}
	public String getNext() {
		String s = null;
		try {
			if(st.nextToken() != StreamTokenizer.TT_EOF) {
				switch(st.ttype) {
					case StreamTokenizer.TT_EOL:
						s = null;
						break;
					case StreamTokenizer.TT_NUMBER:
						s = Double.toString(st.nval);
						break;
					case StreamTokenizer.TT_WORD:
						s = new String(st.sval);
						break;
					default: // single character in ttype
						s = String.valueOf((char)st.ttype);
				}
			}
		} catch(IOException e) {
			System.out.println(e);
		}
		return s;
	}

	public static String strip(String qualified) {
		StripQualifiers sq = new StripQualifiers(qualified);
		String s = "", si;
		while((si = sq.getNext()) != null) {
			int lastDot = si.lastIndexOf('.');
			if(lastDot != -1)
				si = si.substring(lastDot + 1);
			s += si;
		}
		return s;
	}
}

class ShowMethods {
	static final String usage =	"用法:\n" +
								"ShowMethods qualified.class.name\t" +
								"显示类的所有的方法\n"+
								"ShowMethods qualified.class.name word \t" +
								"寻找涉及指定单词的方法";
	

	// 显示类的public方法和public构造　--显示完整类路径名
	public static void showMethods(String[] args){
		if(args.length < 1) {
			System.out.println(usage);
			System.exit(0);
		}
		try {
			Class c = Class.forName(args[0]);
			Method[] m = c.getMethods(); 
			Constructor[] ctor = c.getConstructors();
			if(args.length == 1) {
				System.out.println("--------public methods-----------");
				for (int i = 0; i < m.length; i++)
					System.out.println(m[i].toString());
				System.out.println("--------public constructor-----------");
				for (int i = 0; i < ctor.length; i++)
					System.out.println(ctor[i].toString());
			}else {
				System.out.println("--------public methods-----------");
				for (int i = 0; i < m.length; i++)
					if(m[i].toString().indexOf(args[1])!= -1)
						System.out.println(m[i].toString());
				System.out.println("--------public constructor-----------");
				for (int i = 0; i < ctor.length; i++)
					if(ctor[i].toString().indexOf(args[1])!= -1)
						System.out.println(ctor[i].toString());
			}
		} catch (ClassNotFoundException e) {
			System.out.println("No such class: " + e);
		}
	}


	// 显示类的public方法和public构造　--显示简单类名
	public static void showMethods2(String[] args){
		if(args.length < 1) {
			System.out.println(usage);
			System.exit(0);
		}
		try {
			Class c = Class.forName(args[0]);
			Method[] m = c.getMethods(); 
			Constructor[] ctor = c.getConstructors();
			String[] n = new String[m.length + ctor.length];
			
			for (int i = 0; i < m.length; i++){
				String str = m[i].toString();
				n[i] = StripQualifiers.strip(str);
			}
			
			for (int i = 0; i < ctor.length; i++){
				String str = ctor[i].toString();
				n[i+m.length] = StripQualifiers.strip(str);
			}

			System.out.println("--------public methods-----------");
			if(args.length == 1){
				for(int i=0; i<n.length; i++){
					if(i==m.length) System.out.println("--------public constructor-----------");
					System.out.println(n[i]);
				}
			}else{
				for(int i=0; i<n.length; i++){
					if(n[i].indexOf(args[1])!= -1){
						if(i==m.length) System.out.println("--------public constructor-----------");
						System.out.println(n[i]);
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("No such class: " + e);
		}
	}
	

	public static void main(String[] args) {
		ShowMethods.showMethods2(args);
	}
}


//打印出类的层次结构
class ClassStruct{
	public static void printStruct(Object obj){
		if(obj==null) 
			return;
		Class c = obj.getClass();
		Class s = c.getSuperclass();
		Vector<String> v = new Vector<String>();
		v.add(c.getName());
		
		while(s!=null){
			v.add(s.getName());
			s = s.getSuperclass();
		}

		String format = new String();
		for(int i = v.size()-1; i>=0; i--){
			System.out.println(format+v.get(i));
			format += "\t";
		}
		
	}

	public static void main(String[] args) throws IOException{

		//printStruct(new Integer(0));
		printStruct(new FileReader(new File("./RTTI.java")));
	}
}



//检查对象的类型
class TypeCheck{
	public static void typeCheck(Class c){
		if(c==null) 
			return;
		if(c.isArray())
			System.out.println(c.getName()+":是数组");
		else if(c.isEnum())
			System.out.println(c.getName()+":是枚举");
		else if(c.isPrimitive())
			System.out.println(c.getName()+":基本类型");
		else
			System.out.println(c.getName()+":对象引用");
	}

	public static void main(String[] args) throws IOException{

		//printStruct(new Integer(0));
		typeCheck(new char[1].getClass());
		typeCheck(new String().getClass());
		typeCheck(new Integer(0).getClass());
		typeCheck(int.class);
	}
}


public class RTTI{
	public static void main(String[] args) throws IOException{
		//ShowMethods.main(args);

		//ClassStruct.main(args);

		TypeCheck.main(args);
	}
}


