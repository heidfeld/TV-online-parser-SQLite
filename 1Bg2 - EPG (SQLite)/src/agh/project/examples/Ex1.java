package agh.project.examples;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Ex1 {
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx?";

	public static void main(String[] args) throws IOException {
        
        // Pobranie pliku HTML
        Document doc = Jsoup.connect(url).get();
        // Wybieramy TAGI: TABLE gdzie width=720 i nie te dla których border=0
        Elements media = doc.select("table[width=720]").not("[border=0]");
        
        System.out.println(media);
        System.out.println("TABLES: "+media.size());
        
    }

}


