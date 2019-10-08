package info;

import java.util.LinkedList;
import java.util.List;

public class AwaitGroups {
	
	private String await;
	
	private java.util.List<String> lines = new LinkedList<String>();
	
	public AwaitGroups(String await, List<String> lines) {
		this.await = await;
		this.lines = lines;
	}	
	
	public String getAwait() {
		return await;
	}

	public void setAwait(String await) {
		this.await = await;
	}

	public java.util.List<String> getLines() {
		return lines;
	}

	public void setLines(java.util.List<String> lines) {
		this.lines = lines;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(await);
		sb.append("\n");
		
		for(String line : lines) {
			sb.append("\t");
			sb.append(line);
			sb.append("\n");
		}
		
		return sb.toString();
	}

}