package Aerteris;

public class Monster extends charac {
	
	public String name;
	public String[] moves = new String[4];
	public int heal;
	public int specialmove;
	public static int fibonacci=0;
	public static int charge=1;
	
	
	String[] n = new String[]{
	"skele",
	"Cave Bat",
	"Vampire",
	"Suicide Bomber",
	"Blood Priest",
	"fibonacci",
	"Mad Scientist"
	};
	
	public Monster(int maxhealth, int attack, double crit,int defense) {
		super(maxhealth,attack,crit,defense);
		this.name=n[(int)(Math.floor(Math.random()*n.length))];
		// TODO Auto-generated constructor stub
	}
	
	//just to test specific monsters
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
		System.out.println("> "+m.name+" explodes for "+( m.attack+((m.maxhealth-m.currenthealth)/3))+ "\n" );
		p.currenthealth+=p.defense-( m.attack+((m.maxhealth-m.currenthealth)/3));
		break;
	case "Blood Priest":
		if(m.currenthealth>m.attack) {
		System.out.println("> "+m.name+" uses blood magic and sacrifices " +m.attack+" of his health and deals " +m.attack*2+" damage! \n");
		p.currenthealth+=p.defense-(m.attack*2);
		m.currenthealth-=m.attack;} 
		else Battle.BasicMonsterAttack(p, m);
		break;
	case "fibonacci":
		fibonacci+=1;
		System.out.println("> "+m.name+" DEALS "+fibonacciy(fibonacci+1)+" damage! \n");
		p.currenthealth+=p.defense-fibonacciy(fibonacci+1);
		break;
	case "Mad Scientist":
		System.out.println("> " + m.name+ "mixes chemicals maliciously \n");
		m.charge+=1;
		break;
		}
	}
	

	public static int fibonacciy(int x) {
		if(x==0)return 0;
		else if(x==1) return 1;
		else return fibonacciy(x-1)+fibonacciy(x-2);
	}
	

	public int dropgold(int range) {
		return (int) Math.random()*range;
	}
}
