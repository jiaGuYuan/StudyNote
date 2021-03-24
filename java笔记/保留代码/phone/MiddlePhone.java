import phone_package.BaseMiddlePhone;

public class MiddlePhone extends BaseMiddlePhone{
	public void call(){
		System.out.println("MiddlePhone call");
	}
	public void sendMessages(){
		System.out.println("MiddlePhone sendMessages");
	}


	public void bluetooth(){
		System.out.println("MiddlePhone bluetooth");
	}
	public void camera(){
		System.out.println("MiddlePhone camera");
	}
}