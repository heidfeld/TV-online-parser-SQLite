package agh.project.examples;

public class Watki implements Runnable {
	protected int count = 10;
	private static int task = 0;
	private final int id = task++;
	
	public Watki(){}
	public Watki(int count){
		this.count = count;
	}
	public void run(){
		while(count-- > 0){
			System.out.println(status());
			Thread.yield();
		}
	}
	public String status(){
		return "#" + id + "(" + (count > 0 ? count : "Start!")+")";
	}
}
