


public class TypeConversion{
	
	public static void main(String[] args){
		
		int i = 0;
		Integer integer = null;
		String str = null;

		// int <---> Integer
		integer = new Integer(i+1);
		i = integer.valueInt();

		//int <--> String
		str = new String(i);

	}



	private static void prt(String s){
		System.out.println(s);
	}
}