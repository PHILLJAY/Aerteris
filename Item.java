

public class Item {
	public static int health;
	public static int defense;
	public static int attack;
	public static double crit;
	public static int mana;
	public static boolean equiped;
	public static int type;
	/*Item types;
	 * 0 = sword
	 * 1 = shield
	 * 2 = armour
	 * 3 = potion
	 * 
	 */


	/*Cool item stuff
	 * 
	 * @philip
	 * 
	 */
	public Item(int type){
		this.equiped=false;
		this.type = type;
	}

	public String toString() {
		switch(this.type) {
		case 0:
			return  "Swood";
		case 1:
			return"Shied";
		case 2:
			return"Armour Pot";
		case 3:
			return"Potision";
		}
		return"bazinga";

	}
	//set stuff below

	public static void equip() {
		equiped =! equiped;
	}
	public static void setHealth(int z) {
		health = z;
	}
	public static void setDefense(int z) {
		defense = z;
	}
	public static void setAttack(int z) {
		attack = z;
	}
	public static void setCrit(double z) {
		crit = z;
	}
	public static void setMana(int z) {
		mana = z;
	}
	public void setType(int y) {
		type = y;
	}

	//get stuff below

	public static int getHealth() {
		return health;
	}
	public static int getDefense() {
		return defense;
	}
	public static int getAttack() {
		return attack;
	}
	public static double getCrit() {
		return crit;
	}	
	public static int getMana() {
		return mana;
	}





}
