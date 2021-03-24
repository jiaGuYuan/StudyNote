import java.util.ArrayList;
import java.util.Scanner;

class SleOutOfBoundsException extends Exception{
	SleOutOfBoundsException(){
		super("只能选择:"+Rule.ROCK+","+Rule.PAPER+","+Rule.SCISSORS);
	}
	SleOutOfBoundsException(String str){
		super("只能选择:"+Rule.ROCK+","+Rule.PAPER+","+Rule.SCISSORS+"\t"+str);
	}
	
}

abstract class Rule{
	final static int ROCK = 0; //石头
	final static int PAPER = 1; //剪刀
	final static int SCISSORS = 2; //布
	//RULE[main][guest]: 0:平局, 1:main胜, -1:guest胜
	final static int[][] RULE = {{0, 1, -1},{-1, 0, 1},{1, -1, 0}};
	
	public static void ruleDescription(){
		System.out.println("0:石头\t 1:剪刀\t 2:布 ");
	}
	
	public static boolean checkSel(int sel){
		if(sel!=ROCK && sel!=PAPER && sel!=SCISSORS){
			return false;
		}
		return true;
	}
}

//玩家
class Players{
	private int sel=Rule.ROCK;
	private String name;

	Players(String name){
		this.name = name;
	}
	
	public int getSel() {
		return sel;
	}
	
	public String getSelStr() {
		switch(sel){
		case Rule.ROCK: return "石头";
		case Rule.PAPER: return "剪刀";
		case Rule.SCISSORS: return "布";
		}
		return "";
	}
	
		
	public void setSel(int sel) throws SleOutOfBoundsException {
		if(!Rule.checkSel(sel))
			throw (new SleOutOfBoundsException(new String(name+"选择的是:"+sel)));
		this.sel = sel;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

//裁判
class Referee extends Rule{
	
	/**
	 * 判定输赢
	 * 返回值: 0:平局, 1:main胜, -1:guest胜
	 */ 
	public static int determination(Players main, Players guest){
		return RULE[main.getSel()][guest.getSel()];
	}
	
	public static void result(Players main, Players guest){
		int res = determination(main, guest);
		System.out.println(main.getName()+":"+main.getSelStr()+"\tVS\t"
				+guest.getName()+":"+guest.getSelStr());

		switch(res){
		case -1:
			System.out.println(guest.getName()+"胜");
			break;
		case 0: 
			System.out.println("平局");
			break;
		case 1:
			System.out.println(main.getName()+"胜");
			break;
		}
	}
}

public class Mora {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("是否开始游戏(Y/N):");
		String str = in.next();
		if(!str.equalsIgnoreCase("Y")){
			System.out.println("游戏退出");
			return;
		}

		System.out.println("游戏开始");
		System.out.print("请玩家输入姓名:");
		String name = in.next();
		Players player = new Players(name);
		Players main = new Players("机器");
		
		while(true){
			
			Referee.ruleDescription();
			System.out.print("你的选择是:");
			try{
				main.setSel((int)(Math.random()*3));
				
				int sel = in.nextInt();
				player.setSel(sel);
				Referee.result(main, player);
			}catch(InputMismatchException ex){
				in.next();
				System.out.println("你输入的不是数字!!");
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			
			System.out.print("是否继续游戏(Y/N):");
			String again = in.next();
			if(!again.equalsIgnoreCase("Y")){
				System.out.println("游戏退出");
				break;
			}
		}	
	}//end main

}
