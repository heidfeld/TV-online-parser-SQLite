package agh.project.sqlite;

public class Programs {
	
	private int hour;
	private double time;
	private int min;
	private String title;
	private String description;
	private int ch_id;
	
	public int getHour(){
		return hour;
	}
	public void setHour(int hour){
		this.hour = hour;
	}
	public int getMinutes(){
		return min;
	}
	public void setMinutes(int min){
		this.min = min;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getDescription(){
		return description;
	}
	public void setDescription(String descr){
		this.description = descr;
	}
	public int getChId(){
		return ch_id;
	}
	public void setChId(int cid){
		this.ch_id = cid;
	}
	public double getTime(){
		return time;
	}
	public void setTime(double time){
		this.time = time;
	}
	
	public Programs()	{}
	public Programs(int hour, int min, int cid, String title, String descr){
		this.hour = hour;
		this.min = min;
		this.description = descr;
		this.ch_id = cid;
		this.title = title;
	}
	
	public Programs(double time, int hour, int min, String title){
		this.time = time;
		this.hour = hour;
		this.min = min;
		this.title = title;
	}
	
	@Override
	public String toString(){
		return "["+hour+":"+min+"] " + title + ", CH_ID: " + ch_id;
	}

}
