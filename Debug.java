public class Debug {

	public static void main(String[] args) {

		dungeon();
		//doWall();

	}

	public static void dungeon() {
		int s = 8;

		Dungeon d = new Dungeon(s, 0.7, 0.4, 0.1, false); //decent setup for basic dungeon according to individual tests: n, 0.7, 0.4, 0.1

		//d.insideRoom = new int[]{s-1,0};
		d.newRoom();
		System.out.print(d.room);
		d.printGeneralInfo();
	}

	public static void doWall() { //NOT DONE THIS WAY IN THE DUNGEON CLASS!! THIS IS ONLY A TEST
			double r = Math.random();
			System.out.print(r + ", " + (r * (1 - 1 * 0.2)) + ", " + (r * (1 - 2 * 0.2)));
	}
	
}
