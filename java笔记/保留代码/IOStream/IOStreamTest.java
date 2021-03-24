import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Vector;

import java.io.InputStreamReader;
import java.io.FileInputStream;

public class IOStreamTest{
	/*1.打开一个文本文件，每次读取一行内容。将每行作为一个 String 读入，并将那个 String 对象置入一个Vector 里。
	按相反的顺序打印出 Vector 中的所有行.
	2.又打开一个文本文件，以便将文字写入其中。将 Vector 中的行随同行号一起写入文件。
	3.在文件中查找指定的单词。打印出包含了欲找单词的所有文本行。*/

	// 这个函数读取UTF-8编码文件会乱码
	public static Vector<String> readerText(String fileName){
		File f = new File(fileName);
		FileReader fr = null;
		BufferedReader br = null;
		Vector<String> v = new Vector<String>();
		try{
			fr = new FileReader(f);
			br = new BufferedReader(fr); //按行读需要BufferedReader --FileReader没有readLine()方法
			String lineStr = null;
			while((lineStr = br.readLine())!=null){
				v.add(lineStr);
			}

			// for(int i=v.size()-1; i>0; i-- ){//逆序输出
			// 	System.out.println(v.get(i));
			// }
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(br!=null){
				try{
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}

			if(fr!=null){
				try{
					fr.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return v;
	}

	// 这个函数可以读取UTF-8编码文件
	public static Vector<String> readerTextUTF8(String fileName){
		BufferedReader br = null;
		Vector<String> v = new Vector<String>();
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8")); 
			String lineStr = null;
			while((lineStr = br.readLine())!=null){
				v.add(lineStr);
			}

			// for(int i=v.size()-1; i>0; i-- ){//逆序输出
			// 	System.out.println(v.get(i));
			// }
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(br!=null){
				try{
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return v;
	}


	public static void writerText(String fileName, Vector<String> textList){
		File f = new File(fileName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		try{
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw); //按行读需要BufferedReader --FileReader没有readLine()方法

			String lineStr = null;
			for(String text : textList){
				bw.write(text, 0, text.length());
				bw.newLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(bw!=null){
				try{
					bw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}

			if(fw!=null){
				try{
					fw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public static void findWord(String word, Vector<String> textLineList){
		for(String str : textLineList){
			if(str.indexOf(word)!=-1){
				System.out.println(str);
			}
		}
	}

	public static void main(String[] args){
		Vector<String> textLineList = readerTextUTF8("./IOStreamTest.java");
		//writerText("./a.txt", textLineList);
		findWord("public", textLineList);
	}
}