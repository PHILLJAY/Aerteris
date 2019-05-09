public class Item {
	public int health;
	public int defense;
	public int attack;
	public double crit;
	public int mana;
	public boolean equiped;
	public int type;
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

	public void equip() {
		equiped =! equiped;
	}
	public void setHealth(int z) {
		health = z;
	}
	public void setDefense(int z) {
		defense = z;
	}
	public void setAttack(int z) {
		attack = z;
	}
	public void setCrit(double z) {
		crit = z;
	}
	public void setMana(int z) {
		mana = z;
	}
	public void setType(int y) {
		type = y;
	}

	//get stuff below

	public int getHealth() {
		return health;
	}
	public int getDefense() {
		return defense;
	}
	public int getAttack() {
		return attack;
	}
	public double getCrit() {
		return crit;
	}	
	public int getMana() {
		return mana;
	}





}
