package agh.project.examples;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Lukasz_Test {
	
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx";

	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements media = doc.select("table");
		
		Elements categories = doc.select(".tv-sel");
		Elements li = categories.select("a[href]");
		System.out.println("Categories Size: " + li.size());
		for(Element yolo : li){
			print("Link: %s", yolo.attr("abs:href"));
		}
		
		Elements cl2 = doc.select(".timeline");
		Elements li2 = cl2.select("a[href]").not("a[href=javascript:;]");
		System.out.println("Time Size: " + li2.size());
		for(Element yolo : li2){
			print("Link: %s", yolo.attr("abs:href"));
		}
		//doc.select("table[width=720]").not("[border=0]");
		
		Elements cl3 = doc.select(".tvlogo");
		System.out.println("Logo Size: " + cl3.size());
		for (Element src : cl3){
			System.out.println(src.attr("title"));
		}
		
		for (Element src : media){
			Elements wiersz = src.select("tr");
			System.out.println("TR SIZE: " + wiersz.size());
			for(Element src2 : wiersz){
				System.out.println("-----------------------------------");
				Elements pozycja = src2.select("td");
				System.out.println("LICZBA KOLUMN: " + pozycja.size());
				//System.out.println(pozycja.text());
				if (pozycja.size()>0){
					Element siemano = pozycja.get(2); // w zaleznosci od wartosci w get otrzymamy konkretny program
					System.out.println(siemano.text());
				}
				System.out.println("-----------------------------------");
			}
		}
		
	}
	
	private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

}
