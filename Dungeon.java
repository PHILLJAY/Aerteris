import java.util.Arrays;

public class Dungeon {

	//constructor
	int size;
	String[][] visitedRooms;
	int[] insideRoom;
	double wallChance;
	double farWallChance;

	//room prep
	char[] c = new char[4];
	char[] w = new char[18];
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

	String room = "" +
			"    |       |    " + "\n" +
			"    |       |    " + "\n" +
			"----+       +----" + "\n" +
			"                 " + "\n" +
			"                 " + "\n" +
			"                 " + "\n" +
			"----+       +----" + "\n" +
			"    |       |    " + "\n" +
			"    |       |    " + "\n";

	public Dungeon(int size, double wallChance, double farWallChance, boolean hasQuirk) {
		w[16] = ' ';
		w[17] = '+';
		this.size = size;
		this.wallChance = wallChance;
		this.farWallChance = farWallChance;
		//to save
		String[][] visitedRooms = new String[size][size];
		insideRoom = new int[]{size/2, size/2};

		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		c = new char[]{' ',' ','@',' '};
	}

	public void printGeneralInfo() {
		System.out.print("" +
				"Size: " + size + "x" + size + "\n" +
				"Current room index: " + Arrays.toString(insideRoom) + "\n" +
				"Chance of a wall blocking the path: " + wallChance*100 + "%\n" +
				"Chance of said wall being a pocket: " + farWallChance*100 + "%\n"
				);
	}

	public void generateRoom() { // 1 wall currently -- don't forget to add moveType()
		int[] done = new int[]{-1,-1,-1};
		for (int i = 0; i < 3; i++) {
			int failedAttempts = 0;
			do {
				done[i] = (int)Math.floor(Math.random()*4); 
				failedAttempts++;
			} while ((done[i]==findPlayer(0)||existingWalls(done[i],0))&&failedAttempts<50);
			if (failedAttempts>50) break;
			if (Math.random()<wallChance) {
				if (Math.random()<farWallChance) {
					w[done[i]] = ' ';
					w[done[i]+12] = '+';
					if (done[i]==1||done[i]==2) {
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
					if (done[i]==1||done[i]==2) w[done[i]] = '|';
					else w[done[i]] = '-';
				}
			} else {
				w[done[i]] = ' ';
				w[done[i]+4] = ' ';
				if (done[i]==1||done[i]==2) {
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

	public void generateContents() {

	}

	public void refreshRoom() {
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

	public void newRoom() {
		generateRoom();
		generateContents();
		refreshRoom();
	}

	public void saveRoom() {
		//location, 0.1.2.3.4.5
	}

	public int findPlayer(int i) { //should never be called when player is not in a room
		if (c[i] == playerSym) return i;
		return findPlayer(i+1);
	}
	
	public boolean existingWalls(int test, int index) {
		if (test==done[0])
	}

}
