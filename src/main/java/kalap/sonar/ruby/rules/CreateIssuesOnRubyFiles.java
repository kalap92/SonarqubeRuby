package kalap.sonar.ruby.rules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import kalap.sonar.ruby.RubocopAnalyzedFile;
import kalap.sonar.ruby.RubocopIssue;

public class CreateIssuesOnRubyFiles implements Sensor {

	private static final double ARBITRARY_GAP = 2.0;
	private static final int LINE_1 = 1;
	private final static String JSON_URL = "https://s3-us-west-2.amazonaws.com/kalap/data.json";
	final Logger LOGGER = Loggers.get(CreateIssuesOnRubyFiles.class);

	public static String getHTML(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	public static JSONArray getRubocopJson() {
		try {
			String jsonOutput = getHTML(JSON_URL);
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(jsonOutput);
			return (JSONArray) json.get("files");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Generated rules from rubocop");
		descriptor.onlyOnLanguage("ruby");
		// descriptor.createIssuesForRuleRepositories(RubyRulesDefinition.REPOSITORY);
	}

	public HashMap<String, ArrayList<RubocopIssue>> getIssuesPerFileMap() {
		HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap = new HashMap<String, ArrayList<RubocopIssue>>();
		JSONArray rubocopJson = getRubocopJson();
		for (int i = 0; i < rubocopJson.size(); i++) {
			JSONObject fileJson = (JSONObject) rubocopJson.get(i);
			String path = (String) fileJson.get("path");
			JSONArray offenses = (JSONArray) fileJson.get("offenses");
			RubocopAnalyzedFile rubocopAnalyzedFile = new RubocopAnalyzedFile(path, offenses);
			issuesPerFileMap.put(rubocopAnalyzedFile.getPath(), rubocopAnalyzedFile.getIssuesList());
		}
		return issuesPerFileMap;
	}

	public void execute(SensorContext context) {
		FileSystem fs = context.fileSystem();
		Iterable<InputFile> rubyFiles = fs.inputFiles(fs.predicates().hasLanguage("ruby"));
		HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap = getIssuesPerFileMap();
		
		for (InputFile rubyFile : rubyFiles) {
			LOGGER.info(rubyFile.toString());
			
			ArrayList<RubocopIssue> issues = issuesPerFileMap.get(rubyFile.toString());
			
			for(RubocopIssue rubocopIssue : issues) {			
				int lineNumber = rubocopIssue.getLine().intValue(); 
				String message = rubocopIssue.getMessage(); 

				// no need to define the severity as it is automatically set according
				// to the configured Quality profile
				NewIssue newIssue = context.newIssue().forRule(RubyRulesDefinition.RULE_ON_LINE_1)
						// gap is used to estimate the remediation cost to fix the debt
						.gap(ARBITRARY_GAP);

				NewIssueLocation primaryLocation = newIssue.newLocation().on(rubyFile).at(rubyFile.selectLine(lineNumber))
						.message(message);
				newIssue.at(primaryLocation);
				newIssue.save();
			}

		}
	}

}
