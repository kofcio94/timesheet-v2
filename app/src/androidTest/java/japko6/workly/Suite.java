package japko6.workly;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Suite extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(CalendarUtilsTest.class);
        return suite;
    }
}
