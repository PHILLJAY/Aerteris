

public class Inv {
	static //constructor
	Item[] inventory = new Item[8];
	public void place(int x, int y) {
		inventory[x].setType(y);
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

	public void showInv() {
		for(int z = 0; z<8;z++) {
			System.out.println((inventory[z]));
		}
	}
//	public static String printDesc(int z) {
//		switch(inventory[z]) {
//		}
//		return"bazinga";
//	}	


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
//
//	public static void main(String[] args) {
//		Inv test =  new Inv();
//		test.place(0, 2);

//	}

}
