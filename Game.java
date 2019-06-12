import java.io.*;
import java.util.Scanner;

/**
 * The Aerteris launcher.
 * 
 * @author Philip, Tian and Gavin
 */
public class Game {

	private Scanner in = new Scanner(System.in);
	private String dir;
	private File location;
	File file;
	private BufferedReader br;
	private BufferedWriter bw, bwr;
	charac player;
	Inventory inventory = new Inventory(8);
	Inventory[] shopInv = {new Inventory(3), new Inventory(3), new Inventory(3)};
	int[][] shopPrice = {{999,999,999},{999,999,999},{999,999,999}};
	int[] hotelPrice = {0,0,0};

	boolean spaceMode = false;
	char s = ' ';
	private String name;

	private boolean saved = false;
	private boolean first = true;

	//structures
	//private String[] structure = {"dungeon ","  shop  "," hotel  "," chest  ","end game"};
	private String[][] contentsVisual;
	private int[][] contents = new int[3][2];
	private String[][] dungeon = {
			{
				"                  ",
				"       ____       ",
				"      /    \\__.   ",
				"   .-'    _ __ \\  ",
				"  /  .-'.[@]  | \\ ",
				".'_______[@]__|__\\"
			},{
				"                  ",
				"                  ",
				"                  ",
				"            _     ",
				"    _____.-' \\    ",
				"    \\____\\.-'     "
			},{
				"   , , , .        ",
				"   [  ~]./'.      ",
				"    |-|./   '.    ",
				"    |\\|  '.~/__   ",
				"    | |    \\ ~.|  ",
				"    |@|     |  |  "
			}
	};
	private String[] shop = {
			"   .----------.   ",
			"   |'----------'  ",
			"   |    O    |    ",
			"   |   /|\\   |    ",
			"   |'=========]   ",
			"   '|_________]   "
	};
	private String[] hotel = {
			"     ________     ",
			"    /       /\\    ",
			"   /_______/ o\\   ",
			"    | O  O |  |   ",
			"    |__[]__|_-’   ",
			"    .-'.'         "
	};
	private String[] chest = {
			"                  ",
			"                  ",
			"                  ",
			"       _____      ",
			"      /_._/_\\     ",
			"      |___|_|     "
	};
	private String[] endGame = {
			"     push me!     ",
			"      _____       ",
			"     | (0) |      ",
			"     |_____|      ",
			"        |         ",
			"        |         "
	};

	public Game() {

		dir = upDir(System.getProperty("user.dir")) + "\\" + "AerterisSaves";
		location = new File(dir);

		clearConsole();

		if (!location.exists()) {
			if (location.mkdir()) {
				System.out.print("First time startup save folder created. Hit \"enter\" to continue.");
				in.nextLine();
				clearConsole();
			}
			else {System.out.print("Save folder could not be created."); return;}
		}

		System.out.print("" + //60 x 14
				"+----------------------------------------------------------+\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|           You will want about this much space            |\n" +
				"|           in the console to play comfortably.            |\n" +
				"|                                                          |\n" +
				"|                Hit \"enter\" to continue...                |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"+----------------------------------------------------------+"
				);
		in.nextLine();
		boolean flag = false;
		do {
			clearConsole();
			System.out.print("" + //---------------------------------------------\n"
					"                        "+s+"                        "+s+"          \n" +
					"      "+s+"                                 "+s+"                  "+s+"\n" +
					"            ____ ____ ____ ___ ____ ____ _ ____             \n" +
					"            |__| |___ |__/  |  |___ |__/ | [__        "+s+"     \n" +
					" "+s+"          |  | |___ |  \\  |  |___ |  \\ | ___]             \n" +
					"                                                            \n" +
					"           "+s+"         [ new ]   [ load ]       "+s+"        "+s+"    \n" +
					"                         [ blank ]                        \n" +
					"    "+s+"               "+s+"          "+s+"                         "+s+"  \n" +
					"            "+s+"                                "+s+"              \n" +
					"                            "
					);

			String startAction = "";
			if (in.hasNext()) startAction = in.nextLine();
			if (startAction.equals("new")) {
				try {
					newSave();
				} catch (IOException e) {
					e.getMessage();
				} 
				flag = true;
			} else if (startAction.equals("load")) {
				try {
					loadSave();
				} catch (IOException e) {
					e.getMessage();
				} 
				flag = true;
			} else if (startAction.equals("blank")) {
				clearConsole();
				player = new charac(20,3,0.2,0,0,0);
				file = null;
				play("no","new");
				flag = true;
			} else if (startAction.equals("space")) {
				spaceMode = true; 
				s = '*';
			}
		} while (!flag);

	}

	private String upDir(String dir) {
		if (dir.charAt(dir.length() - 1) != '\\') return upDir(dir.substring(0, dir.length() - 1));
		return dir;
	}

	private void newSave() throws IOException { 

		clearConsole();

		while (true) {
			System.out.print("Enter file name: ");
			name = in.nextLine();
			file = new File(dir + "\\" + name + ".txt");
			if (name.length() < 1) {System.out.print("Name is too short!\n");}
			else if (file.createNewFile()) {
				bwr = new BufferedWriter(new FileWriter(file, false));
				bwr.write("no data");
				bwr.close();
				System.out.print("File created. Hit \"enter\" to continue. ");
				in.nextLine();
				break;
			} else {
				System.out.print("That name is taken! Overwrite file? [y] [n] ");
				if (in.nextLine().equals("y")) {
					file.delete();
					file.createNewFile();
					bwr = new BufferedWriter(new FileWriter(file, false));
					bwr.write("no data");
					bwr.close();
					System.out.print("File overwritten. Hit \"enter\" to continue. ");
					in.nextLine();
					break;
				} else {System.out.print("File not overwritten.\n");}
			}
		}

		clearConsole();
		player = new charac(20,3,0.2,0,0,0);
		play("no","new");

	}

	private void loadSave() throws IOException {

		clearConsole();
		while (true) {
			File[] f = location.listFiles();
			if (f.length == 0) {
				clearConsole();
				System.out.print("No save files have been found - switching to file creation.\nHit \"enter\" to continue. ");
				in.nextLine();
				newSave();
				break;
			} else {
				System.out.print("\nExisting save files:\n");
				for (int i = 0; i < f.length; i++) {
					System.out.print(f[i].getName().substring(0,f[i].getName().length() - 4) + "\n");
				}
				System.out.print("\n");

				while (true) {
					System.out.print("Enter file name: ");
					name = in.nextLine();
					file = new File(dir + "\\" + name + ".txt");
					if (!file.exists()) {
						System.out.print("File does not exist.\n");
					} else {
						System.out.print("File selected. Hit \"enter\" to continue. ");
						in.nextLine();
						break;
					}
				}

				//load file 
				try {
					br = new BufferedReader(new FileReader(file));
					String temp = br.readLine();
					if (temp.equals("no data")) {
						br.close();
						file.delete();
						System.out.print("File contains no save data! Hit \"enter\" to continue. ");
						in.nextLine();
					} else {
						int max = Integer.parseInt(br.readLine().substring(4));
						int cur = Integer.parseInt(br.readLine().substring(4));
						int atk = Integer.parseInt(br.readLine().substring(4));
						double crt = Double.parseDouble(br.readLine().substring(4));
						int def = Integer.parseInt(br.readLine().substring(4));
						int gol = Integer.parseInt(br.readLine().substring(4));
						int exp = Integer.parseInt(br.readLine().substring(4));
						player = new charac(max,atk,crt,def,gol,exp);
						player.currenthealth = cur;
						//inventory here
						String start = br.readLine();
						if (!start.equals("world")) {
							while (!br.readLine().equals("world")) {}
						}
						for (int i = 0; i < 3; i++) {
							temp = br.readLine();
							contents[i][0] = Integer.parseInt(temp.substring(0,1));
							contents[i][1] = Integer.parseInt(temp.substring(2));
						}
						br.close();
						clearConsole();
						play(start, "load");
						break;
					}
				} catch (IOException e) {
					e.getMessage();
				}
			}
		}

	}

	private void play(String start, String init) { 

		boolean skip = false;

		if (start.equals("dungeon")) {
			Dungeon d = new Dungeon(5, 0.7, 0.4, 0.1, 0.25, 0.25, 0.1, 0.05, file, init);
			if (!d.enterDungeon(player, inventory, contents)) return;
			skip = true;
		} else if (start.equals("world")) {
			skip = true;
		}

		while (true) {
			//generate
			if (!skip) {
				int [][] contentsNew = {{(int)(Math.floor(Math.random()*5)),0},
						{(int)(Math.floor(Math.random()*5)),0},{(int)(Math.floor(Math.random()*5)),0}};
				contents = contentsNew;
				if (first) contents[1][0] = 0; //guaranteed first-time dungeon
			}
			first = false;
			skip = false;
			//build
			contentsVisual = new String[3][6];
			for (int i = 0; i < 3; i++) {
				switch (contents[i][0]) {
				case 0:
					contentsVisual[i] = dungeon[(int)(Math.floor(Math.random()*3))];
					break;
				case 1:
					contentsVisual[i] = shop;
					break;
				case 2:
					contentsVisual[i] = hotel;
					break;
				case 3:
					contentsVisual[i] = chest;
					break;
				case 4:
					contentsVisual[i] = endGame;
				}
			}
			printContents();
			//act
			while (true) {
				int approachContent;
				if (checkEnd(contents)) approachContent = 5;
				else approachContent = inputApproach();
				if (approachContent < 3) {
					if (contents[approachContent][1] == 0) {
						switch (contents[approachContent][0]) {
						case 0:
							contents[approachContent][1] = 1;
							Dungeon d = new Dungeon(5, 0.7, 0.4, 0.1, 0.25, 0.25, 0.1, 0.05, file, "new");
							System.out.print("You entered a dungeon!\n");
							if (!d.enterDungeon(player, inventory, contents)) return;
							printContents();
							break;
						case 1:
							contents[approachContent][1] = 1;
							shop(false,approachContent);
							printContents();
							break;
						case 2:
							contents[approachContent][1] = 1;
							hotel(false,approachContent);
							break;
						case 3:
							contents[approachContent][1] = 1;
							int temp = (int)(1+Math.random()*10);
							player.gold += temp;
							System.out.print("You got " + temp + " gold from the chest,\n");
							temp = (int)(1+Math.random()*6);
							int temp2 = player.getLevel()+(int)(Math.random()*3);
							System.out.print("and a level " + temp2 + " " + inventory.typeToString(temp) + ".\n\n");
							inventory.newItem(temp2, temp);
							break;
						case 4:
							contents[approachContent][1] = 1;
							if (endGame() > 0) return;
							break;
						}
					} else {
						switch (contents[approachContent][0]) {
						case 0: case 3: case 4: //dungeon, chest, end
							System.out.print("You cannot visit this again!\n");
							break;
						case 1:
							shop(true,approachContent);
							printContents();
							break;
						case 2:
							hotel(true,approachContent);
							break;
						}
					}
				} else {
					switch (approachContent) {
					case 5:
						System.out.print("You do not have nor can get enough money to continue!\nYou lose!");
					case 4:
						return;
					case 3:
						break;
					}
					break;
				}
			}

		}
	}

	private void shop(boolean visited, int index) {
		if (!visited) {
			shopInv[index].fill(player.getLevel());
			for (int i = 0; i < 3; i++) {
				shopPrice[index][i] = (shopInv[index].getLevel(i))*10 + (int)(Math.random()*21);
			}
		}
		while (true) {
			System.out.print("Your inventory: \n");
			inventory.printInventory();
			System.out.print("Shop's inventory: \n");
			shopInv[index].printInventory();
			while (true) {
				System.out.print("Your gold: " + player.gold + 
						"\nWhat would you like to buy? [1-3] [done] ");
				String temp = in.next();
				if (temp.equals("done")) {
					System.out.print("Come again soon!\n\n");
					return;
				}
				try {
					int tempI = Integer.parseInt(temp);
					if (tempI < 4 && tempI > 0) {
						System.out.print("Do you want to buy the " + shopInv[index].toString(tempI) + 
								" for " + shopPrice[index][tempI] + " gold? [y] [n] ");
						temp = in.next();
						if (temp.equals("y")) {
							if (player.gold < shopPrice[index][tempI]) {
								System.out.print("You need " + (shopPrice[index][tempI]-player.gold) + 
										" more gold!\n\n");
							} else {
								player.gold -= shopPrice[index][tempI];
								//buy
								System.out.print("You bought the " + shopInv[index].toString(tempI) + 
										"! \nYou payed " + shopPrice[index][tempI] + 
										" gold (" + player.gold + " left).\n\n");
							}
						} else 
					} else if (tempI == 4) {
						if (!visited) {
							System.out.print("Ahhhhh... if you insist.\n");
						} else {
							System.out.print("What are you looking for back there?");
						}
					}
				} catch (NumberFormatException e) {}
			}
		}
		System.out.print("Thanks for visiting! Bye!\n\n");
	}

	private void hotel (boolean visited, int index) {
		if (!visited) hotelPrice[index] = (int)(Math.random()*16);
		else if (hotelPrice[index] == 0) hotelPrice[index] = 1+(int)(Math.random()*15);
		String msg;
		if (hotelPrice[index] == 0) msg = "This one's on the house!";
		else msg = "It'll cost you " + hotelPrice[index] + " gold.";
		System.out.print("Your gold: " + player.gold + "\nWould you like to take a nap and heal?\n" + 
				msg + " [y] [n] ");
		String temp = in.next();
		if (temp.equals("y")) {
			if (player.gold < hotelPrice[index]) {
				System.out.print("You need " + (hotelPrice[index]-player.gold) + 
						" more gold to stay here! Bye!\n\n");
			} else {
				player.gold -= hotelPrice[index];
				player.currenthealth = player.maxhealth;
				System.out.print("You're now fully healed! You payed " + hotelPrice[index] + 
						" gold (" + player.gold + " left).\n\n");
			}
		} else System.out.print("Thanks for visiting! Bye!\n\n");
	}

	private int endGame() {
		int cost = (int)(100+Math.random()*151);
		System.out.print("Pay " + cost + " gold to fight the final boss? [y] [n] ");
		String temp = in.next();
		if (temp.contentEquals("y")) {
			if (player.gold < cost) {
				System.out.print("You need " + (cost-player.gold) + 
						" more gold to fight! You are not worthy...\n\n");
			} else {
				player.gold -= cost;
				System.out.print("You payed " + cost + " gold (" + player.gold + " left).\n\n");
				Monster endgame = new Monster(player.maxhealth, player.attack, 0.2, 2, 999999, 999999, "Waterloo Admission Officer");
				inventory.addBuffs(player);
				Battle end = new Battle(player, endgame, inventory, 'f');
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					String killed;
					int overkill = -1*player.currenthealth;
					if (overkill == 0) killed = "Damaged just enough to die\n";
					else if (overkill == 1) killed = "Damaged " + overkill + " health point past death";
					else killed = "Damaged " + overkill + " health points past death";
					System.out.print("\nYour stats:\n" +
							player.gold + " gold\n" +
							player.xp + " experience points (level " + player.getLevel() + ")\n" + 
							killed
							);
					return 1;
				} else {
					System.out.print("You won!!!\n\nYour stats:\n" +
							player.gold + " gold\n" +
							player.xp + " experience points (level " + player.getLevel() + ")\n"
							);
					return 2;
				}
			}
		} else {
			System.out.print("Get outta here then!\n\n");
		}
		return 0;
	}

	private void printContents() {
		System.out.print("\n" +
				" " + contentsVisual[0][0] + " " + contentsVisual[1][0] + " " + contentsVisual[2][0] + "\n" +
				" " + contentsVisual[0][1] + " " + contentsVisual[1][1] + " " + contentsVisual[2][1] + "\n" +
				" " + contentsVisual[0][2] + " " + contentsVisual[1][2] + " " + contentsVisual[2][2] + "\n" +
				" " + contentsVisual[0][3] + " " + contentsVisual[1][3] + " " + contentsVisual[2][3] + "\n" +
				" " + contentsVisual[0][4] + " " + contentsVisual[1][4] + " " + contentsVisual[2][4] + "\n" +
				" " + contentsVisual[0][5] + " " + contentsVisual[1][5] + " " + contentsVisual[2][5] + "\n\n" +
				"_________[1]________________[2]________________[3]__________\n\n"
				);
	}

	private int inputApproach() {
		while (true) {
			System.out.print("Input: ");
			String approach = in.next();
			switch (approach) {
			case "1":
				saved = false;
				return 0;
			case "2":
				saved = false;
				return 1;
			case "3":
				saved = false;
				return 2;
			case "leave":
				if (player.gold >= 5) {
					player.gold -= 5;
					System.out.print("Moving to new area, you payed 5 gold (" + 
							player.gold + " left).\n\n");
					saved = false;
					return 3;
				}
				else System.out.print("You need " + (5-player.gold) + " more gold to do that!\n");
				break;
			case "exit":
				if (file != null) {
					if (!saved) {
						System.out.print("Are you sure you want to exit without saving? [y] [n] ");
						if (!in.next().equals("y")) {
							System.out.print("Returning to game...\n\n");
							break;
						}
					}
				}
				System.out.print("Exiting game.");
				return 4;
			case "save":
				if (file != null) {
					try {
						bwr = new BufferedWriter(new FileWriter(file, false));
						bwr.write("");
						bwr.close();
						bw = new BufferedWriter(new FileWriter(file, true));
						bw.write("player");
						bw.newLine();
						bw.write("max:" + player.maxhealth);
						bw.newLine();
						bw.write("cur:" + player.currenthealth);
						bw.newLine();
						bw.write("atk:" + player.attack);
						bw.newLine();
						bw.write("crt:" + player.crit);
						bw.newLine();
						bw.write("def:" + player.defense);
						bw.newLine();
						bw.write("gol:" + player.gold);
						bw.newLine();
						bw.write("exp:" + player.xp);
						bw.newLine();
						bw.write("world");
						for (int i = 0; i < 3; i++) {
							bw.newLine();
							bw.write(contents[i][0] + "," + contents[i][1]);
						}
						bw.close();
						saved = true;
						System.out.print("Game saved.\n\n");
					} catch (IOException e) {
						e.getMessage();
					}
				} else System.out.print("Saving disabled.\n\n");
				break;
			case "inventory":
				System.out.print("Current health: " + player.currenthealth + "/" + player.maxhealth + "\n");
				System.out.print("Gold: " + player.gold + "\n");
				System.out.print("XP: " + player.xp + " (level " + player.getLevel() + ")\n\n");
				inventory.printInventory();
				inventory.equipMenu(player);
				printContents();
				break;
			case "list":
				System.out.print("" +
						"\"list\" - list all acceptable inputs" + "\n" +
						"\"1\" - approach structure 1" + "\n" +
						"\"2\" - approach structure 2" + "\n" +
						"\"3\" - approach structure 3" + "\n" +
						"\"inventory\" - access inventory" + "\n" +
						"\"leave\" - move to new area (costs 5 gold), DOES NOT SAVE" + "\n" +
						"\"save\" - saves progress and player stats to file" + "\n" +
						"\"exit\" - exits game, DOES NOT SAVE" + "\n" +
						"\n"
						);
			}
		}
	}

	private boolean checkEnd(int[][] approached) {
		if (approached[0][1] == 1 && approached[1][1] == 1 && approached[2][1] == 1 && player.gold < 5) return true;
		else return false;
	}

	private void clearConsole() {
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

}
