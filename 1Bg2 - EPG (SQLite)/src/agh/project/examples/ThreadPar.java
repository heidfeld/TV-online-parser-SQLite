package agh.project.examples;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThreadPar implements Runnable{
	private static int task=0;
	private final int id = task++;
	
	ThreadPar(String src){}
	
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx";
	public static Vector<String> catUrl = new Vector<String>();
	
	public void run(){
		if (!catUrl.isEmpty()){
			try {
		        Document doc = Jsoup.connect(catUrl.elementAt(id)).get();
		        String html = doc.html();
		        BufferedWriter out = new BufferedWriter(new FileWriter("c:/tmp/cat"+id+".html"));
		        out.write(html);
		        out.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect(url).get();
		getCatUrl(doc);
		
		
		for (String catlink : catUrl){
			Runnable r = new ThreadPar(catlink);
			new Thread(r).start();
		}
	}
	
	private static void getCatUrl(Document doc){
		Elements cl = doc.select(".tv-sel");
		Elements links = cl.select("a[href]");
		for (Element temp : links){
			if(!temp.equals(links.first())){
				catUrl.add(temp.attr("abs:href"));
			}
		}
	}
}
