

public class Monster extends charac {

	public String name;
	public String[] moves = new String[4];
	public int specialmove;
	public static int fibonacci=0;
	public static int charge=1;
	public static int armor=0;

	static String[] n = new String[]{
			"skele",
			"Cave Bat",
			"Vampire",
			"Suicide Bomber",
			"Blood Priest",
			"Mad Scientist",
			"Copy Cat",
	};

	String[] b = new String[] { 
			"Joker", 
			"fibonacci", 
			"Gran Torino",
			"Master of Coins",
			"Executioner"
	};


	public Monster(int maxhealth, int attack, double crit,int defense,int gold, int xp) {
		super(maxhealth,attack,crit,defense,gold,xp);
		this.name=n[(int)(Math.floor(Math.random()*n.length))];
		// TODO Auto-generated constructor stub
	}

	//just to test specific monsters
	public Monster(int maxhealth, int attack, double crit,int defense,int gold,int xp,String n) {
		super(maxhealth,attack,crit,defense,gold,xp);
		this.name=n;
		// TODO Auto-generated constructor stub
	}

	public Monster(int maxhealth, int attack, double crit,int defense,int gold,int xp,char type) {
		super(maxhealth,attack,crit,defense,gold,xp);
		switch(type) {
		case 'e':
			this.name=n[(int)(Math.floor(Math.random()*n.length))];
			break;
		case 'E':
			this.name=b[(int)(Math.floor(Math.random()*b.length))];
			break;
		case 'B':
			this.name="Dungeon Boss";
			break;
		case 'f':
			this.name="Waterloo Admission Officer";
		}
		// TODO Auto-generated constructor stub
	}

	public static void FinalBossAttack(charac p, Monster m, int x) {
	switch(x) {
	case 1:
		System.out.println("> Think you're gonna get in??");
		System.out.println("> "+m.name+" Lures you in with Co-op and lowers your defense!\n");
		p.defense-=2;
		break;
	case 2:
		System.out.println("> "+m.name+ " is reviewing your application...");
		System.out.println("> "+m.name+ " has gained "+10+" armor\n");
		m.armor=10;
		break;
	case 3:
		System.out.println("> A furious GOOSE ATTACKS you for "+(m.attack+3)+" damage\n");
		p.currenthealth+=p.defense-(m.attack+3);
		break;
	case 4:
		System.out.println("> Waterloo gets your hopes up with a scholarship and steals your health\n");
		p.maxhealth-=m.attack;
		p.currenthealth-=m.attack;
		m.maxhealth+=m.attack;
		m.currenthealth+=m.attack;
		break;
	case 5:
		System.out.println("> Waterloo releases your Euclid Marks and deals "+(m.attack*2+2)+" damage!\n");
		p.currenthealth+=p.defense-(m.attack*2+2);
		break;
	case 6:
		System.out.println("> Waterloo Alumini flexes on you and your self-esteem lowers\n");
		p.defense-=m.attack;
		break;
	case 7:
		System.out.println("> Justin Trudeau visits the loo and steals your girlfriend and breaks your heart");
		System.out.println("> Your heart is bleeding\n");
		p.bleed=true;
		break;
	case 8:
		System.out.println("> AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		System.out.println("> Waterloo deals "+(m.attack*2)+" damage!\n");
		p.currenthealth+=p.defense-(m.attack*2);
	}
	if(x>8) {
		Battle.BasicMonsterAttack(p, m);
	}
	
}

	public static void SpecialAttack(charac p,Monster m,String name) {
		switch(name) {
		case "skele":
			if(m.attack+2>p.defense) {
				System.out.println("> "+m.name +" uses BONE CLAW and deals " + (m.attack+2-p.defense) +" damage! \n");
				p.currenthealth+=p.defense-(m.attack+2);
			}else System.out.println("> "+m.name +" uses BONE CLAW and deals no damage! \n");
			break;
		case "Cave Bat": case "Vampire": case "leech boi":
			Battle.lifesteal(p,m);
			break;
		case "Suicide Bomber":
			if((m.attack+((m.maxhealth-m.currenthealth)/3))>p.defense) {
				System.out.println("> "+m.name+" explodes for "+( m.attack+((m.maxhealth-m.currenthealth)/3))+ "\n" );
				p.currenthealth+=p.defense-( m.attack+((m.maxhealth-m.currenthealth)/3));
			}else System.out.println("> "+m.name+" explodes for nothing \n" );
			break;
		case "Blood Priest":
			if(m.currenthealth>m.attack) {
				if(m.attack*2>p.defense) {
					System.out.println("> "+m.name+" uses blood magic and sacrifices " +m.attack+" of his health and deals " +m.attack*2+" damage! \n");
					p.currenthealth+=p.defense-(m.attack*2);
					m.currenthealth-=m.attack;
				} else System.out.println("> "+m.name+" uses blood magic and sacrifices " +m.attack+" of his health and deals 0 damage! \n");
			} else Battle.BasicMonsterAttack(p, m);
			break;
		case "fibonacci":
			fibonacci+=1;
			if(fibonacciy(fibonacci+1)>p.defense) {
			System.out.println("> "+m.name+" DEALS "+fibonacciy(fibonacci+1)+" damage! \n");
			p.currenthealth+=p.defense-fibonacciy(fibonacci+1);
			}else System.out.println("> "+m.name+" DEALS no damage! \n");
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
			if((((p.maxhealth-p.currenthealth)/2)-p.defense)>0) {
			System.out.println("> "+m.name + " DROPS the guillitine and deals "+(((p.maxhealth-p.currenthealth)/2)-p.defense)+" damage! \n");
			p.currenthealth+=p.defense-((p.maxhealth-p.currenthealth)/2);
			}else System.out.println("> "+m.name + " DROPS the guillitine and deals no damage! \n");
			break;
		case "Gran Torino":
			System.out.println("> GET OFF MY LAWN \n");		
			Battle.BasicMonsterAttack(p, m);
			Battle.BasicMonsterAttack(p, m);
			break;
		case "Joker":
			if(p.defense>m.attack*2) { System.out.println(m.name+" did no damage lol");}
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


	public static void PrintBoos() {
		System.out.println(
				" .-._                                                   _,-,\r\n" + 
						"  `._`-._                                           _,-'_,'\r\n" + 
						"     `._ `-._                                   _,-' _,'\r\n" + 
						"        `._  `-._        __.-----.__        _,-'  _,'\r\n" + 
						"           `._   `#===\"\"\"           \"\"\"===#'   _,'\r\n" + 
						"              `._/)  ._               _.  (\\_,'\r\n" + 
						"               )*'     **.__     __.**     '*( \r\n" + 
						"               #  .==..__  \"\"   \"\"  __..==,  # \r\n" + 
				"               #   `\"._(_).       .(_)_.\"'   #");

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
