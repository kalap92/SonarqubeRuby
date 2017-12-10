package kalap.sonar.ruby;

import org.json.simple.JSONObject;

public class RubocopIssue {
	private String severity;
	private String message;
	private int line;
	private int column;
	
	public RubocopIssue(JSONObject issue) {
		setSeverity(issue.get("severity").toString());
		setMessage(issue.get("severity").toString());
		setLine((Integer) issue.get("line"));
		setColumn((Integer) issue.get("column"));
	}

	public String getSeverity() {
		return severity;
	}

	private void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public int getLine() {
		return line;
	}

	private void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	private void setColumn(int column) {
		this.column = column;
	}
}
