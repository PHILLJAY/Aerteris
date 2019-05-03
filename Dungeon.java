import java.util.Arrays;

/**
 * Deals with generation of and movement within a dungeon. Will trigger events such as battles, but that is dealt with in a different class.
 * 
 * @version 1.0
 * 
 * @author Gavin Jameson
 */
public class Dungeon {

	//constructor
	int size;
	String[][] visitedRooms;
	int[] insideRoom;
	double wallChance;
	double farWallChance;
	double wallChanceReduction;

	//room prep
	char[] c = new char[4];
	char[] w = new char[18];
	int[] done;
	int[] moveType = new int[4]; //0=no, 1=travel, 2=move and remain

	//symbols
	char playerSym = '@';
	char lootSym = 'C';
	char enemySym = 'E';

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
	 * @param hasQuirk - {@code unused}
	 * 
	 * @see #generateRoom()
	 */
	public Dungeon(int size, double wallChance, double farWallChance, double wallChanceReduction, boolean hasQuirk) {
		w[16] = ' ';
		w[17] = '+';
		this.size = size;
		this.wallChance = wallChance;
		this.farWallChance = farWallChance;
		this.wallChanceReduction = wallChanceReduction;
		//to save
		String[][] visitedRooms = new String[size][size];
		insideRoom = new int[]{size/2, size/2};

		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		c = new char[]{' ',' ',' ','@'};
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
	 */
	private void generateRoom() { // don't forget to add moveType() -> probably to generateWall()
		done = new int[]{-1,-1,-1};
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
			generateWall(1);
		} else if (insideRoom[0] == size - 1) {
			done[a] = 3;
			generateWall(1);
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
	 * {@code unused}
	 * <p>
	 * Generates item drops and enemies in rooms based on available positions.
	 * <p>
	 * Private method called in the {@link #newRoom()} public method.
	 */
	private void generateContents() {

	}

	/**
	 * Updates {@code room} String to be printed with the new walls and contents generated.
	 * <p>
	 * Private method called in the {@link #newRoom()} public method.
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
				w[16] + w[16] + w[16] + w[16] + w[15] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[15] + w[16] + w[16] + w[16] + w[16] + "\n\n";
	}

	/**
	 * Generates a new room in the dungeon, including walls and contents.
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
	 * Private method called in the {@link #generateRoom()} private method.
	 * 
	 * @param i - Index to search from; should always be called with initial value {@code 0}.
	 * 
	 * @return Integer index in the contents array ({@code c}) where the player is currently located.
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
	 * @return True if {@code test} has been found in {@code done} and therefore has already been generated, false if not.
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
	 * @param i - Integer (corresponding to a wall) to be generated.
	 */
	private void generateWall(int i) {
		if (Math.random() < farWallChance) {
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
			w[done[i]+4] = ' ';
			w[done[i]+8] = ' ';
			w[done[i]+12] = ' ';
			if (done[i] == 1 || done[i] == 2) w[done[i]] = '|';
			else w[done[i]] = '-';
		}
	}

}
