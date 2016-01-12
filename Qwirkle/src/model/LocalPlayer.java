package model;

public class LocalPlayer implements Player {
	public String name;
	public int score;
	
	public LocalPlayer(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}

}
