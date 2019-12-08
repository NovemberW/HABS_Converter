package core;

public class Starter {

	public static void main(String[] args) {
		Flywheel2 fw = new Flywheel2();

		String[] args2 = new String[] 
				{ "starter", "-f", "./exampleFiles/bbNew.abs",
						"-s","b1","-i", "x &gt;= 0.0"};
		fw.work(args2);
	}
}
