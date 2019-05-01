public class Debug {

	public static void main(String[] args) {
		
    //DUNGEON.JAVA
		Dungeon d = new Dungeon(8, 0.9, 0.4, false);
		
		d.newRoom();
		System.out.print(d.room);
		d.printGeneralInfo();

	}

}
