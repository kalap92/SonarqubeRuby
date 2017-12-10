package kalap.sonar.ruby.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

public final class RubyRulesDefinition implements RulesDefinition {

	public static final String REPOSITORY = "ruby-example";
	public static final String RUBY_LANGUAGE = "ruby";
	public static final RuleKey RULE_ON_LINE_1 = RuleKey.of(REPOSITORY, "line1");

	public void define(Context context) {
		NewRepository repository = context.createRepository(REPOSITORY, RUBY_LANGUAGE)
				.setName("My Custom Ruby Analyzer");

		NewRule x1Rule = repository.createRule(RULE_ON_LINE_1.rule()).setName("Stupid rule")
				.setHtmlDescription("Generates an issue on every line 1 of Ruby files")
				.setTags("style", "stupid")
				.setSeverity(Severity.MINOR);

		x1Rule.setDebtRemediationFunction(x1Rule.debtRemediationFunctions().linearWithOffset("1h", "30min"));
		repository.done();
	}
}
