package kalap.sonar.ruby;

import org.json.simple.JSONObject;

public class RubocopIssue {
	private String severity;
	private String message;
	private Long line;
	private Long column;
	
	public RubocopIssue(JSONObject issue) {
		setSeverity(issue.get("severity").toString());
		setMessage(issue.get("message").toString());

		JSONObject location = (JSONObject) issue.get("location");
		setLine((Long) location.get("line"));
		setColumn((Long) location.get("column"));
	}
	
	public String toString() {
		return "message: " + message + " severity: " + severity + " line: " + line + " column: " + column;	
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

	public Long getLine() {
		return line;
	}

	private void setLine(Long long1) {
		this.line = long1;
	}

	public Long getColumn() {
		return column;
	}

	private void setColumn(Long long1) {
		this.column = long1;
	}
}
