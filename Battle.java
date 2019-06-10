
import java.util.*;

public class Battle {

	public int tempdamage;
	public static Scanner scan = new Scanner(System.in);
	private char n;

	public Battle(charac x,Monster y ) {
		System.out.println("\n-------------------------------");
		System.out.println("# "+y.name+" Appears! # \n");
		if(y.name.equals("Dungeon Boss")) y.PrintBoos();
		int lostg = y.gold;
		while(true) {
			if(isDead(x)==false) {
				System.out.println("Your HP: "+x.currenthealth);
				if(!y.name.equals("Joker")) System.out.println(y.name+ "'s HP: "+y.currenthealth +"\n");
				else System.out.println(y.name+ "'s HP: ?? \n");
				if((n=move())=='a') {
					attack(x,y);
				} else if(n=='b') { 
					if(Bribe(x,y)) {
						System.out.println("> You threw " +lostg+" on the ground and ran away");
						x.gold-=lostg;
						y.gold+=lostg;
						System.out.println("> You have " +x.gold+" gold left");
						break;
					}else {
						System.out.println("> I'll take that thanks");
						x.gold-=0;
					}
				}
			} else {
				System.out.println("You died");
				break;
			}
			if(isMonsterDead(y)==false) {
				if(y.name.equals("Dungeon Boss")) Monster.SpecialAttack(x, y, y.name);
				else monsterattack(x,y);
			}else {
				System.out.println("> you won \n> "+y.name + " dropped " +y.gold + " gold!");
				x.gold+=y.gold;
				System.out.println("> you gained "+ y.xp+" xp!");
				x.xp+=y.xp;
				break;
			}
		}
	}

	public static boolean Bribe(charac p,Monster m) {
		if(p.gold>=m.gold)return true;
		return false;
	}
	public Battle(charac p, Monster m, char f) {
		System.out.println("\n-------------------------------");
		System.out.println("# Waterpoo Admission Officer Appears! \n");
		for(int i =1; i<100; i++) {
			if(isDead(p)==false) {
				System.out.println("Your HP: "+p.currenthealth);
				System.out.println(m.name+ "'s HP: "+m.currenthealth +"\n");
				if((n=move())=='a') {
					attack(p,m);
				} else if(n=='b') { 
					System.out.println("> We regret to inform you we cannot give you an offer of admission \n");
					System.out.println("> Thanks for applying ;)");
					System.out.println("Your gold: -15 000");
					p.gold=-15000;					
				}
			} else {
				System.out.println("Congratulations, You have been deferred to Geomatics!");
				System.out.println("Let us show you what it means to be a Warrior!");
				break;
			}
			if(isMonsterDead(m)==false) {
				Monster.FinalBossAttack(p,m,i);
				if(p.bleed==true) {
					System.out.println("> You're bleeding.. -2 health \n");
					p.currenthealth-=2;
				}
			}else {
				System.out.println("> you won \n> "+m.name + " dropped " +m.gold + " gold!");
				p.gold+=m.gold;
				System.out.println("> you gained "+ m.xp+" xp!");
				p.xp+=m.xp;
				break;
			}
		}
	}
	public static void attack(charac x, Monster y){	
		if (y.armor<0) y.armor=0;
		if(Math.random()<=x.crit) {
			System.out.println("heckkkk"+y.armor);

			y.currenthealth+=y.defense+y.armor-x.attack*2;
			y.armor-=x.attack*2;
			System.out.println("> You CRIT "+ (x.attack*2-y.defense)+" damage to "+y.name+"!");
			x.tempdamage=x.attack*2;
		}else {	
			if(x.attack>y.defense) {
				System.out.println("heckkkk"+y.armor);
					x.tempdamage=x.attack-y.defense;
					y.currenthealth=y.currenthealth+y.armor+y.defense-x.attack;
					y.armor-=x.attack;
					System.out.println("> You deal "+ (x.tempdamage)+" damage to "+y.name);
				
			} else {
				System.out.println("> you did no damage");
				x.tempdamage=0;
			}		
		}

	}


	public static void monsterattack(charac p, Monster m){
		double r = Math.random();
		if(r<0.65) {
			BasicMonsterAttack(p,m);
		} else {
			Monster.SpecialAttack(p,m,m.name);
		}
	}


	public static void lifesteal(charac p, Monster m) {
		System.out.println("> "+m.name +" SUCKS " + (m.attack-p.defense) +" health! \n");
		if(m.attack+m.currenthealth<=m.maxhealth) {
			p.currenthealth+=p.defense-(m.attack);
			m.currenthealth+=m.attack;
		} else { 
			BasicMonsterAttack(p,m);
		}
	}


	public static void BasicMonsterAttack(charac p, Monster m) {
		if(m.name.equals("fibonacci")) {
			Monster.SpecialAttack(p, m, m.name);
		}else if(m.name.equals("Mad Scientist")) {
			System.out.println("> "+m.name +" deals " + (m.attack*m.charge-p.defense) +" damage \n");
			p.currenthealth+=p.defense-(m.attack*m.charge);
			m.charge=1;
		}
		else{
			if(Math.random()<=m.crit) {
				if(m.attack*2>p.defense) {
					p.currenthealth+=p.defense-m.attack*2;
					System.out.println("> "+m.name +" CRITS " + (m.attack*2-p.defense) +" damage \n");
				}else System.out.println("> "+m.name +" CRITS " + (m.attack*2-p.defense) +" damage \n");
			}else {	
				if(m.attack>p.defense) {
					p.currenthealth+=p.defense-m.attack;
					System.out.println("> "+m.name +" deals " + (m.attack-p.defense) +" damage \n");
				}else System.out.println("> "+m.name +" deals " + (m.attack-p.defense) +" damage \n");
			}
		}
	}

	public char move() {
		while(true) {
			System.out.print("Whatchu wanna do: \nAttack \nBribe \nItem \n");
			char c = scan.next().toLowerCase().charAt(0);
			if(c=='a'||c=='i'||c=='b') {
				return c;	
			}
			else {
				System.out.println("Invalid command \n");
			}
		}	
	}

	public static boolean isDead(charac x) {
		if(x.currenthealth<1) {
			return true;
		}
		return false;
	}

	public static boolean isMonsterDead(Monster x) {
		if(x.currenthealth<1) {
			return true;
		}
		return false;
	}


}
