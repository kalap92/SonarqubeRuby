package kalap.sonar.ruby;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.config.Configuration;

import kalap.sonar.ruby.rules.RubyFilesIssueCreator;

public class RubyFilesIssueCreatorTest {

	@Spy
	RubyFilesIssueCreator rubyFilesIssueCreator;

	Configuration config = mock(Configuration.class);
	SensorContext sensorContext = mock(SensorContext.class);
	FileSystem fileSystem = mock(FileSystem.class);
	FilePredicates filePredictates = mock(FilePredicates.class);
	HashMap<String, ArrayList<RubocopIssue>> issuesPerFileMap = mock(HashMap.class);
	List<InputFile> rubyFiles = new ArrayList<InputFile>();

	@Before
	public void setUp() {
		rubyFilesIssueCreator = new RubyFilesIssueCreator(config);
		rubyFilesIssueCreator = spy(rubyFilesIssueCreator);
	}

	@Test
	public void testExecuteNoUrlGiven() {
		Optional<String> myString = Optional.empty();
		when(config.get("sonar.ruby.rubocop.url")).thenReturn(myString);

		rubyFilesIssueCreator.execute(sensorContext);

		verify(rubyFilesIssueCreator, times(0)).createRubyIssues(eq(rubyFiles), eq(issuesPerFileMap),
				eq(sensorContext));
	}

	@Test
	public void testExecuteEmptyIssuesPerFileMap() {
		Optional<String> myString = Optional.of("sth");
		when(config.get("sonar.ruby.rubocop.url")).thenReturn(myString);
		when(sensorContext.fileSystem()).thenReturn(fileSystem);
		when(fileSystem.predicates()).thenReturn(filePredictates);
		when(filePredictates.hasLanguage("ruby")).thenReturn(null);

		rubyFilesIssueCreator.execute(sensorContext);

		verify(rubyFilesIssueCreator, times(0)).createRubyIssues(eq(rubyFiles), eq(issuesPerFileMap),
				eq(sensorContext));
	}

	@Test
	public void testExecuteCreateRubyIssues() {
		Optional<String> myString = Optional.of("sth");
		when(config.get("sonar.ruby.rubocop.url")).thenReturn(myString);
		when(sensorContext.fileSystem()).thenReturn(fileSystem);
		when(fileSystem.predicates()).thenReturn(filePredictates);
		when(filePredictates.hasLanguage("ruby")).thenReturn(null);
		when(fileSystem.inputFiles(filePredictates.hasLanguage("ruby"))).thenReturn(rubyFiles);

		when(rubyFilesIssueCreator.getIssuesPerFileMap("sth")).thenReturn(issuesPerFileMap);

		rubyFilesIssueCreator.execute(sensorContext);
		verify(rubyFilesIssueCreator, times(1)).createRubyIssues(eq(rubyFiles), eq(issuesPerFileMap),
				eq(sensorContext));
	}
}
