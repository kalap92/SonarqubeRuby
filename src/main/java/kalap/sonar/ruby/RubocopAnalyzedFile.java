package kalap.sonar.ruby;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import kalap.sonar.ruby.rules.CreateIssuesOnRubyFiles;

public class RubocopAnalyzedFile {
	private ArrayList<RubocopIssue> issuesList = new ArrayList<RubocopIssue>();
	private String path;
	final Logger LOGGER = Loggers.get(CreateIssuesOnRubyFiles.class);

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

	public ArrayList<RubocopIssue> getIssuesList() {
		return issuesList;
	}
}
