package agh.project.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesAll {
		private static final Logger log = Logger.getLogger(Main.class);
	
	  public static Properties setAll() {
			
			Properties prop = new Properties();
			OutputStream output = null;

			try {

				output = new FileOutputStream("config.properties");

				// set the properties value
				prop.setProperty("www", "http://tvprogram.idnes.cz/tvprogram.aspx");	//full path to webpage
				prop.setProperty("timeout", "15");										//timeout for downloading pages

				// save properties to project root folder
				prop.store(output, null);

			} catch (IOException io) {
				log.error(io, io);
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						log.error(e, e);
						e.printStackTrace();
					}
				}

			}

			return prop;
			
		  }
}
