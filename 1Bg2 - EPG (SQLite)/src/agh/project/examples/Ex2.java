package agh.project.examples;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Ex2 {
	private final static String url = "http://www.lyngsat.com/Eutelsat-Hot-Bird-13B.html";

	public static void main(String[] args) throws IOException {
        
        // Pobranie pliku HTML
        Document doc = Jsoup.connect(url).get();
        // Wybieramy TAGI: TABLE gdzie width=720 i nie te dla których border=0
        Elements media = doc.select("table[width=720]").not("[border=0]");

        System.out.println("TABLES: "+media.size()); // jest 6 tabel
        
        // Interesują nas tylko te tabelki dla których:
        // pierwszy i ostatni rząd (TR) jest taki sam...
        for (Element src : media) { // przelatuje po tabelach (sze�ciu)
       		System.out.println("--------------------------------");
       		Elements media2 = src.select("tr");
       		System.out.println("TR: "+media2.size());
            Element first=media2.get(0);
            //System.out.println(media2.get(0).select("td").size());
            String sFirst = first.text();
            //System.out.println(first);
            System.out.println("text="+sFirst);
            Element last=media2.get(media2.size()-1);
            //System.out.println(media2.get(media2.size()-1).select("td").size());
            String sLast = last.text();
            //System.out.println(last);
            System.out.println("text="+sLast);
            System.out.println(sFirst.equals(sLast));
            
            if (sFirst.equals(sLast)) {
            	System.out.println("data zmiany: "+sFirst.split("last updated", 2)[1].split(" - ")[0].trim());
            	//System.out.println(media2.size());
            	media2.remove(0);
            	media2.remove(media2.size()-1);
            	System.out.println("ROWS: "+media2.size());
            }
            else {
            	System.out.println("NOT OK");
            }
            
        }
    }

}


