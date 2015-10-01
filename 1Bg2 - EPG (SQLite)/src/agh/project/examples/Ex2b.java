package agh.project.examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class Ex2b {
	public static void main(String[] args) throws IOException {
        
		Properties defaultProps = new Properties();
		File file = new File("defaultProperties");
//		if (file.exists()) {
//			FileInputStream in = new FileInputStream(file);
//			defaultProps.load(in);
//			in.close();		
//		} else {
			FileOutputStream out = new FileOutputStream(file);
			defaultProps.setProperty("url.1", "http://www.lyngsat.com/Eutelsat-Hot-Bird-13B.html");
			defaultProps.setProperty("url.1.type", "parse.channels");
			defaultProps.setProperty("url.2", "http://www.lyngsat.com/Eutelsat-Hot-Bird-13C.html");
			defaultProps.setProperty("url.2.type", "parse.channels");
			defaultProps.setProperty("url.3", "http://www.lyngsat.com/Eutelsat-Hot-Bird-13D.html");
			defaultProps.setProperty("url.3.type", "parse.channels");
			defaultProps.setProperty("parse.channels", "table[width=720],~[border=0]");
			defaultProps.setProperty("parse.channels.rules", "TR.FIRST[text]=TR.LAST[text]");

			defaultProps.setProperty("url.4", "http://www.lyngsat.com/packages/NC-.html");
			defaultProps.setProperty("url.4.type", "parse.packets");
			defaultProps.setProperty("parse.packets", "table[width=720],~[border=0]");
				
			defaultProps.store(out, null);
			out.close();
//		}
		
        // Pobranie pliku HTML
//		Set<Object> keys = defaultProps.keySet();
//		for (Object s: keys) System.out.println(s+": "+defaultProps.getProperty((String) s));

		int i=0;
		String url = null;
		ArrayList<String> al = new ArrayList<String>();
		while((url=defaultProps.getProperty("url."+ ++i)) != null) {
			System.out.println(url+" ["+defaultProps.getProperty("url."+i+".type")+"]");	
			if (!al.contains(defaultProps.getProperty("url."+i+".type"))) al.add(defaultProps.getProperty("url."+i+".type"));
		}
		System.out.println("Different parsing: "+al.size());
		for (String s : al) {
			System.out.println(s+" : "+defaultProps.getProperty(s));
			
		}
		
    }

}


