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
	private File file;
	private BufferedReader br;
	private BufferedWriter bw, bwr;

	boolean spaceMode = false;
	char s = ' ';
	String name;


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
				"|           in the console to play comfortably             |\n" +
				"|                                                          |\n" +
				"|             Press any button to continue...              |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"|                                                          |\n" +
				"+----------------------------------------------------------+\n"
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
					"                                                            \n" +
					"    "+s+"               "+s+"          "+s+"                         "+s+"  \n" +
					"            "+s+"                                "+s+"              \n" +
					"                            "
					);

			String startAction;
			startAction = in.next();
			if (startAction.equals("new")) {
				try {
					newSave();
				} catch (IOException e) {
					e.getMessage();
				} 
				flag = true;
			} else if (startAction.equals("load")) {
				loadSave(); 
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
				System.out.print("File created. Hit \"enter\" to continue. ");
				in.nextLine();
				break;
			} else {
				System.out.print("That name is taken! Overwrite file? [y] [n] ");
				if (in.nextLine().equals("y")) {
					file.delete();
					file.createNewFile();
					System.out.print("File overwritten. Hit \"enter\" to continue. ");
					in.nextLine();
					break;
				} else {System.out.print("File not overwritten.\n");}
			}
		}
		charac player = new charac(20,3,0.2,0);
		play();
		
	}

	private void loadSave() {

	}

	private void play() {

	}

	private void clearConsole() {
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

}
