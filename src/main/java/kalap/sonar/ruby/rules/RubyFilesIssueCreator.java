package kalap.sonar.ruby.rules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import kalap.sonar.ruby.RubocopAnalyzedFile;
import kalap.sonar.ruby.RubocopIssue;

public class RubyFilesIssueCreator implements Sensor {

	protected final Configuration config;
	private static final double ARBITRARY_GAP = 2.0;
	final static Logger LOGGER = Loggers.get(RubyFilesIssueCreator.class);
	protected static final String URL_JSON_DATA = "sonar.ruby.rubocop.url";

	public String jsonDataKey() {
		return URL_JSON_DATA;
	}

	public String getJsonDataUrl() {
		Optional<String> o = config.get(jsonDataKey());
		if (o.isPresent()) {
			return o.get();
		}
		return null;
	}

	public RubyFilesIssueCreator(final Configuration config) {
		this.config = config;
	}

	public static String getRubocopJsonData(String urlToRead) {
		try {
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
		} catch (Exception e) {
			LOGGER.error("Something went wrong with downloading data json from given url: " + urlToRead
					+ " - Cancelling Ruby analysis");
			return null;
		}
	}

	public static JSONArray getRubocopJson(String jsonDataUrl) {
		try {
			String jsonOutput = getRubocopJsonData(jsonDataUrl);
			if (jsonOutput == null) {
				return null;
			}
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(jsonOutput);
			return (JSONArray) json.get("files");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Generated rules from rubocop");
		descriptor.onlyOnLanguage("ruby");
	}

	public HashMap<String, ArrayList<RubocopIssue>> getIssuesPerFileMap(String jsonDataUrl) {
		HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap = new HashMap<String, ArrayList<RubocopIssue>>();
		JSONArray rubocopJson = getRubocopJson(jsonDataUrl);
		if (rubocopJson != null) {
			for (int i = 0; i < rubocopJson.size(); i++) {
				JSONObject fileJson = (JSONObject) rubocopJson.get(i);
				String path = (String) fileJson.get("path");
				JSONArray offenses = (JSONArray) fileJson.get("offenses");
				RubocopAnalyzedFile rubocopAnalyzedFile = new RubocopAnalyzedFile(path, offenses);
				issuesPerFileMap.put(rubocopAnalyzedFile.getPath(), rubocopAnalyzedFile.getIssuesList());
			}
			return issuesPerFileMap;
		}
		return null;
	}

	public void createRubyIssues(Iterable<InputFile> rubyFiles,
			HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap, SensorContext context) {
		for (InputFile rubyFile : rubyFiles) {
			LOGGER.info(rubyFile.toString());

			ArrayList<RubocopIssue> issues = issuesPerFileMap.get(rubyFile.toString());

			if (issues != null) {
				for (RubocopIssue rubocopIssue : issues) {
					int lineNumber = rubocopIssue.getLine().intValue();
					String message = rubocopIssue.getMessage();

					NewIssue newIssue = context.newIssue().forRule(RubyRulesDefinition.GLOBAL_RULE)
							.gap(ARBITRARY_GAP);

					NewIssueLocation primaryLocation = newIssue.newLocation().on(rubyFile)
							.at(rubyFile.selectLine(lineNumber)).message(message);
					newIssue.at(primaryLocation);
					newIssue.save();
				}
			}
		}
	}

	public void execute(SensorContext context) {
		String jsonDataUrl = getJsonDataUrl();
		if (jsonDataUrl == null) {
			LOGGER.error(jsonDataKey() + " must be set. Cancelling Ruby analysis");
			return;
		}

		FileSystem fs = context.fileSystem();
		Iterable<InputFile> rubyFiles = fs.inputFiles(fs.predicates().hasLanguage("ruby"));
		HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap = getIssuesPerFileMap(jsonDataUrl);
		if (issuesPerFileMap == null) {
			return;
		}

		createRubyIssues(rubyFiles, issuesPerFileMap, context);
	}
}
