public class charac {

	public int maxhealth;
	public int defense;
	public int attack;
	public double crit;
	public int currenthealth;
	public int mana;
	
	
	public charac(int maxhealth, int attack,double crit, int defense){
		this.maxhealth = maxhealth;
		this.defense = defense;
		this.attack=attack;
		this.currenthealth=maxhealth;
		this.crit=crit;
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
