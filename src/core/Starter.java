package core;


public class Starter {

	
	public static void main(String[] args) {
		System.out.println("Dummy");
		Flywheel fw = new Flywheel();
		String[] args2 = new String[]{"starter","-f","./exampleFiles/BouncingBall.abs"};
		fw.work(args2);
//		fw.extract();
	}
}
