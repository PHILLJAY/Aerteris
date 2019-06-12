public class Thing {
	
	public int damage = 0;
	public int defense = 0;
	public int heal = 0;
	public int explode = 0;
	public boolean equipped = false;
	public int level = 0;
	public String name = " ";
	public int type = 0; //corresponds with nameBank
	public String[] nameBank = {" ", "Helmet", "Chestplate", "Health Potion",
			"Bomb", "Sword", "Hammer"};
	
	public Thing() {
		damage = 0;
		defense = 0;
		heal = 0;
		explode = 0;
		equipped = false;
		level = 0;
		type = 0;
		name = nameBank[type];
	}
	
	public void generateNew(int level) {
		this.level = level;
		type = (int)(1+Math.random()*(nameBank.length-1));
		name = nameBank[type];
		if (type < 3 && type > 0) {
			defense = (int)(1+(level/4));
		} else if (type == 3) {
			heal = 4*level;
		} else if (type == 4) {
			explode = 4*level;
		} else if (type > 4) {
			damage = (int)(1+(level/2));
		}
	}
	
	public void generateNew(int level, int type) {
		this.level = level;
		this.type = type;
		name = nameBank[type];
		if (type < 3 && type > 0) {
			defense = (int)(1+(level/4));
		} else if (type == 3) {
			heal = 4*level;
		} else if (type == 4) {
			explode = 4*level;
		} else if (type > 4) {
			damage = (int)(1+(level/2));
		}
	}
	
}