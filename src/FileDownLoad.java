import java.io.BufferedInputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FileDownLoad {
	
	
	 
	 private class ThreadTest extends Thread {
		 ThreadTest() {
		 }
		 public void run() {
		 // code here ...
			 try {
				downloadNet();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		 public void downloadNet() throws MalformedURLException {
				// 下载网络文件
				int bytesum = 0;
				int byteread = 0;
				URL url = new URL("http://www.chinanews.com/mil/2013/01-14/4484219.shtml");
				try {
				URLConnection conn = url.openConnection();
				InputStream inStream = conn.getInputStream();
				FileOutputStream fs = new FileOutputStream("D:\\test1.htm");
				byte[] buffer = new byte[1204];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
				}
				} catch (FileNotFoundException e) {
				e.printStackTrace();
				} catch (IOException e) {
				e.printStackTrace();
				}
				} 
		 };
	 public String removetag(String text,String Dirpath,String Picpath) throws IOException
	 {
		 int i;
		 boolean flag=false;
		 String s="",img="";
		 i=0;
		 File dataFile = new File(Picpath+"\\picinfo.txt");
		 FileWriter fw = new FileWriter(dataFile,true);
		 BufferedWriter bw = new BufferedWriter(fw);
		 while(i<text.length())
		 {
			 char tmp=text.charAt(i);
			 if(tmp=='<')
			 {
				 flag=true;
				 s+=" ";
				 img="";
				 if(i+8<text.length())
				 { 
					 String zhushi=text.substring(i, i+8);
					 if(zhushi.startsWith("<!--"))
					 {
						 int pos=text.indexOf("-->", i);
						 if(pos>=0)i=pos+2;
					 }
					 else if(zhushi.startsWith("<style"))
					 {
						 int pos=text.indexOf("</style>", i);
						 if(pos>=0)i=pos+7;
					 }
					 else if(zhushi.startsWith("<script"))
					 {
						 int pos=text.indexOf("</script>", i);
						 if(pos>=0)i=pos+8;
					 }
				 }
			 }
			 else if(tmp=='>')
			 {
				 flag=false;
				 s+=" ";
				 img=img.toLowerCase();
				 img=img.replaceAll("\\s*", "");
				 if(img.indexOf("img")>=0)
				 {
					 int srcbeg=img.indexOf("src=");
					 int titlebeg=img.indexOf("title=");
					 int altbeg=img.indexOf("alt=");
					 if(srcbeg>=0&&(titlebeg>=0||altbeg>=0))
					{
						 String path=Dirpath;
						 srcbeg+=5;
						 if(img.charAt(srcbeg)=='.')
						 {
							 srcbeg++;
						 }
						 if(img.charAt(srcbeg)=='/')//屏蔽掉来自网络的图片，只处理已爬到的本地图片
						 {
							 int srcend=img.indexOf("\"",srcbeg);
							 if(srcend>=0)
							 {
								 String pathtmp=img.substring(srcbeg, srcend);
								 path+=pathtmp.replaceAll("/", "\\\\");
							 
								 String descript="";
								 String title="";
								 if(titlebeg>=0)
								 {
									 titlebeg+=7;
									 int titleend=img.indexOf("\"", titlebeg);
									 if(titleend>=0)
									 {
										 title=img.substring(titlebeg,titleend);
										 if(title.length()>5)descript+=title;
									 }
								 }
								 if(altbeg>=0)
								 {
									 altbeg+=5;
									 int altend=img.indexOf("\"", altbeg);
									 if(altend>=0)
									 {
										 String alt=img.substring(altbeg,altend);
										 if(!alt.equals(title)&&alt.length()>5)descript=descript+" "+alt;
									 }
								 }
								 //将path和title写入文件
								 if(descript.length()>0)
								 {	 
									 bw.write(path+"\t"+descript+"\n");
									 bw.flush();
								 }
							 }
						 }
						 
					}
				 }
			 }
			 else
			 {
				 if(!flag)
				 {
					 s+=tmp;
				 }
				 else 
				 {
					 img+=tmp;
				 }
				 
				
			 }
			 i++;
		 
		 }
		 bw.close();
		 fw.close();
		 return s;
	 }
	 public void processhtml(String ODatadir,String NDatadir,String Picpath) throws IOException
	 {
		 File file = new File(ODatadir);
		 File[] files = file.listFiles();
		 for (int i = 0; i < files.length; i++) {
				if (!files[i].isDirectory()){
					File f = files[i];
					FileInputStream fis = new FileInputStream(f);	
					BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GB2312"));
					String readin = br.readLine();
					String text="";
					while(readin!=null){
						text+=readin;
						readin = br.readLine();
					}
					
					br.close();
					fis.close();
					text=text.replaceAll("\\s*", "");
					text=text.replaceAll("&nbsp;", "");
					text=removetag(text,ODatadir,Picpath);
					//只截取前60%的网页内容
					text=text.substring(0, (int)(0.6*text.length()));
					text.replaceAll("\\s*", " ");
					
					File dataFile = new File(NDatadir+"\\"+f.getName());
					FileWriter fw = new FileWriter(dataFile);
					BufferedWriter bw = new BufferedWriter(fw);
					//断句处理
					String splitword="[。！？.?! ]";
					String[] result=text.split(splitword);
					for(int j=0;j<result.length;j++)
					{
						if(result[j].length()<10)//对短句子进行限制，同时也是对一些广告的屏蔽 
							continue;
						bw.write(result[j]+"\n");
						bw.flush();
					}
					bw.close();
					fw.close();
				}
				
			}
	 }
	 public static void main(String args[]) throws IOException
	 {
		 /*FileDownLoad ff=new FileDownLoad();
		 ThreadTest t = ff.new ThreadTest();
		 t.start();
		 System.out.println("this is a main process\n");*/
		 FileDownLoad ff=new FileDownLoad();
		 String ODatadir="C:\\tfidf-fast\\OdataDir\\d04";
		 String NDatadir="C:\\tfidf-fast\\dataDir\\d04";
		 String Picdir="C:\\tfidf-fast\\imgDir\\d04";
		 ff.processhtml(ODatadir, NDatadir, Picdir);
	 }
}