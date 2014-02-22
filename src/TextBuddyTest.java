import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;

/**
 * @author Hao Eng
 * 
 */

public class TextBuddyTest {
	static String filename = "output.txt";

	/**
	 * Test method for {@link TextBuddy#getCount()}.
	 */
	@Test
	public void testGetCount() {
		assertEquals(0, TextBuddy.getCount());
	}

	@Test
	public void testSearch() throws FileNotFoundException {
		assertEquals("search", TextBuddy.SEARCH);
		assertNotSame("SEARCH", TextBuddy.SEARCH);

		ByteArrayOutputStream sink = new ByteArrayOutputStream();
		System.setOut(new PrintStream(sink, true));
		TextBuddy.checkWordAssignAction(filename, "search xxx");
		assertNotSame("No such word in the file.",
				new String(sink.toByteArray()));
	}

	@Test
	public void testSort() {
		assertEquals("sort", TextBuddy.SORT);
		assertNotSame("SORT", TextBuddy.SORT);
	}
}
