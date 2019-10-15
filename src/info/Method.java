package info;

import abs.frontend.ast.MethodImpl;

public class Method {
	
	private String name;

	public Method(MethodImpl methodImpl) {
		String full = methodImpl.value.toString();
		name = full.substring(full.indexOf(" "),full.indexOf(","));
		System.out.println(methodImpl.value);
	}
}
