package kalap.sonar.ruby;

import org.sonar.api.internal.apachecommons.lang.ArrayUtils;
import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;

public class RubyLanguage extends AbstractLanguage {
	public static final String KEY = "ruby";
	public static final String NAME = "Ruby";
	public static final String DEFAULT_PACKAGE_NAME = "[default]";
	public static final String[] SUFFIXES = { ".rb", ".ruby" };

	public RubyLanguage(String key) {
		super(key);
	}

	public RubyLanguage() {
		super(KEY, NAME);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see AbstractLanguage#getFileSuffixes()
	 */
	public String[] getFileSuffixes() {
		return SUFFIXES;
	}

	public boolean isRubyFile(java.io.File file) {
		String suffix = "." + StringUtils.lowerCase(StringUtils.substringAfterLast(file.getName(), "."));
		return ArrayUtils.contains(SUFFIXES, suffix);
	}
}