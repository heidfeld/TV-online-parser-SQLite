package agh.project.sqlite;

public class Channels {
	
	//Fields
	private int ch_id;
	private String name;
	private int cat_id;
	
	//Methods
	public int getChId(){
		return ch_id;
	}
	public void setChId(int id){
		this.ch_id = id;
	}
	public int getCatId(){
		return cat_id;
	}
	public void setCatId(int id){
		this.cat_id = id;
	}
	public int getName(){
		return ch_id;
	}
	public void setName(String name){
		this.name = name;
	}
	
	//Constructors
	public Channels()	{}
	public Channels(int ch_id, String name, int cat_id){
		this.ch_id = ch_id;
		this.name = name;
		this.cat_id = cat_id;
	}
	
	@Override
	public String toString(){
		return "["+ch_id+"] "+ name;
	}

}
