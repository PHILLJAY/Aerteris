package Aerteris;
import java.util.*;

public class Battle {

	 public static Scanner scan = new Scanner(System.in);
	

	public void Battling(charac x,Monster y ) {
		while(true) {
			if(isDead(x)==false) {
				if(move()=='a') {
					attack(x,y);
					System.out.println("deals "+ x.attack+" damage "+
					y.name+ " HP: "+y.currenthealth );
				} else if(move()=='r') break;
			} else {
				System.out.print("you died");
				break;
			}
			
			if(isMonsterDead(y)==false) {
			monsterattack(x,y);
			System.out.println(y.name +" deals " + y.attack +" damage"+
					" player HP: "+ x.currenthealth);
			
			}else {
				System.out.print("you won");
				break;
			}
		}
	}
	
	
	public static void attack(charac x, Monster y){
		
		y.currenthealth=y.currenthealth+y.defense-x.attack;
	}
	
	public static void monsterattack(charac y, Monster x){
		
		y.currenthealth=y.currenthealth+y.defense-x.attack;
	}
	
	
	
	
	public char move() {
		System.out.print("whatchu wanna do: attack, run, item, ");
		char c = scan.nextLine().charAt(0);
		return c;		
	}
	
	public static boolean isDead(charac x) {
		if(x.currenthealth<0) {
			return true;
		}
		return false;
	}
	
	public static boolean isMonsterDead(Monster x) {
		if(x.currenthealth<0) {
			return true;
		}
		return false;
	}
	
	public int GetHealth(charac x) {
		return x.currenthealth;
	}
	
	public int GetDefense(charac x) {
		return x.defense;
	}
	public int GetAttack(charac x) {
		return x.attack;
	}
	public int Getmaxhealth(charac x) {
		return x.maxhealth;
	}
	
	
	
}
