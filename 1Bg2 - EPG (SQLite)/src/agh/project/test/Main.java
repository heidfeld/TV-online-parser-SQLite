package agh.project.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

//files on Hard Drive already exists, class Main uses GetPageData parses it.
public class Main {
	private static final Logger log = Logger.getLogger(Main.class);
	public static ArrayList<String> filePaths = new ArrayList<String>();
	public static ArrayList<String> fileNames = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		
		new PropertiesAll();
		Properties prop = PropertiesAll.setAll(); 			//Set all properties
		//DownloaderPool.startDownload(prop);					//Download URL-s and save on Hard Disc
		
		GetPageData reader = new GetPageData();			//Class to parse webpage.
		filePaths = reader.searchDir();					//Find HTML-files on HardDisc and return list of them.
		fileNames = reader.getPrograms(filePaths);		//Get All names of channels.
		reader.getTV(filePaths);							//Parse webpage !
		
		
	}
	
	@SuppressWarnings("null")
	public String[] getTitles() throws IOException{		
		GetPageData reader = new GetPageData();				//Class to parse webpage.
		filePaths = reader.searchDir();						//Find HTML-files on HardDisc and return list of them.
		fileNames = reader.getPrograms(filePaths);			//Get All names of channels.
		String[] myList = null;
		int i = 0;
		for (String src : fileNames){
			myList[i] = src;
			i++;
		}
		return myList;
		
	}

}
