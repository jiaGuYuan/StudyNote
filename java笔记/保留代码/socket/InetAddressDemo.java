import java.net.*;  //导入java.net包

public class InetAddressDemo {
    public static void main(String[] args) {
        try {
            //获得当前本地主机的IP地址
            InetAddress add1 = InetAddress.getLocalHost();
            System.out.println("当前本地主机：" + add1);

            //根据域名，通过DNS域名解析，获得相应服务的主机地址
            InetAddress add2 = InetAddress.getByName("www.163.com");
            System.out.println("网易服务器主机：" + add2);

            //根据字符串表现形式的IP地址获得相应的主机地址(若果网络上有该IP地址的话)
            InetAddress add3 = InetAddress.getByName("192.168.0.22");
            System.out.println("IP地址为192.168.0.22的主机：" + add3);

            //根据机器名获得相应的主机地址（如果网络上有该机器名的话）
            InetAddress add4 = InetAddress.getByName("J104");
            System.out.println("机器名为J104的主机：" + add4);
        } catch (UnknownHostException uhe) { 
            uhe.printStackTrace(); 
        }
    }
}
