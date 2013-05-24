/* 
注释内为英文使用opennlp进行断句的预处理
import java.io.*;
import java.nio.charset.Charset;
import java.util.*; 

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import org.w3c.dom.*; 
import javax.xml.parsers.*; 

public class preprocess{
	
    private String content="";
	public  void process(Node nl)
	{
		Node first=nl.getFirstChild();
		while(first!=null)
		{
			if(first.getNodeType()==Node.TEXT_NODE)
				content+=first.getNodeValue();
			else if(first.getNodeType()==Node.ELEMENT_NODE)
			{
				 process(first);
			}
			first=first.getNextSibling();
		}
	}
	public void xmlparse(String dataDir){ 

		
		try
		{ 
			File file = new File(dataDir);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()){
					xmlparse(files[i].getAbsolutePath());
				}
				else{
					File f = files[i];
					content="";
					DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
					DocumentBuilder builder=factory.newDocumentBuilder(); 
					Document doc = builder.parse(f); 
					NodeList nl = doc.getElementsByTagName("TEXT"); 
					process(nl.item(0));
					BufferedWriter output = new BufferedWriter(new FileWriter(f));
				    output.write(content);
				    output.close();
					//System.out.print(doc.getElementsByTagName("TEXT").item(0).getFirstChild().getNodeValue()); 
				
				}
			}
			
		}
		catch(Exception e){ 
			e.printStackTrace(); 
		} 
	}
	
	public void trainnlp(String trainfile,String modelfile)throws Exception
	{
		Charset charset = Charset.forName("UTF-8");				
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainfile),charset);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
		  model = SentenceDetectorME.train("en", sampleStream, true, null, 5, 100);
		}
		finally {
		  sampleStream.close();
		}

		OutputStream modelOut = null;
		try {
		  modelOut = new BufferedOutputStream(new FileOutputStream(modelfile));
		  model.serialize(modelOut);
		} finally {
		  if (modelOut != null) 
		     modelOut.close();      
		}
	}
	public void sentdect(String dataDir,String modelfile) throws Exception
	{
		InputStream modelIn = new FileInputStream(modelfile);

		try {
		  SentenceModel model = new SentenceModel(modelIn);
		  SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		  File file = new File(dataDir);
		  File[] files = file.listFiles();
		  for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()){
					sentdect(files[i].getAbsolutePath(),modelfile);
				}
				else{
					File f = files[i];
					String doc="";
					BufferedReader input = new BufferedReader(new FileReader(f));
					while (input.ready()) {
		                doc=doc+ input.readLine()+"\n";//��ȡһ�� 
		            }
					input.close();
					String sentences[] = sentenceDetector.sentDetect(doc);
					StringBuffer sb = new StringBuffer();
					for(int j = 0; j < sentences.length; j++){
					 sb. append(sentences[j].replace("\n", " ")+"\n");
					}
					doc= sb.toString();
					BufferedWriter output = new BufferedWriter(new FileWriter(f));
				    output.write(doc);
				    output.close();
				}
		  }
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		
		
	}
	
	public static void main(String arge[]) throws Exception 
	{
		String dataDir="C:\\nopre\\dataDir";
		preprocess data=new preprocess();
		//data.xmlparse(dataDir);
		String trainfile="C:\\nopre\\en-sent.train";
		String modelfile="C:\\nopre\\en-sent.bin";
		data.trainnlp(trainfile, modelfile);
		data.sentdect(dataDir,modelfile);
	
	}
}*/