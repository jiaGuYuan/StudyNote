public class CharClient {  //改用字符流的方式收发网络数据
    ……
    Socket s = new Socket(address, SERVER_PORT);
    InputStream is = s.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader in = new BufferedReader(isr);

    OutputStream os = s.getOutputStream();
    PrintWriter out = new PrintWriter(os, true);
    ……
    out.println(strToServer);
    ……
    String strFromServer = in.readLine();
    ……
    out.close();
    in.close();
    ……
}
