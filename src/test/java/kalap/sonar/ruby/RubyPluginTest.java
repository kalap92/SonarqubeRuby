package kalap.sonar.ruby;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

public class RubyPluginTest {

	@Test
	public void testGetExtensions() {
		SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(6, 7), SonarQubeSide.SERVER);
		Plugin.Context context = new Plugin.Context(runtime);
		new RubyPlugin().define(context);
		assertThat(context.getExtensions()).hasSize(5);
	}
}
