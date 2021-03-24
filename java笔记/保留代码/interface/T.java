
public class T{
	public static void main(String[] args){
		//A a = new A(); 
		//a.show(); 

		//A a = new B(); //父类引用指向子类对象
		//a.show(); //此处发生多态
		//a.f();

		A c = new C(); //父类引用指向子类对象
		c.show(); //此处发生多态
		c.f();
	}
}