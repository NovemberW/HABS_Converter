package core;

import abs.frontend.ast.DifferentialExp;
import util.StringTools;

public class Starter {

	public static void main(String[] args) {
		Flywheel2 fw = new Flywheel2();
//		String[] args2 = new String[]{"starter","-f","./exampleFiles/BouncingBall.abs"};

		String[] args2 = new String[] { "starter", "-f", "./exampleFiles/Verkehrssimulation.abs" };
		fw.work(args2);

//		Flywheel f = new Flywheel();

//		args2 = new String[]{"starter","-print","-f","./exampleFiles/BouncingBall.abs"};
//		f.work(args2);
//		f.extract();

	}
}
