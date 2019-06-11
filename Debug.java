public class Debug {

	public static void main(String[] args) {

		//inventory();
		//dungeon();
		//doWall();
		fighting();
		//game();

	}
	
	public static void game() {
	Game g = new Game();
	}
	
	/*public static void inventory() {
		Inv test =  new Inv();
		test.place(0, 1);
		test.place(1, 2);
		//test.showInv();
		test.place(0, 2);
		//test.showInv();
		
	}*/

	/*public static void dungeon() { //OUTDATED, WILL NO LONGER WORK
		int s = 8;
		
		charac player = new charac(20,3,0.2,0);

		Dungeon d = new Dungeon(s, 0.7, 0.4, 0.1, 0.25, 0.25); //decent setup for basic dungeon according to individual tests: n, 0.7, 0.4, 0.1, 0.25, 0.25
		
		d.enterDungeon(player);
	}*/

	public static void doWall() { //NOT DONE THIS WAY IN THE DUNGEON CLASS!! THIS IS ONLY A TEST
			double r = Math.random();
			System.out.print(r + ", " + (r * (1 - 1 * 0.2)) + ", " + (r * (1 - 2 * 0.2)));
	}
	
	public static void fighting() {
		charac player =  new charac(60,4,0.15,2,9,10);
		
//		Dungeon d = new Dungeon(8,0.7,0.4,0.1,0.25,0.25);
//		d.enterDungeon(player);
	//	System.out.print(player.gold);
		Monster SB = new Monster(60,3,0.1,0,10,10,"skele");
		Inventory i= new Inventory(8);
		i.newItem(3,3);
		i.newItem(3,1);
		i.newItem(2,4);
	//	System.out.print(SB.name);
		
//		Monster.PrintBoos();
		Battle b = new Battle(player,SB,i);
		
	}
	
}
