import java.util.Arrays;
import java.util.Scanner;
import java.io.*;

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
	private String[][][] visitedRooms;
	private int[] insideRoom;
	private double wallChance;
	private double farWallChance;
	private double wallChanceReduction;
	private double lootChance;
	private double enemyChance;
	private double minibossChance;
	private double portalChance;

	//room prep
	private char[] c = new char[5];
	private char[] w = new char[18];
	private int[] done;
	private int[] moveType = new int[4]; //values: 0=no, 1=travel, 2=move and remain
	private Scanner input = new Scanner(System.in);
	private int loc;
	String action;
	private boolean rewarded;
	private boolean portal;

	//symbols
	private char playerSym = 'P';
	private char lootSym = 'C';
	private char enemySym = 'e';
	private char miniSym = 'E';
	private char healSym = 'H';
	private char portalSym = '@';

	//saving
	File file;
	charac player;
	Inventory inventory;
	BufferedReader br;
	BufferedWriter bw, bwr;
	private boolean saved = true;
	private String check;
	private int lastLevel;

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
	 * @param minibossChance - The chance that a tough enemy will spawn in a hallway. Less walls will increase number of enemies.
	 * @param portalChance - The chance that a portal will appear in a room; 1 per dungeon.
	 * @param file - Active save file for game.
	 * @param action - {@code String} detailing what to do when initially entering.
	 * 
	 * @see {@link #enterDungeon(charac)}
	 */
	public Dungeon(int size, double wallChance, double farWallChance, 
			double wallChanceReduction, double lootChance, double enemyChance, 
			double minibossChance, double portalChance, File file, String action) {
		w[16] = ' ';
		w[17] = '+';
		this.size = size;
		this.wallChance = wallChance;
		this.farWallChance = farWallChance;
		this.wallChanceReduction = wallChanceReduction;
		this.lootChance = lootChance;
		this.enemyChance = enemyChance;
		this.minibossChance = minibossChance;
		this.portalChance = portalChance;
		this.action = action;
		//to save
		this.file = file;
		visitedRooms = new String[size][size][4];
		insideRoom = new int[]{size/2, size/2};
		rewarded = false;
		portal = false;

		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		c = new char[]{playerSym,' ',' ',' ',' '};
	}

	/**
	 * Starts the progression through the dungeon.
	 * 
	 * @param p - Player's character to be entering the dungeon
	 * 
	 * @return {@code true} if alive at the end, {@code false} if the player has died.
	 * 
	 * @see {@link #getRoom()}, {@link #refreshRoom()}, {@link #takeInput()}
	 */
	public boolean enterDungeon(charac player, Inventory inventory, int[][] contents) {
		this.player = player;
		this.inventory = inventory;
		lastLevel = player.getLevel();
		do {
			switch (action) {
			case "load":
				try {
					br = new BufferedReader(new FileReader(file));
					while (!br.readLine().equals("dungeon")) {}
					size = Integer.parseInt(br.readLine().substring(4));
					wallChance = Double.parseDouble(br.readLine().substring(4));
					farWallChance = Double.parseDouble(br.readLine().substring(4));
					wallChanceReduction = Double.parseDouble(br.readLine().substring(4));
					lootChance = Double.parseDouble(br.readLine().substring(4));
					enemyChance = Double.parseDouble(br.readLine().substring(4));
					minibossChance = Double.parseDouble(br.readLine().substring(4));
					portalChance = Double.parseDouble(br.readLine().substring(4));
					if (br.readLine().substring(4).equals("true")) {
						portal = true;
					} else portal = false;
					if (br.readLine().substring(4).equals("true")) {
						rewarded = true;
					} else rewarded = false;
					visitedRooms = new String[size][size][4];
					insideRoom = new int[]{size/2, size/2};
					String[] tempA = br.readLine().split(",");
					insideRoom[0] = Integer.parseInt(tempA[0]);
					insideRoom[1] = Integer.parseInt(tempA[1]);
					for (int i = 0; i < size; i++) {
						for (int j = 0; j < size; j++ ) {
							String temp = br.readLine();
							if (temp.equals(":::")) {
								for (int k = 0; k < 4; k++) {
									visitedRooms[i][j][k] = null;
								}
							} else {
								visitedRooms[i][j][0] = temp.substring(0,w.length); // length could be changed on w and or c
								visitedRooms[i][j][1] = temp.substring(w.length+1,w.length+c.length+1);
								visitedRooms[i][j][2] = temp.substring(w.length+c.length+2,w.length+c.length+6);
								visitedRooms[i][j][3] = temp.substring(w.length+c.length+7);
							}
						}
					}
					resetContents();
					c[Integer.parseInt(visitedRooms[insideRoom[0]][insideRoom[1]][3])] = playerSym;
					br.close();
				} catch (IOException e) {
					e.getMessage();
				}
			case "new":
				getRoom();
				System.out.print(room);
				saved = false;
				break;
			case "stay":
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "battleN": 
				Monster monster = new Monster(
						(int)(player.maxhealth/2+Math.random()*player.maxhealth/2), 
						(int)(player.attack/2+Math.random()*player.attack/2), 
						(Math.random()/4), 
						(int)(Math.random()*2),
						(int)(1+Math.random()*10),
						(int)(1+Math.random()*10),
						'e'
						);
				inventory.addBuffs(player);
				Battle normal = new Battle(player, monster);
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					deathStats();
					return false;
				}
				levelUp();
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "MbattleN": 
				Monster monsterAdj = new Monster(
						(int)(player.maxhealth/2+Math.random()*player.maxhealth/2), 
						(int)(player.attack/2+Math.random()*player.attack/2), 
						(Math.random()/4), 
						(int)(Math.random()*2),
						(int)(1+Math.random()*10),
						(int)(1+Math.random()*10),
						'e'
						);
				inventory.addBuffs(player);
				Battle normalAdj = new Battle(player, monsterAdj);
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					deathStats();					
					return false;
				}
				levelUp();
				getRoom();
				System.out.print(room);
				saved = false;
				break;
			case "battleM": 
				Monster miniBoss = new Monster(
						(int)(player.maxhealth/2+Math.random()*player.maxhealth/2), 
						(int)(player.attack/2+Math.random()*player.attack/2), 
						(Math.random()/4), 
						(int)(Math.random()*2),
						(int)(1+Math.random()*20),
						(int)(10+Math.random()*11),
						'E'
						);
				inventory.addBuffs(player);
				Battle tough = new Battle(player, miniBoss);
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					deathStats();
					return false;
				}
				levelUp();
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "MbattleM": 
				Monster miniBossAdj = new Monster(
						(int)(player.maxhealth/2+Math.random()*player.maxhealth/2), 
						(int)(player.attack/2+Math.random()*player.attack/2), 
						(Math.random()/4), 
						(int)(Math.random()*2),
						(int)(1+Math.random()*20),
						(int)(10+Math.random()*11),
						'E'
						);
				inventory.addBuffs(player);
				Battle toughAdj = new Battle(player, miniBossAdj);
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					deathStats();
					return false;
				}
				levelUp();
				getRoom();
				System.out.print(room);
				saved = false;
				break;
			case "portal":
				Monster dungeonBoss = new Monster(
						(int)(player.maxhealth/1.5+Math.random()*player.maxhealth/2), 
						(int)(player.attack/1.5+Math.random()*player.attack/2), 
						(Math.random()/3), 
						(int)(Math.random()*2),
						(int)(20+Math.random()*11), //20-30
						(int)(30+Math.random()*21), //30-50
						'B'
						);
				inventory.addBuffs(player);
				Battle boss = new Battle(player, dungeonBoss);
				inventory.removeBuffs(player);
				if (player.currenthealth <= 0) {
					deathStats();
					return false;
				}
				levelUp();
				c[4] = ' ';
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "loot":
				int temp = (int)(1+Math.random()*10);
				player.gold += temp;
				System.out.print("You got " + temp + " gold from the chest,\n");
				temp = (int)(1+Math.random()*6);
				int temp2 = player.getLevel()+(int)(Math.random()*3);
				System.out.print("and a level " + temp2 + " " + inventory.typeToString(temp) + ".\n");
				inventory.newItem(temp2, temp);
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "heal":
				if (player.currenthealth + 10 > player.maxhealth) {
					player.currenthealth = player.maxhealth;
					System.out.print("You have been fully healed!\n\n");
				} else {
					player.currenthealth += 10;
					System.out.print("You healed 10 health points!\n\n");
				}
				refreshRoom();
				System.out.print(room);
				saved = false;
				break;
			case "save":
				if (file != null) {
					try {
						saveRoom();
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
						bw.write("dungeon");
						bw.newLine();
						bw.write("siz:" + size);
						bw.newLine();
						bw.write("wc :" + wallChance);
						bw.newLine();
						bw.write("fwc:" + farWallChance);
						bw.newLine();
						bw.write("wcr:" + wallChanceReduction);
						bw.newLine();
						bw.write("lc :" + lootChance);
						bw.newLine();
						bw.write("ec :" + enemyChance);
						bw.newLine();
						bw.write("mbc:" + minibossChance);
						bw.newLine();
						bw.write("pc :" + portalChance);
						bw.newLine();
						bw.write("por:" + portal);
						bw.newLine();
						bw.write("rew:" + rewarded);
						bw.newLine();
						bw.write(insideRoom[0] + "," + insideRoom[1]);
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < size; j++) {
								bw.newLine();
								if (visitedRooms[i][j][0] == null) {
									bw.write(":::");
								} else {
									bw.write(visitedRooms[i][j][0] + ":" + visitedRooms[i][j][1] + ":" + 
											visitedRooms[i][j][2] + ":" + visitedRooms[i][j][3]);
								}
							}
						}
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
			case "exit":
				if (file != null) {
					if (!saved) {
						System.out.print("Are you sure you want to exit without saving? [y] [n] ");
						check = input.next();
						if (!check.equals("y")) {
							System.out.print("Returning to game...\n\n");
							break;
						}
					}
				}
				System.out.print("Exiting game.");
				return false;
			case "leave":
				System.out.print("You left the dungeon.\n\n");
				return true;
			}
			saveRoom();
			if (!rewarded && checkComplete() && countContents(enemySym) == 0 && countContents(miniSym) == 0) {
				rewarded = true;
				System.out.print("Dungeon cleared! You got 20 gold.\n");
				player.gold += 20;
			}
			action = takeInput();
		} while (true);
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
		moveType = new int[]{1,1,1,1};
		int a = 0;
		int x = insideRoom[0];
		int y = insideRoom[1];
		if (insideRoom[0] == 0) { //edges
			done[a] = 1;
			generateWall(a);
			a++;
		} else if (visitedRooms[x-1][y][2] != null) if (visitedRooms[x-1][y][2].charAt(2) != '1') { //adjacent room
			done[a] = 1;
			generateWall(a);
			a++;
		}
		if (insideRoom[0] == size - 1) {
			done[a] = 2;
			generateWall(a);
			a++;
		} else if (visitedRooms[x+1][y][2] != null) if (visitedRooms[x+1][y][2].charAt(1) != '1') {
			done[a] = 2;
			generateWall(a);
			a++;
		}
		//different side
		if (insideRoom[1] == 0) { //edges
			done[a] = 0;
			generateWall(a);
			a++;
		} else if (visitedRooms[x][y-1][2] != null) if (visitedRooms[x][y-1][2].charAt(3) != '1') { //adjacent room
			done[a] = 0;
			generateWall(a);
			a++;
		}
		if (insideRoom[1] == size - 1) {
			done[a] = 3;
			generateWall(a);
			a++;
		} else if (visitedRooms[x][y+1][2] != null) if (visitedRooms[x][y+1][2].charAt(0) != '1') {
			done[a] = 3;
			generateWall(a);
			a++;
		}
		//make hall
		loc = findPlayer(0);
		if (x > 0) if (visitedRooms[x-1][y][2] != null && loc != 1) if (visitedRooms[x-1][y][2].charAt(2) == '1') {
			done[a] = 1;
			a++;
		}
		if (x < size - 1) if (visitedRooms[x+1][y][2] != null && loc != 2) if (visitedRooms[x+1][y][2].charAt(1) == '1') {
			done[a] = 2;
			a++;
		}
		if (y > 0) if (visitedRooms[x][y-1][2] != null && loc != 0) if (visitedRooms[x][y-1][2].charAt(3) == '1') {
			done[a] = 0;
			a++;
		}
		if (y < size - 1) if (visitedRooms[x][y+1][2] != null && loc != 3) if (visitedRooms[x][y+1][2].charAt(0) == '1') {
			done[a] = 3;
			a++;
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
	 * Generates item drops and enemies in rooms in based on available positions.
	 * <p>
	 * Private method called in the {@link #newRoom()} public method.
	 */
	private void generateContents() {
		for (int i = 0; i < 4; i++) {
			if (c[i] == ' ') {
				if (moveType[i] == 2) {
					if (Math.random() < lootChance) {
						char[] syms = new char[]{lootSym,healSym};
						c[i] = syms[(int)(Math.random()*(syms.length))];
					}
				} else if (moveType[i] == 1) {
					if (Math.random() < minibossChance) c[i] = miniSym;
					else if (Math.random() < enemyChance) c[i] = enemySym;
				}
			}
		}
		if (!portal) if (Math.random() < portalChance) {
			c[4] = portalSym;
			portal = true;
		}
	}

	/**
	 * Updates {@code room} String to be printed with the new walls and contents generated.
	 * <p>
	 * Private method called in the {@link #newRoom()} private method, the {@link #getRoom()} private method and the {@link #enterDungeon(charac)} public method.
	 */
	private void refreshRoom() {
		room = "\n" +
				w[16] + w[16] + w[16] + w[16] + w[12] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[4 ] + w[12] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[8 ] + w[16] + w[16] + w[16] + c[0 ] + w[16] + w[16] + w[16] + w[8 ] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[13] + w[9 ] + w[9 ] + w[9 ] + w[17] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[0 ] + w[17] + w[10] + w[10] + w[10] + w[14] + "\n" +
				w[5 ] + w[16] + w[16] + w[16] + w[1 ] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[2 ] + w[16] + w[16] + w[16] + w[6 ] + "\n" +
				w[5 ] + w[16] + c[1 ] + w[16] + w[1 ] + w[16] + w[16] + w[16] + c[4 ] + w[16] + w[16] + w[16] + w[2 ] + w[16] + c[2 ] + w[16] + w[6 ] + "\n" +
				w[5 ] + w[16] + w[16] + w[16] + w[1 ] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[16] + w[2 ] + w[16] + w[16] + w[16] + w[6 ] + "\n" +
				w[13] + w[9 ] + w[9 ] + w[9 ] + w[17] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[3 ] + w[17] + w[10] + w[10] + w[10] + w[14] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[11] + w[16] + w[16] + w[16] + c[3 ] + w[16] + w[16] + w[16] + w[11] + w[16] + w[16] + w[16] + w[16] + "\n" +
				w[16] + w[16] + w[16] + w[16] + w[15] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[7 ] + w[15] + w[16] + (insideRoom[0]+1) + ", " + (insideRoom[1]+1) + "\n\n";
	}

	/**
	 * Generates a new room in the dungeon, including walls and contents.
	 * <p>
	 * Private method called in the {@link #getRoom} private method.
	 * 
	 * @see {@link #generateRoom()}, {@link #generateContents()}, {@link #refreshRoom()}
	 */
	public void newRoom() {
		generateRoom();
		generateContents();
		refreshRoom();
	}

	/**
	 * Stores location, walls, and remaining contents of a room into an array.
	 * <p>
	 * Private method called in the {@link #enterDungeon(charac)} private method.
	 */
	private void saveRoom() {
		char[] cCopy = c.clone();
		loc = findPlayer(0);
		cCopy[loc] = ' ';
		int x = insideRoom[0];
		int y = insideRoom[1];
		visitedRooms[x][y][0] = "" +
				w[0] +
				w[1] +
				w[2] +
				w[3] +
				w[4] +
				w[5] +
				w[6] +
				w[7] +
				w[8] +
				w[9] +
				w[10] +
				w[11] +
				w[12] +
				w[13] +
				w[14] +
				w[15] +
				w[16] +
				w[17];
		visitedRooms[x][y][1] = "" + cCopy[0] + cCopy[1] + cCopy[2] + cCopy[3] + cCopy[4];
		visitedRooms[x][y][2] = "" + moveType[0] + moveType[1] + moveType[2] + moveType[3];
		visitedRooms[x][y][3] = "" + loc;
		//System.out.println(visitedRooms[insideRoom[0]][insideRoom[1]]);
	}

	/**
	 * Decides whether to make a new room or copy an existing one.
	 * <p>
	 * Private method called in the {@link #enterDungeon(charac)} public method.
	 * 
	 * @see {@link #newRoom()}, {@link #findPlayer(int)}, {@link #refreshRoom()}
	 */
	private void getRoom() {
		w = new char[]{' ',' ',' ',' ',' ',' ',' ',' ','|','-','-','|','|','-','-','|',' ','+'};
		int x = insideRoom[0];
		int y = insideRoom[1];
		if (visitedRooms[x][y][0] == null) {
			newRoom();
		} else {
			w = visitedRooms[x][y][0].toCharArray();
			loc = findPlayer(0);
			c = visitedRooms[x][y][1].toCharArray();
			c[loc] = playerSym;
			//moveType = visitedRooms[x][y][2].chars().map(c -> c-'0').toArray(); //ngl copy pasted this one but I think I get it; it turns a string into an int array UPDATE: doesnt work on older versions aka at school
			for (int i = 0; i < moveType.length; i++) {
				moveType[i] = Character.getNumericValue(visitedRooms[x][y][2].charAt(i));
			}
			refreshRoom();
		}
	}

	/**
	 * Counts number of times a specified {@code char} shows up 
	 * in the contents of all rooms generated so far.
	 * <p>
	 * Private method called in the {@link #takeInput()} private method.
	 * 
	 * @param search - {@code char} to count instances of.
	 * 
	 * @return Number of appearances of {@code search}.
	 */
	private int countContents(char search) {
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (visitedRooms[i][j][1] != null) {
					for (int k = 0; k < 5; k++) { 
						if (visitedRooms[i][j][1].charAt(k) == search) count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * Checks if every hallway is leading to another room.
	 * <p>
	 * Private method called in the {@link #takeInput()} private method.
	 * 
	 * @return {@code true} if the dungeon has no open ends, {@code false} if it does.
	 */
	private boolean checkComplete() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (visitedRooms[i][j][2] != null) {
					for (int k = 0; k < 4; k++) {
						if (visitedRooms[i][j][2].charAt(k) == '1') {
							switch (k) {
							case 0:
								if (visitedRooms[i][j-1][2] == null) return false;
								break;
							case 1:
								if (visitedRooms[i-1][j][2] == null) return false;
								break;
							case 2:
								if (visitedRooms[i+1][j][2] == null) return false;
								break;
							case 3:
								if (visitedRooms[i][j+1][2] == null) return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Finds the location of the player in a room.
	 * <p>
	 * Private method called in the {@link #generateRoom()} private method, the {@link #saveRoom()} private method, 
	 * the {@link #getRoom()} private method and {@link #tryMove(String)} private method.
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
	 * Private method called in the {@link #enterDungeon(charac)} public method.
	 * 
	 * @return {@code String} corresponding to action to be taken.
	 *
	 * @see {@link #listInputs()}, {@link #tryMove(String)}
	 */
	private String takeInput() {
		int r;
		while (true) {
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
					case 3: return "battleN";
					case 4: return "loot";
					case 5: return "heal";
					case 6: return "battleM";
					case 7: return "MbattleN";
					case 8: return "MbattleM";
					}
				} else {
					break;
				}
			case "inventory":
				System.out.print("Health: " + player.currenthealth + "/" + player.maxhealth + "\n");
				System.out.print("Gold: " + player.gold + "\n");
				System.out.print("XP: " + player.xp + " (level " + player.getLevel() + ")\n\n");
				inventory.printInventory();
				inventory.equipMenu(player);
				System.out.print(room);
				break;
			case "details":
				System.out.print("Dungeon status: ");
				if (checkComplete()) {
					if (countContents(enemySym) == 0 && countContents(miniSym) == 0) {
						if (countContents(portalSym) == 0) {
							System.out.print("Conquered\n");
						} else System.out.print("Cleared\n");
					} else System.out.print("Explored\n");
				} else System.out.print("Incomplete\n");
				System.out.print("Discovered monsters remaining: " + countContents(enemySym) + "\n");
				System.out.print("Discovered tough monsters remaining: " + countContents(miniSym) + "\n");
				System.out.print("Discovered portals remaining: " + countContents(portalSym) + "\n\n");
				break;
			case "portal":
				if (c[4] == portalSym) return "portal";
				System.out.print("There is no portal in this room!\n\n");
				break;
			case "leave": return "leave";
			case "save": return "save";
			case "exit": return "exit";
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
	 * @return {@code int} corresponding to movement success and type.
	 *
	 * @see {@link #resetContents()}, {@link #refreshCoords(String)}, {@link #findPlayer(int)}
	 */
	private int tryMove(String direction) {
		int a = 0;
		int b = 3;
		int ret;
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
			ret = 1;
			if (c[a] != enemySym && c[a] != miniSym) {
				switch (a) {
				case 0:
					if (visitedRooms[insideRoom[0]][insideRoom[1]-1][1] != null) {
						if (visitedRooms[insideRoom[0]][insideRoom[1]-1][1].charAt(3) == enemySym) ret = 7;
						if (visitedRooms[insideRoom[0]][insideRoom[1]-1][1].charAt(3) == miniSym) ret = 8;
					}
					break;
				case 1:
					if (visitedRooms[insideRoom[0]-1][insideRoom[1]][1] != null) {
						if (visitedRooms[insideRoom[0]-1][insideRoom[1]][1].charAt(2) == enemySym) ret = 7;
						if (visitedRooms[insideRoom[0]-1][insideRoom[1]][1].charAt(2) == miniSym) ret = 8;
					}
					break;
				case 2:
					if (visitedRooms[insideRoom[0]+1][insideRoom[1]][1] != null) {
						if (visitedRooms[insideRoom[0]+1][insideRoom[1]][1].charAt(1) == enemySym) ret = 7;
						if (visitedRooms[insideRoom[0]+1][insideRoom[1]][1].charAt(1) == miniSym) ret = 8;
					}
					break;
				case 3:
					if (visitedRooms[insideRoom[0]][insideRoom[1]+1][1] != null) {
						if (visitedRooms[insideRoom[0]][insideRoom[1]+1][1].charAt(0) == enemySym) ret = 7;
						if (visitedRooms[insideRoom[0]][insideRoom[1]+1][1].charAt(0) == miniSym) ret = 8;
					}
				}
				resetContents();
				c[b] = playerSym;
				refreshCoords(direction);
				return ret;
			}
		case 2:
			ret = 2;
			if (c[a] == ' ' || c[a] == playerSym);
			else if (c[a] == enemySym) ret = 3;
			else if (c[a] == lootSym) ret = 4;
			else if (c[a] == healSym) ret = 5;
			else if (c[a] == miniSym) ret = 6;
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
	 */
	private void resetContents() {
		c = new char[]{' ',' ',' ',' ',' '};
	}

	/**
	 * Prints stats out that the player might want to see to track score upon death.
	 * <p>
	 * Private method called in the {@link #enterDungeon(charac)} public method.
	 * 
	 * @see {@link #getLevel(int)}
	 */
	private void deathStats() {
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
		//inventory?
	}

	/**
	 * Will check if player has levelled up and modify stats accordingly.
	 * <p>
	 * Private method called in the {@link #enterDungeon(charac, Inv)} public method.
	 */
	private void levelUp() {
		if (player.getLevel() != lastLevel) {
			lastLevel = player.getLevel();
			player.maxhealth += 5;
			player.currenthealth =+ 5;
			player.attack += 1;
			System.out.print("> You levelled up to level " + lastLevel + "!\n"
					+ "> Your health and attack strength have increased.\n\n");
		}
	}

	/**
	 * Lists all valid inputs for {@link #takeInput()}.
	 * <p>
	 * Private method called in the {@link #takeInput()} private method. 
	 */
	private void listInputs() {
		System.out.print("" +
				"\"list\" - list all acceptable inputs" + "\n" +
				"\"w\" - move up" + "\n" +
				"\"a\" - move left" + "\n" +
				"\"s\" - move down" + "\n" +
				"\"d\" - move right" + "\n" +
				"\"portal\" - enter portal if it is in the room (does nothing)" + "\n" +
				"\"inventory\" - access inventory" + "\n" +
				"\"details\" - lists information about dungeon progression" + "\n" +
				"\"leave\" - leave dungeon and return to surface, DOES NOT SAVE" + "\n" +
				"\"save\" - saves dungeon progress and player stats to file" + "\n" +
				"\"exit\" - exits game, DOES NOT SAVE" + "\n" +
				"\n"
				);
	}

}
