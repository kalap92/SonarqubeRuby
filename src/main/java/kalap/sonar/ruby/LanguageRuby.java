package kalap.sonar.ruby;

import org.sonar.api.internal.apachecommons.lang.ArrayUtils;
import org.sonar.api.internal.apachecommons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;

public class LanguageRuby extends AbstractLanguage {

  public static final LanguageRuby INSTANCE = new LanguageRuby("ruby");

    public LanguageRuby(String key) {
    super(key);
  }


    /**
     * Java key
     */
    public static final String KEY = "ruby";

    /**
     * Java name
     */
    public static final String NAME = "Ruby";

    /**
     * Default package name for classes without package def
     */
    public static final String DEFAULT_PACKAGE_NAME = "[default]";

    /**
     * Java files knows suffixes
     */
    public static final String[] SUFFIXES = {".rb", ".ruby"};

    /**
     * Default constructor
     */
    public LanguageRuby() {
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

    public static boolean isRubyFile(java.io.File file) {
      String suffix = "." + StringUtils.lowerCase(StringUtils.substringAfterLast(file.getName(), "."));
      return ArrayUtils.contains(SUFFIXES, suffix);
    }
    
}