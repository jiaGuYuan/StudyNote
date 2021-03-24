package phone_package;

/**
 * BasePhone 手机功能和属性抽象
 */
public abstract class BasePhone implements InfPhoneFunc{

	private String color;

	public String getColor(){
		return color;
	}

	public void setColor(String color){
		this.color = color;
	}
}