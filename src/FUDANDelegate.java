import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.cn.tag.POSTagger;
import edu.fudan.util.exception.LoadModelException;



public class FUDANDelegate {
	private static FUDANDelegate instance;
	
	public static FUDANDelegate getDelegate()
	{
		if (instance == null)
		{
			synchronized (FUDANDelegate.class)
			{
				if (instance == null)
				{
					instance = new FUDANDelegate();
					
					
				}
			}
		}
		return instance;
	}

	public String process(String source) throws Exception
	{
		POSTagger tag;
		CWSTagger cws2 = new CWSTagger("C:\\Users\\DELL390\\workspace\\fudannlp\\src\\models\\seg.m", new Dictionary("C:\\Users\\DELL390\\workspace\\fudannlp\\src\\models\\dict.txt"));
		tag = new POSTagger(cws2, "C:\\Users\\DELL390\\workspace\\fudannlp\\src\\models\\pos.m"
				, new Dictionary("C:\\Users\\DELL390\\workspace\\fudannlp\\src\\models\\dict_pos.txt"), true);//true就替换了之前的dict.txt
		tag.removeDictionary(false);//不移除分词的词典
		tag.setDictionary(new Dictionary("C:\\Users\\DELL390\\workspace\\fudannlp\\src\\models\\dict_pos.txt"), false);//设置POS词典，分词使用原来设置
		String s = tag.tag(source);
		
		
		return  s;
	}

}
