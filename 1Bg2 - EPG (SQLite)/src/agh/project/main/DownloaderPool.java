package agh.project.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloaderPool implements Runnable{
	private static final Logger log = Logger.getLogger(Main.class);
	private static int task=0;
	private final int id = task++;
	
	//class DownloaderPool creates new html files on hard drive, implement it to refresh files
	DownloaderPool(String src){}
	DownloaderPool(){}
	
	private final static String url = "http://tvprogram.idnes.cz/tvprogram.aspx";		//full path to webpage
	public static Vector<String> catUrl = new Vector<String>();							//vector with url-s of all categories
	private static final int to = 30;
	
	//run() is going to create html files on hard disc with all categories, but only when connection is truly good
	public void run(){
		if (!catUrl.isEmpty()){
			try {
				boolean flag = false;
				int timeout_id = to;
				
				while(flag == false && timeout_id > 0){
					Document doc = Jsoup.connect(catUrl.elementAt(id)).get();			//create connection with all categories
					if(doc.select("tr").size() > 0){									//works only when size of all cols is higher than 0
						flag = true;
						String html = doc.html();
						int id_temp = id;
				        BufferedWriter out = new BufferedWriter(new FileWriter("c:/tmp/cat"+id_temp+".html"));
				        out.write(html);												//create html file represents all categories
				        out.close();
				        System.out.println("Create HTML file... c:/tmp/cat"+id_temp+".html");
					}
					else{																//if size of cols is smaller - decrement
						timeout_id--;
					}
				}
		    } catch (IOException e) {
		    	log.error(e, e);
		        e.printStackTrace();
		    }
		}
	}
	
	public static void main(String[] args) throws IOException {
		createDir();
		
		Document doc = Jsoup.connect(url).get();
		getCatUrl(doc);
		
		
		for (String catlink : catUrl){
			Runnable categories = new DownloaderPool(catlink);
			new Thread(categories).start();
		}
	}
	
	public static boolean startDownload(Properties prop) throws IOException{
		try{
		createDir();
			
			Document doc = Jsoup.connect(prop.getProperty("www")).get();
			getCatUrl(doc);
			
			
			for (String catlink : catUrl){
				Runnable categories = new DownloaderPool(catlink);
				new Thread(categories).start();
			}
			return true;
		}
		catch(IOException e){
			log.error(e,e);
			return false;
		}
	}
	
	//function gets all categories URL-s
	private static void getCatUrl(Document doc){
		Elements cl = doc.select(".tv-sel");
		Elements links = cl.select("a[href]");
		for (Element temp : links){
			if(!temp.equals(links.first())){
				catUrl.add(temp.attr("abs:href"));
			}
		}
	}
	
	//function that creates Dir on the Hard Disc
	public static void createDir(){
		File theDir = new File("C://tmp");
		
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		    	log.error(se, se);
		    }        
		}
		else{
			for(File file: theDir.listFiles()) file.delete();
		}
	}
}
