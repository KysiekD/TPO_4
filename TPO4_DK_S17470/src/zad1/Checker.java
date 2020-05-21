package zad1;

public class Checker {
	private static int counter=0;
	
	public Checker() {
		
	}
	
	public static void check(String text) {
		counter = counter + 1;
		System.out.println("=========="+text+counter);
		
	}
}
