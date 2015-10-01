package agh.project.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Get All Names of TV Stations
public class GetPageData {
	private static final Logger log = Logger.getLogger(Main.class);
	MySQL my = new MySQL();
	
	public ArrayList<String> getPrograms(ArrayList<String> filePaths){
		int cid = 0;
		int cat_id = 0;
		String query_sql = "INSERT INTO channels VALUES ";;
		ArrayList<String> channelNames = new ArrayList<String>();
		for(String path : filePaths){
			try {
				File input = new File(path);
				Document doc = Jsoup.parse(input, "UTF-8");
				Elements cl = doc.select(".tvlogo");
				for (Element src : cl){
					channelNames.add(src.attr("title"));
					query_sql += "(NULL, \"" + src.attr("title") + "\", " + cid + ", " + cat_id + "), ";
					cid++;
				}
								
			} catch (IOException e) {
				log.error(e, e);
				e.printStackTrace();
			}
			cat_id++;
		}
		String query_cut = query_sql.substring(0, query_sql.length()-2);
		query_cut += ";";
		my.insertGroupChannel(query_cut);
		return channelNames;
	}
	
	//Get All Data from webpage
	public ArrayList<String> getTV(ArrayList<String> filePaths){
		
		int channel_id = 0;			//Unique channel id - cid
		int channel_memory = 0; 	//Remember last number of programs in category, necessary to correct channel ids
		int chennel_number = 0;		//Temporary integer represents number of programs in category
		int count = 0;				//Count number of all operations
		int count_current = 0;		//Count current number of operation
		
		String sql_query = "INSERT INTO programs VALUES ";
		String parse_descr, parse_title;
		
		for (int j = 0 ; j<2 ; j++){
			for(String path : filePaths){
				try {
					
					File input = new File(path);
					Document doc = Jsoup.parse(input, "UTF-8");
					
					//get all elements from tbody -> tr (not currennt, and advert), all necessary informations are there
					Elements tr1 = doc.select("tbody");
					Elements tr2 = tr1.select("tr").not("tr[id=tv-ad]").not("tr[id=current]");
					for(Element src : tr2){ //for each time group (t00, t05, t09 etc..)
						Elements tr3 = src.select("td"); 	//all programs in one time group
						
						if(j==1){
						chennel_number = tr3.size();
						channel_id = channel_memory;
						}
						
						//for each program get data
						for(Element channel : tr3){
							
							if(channel != tr3.first() && channel != tr3.last()){
								
								Elements program = channel.select("a[href]");
								Elements program_div = program.select("div");
								
								for(Element el0 : program_div){
									if (j==1){
										parse_descr = convertQuery(el0.select("p").text());
										parse_title = convertQuery(el0.select("h3").text());
										
										sql_query += "(NULL, " + Integer.parseInt(el0.select("small").text().substring(0, 2)) + ", " + Integer.parseInt(el0.select("small").text().substring(3)) + ", " + channel_id + ", \"" + parse_title + "\", \"" + parse_descr + "\"), ";
										count_current++;
										
										if(count_current % 200 == 0 || count_current == count-1){
											
											sql_query = sql_query.substring(0, sql_query.length()-2);
											sql_query += ";";
											my.insertGroupProgram(sql_query, count_current, count-1);
											
											sql_query = "INSERT INTO programs VALUES ";
											
										}
									}
									if (j==0){
										count++;
									}
								}
								if(j==1)
									channel_id++;
							}
						}
					}
				} catch (IOException e) {
					log.error(e, e);
					e.printStackTrace();
				}
				if(j==1)
					channel_memory += chennel_number-2;
			}
		}
		return null;
	}
	
	//function get all files from dir with html-s
	public ArrayList<String> searchDir(){
		File folder = new File("C://tmp");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> fileList = new ArrayList<String>();

		for (File file : listOfFiles) {
			String filename = file.getName();
		    if (file.isFile() && filename.substring(filename.length()-4)!="html") {
		        fileList.add(file.getAbsolutePath());
		    }
		}
		return fileList;
	}
	
	//Get Page Title
	public String getPageTitle() throws IOException{
				
		new Downloader();
		Document doc = Downloader.getMainPage();
		Elements elementsTitle = doc.select("title");
		Element elementTitle = elementsTitle.first();
		String title = elementTitle.text();
		
		return title;
	}
	
	//Convert MySQL query (problems with some characters)
	public String convertQuery(String query){
		String new_query;
		
		new_query = query.replace("\"", "^");
		new_query = new_query.replace("(", "[");
		new_query = new_query.replace(")", "]");
		new_query = new_query.replace("'", "^");
		
		return new_query;
	}
	
}