package agh.project.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class SimpleParser {

	private static final String settings = "settings";
	
	private static final String[][] defaultProperty = {
		{"url.sat.1","http://www.lyngsat.com/Eutelsat-Hot-Bird-13B.html"},
		{"url.sat.2","http://www.lyngsat.com/Eutelsat-Hot-Bird-13C.html"},
		{"url.sat.3","http://www.lyngsat.com/Eutelsat-Hot-Bird-13D.html"},
		{"url.sat.4","http://www.lyngsat.com/Eutelsat-36A.html"},
		{"url.sat.5","http://www.lyngsat.com/Eutelsat-36B.html"}
	};
	
	public static void main(String[] args) {
		System.out.println("Load settings");
	
		// create and load default properties
		Properties defaultProps = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(settings);
			try {
				defaultProps.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("New settings");
			for (String tab[]: defaultProperty) defaultProps.setProperty(tab[0], tab[1]);
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(settings);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {

				defaultProps.store(out, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}

}
