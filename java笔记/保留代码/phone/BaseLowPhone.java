package phone_package;

/**
 * BaseLowPhone 低端手机抽象
 */
public abstract class BaseLowPhone extends BaseMiddlePhone{
	// 低端手机庵割了gps,bluetooth,camera功能
	public void gps(){
		//System.out.println("default gps");
	}

	public void bluetooth(){
		//System.out.println("default bluetooth");
	}

	public void camera(){
		//System.out.println("default camera");
	}
}