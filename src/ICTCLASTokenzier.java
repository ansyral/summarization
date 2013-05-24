import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public  class ICTCLASTokenzier extends Tokenizer {
		private  CharTermAttribute termAttr;
		private OffsetAttribute offAttr;
		private Matcher matcher;

		public ICTCLASTokenzier(String segmented) {
			init(segmented);
		}

		private void init(String segmented) {
			termAttr = addAttribute( CharTermAttribute.class);
			offAttr = addAttribute(OffsetAttribute.class);
			//matcher = Pattern.compile("(([^ ]+)| )/(\\w)+").matcher(segmented);
			matcher = Pattern.compile("(([^ ]+)| )/(\\w+)").matcher(segmented);
		}

		private boolean filter(String t, String type) {
			if (t.matches("\\s*"))
				return false;
			if (t.matches("[0-9]+"))
				return false;
			if (t.equals("��")) {
				return false;
			}
			if (type.endsWith("n")) {
				return true;
			}
			if (type.endsWith("nr")) {
				return true;
			}
			if (type.endsWith("ns")) {
				return true;
			}
			if (type.endsWith("nt")) {
				return true;
			}
			if (type.equals("x") && t.length() > 1)
				return true;
			if (type.equals("xx") && t.length() > 1)
				return true;
			if (type.equals("xu") && t.length() > 1)
				return true;
			return false;
		}

		@Override
		public boolean incrementToken() throws IOException {
			int s = offAttr.endOffset();
			clearAttributes();
			while (matcher.find()) {
				String t = matcher.group(1);
				String type = matcher.group(3);
				String compound=t+'/'+type;
				if (filter(t, type)) {
					//termAttr.copyBuffer(t.toCharArray(), 0, t.length());
					termAttr.copyBuffer(compound.toCharArray(), 0, compound.length());
					System.out.println(compound);
					//offAttr.setOffset(s, s + t.length());
					offAttr.setOffset(s, s + compound.length());
					return true;
				} else {
					s += t.length();
				}
			}
			return false;
		}
	}