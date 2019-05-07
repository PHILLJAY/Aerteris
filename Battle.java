package Aerteris;
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
					System.out.println("> You deal "+ (x.attack-y.defense)+" damage to "+y.name);
				} else if(n=='r') { System.out.println("You ran away!\n"); break;}
			} else {
				System.out.print("You died");
				break;
			}

			if(isMonsterDead(y)==false) {
				monsterattack(x,y,"a");
				System.out.println("> "+y.name +" deals " + (y.attack-x.defense) +" damage \n");
			}else {
				System.out.print("You Won!\n\n");
				break;
			}
		}
	}


	public static void attack(charac x, Monster y){	
		if(Math.random()<=x.crit) {
			y.currenthealth=y.currenthealth+y.defense-x.attack*2;
		}else {	y.currenthealth=y.currenthealth+y.defense-x.attack;
		}
	}

	public static void monsterattack(charac p, Monster m,String s){
		if(s.equals("a")) {
			if(Math.random()<=m.crit) {
				p.currenthealth+=p.defense-m.attack*2;
			}else {	p.currenthealth+=p.defense-m.attack;
			}
		} else if(s.equals("sp")) {
			p.currenthealth+=p.defense-m.attack*3;
		}
	}



	public char move() {
		System.out.print("Whatchu wanna do: \nAttack \nRun \nItem \n");
		char c = scan.nextLine().charAt(0);
		return c;		
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
