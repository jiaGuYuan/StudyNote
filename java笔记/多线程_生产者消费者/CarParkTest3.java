import java.lang.Math;
import java.util.concurrent.locks.Condition;  
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReentrantLock; 

//用生产者消费者模拟汽车出入库
// --通过Lock 和 Condition实现 (await() / signal()方法)

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
    private final Lock lock = new ReentrantLock();  // 锁  
    private final Condition produceCond = lock.newCondition();  // 生产条件
    private final Condition consumeCond = lock.newCondition();  // 消费条件
    int s_i = 0;

    CarPark(int size){ //size:停车场的车位数
        buf = new Object[size];
        for(int i=0; i<buf.length; ++i){
            buf[i] = null;
        }
    }

    //[等待]生产num个车位(需要num辆车离库)  
    public void carLeavePart(int num) { 

        lock.lock();  
        while (getUsedCarPortNum() < num) { 
            try {  
                produceCond.await();  
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
        // 唤醒其他所有线程  
        //produceCond.signalAll();  
        consumeCond.signalAll(); 

        lock.unlock();
    }  

    //[等待]消费num个车位 (num辆车入库)  
    public void carEnterPark(int num) {  

        lock.lock();  
        while (getFreeCarPortNum() < num){  
            try {  
                consumeCond.await();  
            }  
            catch (InterruptedException e)  {  
                e.printStackTrace();  
            }  
        }  

        // 消费条件满足情况下，消费num个产品  
        for (int i = 0; i < num; ++i)   {  
            useCarPort(new Cart());  //实际使用时,可通过参数传入List<Cart>
        }  
    
        // 唤醒其他所有线程  
        produceCond.signalAll();  
        //consumeCond.signalAll(); 
        lock.unlock();  
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
public class CarParkTest3  
{  
    public static void main(String[] args)  
    {  
        int carNum = 5000;
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
Condition:
    await()
            造成当前线程在接到信号或被中断之前一直处于等待状态。 
            与此 Condition 相关的锁以原子方式释放，并且出于线程调度的目的，将禁用当前线程，且在发生以下四种情况之一 以前，当前线程将一直处于休眠状态： 

            其他某个线程调用此 Condition 的 signal() 方法，并且碰巧将当前线程选为被唤醒的线程；或者 
            其他某个线程调用此 Condition 的 signalAll() 方法；或者 
            其他某个线程中断当前线程，且支持中断线程的挂起；或者 
            发生“虚假唤醒” 
            在所有情况下，在此方法可以返回当前线程之前，都必须重新获取与此条件有关的锁。在线程返回时，可以保证 它保持此锁。 

            如果当前线程： 

            在进入此方法时已经设置了该线程的中断状态；或者 
            在支持等待和中断线程挂起时，线程被中断， 
            则抛出 InterruptedException，并清除当前线程的中断状态。在第一种情况下，没有指定是否在释放锁之前发生中断测试。 
            实现注意事项 

            假定调用此方法时，当前线程保持了与此 Condition 有关联的锁。这取决于确定是否为这种情况以及不是时，如何对此作出响应的实现。通常，将抛出一个异常（比如 IllegalMonitorStateException）并且该实现必须对此进行记录。 

            与响应某个信号而返回的普通方法相比，实现可能更喜欢响应某个中断。在这种情况下，实现必须确保信号被重定向到另一个等待线程（如果有的话）。 



    signal() --唤醒一个等待线程。 
            如果所有的线程都在等待此条件，则选择其中的一个唤醒。在从 await 返回之前，该线程必须重新获取锁

 */