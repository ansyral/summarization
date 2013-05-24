import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class gethfterm{
	public static void main(String args[]) throws Exception {
		String indexDir="C:\\973\\indexDir";
		String topicDir="C:\\973\\topic\\topicno.txt";
		String TermIndexDir = "C:\\973\\hftermDir\\";
		String TermDir="C:\\973\\hftermDir\\";
		String POSDir="C:\\973\\hftermDir\\";
		Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(dir);
		
		File topicFile = new File(topicDir);		
		FileInputStream fis_topic = new FileInputStream(topicFile);	
		BufferedReader br_topic = new BufferedReader(new InputStreamReader(fis_topic));
		String readin_topic = br_topic.readLine();
		while(readin_topic!=null)
		{
			int topic_id=Integer.parseInt(readin_topic);
			File TermFile = new File(TermDir+topic_id+"term.txt");
			FileWriter fwForsn = new FileWriter(TermFile);
			BufferedWriter bwForsn = new BufferedWriter(fwForsn);
		
			File POSFile = new File(POSDir+topic_id+"pos.txt");
			FileWriter POSfwForsn = new FileWriter(POSFile);
			BufferedWriter POSbwForsn = new BufferedWriter(POSfwForsn);
			
			File STRFile = new File(POSDir+topic_id+"structure.txt");
			FileWriter STRfwForsn = new FileWriter(STRFile);
			BufferedWriter STRbwForsn = new BufferedWriter(STRfwForsn);
			String people="",place="",org="";
		
			File TermIndex = new File(TermIndexDir+topic_id+"termindex.txt");		
			FileInputStream fis = new FileInputStream(TermIndex);	
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String readin = br.readLine();
			String[] splitReadin = readin.split(",");
			int termindex;
			for (int i=0; i < splitReadin.length;i++){
				termindex = Integer.parseInt(splitReadin[i]);
				TermEnum te = reader.terms();
				int no=0;
				String term="";
				while (te.next()){
					Term t = te.term();
					no++;
					if(no==termindex)
					{
						term=t.text();
						break;
					}
				}
				String tt,type;
				String[]tmp=term.split("/");
				tt=tmp[0];
				type=tmp[1];
				bwForsn.write(tt+",");
				bwForsn.flush();
				POSbwForsn.write(type+",");
				POSbwForsn.flush();
				if(type.equals("人名"))
					people+=tt+"\t";
				else if(type.equals("地名"))
					place+=tt+"\t";
				else if(type.equals("机构名"))
					org+=tt+"\t";
			}
		
			STRbwForsn.write(people+"\n"+place+"\n"+org+"\n");
			STRbwForsn.flush();
			bwForsn.close();
			fwForsn.close();
			POSbwForsn.close();
			POSfwForsn.close();
			STRbwForsn.close();
			STRfwForsn.close();
			br.close();
			fis.close();
			
			readin_topic = br_topic.readLine();
		}
		fis_topic.close();
		br_topic.close();
		
	}
}