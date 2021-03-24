import java.util.Scanner;
import java.util.*;

/** 用户管理: 实现用户的增\删\改\查
    查询时: 根据用户名模糊查
    --用HashMap实现.(并不适合,只是作为练习)
   author:^_^ 小G	
*/
public class UserManageView{
	UserManage userManage;
	Scanner in;

	UserManageView(){
		userManage = new UserManage();
		in = new Scanner(System.in);
		init();
	}


	private User inputUser(boolean numExistExit){
		User u = null;
		//System.out.print("请输入编号:");
		try{
			System.out.print("请输入: 编号 名称　性别(男/女)　年龄(0~150)　地址:");
			int num = in.nextInt();
			String name = in.next();
			String sex = in.next();
			int age = in.nextInt();
			String addr = in.next();

			boolean exist = userManage.existNum(num);
			if(numExistExit && exist){
				System.out.println("用户已存在");
				return null;
			}else if(!numExistExit && !exist){
				System.out.println("用户不存在");
				return null;
			}

			if(!sex.equals("男") && !sex.equals("女")){
				System.out.println("性别输入错误(男/女)");
				return null;
			}
			if(age < 0 || age > 150){
				System.out.println("年龄输入不合法(0~150)");
				return null;
			}

			u = new User(num, name, sex.charAt(0), age, addr);

		}catch(InputMismatchException ex){
			System.out.println("输入不合法");
		}catch(NoSuchElementException ex){
			System.out.println("输入项太少了");
		}finally{
			in.nextLine(); //吃掉多余的输入
		}

		return u;
	}

	private boolean delUser(){
		System.out.println("删除:");
		System.out.print("1.按编号删\t2.按名字删: ");
		String strnum = in.next();
		if(strnum.compareTo("1")==0){
			System.out.print("请输入编号: ");
			int num = in.nextInt();
			if(!userManage.existNum(num)){
				System.out.println("用户不存在");
				return false;
			}
			userManage.removeUser(num);

		}else if(strnum.compareTo("2")==0){
			System.out.print("请输入名称: ");
			String name = in.next();
			userManage.removeUser(name);
		}
		return true;
	}

	private void queryUser(){
		System.out.println("查询:");
		System.out.print("1.按编号查\t2.按名字查: ");
		String strnum = in.next();
		if(strnum.compareTo("1")==0){
			System.out.print("请输入编号: ");
			int num = in.nextInt();
			if(!userManage.existNum(num)){
				System.out.println("用户不存在");
			}
			printUser(userManage.queryUser(num));

		}else if(strnum.compareTo("2")==0){
			System.out.print("请输入名称: ");
			String name = in.next();
			List<User> list = userManage.queryUser(name);
			for(User u : list)
				printUser(u);
		}
	}

	private void sortPrint(){
		System.out.println("排序输出");
		System.out.print("1.按编号排序\t2.按名字排序: ");
		String strnum = in.next();
		if(strnum.compareTo("1")==0){
			List<User> list = userManage.sortUserOfNum();
			for(User u : list)
				printUser(u);
		}else if(strnum.compareTo("2")==0){
			List<User> list = userManage.sortUserOfName();
			for(User u : list)
				printUser(u);
		}
	}

	private void init(){

		while(true){	
			System.out.print("1.添加\t2.修改\t3.删除\t4.查询\t5.打印\t6.排序\t7. \t8.退出: ");
			String str = in.next();
			if(str.compareTo("1")==0){
				System.out.println("添加用户");
				User u = inputUser(true);
				if(u !=null){
					userManage.addUser(u);
					System.out.println("添加成功");
				}
				
			}else if(str.compareTo("2")==0){
				System.out.println("修改:");
				User u = inputUser(false);
				if(u !=null){
					userManage.modificationUser(u);
					System.out.println("修改成功");
				}
			}else if(str.compareTo("3")==0){
				if(!delUser())
					continue;
			}else if(str.compareTo("4")==0){
				queryUser();

			}else if(str.compareTo("5")==0){
				System.out.println("打印:");
				 printAll();

			}else if(str.compareTo("6")==0){
				sortPrint();

			}else if(str.compareTo("7")==0){

			}else if(str.compareTo("8")==0){
				System.out.println("退出");
				return;
			}else{
				System.out.println("输入错误");
				in.nextLine(); //越过错误输入之后的内容，转到下一行
			}
			
		}
	}

	private void printAll(){
		for(User user : userManage.users()){
			 printUser(user);
		}
	}

	private void printUser(User user){
		System.out.println(user.getNum()+"\t"+user.getName()+"\t"+user.getSex()+"\t"+user.getAge()+"\t"+user.getAddr());

	}


	public static void main(String[] args){
		UserManageView um = new UserManageView();
	}
}

