package core;

public class Starter {

	public static void main(String[] args) {
		Flywheel2 fw = new Flywheel2();

		String[] args2 = new String[] { "starter", "-f", "./exampleFiles/SimpleBouncingBall.abs","-s","secondAuto"};
		fw.work(args2);
	}
}
