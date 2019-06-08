
public class charac {

	public int maxhealth;
	public int gold;
	public int defense;
	public int attack;
	public double crit;
	public int currenthealth;
	public int mana;
	public int tempdamage;
	public int xp;
	public boolean bleed;
	
	public charac(int maxhealth, int attack,double crit, int defense,int gold,int xp){
		this.maxhealth = maxhealth;
		this.defense = defense;
		this.attack=attack;
		this.currenthealth=maxhealth;
		this.crit=crit;
		this.gold=gold;
		this.xp=xp;
	}
	
	
	
	public static void main(String args[]){
//		charac player =  new charac(20,2,0);
//		Monster a =  new Monster("arduini");
//		
//		attack(player,a);
//		printstat(a,a.name);		
	}

	
	public static void printstat(charac x){
		
		System.out.println(x +"'s stats:");
		System.out.println("health :"+ x.currenthealth);
		System.out.println("attack :"+ x.attack);
		System.out.println("defense :"+ x.defense);
	}
	
}
