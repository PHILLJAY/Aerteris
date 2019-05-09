public class Debug {

	public static void main(String[] args) {

		//inventory();
		dungeon();
		//doWall();
		//fighting();

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
		charac player = new charac(20,3,0.2,0);

		Dungeon d = new Dungeon(s, 0.7, 0.4, 0.1, 0.25, 0.25); //decent setup for basic dungeon according to individual tests: n, 0.7, 0.4, 0.1, 0.25, 0.25
		
		d.enterDungeon(player);
	}

	public static void doWall() { //NOT DONE THIS WAY IN THE DUNGEON CLASS!! THIS IS ONLY A TEST
			double r = Math.random();
			System.out.print(r + ", " + (r * (1 - 1 * 0.2)) + ", " + (r * (1 - 2 * 0.2)));
	}
	
	public static void fighting() {
		charac player = new charac(40,7,0.2,0);
		Monster monster = new Monster(
				(int)((player.maxhealth/2)+Math.floor(Math.random()*(player.maxhealth/2))-(player.maxhealth/4)), 
				(int)((player.attack/2)+Math.floor(Math.random()*(player.attack/2))-(player.attack/4)), 
				(Math.random()/4), 
				(int)((player.defense/2)+Math.floor(Math.random()*(player.defense/2))-(player.defense/4)), 
				"skele"
				);
		Battle b = new Battle(player, monster);
	}
	
}
