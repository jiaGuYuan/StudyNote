import phone_package.BasePhone;

public class ShowPhone {
	public static void main(String[] args){
		BasePhone phone = new HighPhone();
		show(phone);

		System.out.println(" ---------------------- ");
		phone = new MiddlePhone();
		show(phone);

		System.out.println(" --------------------- ");
		phone = new LowPhone();
		show(phone);
	}


	public static void show(BasePhone phone){
		phone.call();
		phone.sendMessages();
		phone.bluetooth();
		phone.camera();
		phone.gps();
	}
}