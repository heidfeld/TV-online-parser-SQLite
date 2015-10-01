package agh.project.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.Vector;

public class DownloadHTMLs implements Runnable{
	
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx";
	private static Vector<String> catUrl = new Vector<String>();
	private static Vector<String> channelNames = new Vector<String>();
	private static int task = 0;
	public final int id = task++;

	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		File theDir = new File("C://tmp");
		
		if (!theDir.exists()) {
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(!result) {    
		        System.out.println("Dir NOT CREATED !");  
		    }
		}
		
		Document doc2 = Jsoup.connect(url).get();
		catUrl = getCatUrl(doc2);
		channelNames = getChannelNames(catUrl);
		
		for(String src : catUrl){
			System.out.println(src);
		}

	}
	public void run(){
		try {
			System.out.println(src);
	        Document doc = Jsoup.connect(src).get();
	        String html = doc.html();
	        BufferedWriter out = new BufferedWriter(new FileWriter("c:/tmp/cat"+i+".html"));
	        out.write(html);
	        out.close();
	        i++;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private static Vector<String> getCatUrl(Document doc){
		Vector<String> catUrl = new Vector<String>();
		Elements cl = doc.select(".tv-sel");
		Elements links = cl.select("a[href]");
		for (Element temp : links){
			catUrl.add(temp.attr("abs:href"));
		}
		return catUrl;
	}
	
	private static Vector<String> getChannelNames(Vector<String> catUrl) throws IOException{
		Vector<String> channelNames = new Vector<String>();
		for(String cat : catUrl){
			Document doc = Jsoup.connect(cat).get();
			Elements cl = doc.select(".tvlogo");
			for (Element src : cl){
				channelNames.add(src.attr("title"));
			}
		}
		System.out.println(channelNames.size());
		return channelNames;
	}

}
