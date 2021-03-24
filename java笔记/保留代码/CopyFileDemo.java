import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyFileDemo {

	//读取文本文件
	void readTextfile() {
		File file = new File("");
		FileReader fileReader = null;
		BufferedReader bufReader = null;

		try {
			fileReader = new FileReader(file);
			bufReader = new BufferedReader(fileReader);
			String msg = bufReader.readLine();// 会忽略换行符
			System.out.println(msg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 有封装关系的流,被依赖的流要后关
			if (bufReader != null)
			{
				try {
					bufReader.close();
				}catch (IOException e) {
				e.printStackTrace();
			}
			
			if(fileReader != null)
			{
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//注意: 两个关闭操作不要放在一个try中. 这样可能会导致另一个关闭操作不会执行
			//try{
			//	if (bufReader != null))
			//		bufReader.close()
			//	if(fileReader != null)
			//		fileReader.close();
			//}catch (IOException e) {
			//		e.printStackTrace();
			//	}
		}

	}

	//复制文本文件
	void copyTextfile() {

		BufferedReader bufReader = null;
		BufferedWriter bufWrite = null;
		try {
			bufReader = new BufferedReader(new FileReader(new File("xxx")));
			bufWrite = new BufferedWriter(new FileWriter(new File("yyy")));
			String msg = null;
			while((msg = bufReader.readLine())!=null){
				bufWrite.write(msg);
				bufWrite.newLine();
			}
			 	
			System.out.println(msg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			
			// 有封装关系的流,被依赖的流要后关
			if (bufReader != null){
				finally {
					bufReader.close();
				}catch (IOException e) {
					e.printStackTrace();
				}	
			}
			if(bufWrite != null){
				try{
					bufWrite.close();
				}catch (IOException e) {
					e.printStackTrace();
				}	
				
			}
		}

	}

	//复制字节文件(按二进制方式复制--对文本文件和其他文件都适用)
	void copyBytefile(){
		File srcFile = new File("test/src.dat"); // 源文件对象
		File destFile = new File("test/dest.dat"); // 目标文件对象
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (!(destFile.exists())) { // 判断目标文件是否存在
				destFile.createNewFile();// 如果不存在则创建新文件
			}
			// 使用源文件对象创建文件输入流对象
			fis = new FileInputStream(srcFile);
			// 使用目标文件对象创建文件输出流对象
			fos = new FileOutputStream(destFile);
			byte[] buf = new byte[1024]; // 创建字节数组，作为临时缓冲
			System.out.println("开始复制文件...");
			int readNum = 0;

			while ((readNum = fis.read(buf)) != -1) { // 循环从文件输入流中读取数据
				fos.write(buf, 0, readNum); // 写入到文件输出流中
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (fis != null){
				try {
					fis.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null){
				try{
					fos.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			} 

		}

		System.out.println("文件复制成功！");

	}
	
	public static void main(String[] args) {
		
	}
}
