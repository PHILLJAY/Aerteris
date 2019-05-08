package Aerteris;

public class Monster extends charac {
	
	public String name;
	public String[] moves = new String[4];
	public int heal;
	public int specialmove;
	String[] n = new String[]{
	"skele",
	"Cave Bat",
	"Vampire",
	"Suicide Bomber"
	};
	
	public Monster(int maxhealth, int attack, double crit,int defense) {
		super(maxhealth,attack,crit,defense);
		this.name=n[(int)(Math.floor(Math.random()*n.length))];
		// TODO Auto-generated constructor stub
	}
	public Monster(int maxhealth, int attack, double crit,int defense,String n) {
		super(maxhealth,attack,crit,defense);
		this.name=n;
		// TODO Auto-generated constructor stub
	}
	public static void SpecialAttack( charac p,Monster m,String name) {
		switch(name) {
	case "skele":
		System.out.println("> "+m.name +" uses BONE CLAW and deals " + (m.attack+2-p.defense) +" damage! \n");
		p.currenthealth+=p.defense-(m.attack+2);
		break;
	case "Cave Bat": case "Vampire": case "leech boi":
		Battle.lifesteal(p,m);
		break;
	case "Suicide Bomber":
		System.out.println(">"+m.name+" explodes for "+( m.attack+((m.maxhealth-m.currenthealth)/3)) );
		p.currenthealth+=p.defense-( m.attack+((m.maxhealth-m.currenthealth)/3));
		break;
		}
	}
	

	
	
	public int dropgold(int range) {
		return (int) Math.random()*range;
	}
}
