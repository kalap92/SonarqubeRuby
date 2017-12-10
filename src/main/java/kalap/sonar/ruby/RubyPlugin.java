package kalap.sonar.ruby;

import org.sonar.api.Plugin;

import kalap.sonar.ruby.rules.CreateIssuesOnRubyFiles;
import kalap.sonar.ruby.rules.RubyRulesDefinition;

public class RubyPlugin implements Plugin {
	public void define(Context context) {
		context.addExtensions(CreateIssuesOnRubyFiles.class, RubyRulesDefinition.class);
		context.addExtensions(LanguageRuby.class, RubyQualityProfile.class);
	}
}