public class Item {
	public int health;
	public int defense;
	public int attack;
	public double crit;
	public int mana;
	public boolean equiped;
	public int type;
	public int value;
	/*Item types;
	 * 0 = empty
	 * 1 = sword
	 * 2 = shield
	 * 3 = can
	 * 4 = potion
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
			return"Empty";
		case 1:
			return"Sword";
		case 2:
			return"Shield";
		case 3:
			return"Can";
		case 4:
			return"Potion";
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
	public void setType(int z) {
		type = z;
	}
	public void genValue() {
		value = (int) ((health+mana+defense+attack+(crit*50)/2)); 
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
	public int getType() {
		return type;
	}

	
	//randomize based on what type of item it is
	public void  genStat(int z) {
		if(type==1) {
			setCrit((z+Math.random()*10)/100);
			setAttack((int)Math.round(z+Math.random()*((z/4)-(-z/4)+(z/4))));
		}else if(type==2) {
			setDefense((int)Math.round(z/2+Math.random()*((z/4)-(-z/4)+(z/4))));
			setHealth((int)Math.round(z+Math.random()*((z/4)-(-z/4)+(z/4))));
		}else if(type==3) {
			setAttack((int)Math.round(1+z/5));
			setCrit((z+Math.random()*5)/100);
		}else if(type==4) {
			setHealth(z*4);
		}
	}




}
