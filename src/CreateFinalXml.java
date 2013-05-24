//package DUC2006;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;



public class CreateFinalXml {
	public void MyCreateFinalXml(String Dir, String ModelDir, String PeerDir) throws Exception{
		File DirName = new File (Dir);
		File Model = new File(ModelDir);
		File Peer = new File(PeerDir);
		File[] ModelFiles = Model.listFiles();
		File[] PeerFiles = Peer.listFiles();
		
		File XmlFile = new File(Dir+"\\"+DirName.getName()+"-"+Peer.getName()+".XML");
		
		FileWriter fw = new FileWriter(XmlFile);
		BufferedWriter bw = new BufferedWriter(fw);
		 
		String MyWriteLine;
		
		MyWriteLine = "<ROUGE-EVAL version=\"1.0\">"+"\n";
		bw.write(MyWriteLine);
		bw.flush();    
		  for (int EvalId=1; EvalId<=60 ;EvalId++){
		//for (int EvalId=1; EvalId<=60 ;EvalId++){
			//if(EvalId==6)continue;
			MyWriteLine = "<EVAL ID=\""+EvalId+"\">"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			MyWriteLine = "<PEER-ROOT>"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			MyWriteLine = "C:\\tfidf-fast\\"+Peer.getName()+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			MyWriteLine = "</PEER-ROOT>"+"\n"+"<MODEL-ROOT>"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			MyWriteLine = "C:\\tfidf-fast\\"+Model.getName()+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			MyWriteLine = "</MODEL-ROOT>"+"\n"+"<INPUT-FORMAT TYPE=\"SPL\">"+"\n"+"</INPUT-FORMAT>"+"\n"+"<PEERS>"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			for (int i=0;i<PeerFiles.length;i++){
				File PeerFile = PeerFiles[i];
				
				if(EvalId<10 && PeerFile.getName().contains("d0"+EvalId)){
					
					MyWriteLine ="<P ID=\""+PeerFile.getName().substring(0, PeerFile.getName().length()-4)+"\">"+PeerFile.getName()+"</P>"+"\n";
					bw.write(MyWriteLine);
					System.out.print(MyWriteLine);
					bw.flush();    		
					
				}else if (EvalId>=10 && PeerFile.getName().contains("d"+EvalId)){
					
					MyWriteLine ="<P ID=\""+PeerFile.getName().substring(0, PeerFile.getName().length()-4)+"\">"+PeerFile.getName()+"</P>"+"\n";
					bw.write(MyWriteLine);
					System.out.print(MyWriteLine);
					bw.flush();    
					
				}		
			}
			
		    MyWriteLine = "</PEERS>"+"\n"+"<MODELS>"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			bw.flush();    
			
			for (int i=0;i<ModelFiles.length;i++){
				File ModelFile = ModelFiles[i];
				
				if(EvalId<10 && ModelFile.getName().contains("d0"+EvalId)){
					MyWriteLine ="<M ID=\""+ModelFile.getName().substring(0, ModelFile.getName().length()-4)+"\">"+ModelFile.getName()+"</M>"+"\n";
					bw.write(MyWriteLine);
					System.out.print(MyWriteLine);
					bw.flush();    					
				}else if (EvalId>=10 && ModelFile.getName().contains("d"+EvalId)){
					MyWriteLine ="<M ID=\""+ModelFile.getName().substring(0, ModelFile.getName().length()-4)+"\">"+ModelFile.getName()+"</M>"+"\n";
					bw.write(MyWriteLine);
					System.out.print(MyWriteLine);
					bw.flush();    
				}
			}
			
			MyWriteLine = "</MODELS>"+"\n"+"</EVAL>"+"\n";
			bw.write(MyWriteLine);
			System.out.print(MyWriteLine);
			
			bw.flush();    

		}
		MyWriteLine = "</ROUGE-EVAL>";
		bw.write(MyWriteLine);
		System.out.print(MyWriteLine);
		bw.close();
	}


public static void main (String args[]) throws Exception {
	
	String ModelDir="C:\\tfidf-fast\\ModelDir";
	
	
	String Dir="C:\\tfidf-fast\\Dir";
	String PeerDir="C:\\tfidf-fast\\PeersDir";
   
    CreateFinalXml cfx = new CreateFinalXml();
    cfx.MyCreateFinalXml(Dir, ModelDir, PeerDir );
}


}
