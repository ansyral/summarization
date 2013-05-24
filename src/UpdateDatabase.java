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
	public void update(String topicDir,String peersDir,String imgresultDir,String hftermDir, String titleDir) throws IOException
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
		//String sql = "insert into topic_summary (topic_id,summary,repimg,repword,repword_weight) values";
		//String sql = "update topic_summary set title=";
		String sql="update topic_summary set ";
	    
		
		File topicFile = new File(topicDir);		
		FileInputStream fis_topic = new FileInputStream(topicFile);	
		BufferedReader br_topic = new BufferedReader(new InputStreamReader(fis_topic));
		String readin_topic = br_topic.readLine();
		while(readin_topic!=null)
		{
			int topic_id=Integer.parseInt(readin_topic);
			String summary="",repreimg="",hfterm="",hfterm_weight="",title="",people="",place="",org="";
			
			/*File File = new File(peersDir+topic_id+".txt");		
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
			
			File = new File(imgresultDir+topic_id+".txt");		
			fis = new FileInputStream(File);	
			br = new BufferedReader(new InputStreamReader(fis));
			repreimg=br.readLine();
			fis.close();
			br.close();
			
			File = new File(hftermDir+topic_id+"term.txt");		
			fis = new FileInputStream(File);	
			br = new BufferedReader(new InputStreamReader(fis));
			hfterm=br.readLine().replace('\"', '\'');
			fis.close();
			br.close();
			
			File = new File(hftermDir+topic_id+"weight.txt");		
			fis= new FileInputStream(File);	
			br = new BufferedReader(new InputStreamReader(fis));
			hfterm_weight=br.readLine();
			fis.close();
			br.close();
			
			File File = new File(titleDir+topic_id+".txt");		
			FileInputStream fis= new FileInputStream(File);	
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			title=br.readLine().replace('\"', '\'');
			fis.close();
			br.close();*/
			
			File File = new File(hftermDir+topic_id+"structure.txt");		
			FileInputStream fis= new FileInputStream(File);	
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			people=br.readLine().replace('\"', '\'');
			place=br.readLine().replace('\"', '\'');
			org=br.readLine().replace('\"', '\'');
			fis.close();
			br.close();
			
			//String state=sql+"("+topic_id+",\""+summary+"\",\""+repreimg+"\",\""+hfterm+"\",\""+hfterm_weight+"\");";
			//String state=sql+"\""+title.trim()+"\" where topic_id="+topic_id+";";
			String state=sql+"people=\""+people.trim()+"\",place=\""+place.trim()+"\",org=\""+org.trim()+"\" where topic_id="+topic_id+";";
			statement.execute(state);
			
			readin_topic = br_topic.readLine();
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
	public static void main (String args[]) throws IOException
	{
		String imgresultDir="C:\\973\\RepreimgDir\\";
		String topicDir="C:\\973\\topic\\topicno.txt";
		String peersDir="C:\\973\\PeersDir\\";
		String hftermDir="C:\\973\\hftermDir\\";
		String titleDir="C:\\973\\title\\";

	    UpdateDatabase updatedatabase = new UpdateDatabase();
	    updatedatabase.update(topicDir,peersDir,imgresultDir,hftermDir,titleDir);
	}
}