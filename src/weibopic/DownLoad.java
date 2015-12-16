package weibopic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Http;

/**
 * @author Administrator
 * @description  去"他的相册"抓包,进入相册后点击图片,会异步,抓第二个包"popview"这个..里面的url..会有一堆pid..这个就是图片名
 *2014-10-18  下午7:37:11
 */
public class DownLoad {
    public static void main(String[] args) throws IOException {
	String template = "http://ww1.sinaimg.cn/large/b7f4190bjw1em8jpp6g0kj20t20qeq61.jpg";
	String word = "b7f4190bjw1em8jpp6g0kj20t20qeq61";
	BufferedReader reader = new BufferedReader(new FileReader("src//test.txt"));
	StringBuilder builder = new StringBuilder();
	String str = "";
	while ((str=reader.readLine())!=null) {
	   builder.append(str);
	}
	Matcher m = Pattern.compile("\"pid\":\"(.*?)\"").matcher(builder.toString());
	List<String> pids = new ArrayList<String>();
	while (m.find()) 
	    pids.add(template.replace(word, m.group(1)));
	Http.downloadPics(pids);
	reader.close();
	
    }
}
