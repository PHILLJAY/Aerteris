import java.util.*;

public class Battle {

	public static Scanner scan = new Scanner(System.in);
	private char n;

	public Battle(charac x,Monster y ) {
		System.out.println("\n-------------------------------");
		System.out.println("# "+y.name+" Appears! # \n");

		while(true) {
			if(isDead(x)==false) {
				System.out.println("Your HP: "+x.currenthealth);
				System.out.println(y.name+ "'s HP: "+y.currenthealth +"\n");
				if((n=move())=='a') {
					attack(x,y);
				} else if(n=='r') { System.out.println("You ran away!\n"); break;}
			} else {
				System.out.println("You died");
				break;
			}

			if(isMonsterDead(y)==false) {
				monsterattack(x,y);
			}else {
				System.out.println("you won");
				break;
			}
		}
	}


	public static void attack(charac x, Monster y){	
		if(Math.random()<=x.crit) {
			y.currenthealth=y.currenthealth+y.defense-x.attack*2;
			System.out.println("> You CRIT "+ (x.attack*2-y.defense)+" damage to "+y.name+"!");
		}else {	y.currenthealth=y.currenthealth+y.defense-x.attack;
		System.out.println("> You deal "+ (x.attack-y.defense)+" damage to "+y.name);
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
				p.currenthealth+=p.defense-m.attack*2;
				System.out.println("> "+m.name +" CRITS " + (m.attack*2-p.defense) +" damage \n");
			}else {	p.currenthealth+=p.defense-m.attack;
			System.out.println("> "+m.name +" deals " + (m.attack-p.defense) +" damage \n");
			}
		}
	}

	public char move() {
		while(true) {
			System.out.print("Whatchu wanna do: \nAttack \nRun \nItem \n");
			char c = scan.next().toLowerCase().charAt(0);
			if(c=='a'||c=='i'||c=='r') {
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

