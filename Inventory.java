public class Inventory {

	Thing[] inventory;
	int size;

	public Inventory(int size) {
		inventory = new Thing[size];
		for (int i = 0; i < size; i++) {
			inventory[i] = new Thing();
		}
		this.size = size;
	}

	public void newItem(int level) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].name.equals("")) {
				inventory[i].generateNew(level);
				return;
			}
			//replaceItem();
		}
	}
	
	public void newItem(int level, int type) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].name.equals("")) {
				inventory[i].generateNew(level, type);
				return;
			}
			//replaceItem();
		}
	}

	private void replaceItem() {
		printInventory();
		System.out.print("Which item do you want to replace? ");
		//pick
	}
	
	public void fill(int level) {
		for (int i = 0; i < size; i++) {
			inventory[i].generateNew((int)(level-1 + Math.random()*3));
		}
	}

	public void printInventory() {
		if (countItems() > 0) {
			sort();
			for (int i = 0; i < countItems(); i++) {
				System.out.print((i+1) + " -> " + inventory[i].name + " level " + inventory[i].level + ": +");
				if (inventory[i].type < 3 && inventory[i].type > 0) {
					System.out.print(inventory[i].defense + " defense" + equippedToString(i));
				} else if (inventory[i].type == 3) {
					System.out.print(inventory[i].heal + " instant health\n");
				} else if (inventory[i].type == 4) {
					System.out.print(inventory[i].explode + " instant damage\n");
				} else if (inventory[i].type > 4) {
					System.out.print(inventory[i].damage + " damage" + equippedToString(i));
				}
			}
			System.out.print("\n");
		} else System.out.print("You have no items.\n\n");
	}

	private String equippedToString(int index) {
		if (inventory[index].equipped) return ", equipped\n";
		return "\n";
	}
	
	public String typeToString(int type) {
		Thing temp = new Thing();
		return temp.nameBank[type];
	}

	private int countItems() {
		int count = 0;
		for (int i = 0; i < size; i++) {
			if (inventory[i].type > 0) count++;
		}
		return count;
	}

	private void sort() {
		boolean ready = false;
		while (true) {
			ready = true;
			for (int i = 0; i < countItems(); i++) {
				if (inventory[i].type == 0) ready = false;
			}
			if (ready) return;
			for (int i = 1; i < size; i++) {
				if (inventory[i-1].type == 0 && inventory[i].type != 0) {
					inventory[i-1] = inventory[i];
					inventory[i] = new Thing();
				}
			}
		}
	}
	
	public void addBuffs(charac player) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].type > 0 && inventory[i].type < 3) player.defense += inventory[i].defense;
			else if (inventory[i].type > 4) player.attack += inventory[i].damage;
		}
	}
	
	public void removeBuffs(charac player) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].type > 0 && inventory[i].type < 3) {
				player.defense -= inventory[i].defense;
			}
			else if (inventory[i].type > 4) {
				player.attack -= inventory[i].damage;
			}
		}
	}

}
