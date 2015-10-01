package agh.project.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import agh.project.sqlite.Channels;
import agh.project.sqlite.Programs;

public class MySQL {
	private static final Logger log = Logger.getLogger(Main.class);
	
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:biblioteka.db";
 
    private Connection conn;
    private Statement stat;
    
    public MySQL(){
    	try {
            Class.forName(MySQL.DRIVER);
        } catch (ClassNotFoundException e) {
        	log.error(e, e);
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }
 
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
        	log.error(e, e);
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
 
        createTables();
    }
    
    public boolean createTables()  {
    	String clearChannels = "DROP TABLE channels";
    	String clearPrograms = "DROP TABLE programs";
        String createChannels = "CREATE TABLE IF NOT EXISTS channels (id_channel INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(255), ch_id int, cat_id int)";
        String createPrograms = "CREATE TABLE IF NOT EXISTS programs (id_program INTEGER PRIMARY KEY AUTOINCREMENT, hour int, min int, ch_id int, title varchar(255), description varchar(255))";
        try {
        	stat.execute(clearChannels);
        	stat.execute(clearPrograms);
            stat.execute(createChannels);
            stat.execute(createPrograms);
        } catch (SQLException e) {
        	log.error(e, e);
            System.err.println("Blad przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean insertChannel(String name, int ch_id, int cat_id) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into channels values (NULL, ?, ?, ?);");
            prepStmt.setString(1, name);
            prepStmt.setInt(2, ch_id);
            prepStmt.setInt(3, cat_id);
            prepStmt.execute();
        } catch (SQLException e) {
        	log.error(e, e);
            System.err.println("Error in channel INSERT");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean insertProgram(int hour, int min, int ch_id, String title, String description) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into programs values (NULL, ?, ?, ?, ?, ?);");
            prepStmt.setInt(1, hour);
            prepStmt.setInt(2, min);
            prepStmt.setInt(3, ch_id);
            prepStmt.setString(4, title);
            prepStmt.setString(5, description);
            prepStmt.execute();
        } catch (SQLException e) {
        	log.error(e, e);
            System.err.println("Error in program INSERT");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean insertGroupProgram(String query, int count_current, int count){
    	try{
    		PreparedStatement prepStmt = conn.prepareStatement(query);
    		prepStmt.execute();
    		System.out.println("INSERT GROUP VALUES - DONE! (" + count_current + "/" + count + ")");
    		
    	} catch(SQLException e){
    		log.error(e, e);
    		System.err.println("Error in program INSERT");
            e.printStackTrace();
            return false;
    	}
    	return true;
    }
    
    public boolean insertGroupChannel(String query){
    	try{
    		PreparedStatement prepStmt = conn.prepareStatement(query);
    		prepStmt.execute();
    		System.out.println("INSERT GROUP (CHANNELS) VALUES - DONE!");
    		
    	} catch(SQLException e){
    		log.error(e, e);
    		System.err.println("Error in program INSERT");
            e.printStackTrace();
            return false;
    	}
    	return true;
    }
    
    public List<Channels> selectChannels() {
        List<Channels> channels = new LinkedList<Channels>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM channels");
            int ch_id, cat_id;
            String name;
            while(result.next()) {
                ch_id = result.getInt("ch_id");
                name = result.getString("name");
                cat_id = result.getInt("cat_id");
                channels.add(new Channels(ch_id, name, cat_id));
            }
        } catch (SQLException e) {
        	log.error(e, e);
            e.printStackTrace();
            return null;
        }
        return channels;
    }
    
    public List<Programs> selectPrograms() {
        List<Programs> programs = new LinkedList<Programs>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM programs");
            int hour, min, cid;
            String title, descr;
            while(result.next()) {
                cid = result.getInt("ch_id");
                title = result.getString("title");
                hour = result.getInt("hour");
                min = result.getInt("min");
                descr = result.getString("description");
                programs.add(new Programs(hour, min, cid, title, descr));
            }
        } catch (SQLException e) {
        	log.error(e, e);
            e.printStackTrace();
            return null;
        }
        return programs;
    }
    
    public List<Programs> getOneChannel(int cid){
    	List<Programs> programs = new LinkedList<Programs>();
    	String query = "SELECT * FROM programs WHERE ch_id = " + cid + ";";
    	
        try {
            ResultSet result = stat.executeQuery(query);
            int hour, min, ch_id;
            String title, descr;
            while(result.next()) {
                ch_id = result.getInt("ch_id");
                title = result.getString("title");
                hour = result.getInt("hour");
                min = result.getInt("min");
                descr = result.getString("description");
                
                programs.add(new Programs(hour, min, ch_id, title, descr));
            }
        } catch (SQLException e) {
        	log.error(e, e);
            e.printStackTrace();
            return null;
        }
    	
    	
    	return programs;
    }
    
    public List<Programs> getCurrent(int cid){
    	List<Programs> programs = new LinkedList<Programs>();
    	List<Programs> current_prog = new LinkedList<Programs>();
    	String query = "SELECT * FROM programs WHERE ch_id = " + cid + ";";
    	
        try {
            ResultSet result = stat.executeQuery(query);
            int hour, min;
            String title;
            double time;
            while(result.next()) {
                title = result.getString("title");
                hour = result.getInt("hour");
                min = result.getInt("min");       
                time = hour + Math.round(((float)min)/60*1000.0)/1000.0;
                
                programs.add(new Programs(time, hour, min, title));
                
            }
        	Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            double current_time = hours + Math.round(((float)minutes)/60*1000.0)/1000.0;
            boolean myFlag = true;
            double last_value = 0;
            
            for(int i = 0 ; i<programs.size() ; i++){
            	if(current_time>last_value && current_time<programs.get(i).getTime() && myFlag == true){
            		
            		if(programs.get(i-2) != null)current_prog.add(new Programs(programs.get(i-2).getTime(), programs.get(i-2).getHour(), programs.get(i-2).getMinutes(), programs.get(i-2).getTitle()));
            		if(programs.get(i-1) != null)current_prog.add(new Programs(programs.get(i-1).getTime(), programs.get(i-1).getHour(), programs.get(i-1).getMinutes(), programs.get(i-1).getTitle()));
            		current_prog.add(new Programs(programs.get(i).getTime(), programs.get(i).getHour(), programs.get(i).getMinutes(), programs.get(i).getTitle()));
            		
            		myFlag = false;
            	}
            	last_value = programs.get(i).getTime();
            }
            
        } catch (SQLException e) {
        	log.error(e, e);
            e.printStackTrace();
            return null;
        }
    	
    	
    	return current_prog;
    }
    
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
        	log.error(e, e);
            System.err.println("Trouble with connection close..");
            e.printStackTrace();
        }
    }  
}
