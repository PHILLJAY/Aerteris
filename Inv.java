
public class Inv {
	static //constructor
	/*
	 * 
	 */	
	int inventory[] = new int[8];
	public static void place(int x, int y) {
		inventory[x] = y;
	}
	public static String printItem(int z) {
		switch(z) {
		case 0:
			return  "Nothing";
		case 1:
			return"Sword";
		case 2:
			return"Health Pot";
		case 3:
			return"Shelf";
		}
		return"bazinga";
	}	

	public static void showInv() {
		for(int z = 0; z<8;z++) {
			System.out.println(printItem(inventory[z]));
		}
	}
	public static String printDesc(int z) {
		switch(inventory[z]) {
		case 0:
			return  "Its empty, what why would you want to check this";
		case 1:
			return"Sharp and pointy, OOF it hurts";
		case 2:
			return"Chug Jug Boys";
		case 3:
			return"V I R T U A L   S H E L F";
		}
		return"bazinga";
	}	


	/*First int is the type of item
	 * Second int is a modifier
	 * 0 - Standard - Remains the same
	 * 1 - Amazing - + 25% to all stats
	 * 2 - Good -  +10% to all stats
	 * 3 - Sharp - +10% to damage
	 * 4 - trash - -20% to all stats
	 */

	/*Here are the items for now
	 * 1 - Basic Sword, + 5 attack
	 * 2 - Small Health Potion +10 health
	 * 3 - Shelf -10 Health (you hit yourself)
	 */

	public static void main(String[] args) {
		Inv test =  new Inv();
		test.place(0, 2);

	}

}