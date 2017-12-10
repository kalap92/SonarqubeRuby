package kalap.sonar.ruby;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RubocopAnalyzedFile {
	private ArrayList<RubocopIssue> issuesList;
	private String path;

	public RubocopAnalyzedFile(String path, JSONArray offenses) {
		for (int i = 0; i < offenses.size(); i++) {
			JSONObject issue = (JSONObject) offenses.get(i);
			RubocopIssue rubocopIssue = new RubocopIssue(issue);
			issuesList.add(rubocopIssue);
		}

		setPath(path);
	}

	public String getPath() {
		return path;
	}

	private void setPath(String path) {
		this.path = path;
	}

	public ArrayList<RubocopIssue> getFilesPerIssues() {
		return issuesList;
	}
}
