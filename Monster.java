package Aerteris;

public class Monster extends charac {
	
	public String name;
	public String[] moves = new String[4];
	
	
	
	public Monster(int maxhealth, int attack, double crit,int defense, String n) {
		super(maxhealth,attack,crit,defense);
		this.name=n;
		// TODO Auto-generated constructor stub
	}
	
	
	
	public int dropgold(int range) {
		return (int) Math.random()*range;
	}
}
