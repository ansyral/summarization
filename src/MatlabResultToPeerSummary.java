//package DUC2006;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MatlabResultToPeerSummary {
	
	public void MatResToPeers(IndexReader reader,String MatlabResultDir,String PeersDir, String sentenceNumberOfMatrixDir) throws Exception{
		File file = new File(MatlabResultDir);
		File[] files = file.listFiles();
		
		
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (files[i].isDirectory()){
				File temp = new File(PeersDir+f.getAbsolutePath().substring(MatlabResultDir.length()));
				temp.mkdirs();
				//System.out.println("make dir: "+temp.getName());
				MatResToPeers(reader, f.getAbsolutePath(),temp.getAbsolutePath(),sentenceNumberOfMatrixDir);				
			}
			else{
				OneMatToPeer(reader, f.getAbsolutePath(),PeersDir,sentenceNumberOfMatrixDir);
			}
		}
	}
	
	public void OneMatToPeer(IndexReader reader, String MatlabResultFile,String PeersDir,String sentenceNumberOfMatrixDir) throws Exception{
		File MatResult = new File(MatlabResultFile);
		
		FileInputStream fis = new FileInputStream(MatResult);	
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String readin = br.readLine();
		
		File PeerFile = new File(PeersDir+MatResult.getName());
		FileWriter fw = new FileWriter(PeerFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		File sentNumOfMatrix = new File(sentenceNumberOfMatrixDir);
		File[] sentNumOfMatrixList = sentNumOfMatrix.listFiles();
		
		
		int SummaryLengthCount = 0 ;
		int SummaryLengthLimit =200;
		
		//System.out.println(MatResult.getName());
		
		if(readin.equals("")){
			throw new Exception("ERROR: Matlab result file "+MatResult.getName()+"should not be NULL!");
		}
		else{
			
			String[] splitReadin = readin.split(",");
			String presentence="";
			HashSet hashSet=new HashSet();
			for (int i=0; i < splitReadin.length;i++){
				int SentenceNumberInMatrix = Integer.parseInt(splitReadin[i]); //get a sentence index in the matrix from the matlab result file
				
				String docName = MatResult.getName();
				int docNameLength = docName.length();
				System.out.println(""+SentenceNumberInMatrix);
				//System.out.println("Sentence Number In Matrix is "+SentenceNumberInMatrix);
				
				String WriteLine ="";
				
				for(int j=0;j < sentNumOfMatrixList.length; j++ ){
					
					//System.out.println("sentence number file is "+sentNumOfMatrixList[j].getName());
					//System.out.println("matrixfile of matlab result is "+docName.subSequence(0, docNameLength-4));
					
					if(sentNumOfMatrixList[j].getName().contains(docName.subSequence(0, docNameLength-4))){
						FileInputStream fisForsnom = new FileInputStream(sentNumOfMatrixList[j]);	
						BufferedReader brForsnom = new BufferedReader(new InputStreamReader(fisForsnom));
						String readinForsnom = brForsnom.readLine();
						
						String[] splitReadinForsnom = readinForsnom.split(" ");
						
						//System.out.println(splitReadinForsnom.length);
						//System.out.println("Sentence Number In indexData is "+splitReadinForsnom[SentenceNumberInMatrix]);
						
						int SentenceNumberIndex = Integer.parseInt(splitReadinForsnom[SentenceNumberInMatrix]);//get the sentence index in the index
						
						//System.out.println(reader.document(SentenceNumberIndex).getFieldable("FileName").stringValue());
						
						WriteLine = reader.document(SentenceNumberIndex).getFieldable("Sen").stringValue()+"\n";
						
					
						
						break;
					}
				}
				
				if(presentence=="")
					presentence=WriteLine;
				else if(presentence.equals(WriteLine))
					continue;	
				else if(WriteLine.length()<12)
					continue;
				if(hashSet.contains(WriteLine))
					continue;
				hashSet.add(WriteLine);
				SummaryLengthCount = SummaryLengthCount + WriteLine.length();
			
				//System.out.print(SummaryLengthCount+"\n");
				
				if (SummaryLengthCount > SummaryLengthLimit){
					//System.out.print("Summary Length limited..."+"\n");
					if(i==0)
					{
						bw.write(WriteLine.substring(0, 250));
                        bw.flush();
                        i++;
					}
					System.out.print(i+"\n");
					break;
				}
				else{
					
					bw.write(WriteLine);
		//			System.out.print(WriteLine);
					bw.flush();
					
				}

			}
			
			br.close();
			fis.close();
			bw.close();
			fw.close();
		}
		
		
		
		

	}
	
	public void SqlMatResToPeers(IndexReader reader,String MatlabResultDir,String PeersDir, String sentenceNumberOfMatrixDir)
	{
		
	}
	
	public static void main(String args[]) throws Exception {
		String baseDir="E:\\DCD\\summarization\\EvoLDA_data\\epoch3\\";
		String indexDir=baseDir+"summarization\\indexDir";
		String MatlabResultDir =baseDir+ "summarization\\MatlabResultDir\\";
		String PeersDir =baseDir+ "summarization\\PeersDir\\";
		String sentenceNumberOfMatrixDir = baseDir+"summarization\\SentenceNumberOfMatrixDir\\";
		
		Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader reader = IndexReader.open(dir);
		
		//System.out.print(reader.numDocs()+"\n");
		
		MatlabResultToPeerSummary MRTPS = new MatlabResultToPeerSummary();
		MRTPS.MatResToPeers(reader, MatlabResultDir,PeersDir,sentenceNumberOfMatrixDir);
		//MRTPS.OneMatToPeer(reader, MatlabResultDir,PeersDir,sentenceNumberOfMatrixDir);
		//MRTPS.SqlMatResToPeers(reader, MatlabResultDir,PeersDir,sentenceNumberOfMatrixDir);
	}

}
