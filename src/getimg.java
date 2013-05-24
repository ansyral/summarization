import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class getimg{
	
	
	/*通过图片所在的doc的权值排序来挑选doc，再在doc中随机挑选一张（选取第一张）作为pic
	 public class pair
	{
		int idx;
		int value;
		
	};
	 public class ByValueComparator implements Comparator {

        public final int compare(Object pFirst, Object pSecond) {
        	 int aFirstValue = ((pair) pFirst).value;

             int aSecondValue = ((pair) pSecond).value;

             int diff = aFirstValue - aSecondValue;

             if (diff > 0)

          	   return 1;

             if (diff < 0)

          	   return -1;

             else
          	   return 0;
          
        }
	};
	  public void get(IndexReader reader, String indexDir, String matlabresultDir, String picDir) throws IOException
	{
		pair[]count=new pair[8];
		for(int i=0;i<8;i++)
		{
			count[i]=new pair();
			count[i].idx=i+1;
			count[i].value=0;
		}
		File picFile = new File(picDir);
		FileWriter picfwForsn = new FileWriter(picFile);
		BufferedWriter picbwForsn = new BufferedWriter(picfwForsn);
		
        File matlabFile = new File(matlabresultDir);		
		FileInputStream fis = new FileInputStream(matlabFile);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		String[] splitReadin = readin.split(",");
		for(int j=0;j<10;j++)
		{
			int idx=Integer.parseInt(splitReadin[j]);
			String filename=reader.document(idx).getFieldable("FileName").stringValue();
			char no=filename.charAt(filename.length()-5);
			count[no-'0'].value+=1;
		}
		Arrays.sort(count,new ByValueComparator());
		for(int j=0;j<4;j++)
		{
			String picname=count[j].idx+"_1.jpg\n";
			picbwForsn.write(picname);
			picbwForsn.flush();
		}
		picfwForsn.close();
		picbwForsn.close();
		fis.close();
		br.close();
		
	}*/
	
	private class info
	{
		public String filename;
		public String content;
		public double score;
		public info(String f,String c,double s)
		{
			filename=f;
			content=c;
			score=s;
		}
	};
	private class sqlinfo
	{
		public String news_page_id;
		public String title;
		public double score;
		public sqlinfo(String n,String t,double s)
		{
			news_page_id=n;
			title=t;
			score=s;
		}
	};
	public class ByValueComparator implements Comparator {

        public final int compare(Object pFirst, Object pSecond) {
        	 double aFirstValue = ((info) pFirst).score;

             double aSecondValue = ((info) pSecond).score;

             double diff = aFirstValue - aSecondValue;

             if (diff > 0)

          	   return -1;

             if (diff < 0)

          	   return 1;

             else
          	   return 0;
          
        }
	};
	public class SqlByValueComparator implements Comparator {

        public final int compare(Object pFirst, Object pSecond) {
        	 double aFirstValue = ((sqlinfo) pFirst).score;

             double aSecondValue = ((sqlinfo) pSecond).score;

             double diff = aFirstValue - aSecondValue;

             if (diff > 0)

          	   return -1;

             if (diff < 0)

          	   return 1;

             else
          	   return 0;
          
        }
	};
	
	public double pearson(IndexReader reader,String content,String summaryvecDir) throws IOException
	{
		// get pic vector
		Vector pic=new Vector();
		TermEnum te = reader.terms();
		while (te.next()){
			Term t = te.term();
			String []tmp=t.text().split("/");
			if(content.contains(tmp[0]))
			{
				pic.add(1.0);
			}
			else
				pic.add(0.0);
		}
		// get summary vector
		File summaryFile = new File(summaryvecDir);		
		FileInputStream fis = new FileInputStream(summaryFile);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		String[]tmp=readin.split(",");
		Vector summ=new Vector();
		int veclen=tmp.length;
		for(int i=0;i<veclen;i++)
		{
			summ.add(Double.parseDouble(tmp[i]));
		}
		// get pearson result
		double sum1=0.0,sum2=0.0,sum1Sq=0.0,sum2Sq=0.0,pSum=0.0;
		for(int i=0;i<veclen;i++)
		{
			double tmp1=(double)pic.elementAt(i);
			double tmp2=(double)summ.elementAt(i);
			sum1+=tmp1;
			sum2+=tmp2;
			sum1Sq+=(double)(tmp1*tmp1);
			sum2Sq+=(double)(tmp2*tmp2);
			pSum+=(double)(tmp1*tmp2);
		}
		double score=pSum-(sum1*sum2/veclen);
		double fenmu=Math.sqrt((sum1Sq-sum1*sum1/veclen)*(sum2Sq-sum2*sum2/veclen));
		if(fenmu==0)
			return -1000;
		score/=fenmu;
		return score;
		
		
	}
	public void get(IndexReader reader, String picorgDir, String summaryvecDir, String picDir) throws IOException
	{
		Vector picvec=new Vector();
		File picorgFile = new File(picorgDir);		
		FileInputStream fis = new FileInputStream(picorgFile);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		while(readin!=null)
		{
			double score=0.0;			
			String []picinfo=readin.split("\t");
			score=pearson(reader,picinfo[1],summaryvecDir);
			picvec.add(new info(picinfo[0],picinfo[1],score));
			readin=br.readLine();
		}
		
		Collections.sort(picvec, new ByValueComparator());
		/*
		 * for(int j=0;j<picvec.size();j++)
		{
			System.out.println(((info)picvec.elementAt(j)).score+"\t");
		}
		*/
		File picFile = new File(picDir);
		FileWriter picfwForsn = new FileWriter(picFile);
		BufferedWriter picbwForsn = new BufferedWriter(picfwForsn);
		for(int j=0;j<5;j++)
		{
			info tmp=(info) picvec.elementAt(j);
			String picname=tmp.filename;
			picbwForsn.write(picname+"\n");
			picbwForsn.flush();
		}
		picfwForsn.close();
		picbwForsn.close();
		fis.close();
		br.close();
		
	}
	public void Sqlget(IndexReader reader, String topicDir,String imgDir, String summaryvecDir, String imgresultDir, String titleDir) throws IOException 
	{
		
		
		
		String driver = "com.mysql.jdbc.Driver";			
		String url = "jdbc:mysql://mysql.973.udms.org/project973_development";
		String user = "project973";			
		String password = "008059c20c2ec4bca8e18ce5c9e2bea0";
		try {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		if(!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
		Statement statement = conn.createStatement();			
		String sql = "select url from images where resource_id in";
		
		
		File topicFile = new File(topicDir);		
		FileInputStream fis_topic = new FileInputStream(topicFile);	
		BufferedReader br_topic = new BufferedReader(new InputStreamReader(fis_topic));
		String readin_topic = br_topic.readLine();
		while(readin_topic!=null)
		{
			int topic_id=Integer.parseInt(readin_topic);
		
			Vector imgvec=new Vector();
			
			File imgsrcFile = new File(imgDir+topic_id+"id_title.txt");		
			FileInputStream fis_imgsrc = new FileInputStream(imgsrcFile);	
			BufferedReader br_imgsrc = new BufferedReader(new InputStreamReader(fis_imgsrc));
			String readin_imgsrc = br_imgsrc.readLine();
			while(readin_imgsrc!=null)
			{
				double score=0.0;			
				String []imginfo=readin_imgsrc.split("\t");
				score=pearson(reader,imginfo[1],summaryvecDir+topic_id+".txt");
				imgvec.add(new sqlinfo(imginfo[0],imginfo[1],score));
				
				readin_imgsrc=br_imgsrc.readLine();
				
			}
		
			Collections.sort(imgvec, new SqlByValueComparator());
		/*
		 * for(int j=0;j<picvec.size();j++)
		{
			System.out.println(((info)picvec.elementAt(j)).score+"\t");
		}
		*/
			
			String eventid_set="(";
			int imgno=imgvec.size()>5?5:imgvec.size();
			String title="";
			for(int j=0;j<imgno;j++)
			{
				sqlinfo tmp=(sqlinfo) imgvec.elementAt(j);
				String news_page_id=tmp.news_page_id;
				if(j<imgno-1)
					eventid_set+=news_page_id+",";	
				else
					eventid_set+=news_page_id+");";
				if(j==0)
					title+=tmp.title;
			}
			
		    ResultSet rs = statement.executeQuery(sql+eventid_set);
		    String urls="";
		    while(rs.next()) { 
		    	//urls+="http://assets.973.udms.org/"+rs.getString("url")+"\t";
		    	urls+=rs.getString("url")+"\t";
		    }
		    rs.close();
		    
		    File imgresultFile = new File(imgresultDir+topic_id+".txt");
			FileWriter fw = new FileWriter(imgresultFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(urls);
			bw.flush();
			
			File titleFile = new File(titleDir+topic_id+".txt");
			FileWriter fw_title = new FileWriter(titleFile);
			BufferedWriter bw_title = new BufferedWriter(fw_title);
			bw_title.write(title);
			bw_title.flush();
			
			fw.close();
			bw.close();
			fw_title.close();
			bw_title.close();
			fis_imgsrc.close();
			br_imgsrc.close();
			readin_topic=br_topic.readLine();
		}
		fis_topic.close();
		br_topic.close();
		conn.close();
		} catch(ClassNotFoundException e) {   
		    System.out.println("Sorry,can`t find the Driver!");   
		    e.printStackTrace();   
		    } catch(SQLException e) {   
		    e.printStackTrace();   
		    } catch(Exception e) {   
		    e.printStackTrace();   
		    }
	}
	
	public static void main(String args[]) throws Exception {
		/*String indexDir="C:\\tfidf-fast\\indexDir";
		String summaryvecDir="C:\\tfidf-fast\\imgDir\\d04\\summaryvector.txt";
        String picresultDir="C:\\tfidf-fast\\imgDir\\d04\\pic.txt";
        String picorgDir="C:\\tfidf-fast\\imgDir\\d04\\picinfo.txt";
		
	    Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(dir);
		
		getimg gg = new getimg();
		gg.get(reader,picorgDir, summaryvecDir, picresultDir);*/
		
		String indexDir="C:\\973\\indexDir";
		String summaryvecDir="C:\\973\\SummaryVectorDir\\";
        String imgresultDir="C:\\973\\RepreimgDir\\";
        String imgDir="C:\\973\\img\\";
        String topicDir="C:\\973\\topic\\topicno.txt";
        String titleDir="C:\\973\\title\\";
        
        Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(dir);
		
		getimg gg = new getimg();
		gg.Sqlget(reader,topicDir,imgDir, summaryvecDir,imgresultDir,titleDir);
	}
};