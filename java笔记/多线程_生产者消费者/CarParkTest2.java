import java.lang.Math;


//用生产者消费者模拟汽车出入库
// --通过wait() / notify()方法实现

class Cart{
    private static int count=0;
    private int num;
    Cart(){
        num = count++;
    }

    public String toString(){
        return "[汽车"+num+"]";
    }    
}

//停车场
class CarPark {
    private Object[] buf;
    int s_i = 0;

    CarPark(int size){ //size:停车场的车位数
        buf = new Object[size];
        for(int i=0; i<buf.length; ++i){
            buf[i] = null;
        }
    }

    //[等待]生产num个车位(需要num辆车离库)  
    public void carLeavePart(int num) { 

        synchronized (buf) {//使线程成为buf对象监视器的持有者
            while (getUsedCarPortNum() < num) { 
                try {  
                    buf.wait(); //释放对buf监视器的所有权,并阻塞线程, 醒来后等到再次获得所有权才会执行
                } catch (InterruptedException e){  
                    e.printStackTrace();  
                }  
            }
  
            // 生产num个车位
            for (int i = 0; i < num; ++i) {  
                //从车库中随机指定一辆车出库
                int n = (int)(Math.random()*(getUsedCarPortNum()));
                int c = 0;
                for(int j=0; j<buf.length; ++j){
                    if(buf[j] != null)
                        if(c++ == n){
                            freeCarPort(j);
                            break;
                        }
                }
            }  
            buf.notifyAll();  
        }  
    }  

    //[等待]消费num个车位 (num辆车入库)  
    public void carEnterPark(int num) {  

        synchronized (buf) {  
            while (getFreeCarPortNum() < num){  
                try {  
                    buf.wait();  
                }  
                catch (InterruptedException e)  {  
                    e.printStackTrace();  
                }  
            }  
  
            // 消费条件满足情况下，消费num个产品  
            for (int i = 0; i < num; ++i)   {  
                useCarPort(new Cart());  //实际使用时,可通过参数传入List<Cart>
            }  
    
            buf.notifyAll();  
        }  
    }  

    //剩余的车位数
    private int getFreeCarPortNum(){
        return buf.length - getUsedCarPortNum();
    }

    //已占用的车位数
    private int getUsedCarPortNum(){
        int c = 0;
        for(int i=0; i<buf.length; ++i){
            if(buf[i] != null)
                c++;
        }
        return c;
    }

    //获取第一个可用的车位号
    private int getFristCarPort(){
        for(int i=0; i<buf.length; ++i){
            if(buf[i] == null)
                return i;
        }
        return -1;
    }

    //车出库(释放编号为index的车位) 
    private void freeCarPort(int index){
        System.out.println("\t\t"+buf[index]+"出库: 第 "+index+" 号车位可用--出库总次数:"+(s_i++));
        buf[index] = null;
    }

    //车入库(使用一个车位)
    private void useCarPort(Object car){
        int carPort = getFristCarPort();
        System.out.println(car+"入库: 占用第"+carPort+"号车位");
        buf[carPort] = car;
    }
}

/** 
 * 生产者线程Producer -- 模拟车出库(产生车位)
 */
class Producer extends Thread {  
    private CarPark carPark;  // 所在放置的仓库  
  
    // 构造函数，设置仓库  
    public Producer(CarPark carPark) {  
        this.carPark = carPark;  
    }  
  
    // 线程run函数  
    public void run() {
        try {
            Thread.sleep((int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 
        produce(1);  
    }  
  
    // num:出库的车数目
    public void produce(int num) {  
        carPark.carLeavePart(num);  
    }  
}  

/** 
 * 消费者线程Consumer -- 模拟车入库(消费车位)
 */  
class Consumer extends Thread  
{   
    private CarPark carPark;  
  
    // 构造函数，设置仓库  
    public Consumer(CarPark carPark) {  
        this.carPark = carPark;  
    }  
  
    // 线程run函数  
    public void run() { 
        try {
            Thread.sleep((int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
        consume(1);  
    }  
  
    // num:一次性需要的车位数  
    public void consume(int num) {  
        carPark.carEnterPark(num);  
    }  
}  

/** 
 * 测试类Test 
 */  
public class CarParkTest2  
{  
    public static void main(String[] args)  
    {  
        int carNum = 50;
        CarPark carPark = new CarPark(10);  
  
        Producer[] p = new Producer[carNum];
        Consumer[] c = new Consumer[carNum];
        for(int i=0; i<carNum; i++){
            p[i] = new Producer(carPark);  
            c[i] = new Consumer(carPark);  
        }
        
        for(int i=0; i<carNum; i++){
            c[i].start();  
            p[i].start(); 
        } 
    }  
}  


/*
Object:
    notify() --唤醒在此对象监视器上等待的单个线程. 如果所有线程都在此对象上等待，则会选择唤醒其中一个线程。
        选择是任意性的，并在对实现做出决定时发生。线程通过调用 wait 方法，在对象的监视器上等待。
        直到当前线程放弃此对象上的锁定，才能继续执行被唤醒的线程。
        被唤醒的线程将以常规方式与在该对象上主动同步的其他所有线程进行竞争
        此方法只应由作为此对象监视器的所有者的线程来调用。
        通过以下三种方法之一，线程可以成为此对象监视器的所有者： 
            1.通过执行此对象的同步实例方法。 
            2.通过执行在此对象上进行同步的 synchronized 语句的正文。 
            3.对于 Class 类型的对象，可以通过执行该类的同步静态方法。 
        一次只能有一个线程拥有对象的监视器。 


    wait() --阻塞当前线程,走到在其他线程调用此对象的 notify() 方法或 notifyAll() 方法.
        在执行wait()时当前线程必须拥有此对象监视器。该线程释放对此监视器的所有权并等待,
        直到其他线程通过调用 notify 方法或 notifyAll 方法通知在此对象的监视器上等待的线程醒来。
        然后该线程将等到重新获得对监视器的所有权后才能继续执行。 
        对于某一个参数的版本，实现中断和虚假唤醒是可能的，而且此方法应始终在循环中使用： 
        synchronized (obj) {
            while (条件不满足){
                obj.wait();
            }
            //...执行适合条件的操作
        }
        此方法只应由作为此对象监视器的所有者的线程来调用。


    简言之: obj.wait()不只是简单的使线程阻塞, 在阻塞之前它还会释放对obj对象监视器的所有权 --所有它要求持有obj对象的监视器;
            在醒来之后它将等到重新获得obj对象监视器的所有权后才继续执行.
 */