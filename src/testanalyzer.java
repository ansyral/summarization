import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;




public class testanalyzer{
public static void main(String[] args)

 {

    //����Ӣ�ĵĽ����ű��.,StandardAnalyzer����ʶ��

    String string = new String("近日，一篇名为\"农妇警告：在中国吃猪肉等于自杀\"的陈年旧帖在网上流传，网文称，十几斤重的小猪使用大量激素以及药物添加剂催肥，五六个月就出栏了。长期食用这些猪肉，会诱发各种不适症状甚至癌症。为此，农业部专门辟谣。此前一则《原料鸡45天速成》的新闻也引发了不少人担忧。且不说新闻报道的不严谨之处，公众担心的核心问题还是\"45天速成的鸡\"和\"160天出栏的猪\"是怎么喂起来的，是否安全。有人猜测投放了激素，实际上喂食激素将使得鸡和猪体质脆弱，容易死亡。肉鸡之所以生长快，得益于品种的优化和科学的饮食、饲养方案，经过半个多世纪的高强度选育，肉鸡出栏日期从1935年时的95天缩短为如今的45天。相同的是，国外好品种的猪养殖周期较短，而国内地方品种生长周期将明显加长。不可否认的是，现代养殖工业将导致肉类口感的下降。但有比口感更重要的——\"斗米斤鸡\"是我国传统养殖的代表，然而现代养殖业已经可以把两斤饲料转化成一斤鸡肉；如果不喂饲料只喂杂粮、猪草等传统猪食，猪的生长周期也至少增加一个月。要知道，拜现代食品工业所赐，相比于四十多年前，美国的食物总体价格增加了近6倍，而鸡肉则只增加了2倍多。而鸡肉占我国肉类消费比重从1982年的5%持续上升到目前的20%左右，如果没有现代养殖工业，不仅供应跟不上，价格还将飞涨。");

    Analyzer analyzer = new ICTCLASAnalyzer(ICTCLASDelegate.getDelegate());

    TokenStream ts = analyzer.tokenStream("dummy", new StringReader(string));

    String token;
    CharTermAttribute termAtt = (CharTermAttribute) ts
            .getAttribute(CharTermAttribute.class);

    try

    {

      while ( ts.incrementToken())

      {
    	token=new String(termAtt.buffer(),0,termAtt.length());
        System.out.println(token);

      }

    }

    catch (IOException ioe)

    {

      ioe.printStackTrace();

    }

 }

}
