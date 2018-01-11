package kalap.sonar.ruby.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;

public final class RubyRulesDefinition implements RulesDefinition {

	public static final String REPOSITORY = "ruby-example";
	public static final String RUBY_LANGUAGE = "ruby";
	public static final RuleKey GLOBAL_RULE = RuleKey.of(REPOSITORY, "globalRule");

	public void define(Context context) {
		NewRepository repository = context.createRepository(REPOSITORY, RUBY_LANGUAGE)
				.setName("My Custom Ruby Analyzer");

		NewRule x1Rule = repository.createRule(GLOBAL_RULE.rule()).setName("Global Rule")
				.setHtmlDescription("To be done...Soon")
				.setSeverity(Severity.BLOCKER);

		x1Rule.setDebtRemediationFunction(x1Rule.debtRemediationFunctions().linearWithOffset("1h", "30min"));
		repository.done();
	}
}
