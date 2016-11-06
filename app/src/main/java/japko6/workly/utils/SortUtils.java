package japko6.workly.utils;


import japko6.workly.objects.DateKey;

public class SortUtils {

    public static boolean isOneLowerAndTwoHigherThanThree(DateKey one, DateKey two, DateKey three) throws Exception {
        if (isLeftGreaterThanRight(three, one) && isLeftGreaterThanRight(two, three)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * If equals throws exception
     *
     * @param left
     * @param right
     * @return
     */
    public static boolean isLeftGreaterThanRight(DateKey left, DateKey right) throws Exception {
        if (left.getYear() > right.getYear()) {
            return true;
        } else if (left.getYear() < right.getYear()) {
            return false;
        } else {
            if (left.getMonth() > right.getMonth()) {
                return true;
            } else if (left.getMonth() < right.getMonth()) {
                return false;
            } else {
                if (left.getDay() > right.getDay()) {
                    return true;
                } else if (left.getDay() < right.getDay()) {
                    return false;
                } else {
                    throw new Exception("EQUALS!!!");
                }
            }
        }
    }
}
