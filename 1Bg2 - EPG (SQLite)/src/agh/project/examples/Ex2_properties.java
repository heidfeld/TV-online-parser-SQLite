package agh.project.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ex2_properties {

	final static Logger log = Logger.getLogger(Ex2_properties.class);

	final static String[] defaults = {
			"#Co parsować",
			"parse=channel, package",
			"\n",
			"#Jakie channel",
			"channel=HOTBIRD-C,HOTBIRD-B, HOTBIRD-D",
			"\n",
			"#URL dla każdego satelity",
			"channel.HOTBIRD-B.url=http://www.lyngsat.com/Eutelsat-Hot-Bird-13B.html",
			"channel.HOTBIRD-C.url=http://www.lyngsat.com/Eutelsat-Hot-Bird-13C.html",
			"channel.HOTBIRD-D.url=http://www.lyngsat.com/Eutelsat-Hot-Bird-13D.html",
			"\n", "#jakie reguły parsowania", "channel.rules=1,2",
			"parse.channel.rules.1=TR.FIRST[text]=TR.LAST[text]",
			"parse.channel.rules.2=table[width=720],~[border=0]", "\n",
			"#Jakie package", "package=NC",
			"package.NC.url=http://www.lyngsat.com/packages/NC-.html", "\n",
			"#jakie reguły parsowania", "package.rules=1",
			"parse.package.rules.1=table[width=720],~[border=0]" };

	final static class Parser implements Runnable {

		String url;
		ArrayList<String> rules;

		public Parser(String url, ArrayList<String> rules) {
			this.url = url;
			this.rules = rules;
		}

		public void run() {

			Document doc = null;
			try {
				doc = Jsoup.connect(url).get();

				// TUTAJ RESZTA KODU
			
			} catch (IOException e) {
				log.error("JSoup problems", e);
			}


		}
	};

	public static void main(String[] args) throws IOException {

		Properties defaultProps = new Properties();
		File file = new File("defaultProperties");
		if (file.exists()) {
			log.trace("File \"defaultProperties\" exists");
			FileInputStream in = new FileInputStream(file);
			defaultProps.load(in);
			in.close();
			log.info("properties have been loaded successfully");
		} else {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			log.trace("File \"defaultProperties\" does not exist");
			for (String s : defaults) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
			log.trace("File \"defaultProperties\" with default setting has been created successfully.");
			FileInputStream in = new FileInputStream(file);
			defaultProps.load(in);
			in.close();
			log.info("properties have been loaded successfully");
		}

		// Pobranie pliku HTML
		Set<Object> keys = defaultProps.keySet();
		for (Object s : keys)
			log.trace(s + ": " + defaultProps.getProperty((String) s));

		String url = null;
		ArrayList<String> rules = null;

		String[] types = defaultProps.getProperty("parse").split(",");
		log.trace("There are " + types.length
				+ " different types of resources to be parsed");
		for (String t : types) {
			t = t.trim();
			log.trace("Type: " + t + ": " + defaultProps.getProperty(t));

			rules = new ArrayList<String>();
			log.trace("rules for " + t);
			String[] r = defaultProps.getProperty(t + ".rules").split(",");

			for (String r2 : r) {
				rules.add(defaultProps.getProperty(
						"parse." + t + ".rules." + r2.trim()).trim());
			}
			log.trace(rules);

			String[] subtypes = defaultProps.getProperty(t).split(",");
			for (String t2 : subtypes) {
				t2 = t2.trim();
				log.trace("Type: " + t + ", Subtype: " + t2);

				// Tę klasę dodajemy do kolejki obsługiwanej przez serwis wątków
				new Parser(defaultProps.getProperty(t + "." + t2 + ".url"),
						rules);

			}
			rules = null;
		}

	}

}
