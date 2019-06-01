

public class Monster extends charac {
	
	public String name;
	public String[] moves = new String[4];
	public int specialmove;
	public static int fibonacci=0;
	public static int charge=1;
		
	static String[] n = new String[]{
	"skele",
	"Cave Bat",
	"Vampire",
	"Suicide Bomber",
	"Blood Priest",
	"Mad Scientist",
	"Copy Cat",
	"Executioner"
	};
	
	String[] b = new String[] { 
			"Joker", 
			"fibonacci", 
			"Gran Torino",
			"Master of Coins"
			};
	
	
	public Monster(int maxhealth, int attack, double crit,int defense,int gold) {
		super(maxhealth,attack,crit,defense,gold);
		this.name=n[(int)(Math.floor(Math.random()*n.length))];
		// TODO Auto-generated constructor stub
	}
	
	//just to test specific monsters
	public Monster(int maxhealth, int attack, double crit,int defense,int gold,String n) {
		super(maxhealth,attack,crit,defense,gold);
		this.name=n;
		// TODO Auto-generated constructor stub
	}
	
	public Monster(int maxhealth, int attack, double crit,int defense,int gold,char type) {
		super(maxhealth,attack,crit,defense,gold);
		switch(type) {
		case 'e':
			this.name=n[(int)(Math.floor(Math.random()*n.length))];
			break;
		case 'E':
			this.name=b[(int)(Math.floor(Math.random()*b.length))];
			break;
		case 'B':
			this.name="Dungeon Boss";
		}
		// TODO Auto-generated constructor stub
	}
	
	
	
	public static void SpecialAttack(charac p,Monster m,String name) {
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
		System.out.println("> " + m.name+ " is mixing chemicals.... \n");
		m.charge+=1;
		break;
	case "Copy Cat":
		System.out.println("> "+m.name+" COPIES your attack and deals "+(p.tempdamage)+" damage" );
		p.currenthealth-=p.tempdamage+p.defense;
		break;
	case "Executioner":
		System.out.println("> "+m.name + " DROPS the guillitine and deals "+(((p.maxhealth-p.currenthealth)/2)-p.defense)+" damage! \n");
		p.currenthealth+=p.defense-((p.maxhealth-p.currenthealth)/2);
		break;
	case "Gran Torino":
		System.out.println("> GET OFF MY LAWN \n");		
		Battle.BasicMonsterAttack(p, m);
		Battle.BasicMonsterAttack(p, m);
		break;
	case "Joker":
		if(p.defense>m.attack*2) System.out.println(m.name+" did no damage lol");
		else System.out.println("> "+m.name+ " BACKSTABS you for "+ (m.attack*2-p.defense));
		p.currenthealth-=m.attack*2+p.defense;
		break;
	case "Master of Coins":
		int stealgold=m.attack*2+m.gold/4;
		if(stealgold<m.gold) {
			System.out.println("> Master of Coins steals "+stealgold+" gold from you");
			p.gold-=stealgold;
			m.gold+=stealgold;
		}else if(p.gold>0) {
			System.out.println("> Master of Coins steals "+stealgold+" gold from you");
			m.gold+=p.gold;
			p.gold=0;
		} else {
			if(p.defense>m.attack*3) System.out.println("> did no dmg");
			else System.out.println("> Master of Coins THROWS HIS MONEY BAG on you and deals " +m.attack*3+ " damage");
			p.currenthealth-=m.attack*3+p.defense;
		}
		break;
	case "Dungeon Boss":
		SpecialAttack(p,m,n[(int)(Math.floor(Math.random()*n.length))]);
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
