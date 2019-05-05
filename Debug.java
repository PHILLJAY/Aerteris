public class Debug {

	public static void main(String[] args) {

		//inventory();
		//dungeon();
		//doWall();

	}
	public static void inventory() {
		Inv test =  new Inv();
		test.place(0, 1);
		test.place(1, 2);
		test.showInv();
		test.place(0, 2);
		test.showInv();
		
	}

	public static void dungeon() {
		int s = 8;

		Dungeon d = new Dungeon(s, 0.7, 0.4, 0.1, false); //decent setup for basic dungeon according to individual tests: n, 0.7, 0.4, 0.1
		
		d.enterDungeon();
	}

	public static void doWall() { //NOT DONE THIS WAY IN THE DUNGEON CLASS!! THIS IS ONLY A TEST
			double r = Math.random();
			System.out.print(r + ", " + (r * (1 - 1 * 0.2)) + ", " + (r * (1 - 2 * 0.2)));
	}
	
}
