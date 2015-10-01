package agh.project.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import agh.project.sqlite.Programs;

public class test {
	public static ArrayList<String> filePaths = new ArrayList<String>();
	public static ArrayList<String> fileNames = new ArrayList<String>();

	public static void main(String[] args) {
		MySQL my = new MySQL();
		
		GetPageData reader = new GetPageData();			//Class to parse webpage.
		filePaths = reader.searchDir();					//Find HTML-files on HardDisc and return list of them.
		fileNames = reader.getPrograms(filePaths);		//Get All names of channels.
		reader.getTV(filePaths);							//Parse webpage !

		List<Programs> programs = my.getCurrent(180);

	}

}
