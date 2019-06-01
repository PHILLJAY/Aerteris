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

	boolean spaceMode = false;
	char s = ' ';
	private String name;


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
				player = new charac(20,3,0.2,0,0);
				play("new");
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
		player = new charac(20,3,0.2,0,0);
		play("new");

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
						player = new charac(max,atk,crt,def,gol);
						player.currenthealth = cur;
						br.close();
						clearConsole();
						play("load");
						break;
					}
				} catch (IOException e) {
					e.getMessage();
				}
			}
		}

	}

	private void play(String init) { 

		Dungeon d = new Dungeon(5, 0.7, 0.4, 0.1, 0.25, 0.25, 0.1, 0.05, file, init);

		d.enterDungeon(player);

	}

	private void clearConsole() {
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

}
