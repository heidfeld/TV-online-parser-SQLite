package agh.project.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Downloader {	
	private final static String url1 = "http://tvprogram.idnes.cz/tvprogram.aspx";
	
	public static Document getMainPage() throws IOException{
		
		Document doc = Jsoup.connect(url1).get();
		
		return doc;
	}

}
