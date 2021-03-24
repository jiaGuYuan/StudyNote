

public class ImplC implements InfA, InfB{
	public static void main(String[] args){
		ImplC c = new ImplC();
		c.fa1();
		c.fa2();
		c.fb1();
	}

	public void fa1(){
		System.out.println("fa1");
	}
	public void fa2(){
		System.out.println("fa2");
	}
	public void fb1(){
		System.out.println("fb1");
	}
}
