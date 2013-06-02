import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateDatabase
{
	public void update(String peersDir,String imgresultDir,String hftermDir,int monthid) throws IOException
	{
		String driver = "com.mysql.jdbc.Driver";			
		String url = "jdbc:mysql://127.0.0.1/summaryshow";
		String user = "root";			
		String password = "a19890624";
		try {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		if(!conn.isClosed())
			System.out.println("Succeeded connecting to the Database!");
		Statement statement = conn.createStatement();			
		String sql = "insert into topic_evolution (topic_id,month_id,summary,repimg,repword,repword_weight) values";
		//String sql = "update topic_summary set title=";
		//String sql="update topic_summary set ";
	    
		
		
		String summary="",repreimg="",hfterm="",hfterm_weight="",title="",people="",place="",org="";
			
		File File = new File(peersDir+"d01.txt");		
		FileInputStream fis = new FileInputStream(File);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin=br.readLine();
		while(readin!=null)
		{
			summary+=readin.replace('\"', '\'')+"\n";
			readin=br.readLine();
		}
		fis.close();
		br.close();
			
		File = new File(imgresultDir+"d01.txt");		
		fis = new FileInputStream(File);	
		br = new BufferedReader(new InputStreamReader(fis));
		repreimg=br.readLine();
		fis.close();
		br.close();
			
		File = new File(hftermDir+"term.txt");		
		fis = new FileInputStream(File);	
		br = new BufferedReader(new InputStreamReader(fis));
		hfterm=br.readLine().replace('\"', '\'');
		fis.close();
		br.close();
			
		File = new File(hftermDir+"weight.txt");		
		fis= new FileInputStream(File);	
		br = new BufferedReader(new InputStreamReader(fis));
		hfterm_weight=br.readLine();
		fis.close();
		br.close();
			
			
			
		
			
		String state=sql+"("+1+","+monthid+",\""+summary+"\",\""+repreimg+"\",\""+hfterm+"\",\""+hfterm_weight+"\");";
		//String state=sql+"\""+title.trim()+"\" where topic_id="+topic_id+";";
		//String state=sql+"people=\""+people.trim()+"\",place=\""+place.trim()+"\",org=\""+org.trim()+"\" where topic_id=1;";
		statement.execute(state);
			
		
		
		
 
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
	public static void main (String args[]) throws IOException
	{
		int monthid=11;
		String baseDir="E:\\DCD\\summarization\\EvoLDA_data\\epoch"+monthid+"\\";		
		String imgresultDir=baseDir+"summarization\\RepreimgDir\\";
		//String topicDir="C:\\973\\topic\\topicno.txt";
		String peersDir=baseDir+"summarization\\PeersDir\\";
		String hftermDir=baseDir+"summarization\\hftermDir\\";
		//String titleDir="C:\\973\\title\\";
		//String imporDir="E:\\DCD\\summarization\\EvoLDA_data\\importance.txt";
		
	    UpdateDatabase updatedatabase = new UpdateDatabase();
	    updatedatabase.update(peersDir,imgresultDir,hftermDir,monthid);
	}
}