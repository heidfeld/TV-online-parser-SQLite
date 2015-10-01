package agh.project.examples;

public class Watki2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Watki launch = new Watki();
		//launch.run();
		
		for (int i = 0 ; i<5 ; i++){
			new Thread(new Watki()).start();
		}

	}

}
