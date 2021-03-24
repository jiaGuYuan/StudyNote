public class User{
	private int num;
	private String name;
	private char sex;
	private int age;
	private String addr;

	public User(int num, String name, char sex, int age, String addr){
		this.num  = num;
		this.name = name;
		this.sex = sex; 
		this.age = age;
		this.addr = addr;
	}

	User(String name){
		this.name = name;
	}

	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o.getClass() == this.getClass() 
			&& ((User)o).name == this.name){
			return true;
		}
		else
			return false;
	}

	public void setNum(int num){
		this.num = num;
	}

	public int getNum(){
		return this.num;
	}


	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}

	public void setSex(char sex){
		this.sex = sex;
	}
	public char getSex(){
		return this.sex;
	}

	public void setAge(int age){
		this.age = age;
	}
	public int getAge(){
		return this.age;
	}

	public void setAddr(String addr){
		this.addr = addr;
	}
	public String getAddr(){
		return this.addr;
	}
}//end User

