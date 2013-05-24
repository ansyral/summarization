import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class GetData
{
	public String ReadFile(String path){
	    File file = new File(path);
	    BufferedReader reader = null;
	    String laststr = "";
	    try {
	     //System.out.println("以行为单位读取文件内容，一次读一整行：");
	     reader = new BufferedReader(new FileReader(file));
	     String tempString = null;
	     int line = 1;
	     //一次读入一行，直到读入null为文件结束
	     while ((tempString = reader.readLine()) != null) {
	      //显示行号
	     // System.out.println("line " +line+ ": "+ tempString);
	      laststr = laststr +tempString;
	      line ++;
	     }
	     reader.close();
	    } catch (IOException e) {
	     e.printStackTrace();
	    } finally {
	     if (reader != null) {
	      try {
	       reader.close();
	      } catch (IOException e1) {
	      }
	     }
	    }
	    return laststr;
	}
	public static void main (String args[]) throws IOException
	{
		
		GetData g=new GetData();
		String path="C:\\data\\topics-130313.json";
		String sets=g.ReadFile(path);
		JSONArray jo=JSONArray.fromObject(sets);
		//Object[] obj=jo.toArray();
		String text="";
		for(int i=0;i<1;i++){ 
			JSONArray tmp=jo.getJSONObject(i).getJSONArray("Events");
			for(int j=0;j<tmp.size();j++)
			{
				JSONArray childdocs=tmp.getJSONObject(j).getJSONArray("childDocs");
				for(int k=0;k<childdocs.size();k++)
					text+=childdocs.getJSONObject(k).get("content");
			}
			//System.out.println(tmp);  
		}
		File dataFile = new File("C:\\tfidf-fast\\dataDir\\d05\\1.txt");
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
		//String url=jo.getString("childDocs");
		//System.out.println( jo );
	}
}