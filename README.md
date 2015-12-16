# HttpTools
主要用来实现用HTTP请求下载图片以及模拟相关的请求返回数据,并练习正则group的使用..

正则的运用 :   

    Matcher m = Pattern.compile("\"pid\":\"(.*?)\"").matcher(builder.toString());
    List<String> pids = new ArrayList<String>();
    while (m.find()) {
    	    pids.add(template.replace(word, m.group(1)));
    }
    	    

###提供神写的一个HTTP的工具类Http.java.并提供https请求实现..

      *get()
      *post()
      *file()
      *downloadPics()  
      
      
###这个demo的主要价值就在这个别人写的工具类..



