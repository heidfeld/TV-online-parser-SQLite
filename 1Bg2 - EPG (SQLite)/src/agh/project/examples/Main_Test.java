package agh.project.examples;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.util.Vector;

public class Main_Test {
	
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx";
	private static Vector<String> catUrl = new Vector<String>();
	private static Vector<String> datUrl = new Vector<String>();
	public static Vector<String> channelNames = new Vector<String>();

	public static void main(String[] args) throws IOException{
		//Download HTML file
		Document doc = Jsoup.connect(url).get();
		//Fill Vector catUrl
		catUrl = getCatUrl(doc);
		//Fill Vector datUrl
		datUrl = getDatUrl(doc);
		getChannelNames(catUrl);
	}
	//Method getCatUrl gets links from class .tv-sel, loads into Vector and returns as categories links
	public static Vector<String> getCatUrl(Document doc){
		Vector<String> catUrl = new Vector<String>();
		Elements cl = doc.select(".tv-sel");
		Elements links = cl.select("a[href]");
		for (Element temp : links){
			catUrl.add(temp.attr("abs:href"));
		}
		return catUrl;
	}
	//Method getDatUrl gets links from class .timeline that represents different TV dates, loads them into a Vector, and returns it. The first date is today, next increment.
	public static Vector<String> getDatUrl(Document doc){
		Vector<String> datUrl = new Vector<String>();
		Elements cl = doc.select(".timeline");
		Elements links = cl.select("a[href]").not("a[href=javascript:;]");
		for(Element temp : links){
			datUrl.add(temp.attr("abs:href"));
		}
		return datUrl;
	}
	
	public static void getChannelNames(Vector<String> catUrl) throws IOException{
		for(String cat : catUrl){
			Document doc = Jsoup.connect(cat).get();
			Elements cl = doc.select(".tvlogo");
			for (Element src : cl){
				channelNames.add(src.attr("title"));
			}
		}
		System.out.println(channelNames.size());
	}
	
	public static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}
