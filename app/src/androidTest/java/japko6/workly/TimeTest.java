package japko6.workly;

import junit.framework.TestCase;

import japko6.workly.objects.Time;

public class TimeTest extends TestCase {
    public void testTimeAdd() {
        Time expected = new Time(2, 0);

        Time actual = new Time(0, 35);
        Time addingTime = new Time(1, 25);
        actual.addTime(addingTime);

        assertEquals(expected.getHour(), actual.getHour());
        assertEquals(expected.getMinute(), actual.getMinute());
    }
}
