package kalap.sonar.ruby;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class RubyLanguageTest {
	File rubyFileOne = mock(File.class);
	File rubyFileTwo = mock(File.class);
	File notRubyFile = mock(File.class);

	RubyLanguage language = new RubyLanguage("ruby");

	@Before
	public void setUp() {
		when(rubyFileOne.getName()).thenReturn("test.rb");
		when(rubyFileTwo.getName()).thenReturn("test.ruby");
		when(notRubyFile.getName()).thenReturn("test.java");
	}

	@Test
	public void testConfig() {
		assertThat(language.getKey()).isEqualTo("ruby");
		assertThat(language.getName()).isEqualTo("ruby");
		assertThat(language.getFileSuffixes()).hasSize(2).contains(".ruby").contains(".rb");
	}

	@Test
	public void testIsRubyFileTrue() {
		assertEquals(language.isRubyFile(rubyFileOne), true);
		assertEquals(language.isRubyFile(rubyFileTwo), true);
		assertEquals(language.isRubyFile(notRubyFile), false);
	}
}
