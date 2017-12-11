package kalap.sonar.ruby;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import kalap.sonar.ruby.rules.CreateIssuesOnRubyFiles;
import kalap.sonar.ruby.rules.RubyRulesDefinition;

public class RubyPlugin implements Plugin {
	public void define(Context context) {
		context.addExtension(PropertyDefinition.builder("sonar.ruby.rubocop.url")
		        .name("URL to rubocop report json")
		        .description("URL to report in json form produced by rubocop on previously analyzed project")
		        .category("Ruby")
		        .subCategory("General")
		        .onQualifiers(Qualifiers.PROJECT)
		        .defaultValue(null)
		        .build());
		context.addExtensions(CreateIssuesOnRubyFiles.class, RubyRulesDefinition.class);
		context.addExtensions(RubyLanguage.class, RubyQualityProfile.class);
	}
}