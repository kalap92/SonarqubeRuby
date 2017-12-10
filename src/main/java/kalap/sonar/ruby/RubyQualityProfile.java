package kalap.sonar.ruby;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import static kalap.sonar.ruby.rules.RubyRulesDefinition.REPOSITORY;

public class RubyQualityProfile implements BuiltInQualityProfilesDefinition {

	public void define(Context context) {
		NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("Ruby Rules", LanguageRuby.KEY);
		profile.setDefault(true);

		NewBuiltInActiveRule rule1 = profile.activateRule(REPOSITORY, "line1");
		rule1.overrideSeverity("BLOCKER");

		profile.done();
	}

}
