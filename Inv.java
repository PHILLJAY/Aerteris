
public class Inven {
	static //constructor
	/*
	 * 
	 */	
	int inventory[] = new int[8];
	public static void place(int x, int y) {
		inventory[x] = y;
	}
	public static void showInv() {
		for(int z = 0; z<8;z++) {
			
		}
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
		Inven test =  new Inven();
		test.place(0, 2);
		
	}
	
}