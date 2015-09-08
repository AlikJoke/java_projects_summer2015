package mysummerproject;


public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		Frame frame = new Frame();
		frame.show();
		
		DataBase dataBase = new DataBase();
		dataBase.actionWithDownloadList();
	}
}
