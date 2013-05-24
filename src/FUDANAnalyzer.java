import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
public class FUDANAnalyzer extends Analyzer {

	private FUDANDelegate ictc;
	private String result;

	public FUDANAnalyzer() {
		this(null);
	}

	public FUDANAnalyzer(FUDANDelegate ictc) {
		this.ictc = ictc;
	}

	public String getSegmentedString() {
		return result;
	}

	public int getlen(char[]buff)
	{
		int i;
		for(i=0;buff[i]!=0;i++);
		return i;
	}
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		try {
			char[] buff = new char[1024];
			int cpos = 0;
			int len;
			while ((len = reader.read(buff, cpos, 1024)) == 1024) {
				char[] t = buff;
				buff = new char[buff.length + 1024];
				System.arraycopy(t, 0, buff, 0, t.length);
				cpos += len;
			}
			if(len==-1)
				len=getlen(buff)%1024;
			cpos += len;
			if (ictc != null)
				result = ictc.process(new String(buff, 0, cpos));
			else
				result = new String(buff, 0, cpos);
			return new FUDANTokenizer(result);
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
