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

public class processtag{
	public static void main(String args[]) throws Exception {
		String TermDir="C:\\tfidf-fast\\hf_term\\d04\\term.txt";
		String posDir="C:\\tfidf-fast\\hf_term\\d04\\pos.txt";
		String SDir="C:\\tfidf-fast\\hf_term\\d04\\structure.txt";
		String nr="";
		String nd="";
		String nj="";
		String ns="";
		String ne="";
		
		File POSFile = new File(posDir);
		FileInputStream fwForsn = new FileInputStream(POSFile);
		BufferedReader bwForsn = new BufferedReader(new InputStreamReader(fwForsn));
		String posreadin=bwForsn.readLine();
		String []pos=posreadin.split(",");
		
		File SFile = new File(SDir);
		FileWriter fsForsn = new FileWriter(SFile);
		BufferedWriter bsForsn = new BufferedWriter(fsForsn);
		
		File TermIndex = new File(TermDir);		
		FileInputStream fis = new FileInputStream(TermIndex);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		String[] splitReadin = readin.split(",");
		for (int i=0; i < pos.length;i++){
			
			
				String type=pos[i];				
				if(type.equals("人名"))
					nr=nr+splitReadin[i]+",";
				else if(type.equals("地名"))
					nd=nd+splitReadin[i]+",";
				else if(type.equals("机构名"))
					nj=nj+splitReadin[i]+",";
				else if(type.equals("事件名"))
					ns=ns+splitReadin[i]+",";
				else if(type.equals("实体名"))
					ne=ne+splitReadin[i]+",";
			
		}
		bsForsn.write("人名:"+nr+"\n"+"地名:"+nd+"\n"+"机构名:"+nj+"\n"+"事件名:"+ns+"\n"+"实体名:"+ne+"\n");
		bsForsn.flush();
		bwForsn.close();
		fwForsn.close();
		bsForsn.close();
		fsForsn.close();
		br.close();
		fis.close();
	}
}