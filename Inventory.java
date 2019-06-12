import java.util.Scanner;

public class Inventory {

	Thing[] inventory;
	int size;
	Scanner in = new Scanner(System.in);

	public Inventory(int size) {
		inventory = new Thing[size];
		for (int i = 0; i < size; i++) {
			inventory[i] = new Thing();
		}
		this.size = size;
	}

	public void newItem(int level) {
		int replace;
		for (int i = 0; i < size; i++) {
			if (inventory[i].name.equals("")) {
				inventory[i].generateNew(level);
				return;
			}
		}
		if ((replace = replaceItem()-1) == 8) {
			System.out.print("You did not take the item.\n\n");
		} else {
			System.out.print("You dropped your level " + inventory[replace].level + " " +
					inventory[replace].name + ".\n\n");
			inventory[replace] = new Thing();
			inventory[replace].generateNew(level);
		}
	}

	public void newItem(int level, int type) {
		int replace;
		for (int i = 0; i < size; i++) {
			if (inventory[i].name.equals("")) {
				inventory[i].generateNew(level, type);
				return;
			}
		}
		if ((replace = replaceItem()-1) == 8) {
			System.out.print("You did not take the item.\n\n");
		} else {
			System.out.print("You dropped your " + toString(replace) + ".\n\n");
			inventory[replace] = new Thing();
			inventory[replace].generateNew(level, type);
		}
	}

	private int replaceItem() {
		printInventory();
		System.out.print("Which item do you want to replace? [1-8] [n] ");
		String temp = in.next();
		try {
			if (Integer.parseInt(temp) > 0 && Integer.parseInt(temp) < 9) return Integer.parseInt(temp);
		} catch (NumberFormatException e) {}
		return 9;
	}

	public void deleteItem(int index) {
		inventory[index] = new Thing();
	}

	public void addItem(Thing item) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].name.equals("")) {
				inventory[i] = item;
				return;
			}
		}
		printInventory();
		System.out.print("Which item do you want to replace? [1-8] ");
		String temp = in.next();
		try {
			if (Integer.parseInt(temp) > 0 && Integer.parseInt(temp) < 9) {
				inventory[Integer.parseInt(temp)-1] = item;
				System.out.print("You dropped your " + toString(Integer.parseInt(temp)-1) + ".\n\n");
				return;
			}
		} catch (NumberFormatException e) {}
		System.out.print("You actually didn't take \n" +
				"the item you just spent money on...\n" + 
				"Why would you do that???\n\n"); 
		return;
	}

	public void equipMenu(charac player) {
		System.out.print("Equip/use items? [y] [n] ");
		String temp = in.next();
		if (temp.equals("y")) {
			while (true) {
				System.out.print("Which item? [1-8] [done] ");
				temp = in.next();
				if (temp.equals("done")) {
					break;
				}
				try {
					if (Integer.parseInt(temp) > 0 && Integer.parseInt(temp) < 9) {
						int use = Integer.parseInt(temp)-1;
						if ((inventory[use].type > 0 && inventory[use].type < 3) || inventory[use].type > 4) {
							if (inventory[use].equipped) {
								inventory[use].equipped = false;
								System.out.print("You unequipped your " + toString(use) + ".\n\n");
							} else {
								int check = inventory[use].type;
								for (int i = 0; i < size; i++) {
									if (use != i) {
										if (check == inventory[i].type && inventory[i].equipped) {
											inventory[i].equipped = false;
											System.out.print("You unequipped your " + toString(i) + ".\n");
										} else if (check > 4 && inventory[i].type > 4 && inventory[i].equipped) {
											inventory[i].equipped = false;
											System.out.print("You unequipped your " + toString(i) + ".\n");
										}
									}
								}
								inventory[use].equipped = true;
								System.out.print("You equipped your " + toString(use) + ".\n\n");
							}
						} else if (inventory[use].type == 3) {
							if (player.maxhealth-player.currenthealth < inventory[use].heal) {
								player.currenthealth = player.maxhealth;
								System.out.print("You drank the potion and regained all your health!\n(" +
										player.currenthealth + "/" + player.maxhealth + ")\n\n");
							} else {
								player.currenthealth += inventory[use].heal;
								System.out.print("You drank the potion and regained " + inventory[use].heal + 
										" of your health.\n(" + player.currenthealth + "/" + player.maxhealth + ")\n\n");
							}
							inventory[use] = new Thing();
						} else if (inventory[use].type == 4) System.out.print("\n");
					}
				} catch (NumberFormatException e) {}
				printInventory();
			}
			System.out.print("\n");
		}
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

	public boolean printConsumables() {
		if (countItems() > 0) {
			int count = 0;
			sort();
			for (int i = 0; i < countItems(); i++) {
				if (inventory[i].type == 3 || inventory[i].type == 4) {
					count++;
					System.out.print((i+1) + " -> " + inventory[i].name + " level " + inventory[i].level + ": +");
					if (inventory[i].type == 3) {
						System.out.print(inventory[i].heal + " instant health\n");
					} else {
						System.out.print(inventory[i].explode + " instant damage\n");
					}
				}
			}
			if (count > 0) {
				System.out.print("\n");
				return true;
			} else {
				System.out.print("> You have no items to use in battle\n\n");
				return false;
			}
		} else {
			System.out.print("> You have no items\n\n");
			return false;
		}
	}

	private String equippedToString(int index) {
		if (inventory[index].equipped) return ", equipped\n";
		return "\n";
	}

	public String typeToString(int type) {
		Thing temp = new Thing();
		return temp.nameBank[type];
	}

	public String toString(int index) {
		return "level " + inventory[index].level + " " + inventory[index].name;
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
			if (inventory[i].type > 0 && inventory[i].type < 3 && inventory[i].equipped) player.defense += inventory[i].defense;
			else if (inventory[i].type > 4 && inventory[i].equipped) player.attack += inventory[i].damage;
		}
	}

	public void removeBuffs(charac player) {
		for (int i = 0; i < size; i++) {
			if (inventory[i].type > 0 && inventory[i].type < 3 && inventory[i].equipped) {
				player.defense -= inventory[i].defense;
			}
			else if (inventory[i].type > 4 && inventory[i].equipped) {
				player.attack -= inventory[i].damage;
			}
		}
	}

	public int[] getStat(int index) {
		if (inventory[index].type == 3) {
			return new int[] {3,inventory[index].heal};
		} else if (inventory[index].type == 4) {
			return new int[] {4,inventory[index].explode};
		} else return new int[] {0,0};
	}

	public int getLevel(int i) {
		return inventory[i].level;
	}

}