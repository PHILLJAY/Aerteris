import java.util.Arrays;
import java.util.Scanner;

/**
 * Deals with generation of and movement within a dungeon. Will trigger events such as battles, but that is dealt with in a different class.
 * 
 * @version 1.1
 * 
 * @author Gavin Jameson
 */
public class Dungeon {

	//constructor
	int size;
	private String[][] visitedRooms;
	private int[] insideRoom;
	private double wallChance;
	private double farWallChance;
	private double wallChanceReduction;
	private double lootChance;
	private double enemyChance;

	//room prep
	private char[] c = new char[4];
	private char[] w = new char[18];
	private int[] done;
	private int[] moveType = new int[4]; //values: 0=no, 1=travel, 2=move and remain
	private Scanner input = new Scanner(System.in);

	//symbols
	private char playerSym = '@';
	private char lootSym = 'C';
	private char enemySym = 'E';

	/* 
	 * Layout:
	 * 
	 *   c4c  
	 *   8 8  
	 * d9 0 ae
	 * 5 1 2 6
	 * d9 3 ae
	 *   b b  
	 *   f7f  
	 */

	String room = "" + //what a room with 'no walls' looks like
			"    |       |    " + "\n" +
			"    |       |    " + "\n" +
			"----+       +----" + "\n" +
			"                 " + "\n" +
			"                 " + "\n" +
			"                 " + "\n" +
			"----+       +----" + "\n" +
			"    |       |    " + "\n" +
			"    |       |    " + "\n";

	/**
	 * Creates an instance of a {@code Dungeon()} object with parameters to specify details of room generation.
	 * 
	 * @param size - The length, in rooms, of one side of the square-shaped space rooms can be generated. When a dungeon is generated, it may end up smaller than {@code size}, but never bigger.
	 * @param wallChance - The chance that a wall will be generated; will continue to try generating walls on all applicable sides until it fails. Sides are picked at random.
	 * @param farWallChance - The chance that a wall will have a pocket in which item drops or enemies can spawn.
	 * @param wallChanceReduction - What percent is subtracted from {@code wallChance} on each sequential generation in a room.
	 * @param lootChance - The chance that a pocket will have a loot. Less far walls will decrease the number of chests.
	 * @param enemyChance - The chance that an enemy will spawn in a hallway. Less walls will increase number of enemies.
	 * 
	 * @see {@link #enterDungeon()}
	 */
	public Dungeon(int size, double wallChance, double farWallChance, double wallChanceReduction, double lootChance, double enemyChance) {
		w[16] = ' ';
		w[17] = '+';
		this.size = size;
		this.wallChance = wallChance;
		this.farWallChance = farWallChance;
		this.wallChanceReduction = wallChanceReduction;
		this.lootChance = lootChance;
		this.enemyChance = enemyChance;
		//to save
		String[][] visitedRooms = new String[size][size];
		insideRoom = new int[]{size/2, size/2};

		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		c = new char[]{' ',' ',' ','@'};
	}

	/**
	 * Starts the progression through the dungeon.
	 * 
	 * @see {@link #newRoom()}, {@link #refreshRoom()}, {@link #takeInput()}
	 */
	public void enterDungeon(Charac player) {
		String action = "new";
		do {
			switch (action) {
			case "new":
				newRoom();
				System.out.print(room);
				break;
			case "stay":
				refreshRoom();
				System.out.print(room);
				break;
			case "battle": 
				//Monster monster = new Monster((int)(10+Math.floor(Math.random()*7)-3), (1), (0.1), (0), "TESTSUBJECTSKELE");
				//Battle b = new Battle(player, monster);
				refreshRoom();
				System.out.print(room);
				break;
			case "loot":
				//loot class? in dungeon or elsewhere?
				System.out.print("[LOOTING...]\n\n");
				refreshRoom();
				System.out.print(room);
				break;
			}
			action = takeInput();
		} while (!action.equals("leave"));
		System.out.print("You left the dungeon.\n\n");
	}

	/**
	 * Prints information about the dungeon structure and generation specifics.
	 */
	public void printGeneralInfo() {
		System.out.print("" +
				"Size: " + size + "x" + size + "\n" +
				"Current room index: " + Arrays.toString(insideRoom) + "\n" +
				"Chance of a wall blocking the path: " + wallChance*100 + "%\n" +
				"Chance of said wall being a pocket: " + farWallChance*100 + "%\n" +
				"Percent subtracted from chance of wall for each new wall: " + wallChanceReduction*100 + "%\n"
				);
	}

	/**
	 * Decides how many walls there should be and generates them based on player position, size restrictions, and adjacent rooms ({@code not implemented}).
	 * <p>
	 * Private method called in the {@link #newRoom()} public method.
	 * 
	 * @see {@link #resetMoveType(int)}, {@link #generateWall(int)}, {@link #findPlayer(int)}, {@link #isExistingWall(int,int)}
	 */
	private void generateRoom() {
		done = new int[]{-1,-1,-1};
		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		resetMoveType(0);
		int a;
		if (insideRoom[0] == 0) {
			done[0] = 1;
			generateWall(0);
		} else if (insideRoom[0] == size - 1) {
			done[0] = 2;
			generateWall(0);
		} 
		if (done[0] == -1) a = 0;
		else a = 1;
		if (insideRoom[1] == 0) {
			done[a] = 0;
			generateWall(a);
		} else if (insideRoom[1] == size - 1) {
			done[a] = 3;
			generateWall(a);
		}
		for (int i = 0; i < 3; i++) {
			if (done[i] == -1) {
				int failedAttempts = 0;
				do {
					done[i] = (int)Math.floor(Math.random()*4); 
					failedAttempts++;
				} while ((done[i] == findPlayer(0) || isExistingWall(i,0)) && failedAttempts < 50);
				if (failedAttempts > 50) break;
				if (Math.random() + (i * wallChanceReduction) < wallChance) {
					generateWall(i);
				} else {
					w[done[i]] = ' ';
					w[done[i]+4] = ' ';
					if (done[i] == 1 || done[i] == 2) {
						w[done[i]+8] = '-';
						w[done[i]+12] = '-';
					} else {
						w[done[i]+8] = '|';
						w[done[i]+12] = '|';
					}
					break;
				}
			}
		}
	}

	/**
	 * {@code empty}
	 * <p>
	 * Generates item drops and enemies in rooms in based on available positions.
	 * <p>
	 * Private method called in the {@link #newRoom()} public method.
	 */
	private void generateContents() {
		for (int i = 0; i < 4; i++) {
			if (c[i] == ' ') {
				if (moveType[i] == 2) {
					if (Math.random() < lootChance) c[i] = lootSym;
				} else if (moveType[i] == 1) {
					if (Math.random() < enemyChance) c[i] = enemySym;
				}
			}
		}
	}

	/**
	 * Updates {@code room} String to be printed with the new walls and contents generated.
	 * <p>
	 * Private method called in the {@link #newRoom()} private method and the {@link #enterDungeon()} public method.
	 */
	private void refreshRoom() {
		room = "" +
				w[16] + w[16] + w[16] + w[16] + w[12] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[12] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[8 ] + w[16] + w[16] + w[16] + c[0 ] + w[16] + w[16] + w[16] + w[8 ] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[13] + w[9 ] + w[9 ] + w[9 ] + w[17] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[17] + w[10] + w[10] + w[10] + w[14] + "\n" +
				w[5 ] + w[16] + w[16] + w[16] + w[1 ] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[2 ] + w[16] + w[16] + w[16] + w[6 ] + "\n" +
				w[5 ] + w[16] + c[1 ] + w[16] + w[1 ] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[2 ] + w[16] + c[2 ] + w[16] + w[6 ] + "\n" +
				w[5 ] + w[16] + w[16] + w[16] + w[1 ] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[2 ] + w[16] + w[16] + w[16] + w[6 ] + "\n" +
				w[13] + w[9 ] + w[9 ] + w[9 ] + w[17] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[17] + w[10] + w[10] + w[10] + w[14] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[11] + w[16] + w[16] + w[16] + c[3 ] + w[16] + w[16] + w[16] + w[11] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[15] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[15] + w[16] + (insideRoom[0]+1) + ", " + (insideRoom[1]+1) + "\n\n";
	}

	/**
	 * Generates a new room in the dungeon, including walls and contents.
	 * <p>
	 * Private method called in the {@link #enterDungeon()} public method.
	 * 
	 * @see {@link #generateRoom()}, {@link #generateContents()}, {@link #refreshRoom()}
	 */
	public void newRoom() {
		generateRoom();
		generateContents();
		refreshRoom();
	}

	/**
	 * {@code unused}
	 * <p>
	 * Stores location, walls, and remaining contents of a room. Will be stored in a save file if game is saved while in a dungeon.
	 */
	public void saveRoom() {
		//location, 0.1.2.3.4.5
	}

	/**
	 * Finds the location of the player in a room.
	 * <p>
	 * Private method called in the {@link #generateRoom()} private method and {@link #tryMove(String)} private method.
	 * 
	 * @param i - Index to search from; should always be called with initial value {@code 0}.
	 * 
	 * @return {@code int} index in the contents array ({@code c}) where the player is currently located.
	 */
	private int findPlayer(int i) { //should never be called when player is not in a room
		if (c[i] == playerSym) return i;
		return findPlayer(i+1);
	}

	/**
	 * Determines if the selected wall has already been decided.
	 * <p>
	 * Private method called in the {@link #generateRoom()} private method.
	 * 
	 * @param test - Integer (corresponding to a wall) to be searched for.
	 * @param index - Index to search from; should always be called with initial value {@code 0}.
	 * 
	 * @return {@code true} if {@code test} has been found in {@code done} and therefore has already been generated, {@code false} if not.
	 */
	private boolean isExistingWall(int test, int index) {
		if (index >= 3) return false;
		if (test == index) return isExistingWall(test,index+1);
		if (done[test] == done[index]) return true;
		return isExistingWall(test,index+1);
	}

	/**
	 * Generates the depth of a wall and draws walls.
	 * <p>
	 * Private method called in the {@link #generateRoom()} private method.
	 * 
	 * @param i - Integer in the array {@code done} (corresponding to a wall) to be generated.
	 */
	private void generateWall(int i) {
		if (Math.random() < farWallChance) {
			moveType[done[i]] = 2;
			w[done[i]] = ' ';
			w[done[i]+12] = '+';
			if (done[i] == 1 || done[i] == 2) {
				w[done[i]+4] = '|';
				w[done[i]+8] = '-';
			} else {
				w[done[i]+4] = '-';
				w[done[i]+8] = '|';
			}
		} else {
			moveType[done[i]] = 0;
			w[done[i]+4] = ' ';
			w[done[i]+8] = ' ';
			w[done[i]+12] = ' ';
			if (done[i] == 1 || done[i] == 2) w[done[i]] = '|';
			else w[done[i]] = '-';
		}
	}

	/**
	 * Allows the user to input a {@code String} corresponding to movement or menu options. Will loop until a movement is received.
	 * All inputs can be printed to the console with {@link #listInputs()}.
	 * <p>
	 * Private method called in the {@link #enterDungeon()} public method.
	 * 
	 * @return {@code "leave"} if the input was leave, {@code "new"} if moving to a new room, {@code "stay"} if staying in the room, {@code "battle"} if entering a battle, and {@code "loot"} if looting.
	 *
	 * @see {@link #listInputs()}, {@link #tryMove(String)}
	 */
	private String takeInput() {
		int r;
		while (true) {
			//while (input.hasNext()) input.next(); //clear queue
			System.out.print("Input: ");
			String in = input.next();
			switch (in) {
			case "list":
				listInputs();
				break;
			case "w": case "a": case "s": case "d":
				if ((r = tryMove(in)) > 0) {
					switch (r) {
					case 1: return "new";
					case 2: return "stay";
					case 3: return "battle";
					case 4: return "loot";
					}
				} else {
					break;
				}
			case "inventory":
				System.out.print("[this is your inventory]\n");
				//manageInventory?
				break;
			case "leave":
				return "leave";
			}
		}
	}

	/**
	 * Attempts to move in the direction specified in the parameter corresponding to the 'wasd' keys.
	 * <p>
	 * Private method called in the {@link #takeInput()} private method.
	 * 
	 * @param direction - {@code "w"}, {@code "a"}, {@code "s"}, or {@code "d"}.
	 * 
	 * @return {@code 0} if unsuccessful, {@code 1} if passing to another room, {@code 2} if staying in the room, {@code 3} if entering a battle, and {@code 4} if looting.
	 *
	 * @see {@link #resetContents(int)}, {@link #refreshCoords(String)}, {@link #findPlayer(int)}
	 */
	private int tryMove(String direction) {
		int a = 0;
		int b = 3;
		switch (direction) {
		case "w":
			a = 0;
			b = 3;
			break;
		case "a":
			a = 1;
			b = 2;
			break;
		case "s":
			a = 3;
			b = 0;
			break;
		case "d":
			a = 2;
			b = 1;
			break;
		}
		switch (moveType[a]) {
		case 0:
			System.out.print("You can't go there!\n");
			return 0;
		case 1:
			if (c[a] != enemySym) {
				resetContents(0);
				c[b] = playerSym;
				refreshCoords(direction);
				return 1;
			}
		case 2:
			int ret = 2;
			if (c[a] == ' ' || c[a] == playerSym);
			else if (c[a] == enemySym) ret = 3;
			else if (c[a] == lootSym) ret = 4;
			c[findPlayer(0)] = ' ';
			c[a] = playerSym;
			return ret;
		}
		System.err.print("Unable to find a movement option.\n");
		return 0;
	}

	/**
	 * Updates the current coordinates of the room in the dungeon, stored in the {@code insideRoom} array. 
	 * <p>
	 * Private method called in the {@link #tryMove(String)} private method.
	 * 
	 * @param direction - A successful input from {@link #tryMove(String)}; {@code "w"}, {@code "a"}, {@code "s"}, or {@code "d"}.
	 */
	private void refreshCoords(String direction) {
		switch (direction) {
		case "w": insideRoom[1]--; return;
		case "a": insideRoom[0]--; return;
		case "s": insideRoom[1]++; return;
		case "d": insideRoom[0]++; return;
		}
	}

	/**
	 * Sets all indexes in the contents array ({@code c}) to a whitespace {@code char}.
	 * <p>
	 * Private method called in the {@link #tryMove(String)} private method.
	 * 
	 * @param i - Index to search from; should always be called with initial value {@code 0}.
	 */
	private Object resetContents(int i) {
		if (i > 3) return null;
		c[i] = ' ';
		return resetContents(i+1);
	}

	/**
	 * Sets all indexes in the array ({@code moveType}) to {@code 1} (open).
	 * <p>
	 * Private method called in the {@link #generateRoom()} private method.
	 * 
	 * @param i - Index to search from; should always be called with initial value {@code 0}.
	 */
	private Object resetMoveType(int i) {
		if (i > 3) return null;
		moveType[i] = 1;
		return resetMoveType(i+1);
	}

	/**
	 * Lists all valid inputs for {@link #takeInput()}.
	 * <p>
	 * Private method called in the {@link #takeInput()} private method.
	 */
	private void listInputs() {
		System.out.print("" +
				"\"list\" - list all acceptable inputs (what this does)" + "\n" +
				"\"w\" - move up" + "\n" +
				"\"a\" - move left" + "\n" +
				"\"s\" - move down" + "\n" +
				"\"d\" - move right" + "\n" +
				"\"inventory\" - access inventory (does nothing)" + "\n" +
				"\"leave\" - leave dungeon and return to surface" + "\n" +
				"\n"
				);
	}

}









