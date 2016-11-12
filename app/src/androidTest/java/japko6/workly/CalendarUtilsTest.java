package japko6.workly;

import junit.framework.Assert;
import junit.framework.TestCase;

import japko6.workly.objects.Time;
import japko6.workly.utils.CalendarUtils;


public class CalendarUtilsTest extends TestCase {

    public void testIsLeapYear() {
        Assert.assertEquals(true, CalendarUtils.isLeapYear(2016));
    }

    public void testGetActualMonthLength() {
        assertEquals(30, CalendarUtils.getActualMonthLength());
    }

    public void testGetWorkingTime() {
        Time expected1 = new Time(8, 0);
        Time expected2 = new Time(7, 50);
        Time expected3 = new Time(0, 50);
        Time expected4 = new Time(8, 20);

        Time actual = CalendarUtils.getWorkingTime(9, 0, 17, 0);
        assertEquals("case 0", expected1.getHour(), actual.getHour());
        assertEquals("case 0", expected1.getMinute(), actual.getMinute());

        actual = CalendarUtils.getWorkingTime(9, 0, 16, 50);
        assertEquals("case 1", expected2.getHour(), actual.getHour());
        assertEquals("case 1", expected2.getMinute(), actual.getMinute());

        actual = CalendarUtils.getWorkingTime(9, 0, 9, 50);
        assertEquals("case 2", expected3.getHour(), actual.getHour());
        assertEquals("case 2", expected3.getMinute(), actual.getMinute());

        actual = CalendarUtils.getWorkingTime(8, 40, 9, 30);
        assertEquals("case 3", expected3.getHour(), actual.getHour());
        assertEquals("case 3", expected3.getMinute(), actual.getMinute());

        actual = CalendarUtils.getWorkingTime(8, 40, 17, 0);
        assertEquals("case 4", expected4.getHour(), actual.getHour());
        assertEquals("case 4", expected4.getMinute(), actual.getMinute());
    }

    /**
     * in expected time, chage value "hour" to actual hour
     * method is checking if it is working 24 h clock
     */
    public void testGetHour() {
        Time expected = new Time(17, 0);
        Time actualTime = new Time();

        assertEquals("ASSERTING HOURS\n\n\nEXPECTED:\n\t"
                        + String.valueOf(expected.getHour())
                        + " : "
                        + String.valueOf(expected.getMinute())
                        + "\n\nACTUAL:\n\t"
                        + String.valueOf(actualTime.getHour())
                        + " : "
                        + String.valueOf(actualTime.getMinute()) + "\n\n",
                expected.getHour(), actualTime.getHour());
    }
}