package baidupic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Http;


/**
 * @author Administrator 这是以前写的抓取百度图片的小Demo..
 * 主要的练习目的是合理引用java的正则 : 
 * 	find(),group(int),(.*?)
 * @description
 *2014-10-18  下午5:14:58
 */
public class DownPic {
    public static void main(String[] args) throws Exception {
	Http http = new Http();
	String url = "http://image.baidu.com/channel?c=%E5%8A%A8%E6%BC%AB&t=%E6%97%A5%E6%9C%AC%E6%BC%AB%E7%94%BB&s=0";
	String str = http.get(url, "gbk");
	String picurl = "http://image.baidu.com/detail/newindex?col=%E7%BE%8E%E5%A5%B3&tag=%E5%B0%8F%E6%B8%85%E6%96%B0&pn=0&pid={1}&aid=&user_id={2}&setid=-1&sort=0&from=1";
	List<String> picUrls = new ArrayList<String>();
	Matcher m1 = Pattern.compile("\"photoId\" : \"(.*?)\",    \"userId\" : \"(.*?)\"").matcher(str);
	while(m1.find()){
	    String link = picurl.replace("{1}", m1.group(1));
	    link =link.replace("{2}", m1.group(2));
	    picUrls.add(link);
	    System.out.println(link);
	}
	
	int count = 0;
	OutputStream out = null;
	String bigLink = "\"downloadUrl\"   : \"(.*?)\"";
	String realyLink = null;
	for (String string : picUrls) {
	    String text = http.get(string,"gbk");
	    Matcher m2 = Pattern.compile(bigLink).matcher(text);
	    while (m2.find()) {
		 realyLink = m2.group(1);
		 System.out.println(realyLink);
		 byte[] b = http.file(realyLink);
		 String name = "d://dm//"+count++ +".jpg";
		 File file = new File(name);
		 out = new FileOutputStream(file);
		 out.write(b);
	    }
	    
	}
	out.close();
    }
}
