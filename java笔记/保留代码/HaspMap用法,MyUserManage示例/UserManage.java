import java.util.*;




public class UserManage{
	private Compare keyCompare;
	private Compare userCompare;
	private Map<Integer, User> num2user;

	UserManage(){
		
		this.userCompare = new Compare(){ //匿名类实现接口
			public boolean lessThan(Object l, Object r) {
				return ((User)l).getName().compareToIgnoreCase(((User)r).getName()) < 0;
			}
			public boolean lessThanOrEqual(Object l, Object r) {
				return ((User)l).getName().compareToIgnoreCase(((User)r).getName()) <= 0;
			}
		};
		
		this.keyCompare = new Compare(){ //匿名类实现接口
			public boolean lessThan(Object l, Object r) {
				return (Integer)l < (Integer)r ;
			}
			public boolean lessThanOrEqual(Object l, Object r) {
				return (Integer)l <= (Integer)r ;
			}
		};
		
		num2user = new HashMap<Integer, User>();
	}

	UserManage(Compare userCompare, Compare keyCompare){
		this();

		if(userCompare !=null )	{
			this.userCompare = userCompare;
		}

		if(keyCompare !=null )	{
			this.keyCompare = keyCompare;
		}
		
	}

	public boolean addUser(User user){
		if(num2user.containsKey(user.getNum())){
			return false;
		}
		num2user.put(user.getNum(), user);
		return true;
	}

	public boolean removeUser(Integer num){
		if(!num2user.containsKey(num)){
			return false;
		}
		num2user.remove(num);
		return true;
	}

	public boolean removeUser(String name){
		//在遍历过程中移除元素
		for (Iterator<Map.Entry<Integer, User>> iter = num2user.entrySet().iterator(); iter.hasNext();){
    		Map.Entry<Integer, User> item = iter.next();
    		if(item.getValue().getName().indexOf(name) != -1){//判断元素是否符合删除条件
    			iter.remove();
    		}
    	}
		return true;
	}

	public User queryUser(Integer num){
		if(!num2user.containsKey(num)){
			return null;
		}
		return num2user.get(num);
	}

	public List<User> queryUser(String name){
		List<User> userlist = new ArrayList<User>();
		Collection<User> vaules = num2user.values();
		for(User user : vaules){
			if(user == null)
				continue;
			else
				if(user.getName().indexOf(name) != -1){
					userlist.add(user);
				}
		}
		return userlist;
	}

	public boolean existNum(Integer num){
		return num2user.containsKey(num);
	}

	public boolean modificationUser(User user){
		if(!existNum(user.getNum())){
			return false;
		}
		num2user.put(user.getNum(), user);
		return true;
	}

	private List<Integer> sort(Set<Integer> set){
		Integer[] array = set.toArray(new Integer[]{1});
		for(int i = 0; i<array.length-1; i++){
			int min = i;
			for(int j=i+1; j<array.length; j++){
				if(!keyCompare.lessThanOrEqual(array[min], array[j])){
					min = j;
				}
			}

			if(min != i){
				Integer temp = array[i];
				array[i] = array[min];
				array[min] = temp;
			}
		}
		return Arrays.asList(array);
	}


	private List<User> sort(Collection<User> list){
		User[] array = list.toArray(new User[]{});
		for(int i = 0; i<array.length-1; i++){
			int min = i;
			for(int j=i+1; j<array.length; j++){
				if(!userCompare.lessThanOrEqual(array[min], array[j]))
					min = j;
			}

			if(min != i){
				User temp = array[i];
				array[i] = array[min];
				array[min] = temp;
			}
		}
		return Arrays.asList(array);
	}

	public List<User> sortUserOfNum(){
		List<User> userlist = new ArrayList<User>();

		Set<Integer> keys = num2user.keySet();
		List<Integer> sortKeys = sort(keys);

		for(Integer key : sortKeys){
			userlist.add(num2user.get(key));
		}

		return userlist;
	 }

	public List<User> sortUserOfName(){
		return sort(num2user.values());
	}

	public Collection<User> users(){
		return new ArrayList(num2user.values());
	}

	public Map<Integer, User> allUser(){
		return new HashMap(num2user);
	}

	public static void main(String[] args){

		UserManage um = new UserManage();

		for(int i=0; i<10; i++){
			int x = (int)(Math.random()*20);
			int num = (int)(Math.random()*1000);
			String name = "name"+x;
			char sex = x%2==0?'男':'女';
			int age = x;
			String addr = "addr"+x;
			User u = new User(num, name, sex, age, addr);
			um.addUser(u);
		}

		System.out.println("未排序输出:");
		for(User user : um.users()){
			System.out.println(user.getNum()+"\t"+user.getName()+"\t"+user.getSex()+"\t"+user.getAge()+"\t"+user.getAddr());
		}
		System.out.println("\n");

		System.out.println("按编号排序输出:");
		for(User user : um.sortUserOfNum()){
			System.out.println(user.getNum()+"\t"+user.getName()+"\t"+user.getSex()+"\t"+user.getAge()+"\t"+user.getAddr());
		}
		System.out.println("\n");

		System.out.println("按名称排序输出:");
		for(User user : um.sortUserOfName()){
			System.out.println(user.getNum()+"\t"+user.getName()+"\t"+user.getSex()+"\t"+user.getAge()+"\t"+user.getAddr());
		}
		System.out.println("\n");
	}

}



