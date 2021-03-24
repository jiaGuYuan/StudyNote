import javax.swing.*;

import java.awt.*;       //布局管理器在此包中
import javax.swing.*;
class FlowLayoutTest extends JFrame {
  private JButton btn1, btn2, btn3;   //声明三个按钮
  public FlowLayoutTest() {
    btn1 = new JButton("按钮1");       //分别将三个按钮实例化
    btn2 = new JButton("按钮2");
    btn3 = new JButton("按钮3");
    Container me = this.getContentPane();  //获取当前窗体的内容面板
    me.setLayout(new FlowLayout());        //设置窗体为流式布局
    me.add(btn1);       //分别将三个按钮添加到窗体中
    me.add(btn2);
    me.add(btn3);
    this.setTitle("这是测试窗体");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(300, 200);
    this.show();
  }
  public static void main(String[] args) {
    new FlowLayoutTest();
  }
}

class BorderLayoutTest extends JFrame {
  /*声明5个按钮*/
  private JButton btn1, btn2, btn3, btn4, btn5;
  /*构造方法*/
  public BorderLayoutTest() {
    btn1 = new JButton("中间按钮");     //分别创建5个按钮
    btn2 = new JButton("北边按钮");
    btn3 = new JButton("南边按钮");
    btn4 = new JButton("西边按钮");
    btn5 = new JButton("东边按钮");
    Container me = this.getContentPane();  //获取当前窗体的容器面板
    me.setLayout(new BorderLayout());      //设置窗体为边界布局
    me.add(btn1, BorderLayout.CENTER);//分别将按钮添加到窗体的不同方位
    me.add(btn2, BorderLayout.NORTH);
    me.add(btn3, BorderLayout.SOUTH);
    me.add(btn4, BorderLayout.WEST);
    me.add(btn5, BorderLayout.EAST);
    this.setTitle("这是测试窗体");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(300, 200);
    this.show();
  }
  /*main函数*/
  public static void main(String[] args){ new BorderLayoutTest(); }
}


class GridLayoutTest extends JFrame
{
  private JButton[] btnAry;  //按钮数组
  public GridLayoutTest() {
    btnAry = new JButton[12];  //创建有12个按钮的数组
    Container me = this.getContentPane();
    
    me.setLayout(new GridLayout(4, 3)); //设置窗体为4行3列的网格布局
    for (int i = 0; i < btnAry.length; i++) {
      btnAry[i] = new JButton("按钮" + (i + 1)); //循环创建12个按钮
      //设置按钮的悬停提示
      btnAry[i].setToolTipText("这是第" + (i + 1) + "个按钮");
      me.add(btnAry[i]);
    }
    this.setTitle("这是测试窗体");
    //设置窗体为关闭即退出程序
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(300, 200);
    this.show();
  }
  public static void main(String[] args) { new GridLayoutTest(); }
}




public class SwingDemo extends JFrame {
  /*构造方法*/
  public SwingDemo() {
    this.setTitle("这是测试窗体");  //设置窗体的标题
    this.setSize(300, 200);       //设置窗体的大小
    this.show();                  //将窗体显示
  }
  
  /*main方法，程序入口*/
  public static void main(String[] args){

    //FlowLayoutTest.main(args);

    //BorderLayoutTest.main(args);

    GridLayoutTest.main(args);

  }
}
