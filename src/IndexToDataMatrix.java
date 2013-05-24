

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class IndexToDataMatrix {
	public String process(String line)
	{
		String []word=line.split(" ");
		double sum=0;
		double []s=new double[word.length];
		String newline="";
		for(int j=1;j<word.length;j++)
		{
			String str=word[j];
			s[j]=Double.parseDouble(str);
			sum+=(s[j])*(s[j]);
	
		}
		sum=Math.sqrt(sum);
		for(int j=1;j<word.length;j++)
		{
			if(s[j]==0)
				newline=newline+" "+0;
			else
			    newline=newline+" "+s[j]/sum;
		}
		return newline;
	}
	
	public void MyIndexToDataMatrix(IndexReader reader, String indexDir, String dataMatrixDir, String sentenceNumberOfMatrixDir) throws Exception{
		int docFileNumber = 4;
		//int totolDocFile = 60;
		int totolDocFile = 1;
		
		
		for (int i = 0;i<totolDocFile;i++){
			
			docFileNumber++;
			String name="\\d"+docFileNumber;
			if(docFileNumber<10)
				name="\\d0"+docFileNumber;
			File matrixFile = new File(dataMatrixDir+name+".txt");
			FileWriter fw = new FileWriter(matrixFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			File senNumFile = new File(sentenceNumberOfMatrixDir+name+".txt");
			FileWriter fwForsn = new FileWriter(senNumFile);
			BufferedWriter bwForsn = new BufferedWriter(fwForsn);
			String wlForsn = "";

			
			
			for (int j=0;j<reader.numDocs();j++){//search all the document in the reader 
				
				
				if(reader.document(j).getFieldable("FileName").stringValue().contains(name)){//find a sentence of the document set 
					
					if(reader.getTermFreqVector(j, "Sen")==null){// some sentence does not contain valuable terms so the term vector is null.
						continue;
					}
					else{
						//System.out.println("find a sentence of the document "+"D0"+docFileNumber);
						//System.out.println(reader.document(j).getField("FileName").stringValue());
						//System.out.println(reader.document(j).getField("Sen").stringValue());
						
						
						wlForsn = wlForsn+" "+j;
						
						String WriteLine = "";// one sentence is one line in the matMatrix for matlab input data
						TermEnum te = reader.terms();
								
						while (te.next()){
							Term t = te.term();
							if (t.field().equals("Sen")){
								//System.out.println("term "+t.text());
								//System.out.println(reader.getTermFreqVector(j, "Sen"));
								TermDocs termdoc=reader.termDocs(t);
								int num=0;
								while(termdoc.next())
									num++;
								int termIndex = reader.getTermFreqVector(j, "Sen").indexOf(t.text());
								if(termIndex>=0){
									//System.out.println("find the term "+t.text());
									//double tf=1.0*reader.getTermFreqVector(j, "Sen").getTermFrequencies()[termIndex]/sum(reader.getTermFreqVector(j, "Sen").getTermFrequencies());
									double tf=1.0*reader.getTermFreqVector(j, "Sen").getTermFrequencies()[termIndex];
									double idf=Math.log10(1.0*reader.numDocs()/num);
									WriteLine = WriteLine+" "+tf*idf;
									//System.out.println(WriteLine);
								}
								else{//this sentence does not contain the term t
									//System.out.println("this sentence does not contain the term t");
									WriteLine = WriteLine+" "+ 0;
									//System.out.println(WriteLine);
								}
							}
							else{//the term comes from the field of filename
								//System.out.println("the term comes from the field of filename");
								WriteLine = WriteLine+" 0";
								//System.out.println(WriteLine);
							}
						}
						
						WriteLine=process(WriteLine);
						WriteLine = WriteLine+"\n";
						bw.write(WriteLine);
						//System.out.println(WriteLine);
						bw.flush();
						
					}//end of else
					
					
				}//end of if

			}//end of for
			
			
			bw.close();
			fw.close();
			
			bwForsn.write(wlForsn);
			bwForsn.flush();
			//System.out.println(wlForsn);

			bwForsn.close();
			fwForsn.close();
			
			//System.out.println("matMatrixFile "+matrixFile.getName()+" is completed");
			//System.out.println("senNumFile "+senNumFile.getName()+" is completed");

		}
		
		
	}
	
	public int sum(int fre[])
	{
		int sum=0;
		for(int i : fre)
			sum+=i;
		return sum;
	}
	public void SqlIndexToDataMatrix(IndexReader reader, String indexDir, String topicDir,String dataMatrixDir, String sentenceNumberOfMatrixDir) throws IOException
	{
		File topicFile = new File(topicDir);
		FileInputStream fis = new FileInputStream(topicFile);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		while(readin!=null)
		{
			int topic_id=Integer.parseInt(readin);
			String name=""+topic_id;
			
			File matrixFile = new File(dataMatrixDir+topic_id+".txt");
			FileWriter fw = new FileWriter(matrixFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			File senNumFile = new File(sentenceNumberOfMatrixDir+topic_id+".txt");
			FileWriter fwForsn = new FileWriter(senNumFile);
			BufferedWriter bwForsn = new BufferedWriter(fwForsn);
			
			String wlForsn = "";
			for (int j=0;j<reader.numDocs();j++){//search all the document in the reader 
				
				
				if(reader.document(j).getFieldable("FileName").stringValue().contains(name)){//find a sentence of the document set 
					
					if(reader.getTermFreqVector(j, "Sen")==null){// some sentence does not contain valuable terms so the term vector is null.
						continue;
					}
					else{
						//System.out.println("find a sentence of the document "+"D0"+docFileNumber);
						//System.out.println(reader.document(j).getField("FileName").stringValue());
						//System.out.println(reader.document(j).getField("Sen").stringValue());
						
						
						wlForsn = wlForsn+" "+j;
						
						String WriteLine = "";// one sentence is one line in the matMatrix for matlab input data
						TermEnum te = reader.terms();
								
						while (te.next()){
							Term t = te.term();
							if (t.field().equals("Sen")){
								//System.out.println("term "+t.text());
								//System.out.println(reader.getTermFreqVector(j, "Sen"));
								TermDocs termdoc=reader.termDocs(t);
								int num=0;
								while(termdoc.next())
									num++;
								int termIndex = reader.getTermFreqVector(j, "Sen").indexOf(t.text());
								if(termIndex>=0){
									//System.out.println("find the term "+t.text());
									//double tf=1.0*reader.getTermFreqVector(j, "Sen").getTermFrequencies()[termIndex]/sum(reader.getTermFreqVector(j, "Sen").getTermFrequencies());
									double tf=1.0*reader.getTermFreqVector(j, "Sen").getTermFrequencies()[termIndex];
									double idf=Math.log10(1.0*reader.numDocs()/num);
									WriteLine = WriteLine+" "+tf*idf;
									//System.out.println(WriteLine);
								}
								else{//this sentence does not contain the term t
									//System.out.println("this sentence does not contain the term t");
									WriteLine = WriteLine+" "+ 0;
									//System.out.println(WriteLine);
								}
							}
							else{//the term comes from the field of filename
								//System.out.println("the term comes from the field of filename");
								WriteLine = WriteLine+" 0";
								//System.out.println(WriteLine);
							}
						}
						
						WriteLine=process(WriteLine);
						WriteLine = WriteLine+"\n";
						bw.write(WriteLine);
						//System.out.println(WriteLine);
						bw.flush();
						
					}//end of else
					
					
				}//end of if

			}//end of for
			
			
			bw.close();
			fw.close();
			
			bwForsn.write(wlForsn);
			bwForsn.flush();
			//System.out.println(wlForsn);

			bwForsn.close();
			fwForsn.close();
			
			//System.out.println("matMatrixFile "+matrixFile.getName()+" is completed");
			//System.out.println("senNumFile "+senNumFile.getName()+" is completed");
						
			readin=br.readLine();
		}
		fis.close();
		br.close();
	}
	
	public static void main(String args[]) throws Exception {
		String indexDir="C:\\973\\indexDir";
		String dataMatrixDir="C:\\973\\DataMatrixDir\\";
	    String sentenceNumberOfMatrixDir = "C:\\973\\SentenceNumberOfMatrixDir\\";
		String topicDir="C:\\973\\topic\\topicno.txt";
	    
	    Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(dir);
		
		IndexToDataMatrix ITDM = new IndexToDataMatrix();
		//ITDM.MyIndexToDataMatrix(reader, indexDir, dataMatrixDir, sentenceNumberOfMatrixDir);
		ITDM.SqlIndexToDataMatrix(reader, indexDir, topicDir,dataMatrixDir, sentenceNumberOfMatrixDir);
	}

}
