//package DUC2006;

import java.io.BufferedInputStream;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
//import ICTCLASAnalyzer;
import org.apache.lucene.util.Version;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Indexer {
	public void MyIdexer(String dataDir, String indexDir) 
			throws CorruptIndexException, LockObtainFailedException, IOException{
				
		     Analyzer simple = new StandardAnalyzer(Version.LUCENE_36);		
			//Analyzer simple = new SimpleAnalyzer(Version.LUCENE_36);
		    //Analyzer simple = new PaodingAnalyzer();
		    //Analyzer simple = new ICTCLASAnalyzer(ICTCLASDelegate.getDelegate());
		   //Analyzer simple = new FUDANAnalyzer(FUDANDelegate.getDelegate());
		   // IndexWriter writer = new IndexWriter(indexDir, simple, true, IndexWriter.MaxFieldLength.UNLIMITED);
		    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,simple);
		    Directory dir = FSDirectory.open(new File(indexDir));
		    IndexWriter writer = new IndexWriter(dir, conf);
		    //IndexWriter writer = new IndexWriter(indexDir, simple, true, IndexWriter.UNLIMITED);
		    
			fileList(dataDir,writer);
			//writer.optimize();
		    writer.close();    
		}
			
	public void fileList(String s,IndexWriter writer) throws IOException{
		
			File file = new File(s);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()){
					fileList(files[i].getAbsolutePath(), writer);
				}
				else{
					File f = files[i];
					OneIndexer(f, writer);
				}
			}
		}
	public void OneIndexer(File file, IndexWriter writer) throws IOException{		
				
			FileInputStream fis = new FileInputStream(file);	
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			
			String readin = br.readLine();
			while(readin!=null){
				Document doc = new Document();
				doc.add(new Field("FileName", file.getAbsolutePath() ,Field.Store.YES, Field.Index.NO));
				doc.add(new Field("Sen", readin ,Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				System.out.println(file.getName()+": "+readin);
				writer.addDocument(doc);
				readin = br.readLine();
			}
			
			br.close();
			fis.close();
		}
		
	public void writeindex(String content,IndexWriter writer,String pre_topic_id) throws CorruptIndexException, IOException
	{
		String splitword="[。！？\\.?!]";
		String []sent_content=content.split(splitword);
		for(int j=0;j<sent_content.length;j++)
		{
			Document doc = new Document();
			doc.add(new Field("FileName", pre_topic_id ,Field.Store.YES, Field.Index.NO));
			doc.add(new Field("Sen", sent_content[j].trim() ,Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			System.out.println(pre_topic_id+": "+sent_content[j]);
			writer.addDocument(doc);
		}
	}
    public void SqlIndex(String indexDir,String imgDir,String topicDir) throws IOException
    {
    	Analyzer simple = new FUDANAnalyzer(FUDANDelegate.getDelegate());
    	IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,simple);
	    Directory dir = FSDirectory.open(new File(indexDir));
	    IndexWriter writer = new IndexWriter(dir, conf);
    	//connect to the database 
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
		String sql = "select topic_id,news_pages.id,news_pages.content,news_pages.title from events_topics join event_documents join news_pages on event_documents.event_id=events_topics.event_id and event_documents.document_type=\"NewsPage\" and event_documents.document_id=news_pages.id and events_topics.topic_id < 924 order by topic_id";
	    ResultSet rs = statement.executeQuery(sql);
	    File imgFile,topicFile=new File(topicDir+"topicno.txt");
		FileWriter fw_img = null,fw_topic=new FileWriter(topicFile);
		BufferedWriter bw_img = null,bw_topic=new BufferedWriter(fw_topic);
	    int pre_topic_id=-1;
	    String content="";
	   
	    while(rs.next()) {  
    		int topic_id = rs.getInt("topic_id");
	    	if(topic_id!=pre_topic_id)
	    	{
	    		if(pre_topic_id!=-1)
	    		{
	    			bw_img.close();
					fw_img.close();
					writeindex(content,writer,""+pre_topic_id);
		    		content="";
	    		}
		    	imgFile=new File(imgDir+topic_id+"id_title.txt");
	    		fw_img=new FileWriter(imgFile);
	    		bw_img = new BufferedWriter(fw_img);
	    		bw_topic.write(topic_id+"\n");
	    		bw_topic.flush();
	    		
	    	}
	    	
	    	String c=rs.getString("news_pages.content");
	    	//content+=new String(c.getBytes("ISO-8859-1"),"GB2312");
	    	content+=c;
	    	String title=rs.getString("news_pages.title");
	    	title=title.trim();
	    	int id=rs.getInt("news_pages.id");
	    	if(title!="")
	    	{
	    		bw_img.write(id+"\t"+title+"\n");
	    		bw_img.flush();
	    	}
	    	
	    	pre_topic_id=topic_id;
	    	
	    	
	    }  
	    bw_img.close();
	    fw_img.close();
	    writeindex(content,writer,""+pre_topic_id);
	    
	    bw_topic.close();
	    fw_topic.close();
	    writer.close(); 
	    rs.close();  
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
    
    public void xmlIndex(String dataDir, String indexDir) throws IOException, ParserConfigurationException, SAXException
    {
    	Analyzer simple = new StandardAnalyzer(Version.LUCENE_36);		
	    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,simple);
	    Directory dir = FSDirectory.open(new File(indexDir));
	    IndexWriter writer = new IndexWriter(dir, conf);
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	    DocumentBuilder db = dbf.newDocumentBuilder(); 
	    org.w3c.dom.Document document = db.parse(new File(dataDir)); 
	    NodeList topics = document.getElementsByTagName("topic"); 	    
	    for (int i = 0; i < topics.getLength(); i++) { 
	    	 Node topic = topics.item(i);
	    	 Node attr=topic.getAttributes().item(0);
	    	 if(attr.getNodeName().equals("gid")&&attr.getNodeValue().equals("2"))
	    	 {
	    		 NodeList docs=topic.getChildNodes();
	    		 for(int j=0;j<docs.getLength();j++)
	    		 {
	    			 Node doc=docs.item(j);
	    			 if(doc.getNodeName().equals("docs"))
	    			 {
	    				 NamedNodeMap docattrs=doc.getAttributes();
	    				 if(Double.parseDouble(docattrs.item(1).getNodeValue())>=0.6)
	    				 {
	    					 String filename=docattrs.item(0).getNodeValue();
	    					 FileInputStream fis = new FileInputStream(filename);	
	    					 BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	    					 String readbin=br.readLine();
	    					 String content="";
	    					 while(readbin!=null)
	    					 {
	    						 content+=readbin;
	    						 readbin=br.readLine();
	    					 }
	    					 
	    					 writeindex(content, writer,filename);
	    					 br.close();
	    					 fis.close();
	    				 }
	    			 }
	    		 }
	    		 break;
	    	 }
	    }
	    writer.close();
    }
    
	public static void main(String args[]) throws Exception {
				String baseDir="E:\\DCD\\summarization\\EvoLDA_data\\epoch3\\";
				String indexDir=baseDir+"summarization\\indexDir";
				String dataDir=baseDir+"topics_3.xml";
				//String imgDir=baseDir+"summarization\\img\\";
				//String topicDir=baseDir+"summarization\\topic\\";
			    Indexer indexer = new Indexer();
			    //indexer.SqlIndex(indexDir,imgDir,topicDir);
			    indexer.xmlIndex(dataDir,indexDir);
			    //indexer.MyIdexer(dataDir, indexDir); 
				  
		  }

	}
