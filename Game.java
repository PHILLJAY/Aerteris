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
	Inv inv = new Inv(8);
	Inv[] shopInv = {new Inv(3), new Inv(3), new Inv(3)};
	int[] hotelPrice = {0,0,0};

	boolean spaceMode = false;
	char s = ' ';
	private String name;

	private boolean saved = false;
	private boolean first = true;

	//structures
	//private String[] structure = {"dungeon ","  shop  "," hotel  "," chest  ","end game"};
	private String[][] contentsVisual;
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
			"    |__[]__|_-�   ",
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
				inv.initialize();
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
		inv.initialize();
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
						String start = br.readLine();
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

		if (start.equals("dungeon")) {
			Dungeon d = new Dungeon(5, 0.7, 0.4, 0.1, 0.25, 0.25, 0.1, 0.05, file, init);
			if (!d.enterDungeon(player, inv)) return;
		}

		while (true) {
			//generate
			int[][] contents = {{(int)(Math.floor(Math.random()*5)),0},
					{(int)(Math.floor(Math.random()*5)),0},{(int)(Math.floor(Math.random()*5)),0}};
			if (first) contents[1][0] = 0; //guaranteed first-time dungeon
			first = false;
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
				int approachContent = inputApproach();
				if (approachContent < 3) {
					if (contents[approachContent][1] == 0) {
						switch (contents[approachContent][0]) {
						case 0:
							Dungeon d = new Dungeon(5, 0.7, 0.4, 0.1, 0.25, 0.25, 0.1, 0.05, file, init);
							System.out.print("You entered a dungeon!\n");
							if (!d.enterDungeon(player, inv)) return;
							contents[approachContent][1] = 1;
							break;
						case 1:
							shopInv[approachContent].initialize();
							shopInv[approachContent].showInv();
							contents[approachContent][1] = 1;
							printContents();
							break;
						case 2:
							hotel(false,approachContent);
							contents[approachContent][1] = 1;
							break;
						case 3:
							int temp = (int)(1+Math.random()*10);
							player.gold += temp;
							System.out.print("You got " + temp + " gold from the chest,\n");
							temp = (int)(Math.random()*4);
							int temp2 = player.getLevel()+(int)(Math.random()*3);
							//inv.chestitemGen(temp,temp2);
							System.out.print("and a level " + temp2 + " " + inv.printItem(temp) + ".\n\n");
							contents[approachContent][1] = 1;
							break;
						case 4:
							//new end game
							contents[approachContent][1] = 1;
							printContents();
							break;
						}
					} else {
						switch (contents[approachContent][0]) {
						case 0: case 3: case 4: //dungeon, chest, end
							System.out.print("You cannot visit this again!\n");
							break;
						case 1:
							shopInv[approachContent].showInv();
							printContents();
							break;
						case 2:
							hotel(true,approachContent);
							break;
						}
					}
				} else {
					switch (approachContent) {
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
	
	private void hotel(boolean visited, int index) {
		if (!visited) hotelPrice[index] = (int)(Math.random()*16);
		else if (hotelPrice[index] == 0) hotelPrice[index] = 1+(int)(Math.random()*15);
		String msg;
		if (hotelPrice[index] == 0) msg = "This one's on the house!";
		else msg = "It'll cost you " + hotelPrice[index] + " gold.";
		System.out.print("Would you like to take a nap and heal?\n" + 
		msg + " [y] [n] ");
		String temp = in.next();
		if (temp.contentEquals("y")) {
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

	private void printContents() {
		System.out.print("\n" +
				" " + contentsVisual[0][0] + " " + contentsVisual[1][0] + " " + contentsVisual[2][0] + "\n" +
				" " + contentsVisual[0][1] + " " + contentsVisual[1][1] + " " + contentsVisual[2][1] + "\n" +
				" " + contentsVisual[0][2] + " " + contentsVisual[1][2] + " " + contentsVisual[2][2] + "\n" +
				" " + contentsVisual[0][3] + " " + contentsVisual[1][3] + " " + contentsVisual[2][3] + "\n" +
				" " + contentsVisual[0][4] + " " + contentsVisual[1][4] + " " + contentsVisual[2][4] + "\n" +
				" " + contentsVisual[0][5] + " " + contentsVisual[1][5] + " " + contentsVisual[2][5] + "\n\n" +
				"_________[1]________________[2]________________[3]__________\n"
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
				if (!saved) {
					System.out.print("Are you sure you want to exit without saving? [y] [n] ");
					if (!in.next().equals("y")) {
						System.out.print("Returning to game...\n\n");
						break;
					}
				}
				System.out.print("Exiting game.");
				return 4;
			case "save":
				saved = true;
				break;
			case "inventory":
				System.out.print("Current health: " + player.currenthealth + "\n");
				System.out.print("Gold: " + player.gold + "\n");
				System.out.print("XP: " + player.xp + " (level " + player.getLevel() + ")\n");
				inv.showInv();
				System.out.print("\n");
				//manageInventory?
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
	private void clearConsole() {
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

}
