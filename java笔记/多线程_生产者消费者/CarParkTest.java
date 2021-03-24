import java.lang.Math;
import java.util.concurrent.Semaphore;
import java.lang.IllegalArgumentException;
import java.lang.InterruptedException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//用生产者消费者模拟汽车出入库
// --通过锁与信号量实现

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
    private final Semaphore sFreeCarPort;
    private final Semaphore sUsedCarPort;
    private final Lock lock;

    int s_i = 0;

    CarPark(int size){ //size:停车场的车位数
        sFreeCarPort = new Semaphore(size, true);
        sUsedCarPort = new Semaphore(0, true);
        lock = new ReentrantLock();

        buf = new Object[size];
        for(int i=0; i<buf.length; ++i){
            buf[i] = null;
        }
    }

    //[等待]生产num个车位(需要num辆车离库)  
    public void carLeavePart(int num) { 
        try{
            sUsedCarPort.acquire(num);
        
            lock.lock(); //通过锁来互斥, 也可以通过如下synchronized来互斥
            //synchronized (buf) {    
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
            //}
            lock.unlock();

            sFreeCarPort.release(num);
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }catch(IllegalMonitorStateException e){

        }finally {
        }
    }  

    //[等待]消费num个车位 (num辆车入库)  
    public void carEnterPark(int num) {  
        try{
            sFreeCarPort.acquire(num);
        
            lock.lock();
            //synchronized (buf) {  
                // 消费条件满足情况下，消费num个产品  
                for (int i = 0; i < num; ++i)   {  
                    useCarPort(new Cart());  //实际使用时,可通过参数传入List<Cart>
                }  
            //}
            lock.unlock();
            sUsedCarPort.release(num);
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }catch(IllegalMonitorStateException e){

        }finally {
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
        System.out.println(car+"入库: 占用第 "+carPort+" 号车位");
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
public class CarParkTest  
{  
    public static void main(String[] args)  
    {  
        int carNum = 500;
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
