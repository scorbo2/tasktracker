package ca.corbett.tasktracker.date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Objects;

/**
 * This is kind of dumb and feels a bit hacky, but for certain fields here (project start/end date,
 * version release date, etc), I don't care at all about time, I just want a pure date, and preferably
 * in a predictable, user-presentable format. So, here's a class that wraps a java.util.Date instance
 * and treats it rather strictly as a yyyy-MM-dd string value. And yeah, I have a particular affinity
 * for yyyy-MM-dd, so this class by design rejects literally all other date formats.
 *
 * @author scorbo2
 */
public final class YMDDate implements Comparator<YMDDate> {

    @JsonIgnore
    private java.util.Date _date;

    @JsonIgnore
    private final SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates a new YMDDate with a default value of today.
     */
    public YMDDate() {
        _date = new java.util.Date();
    }

    /**
     * Creates a new YMDDate with the specified millisecond value, similar
     * to the constructor for Date.
     *
     * @param ms Number of milliseconds since Jan 1 1970
     */
    public YMDDate(long ms) {
        _date = new java.util.Date(ms);
    }

    /**
     * Creates a new YMDDate with the specified Date value.
     *
     * @param date the Date value to use.
     */
    public YMDDate(java.util.Date date) {
        _date = date;
    }

    /**
     * Creates a new YMDDate with the specified value. Format is yyyy-MM-dd always. If your format is
     * literally anything else, the date will be unset.
     *
     * @param src A string in the format yyyy-MM-dd.
     */
    @JsonCreator
    public YMDDate(String src) {
        setDate(src);
    }

    /**
     * Creates a new YMDDate based on the given source date. If the given date is null or unset, the
     * resulting date will be unset. Otherwise, the new date will represent the same date as the given
     * date.
     *
     * @param other The source date from which to copy.
     */
    public YMDDate(YMDDate other) {
        if (other != null && other.isValid()) {
            this._date = other._date;
        }
    }

    /**
     * Unsets the current date and invalidates this date. Essentially blanks out the date value so we
     * don't contain any particular date.
     */
    public void clear() {
        _date = null;
    }

    /**
     * Reports whether this instance contains a valid date.
     *
     * @return Whether this YMDDate contains an actual date.
     */
    public boolean isValid() {
        return _date != null;
    }

    /**
     * Sets this date to the specified value. Format is yyyy-MM-dd always. If your format is
     * literally anything else, the date will be unset.
     *
     * @param src A string in the format yyyy-MM-dd.
     */
    public void setDate(String src) {
        if (src == null || src.trim().isEmpty() || src.length() != 10) {
            _date = null;
            return;
        }

        try {
            _date = _format.parse(src);
        }
        catch (ParseException ex) {
            _date = null;
        }
    }

    /**
     * Returns the numerical year associated with this date, or 0 if the date is not set.
     *
     * @return The numerical year for this date, or 0 if not set.
     */
    public int getYear() {
        if (_date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(_date);
        return c.get(Calendar.YEAR);
    }

    /**
     * Returns a four character string representing the year of this date, or empty string if this
     * date is not set.
     *
     * @return A four character string for the year of this date, or empty string.
     */
    public String getYearStr() {
        int year = getYear();
        if (year == 0) {
            return "";
        }
        return Integer.toString(year);
    }

    /**
     * Returns the numerical month associated with this date, or 0 if the date is not set.
     *
     * @return The numerical month for this date (1-12), or 0 if not set.
     */
    public int getMonth() {
        if (_date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(_date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * Returns a two character string representation of the current month, from 01 to 12. If the
     * current date is not set, returns an empty string.
     *
     * @return A two character string from 01-12 representing the month, or "" if not set.
     */
    public String getMonthStr() {
        int month = getMonth();
        if (month == 0) {
            return "";
        }
        String monthStr = Integer.toString(month);
        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        return monthStr;
    }

    /**
     * Returns the numerical day associated with this date, or 0 if the date is not set.
     *
     * @return The numerical day for this date (1-31), or 0 if not set.
     */
    public int getDay() {
        if (_date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(_date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns a two character string representation of the current day, from 01 to 31. If the current
     * date is not set, returns an empty string.
     *
     * @return A two character string from 01-31 representing the day, or "" if not set.
     */
    public String getDayStr() {
        int day = getDay();
        if (day == 0) {
            return "";
        }
        String dayStr = Integer.toString(day);
        if (day < 10) {
            dayStr = "0" + dayStr;
        }
        return dayStr;
    }

    /**
     * Returns the human-readable day name, for example "Tuesday". If the current date is not
     * set, returns an empty string.
     *
     * @return An empty string if date is invalid, or the name of the day (example: "Thursday").
     */
    public String getDayName() {
        final String[] dayNames = new String[]{"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int selectedIndex = 0;
        if (_date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(_date);
            int index = c.get(Calendar.DAY_OF_WEEK);
            if (index >= 1 && index <= 7) {
                selectedIndex = index;
            }
        }
        return dayNames[selectedIndex];
    }

    /**
     * Returns a string representation of the current date. If the current date is not set, return is
     * an empty string. Format is yyyy-MM-dd always.
     *
     * @return A date string in the format yyyy-MM-dd, or an empty string if no date is set.
     */
    @JsonValue
    @Override
    public String toString() {
        if (_date == null) {
            return "";
        }

        return _format.format(_date);
    }

    /**
     * Compares this date instance to the specified other date instance.
     *
     * @param other The other date.
     * @return -1 if this date is before other, 0 if they're equal, +1 if this date is after other.
     */
    public int compareTo(YMDDate other) {
        if (_date == null || other._date == null) {
            return 0;
        }
        return toString().compareTo(other.toString());
    }

    /**
     * Compares this date instance to the specified date value. Format is yyyy-MM-dd always.
     *
     * @param str The date string to compare this date to.
     * @return -1 if this date is before the other, 0 if they're equals, +1 if this date is after.
     */
    public int compareTo(String str) {
        return compareTo(new YMDDate(str));
    }

    /**
     * Compares the given date instances.
     *
     * @param o1 First date instance
     * @param o2 Second date instance
     * @return -1 if first is before second, 0 if they're equal, +1 if first is after second.
     */
    @Override
    public int compare(YMDDate o1, YMDDate o2) {
        if (o1._date == null || o2._date == null) {
            return 0;
        }

        return o1.toString().compareTo(o2.toString());
    }

    /**
     * Reports whether this date instance is equal to another.
     *
     * @param other The object to which to compare this one.
     * @return True if the two objects represent the same date (or if both are invalid).
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof YMDDate)) {
            return false;
        }

        return toString().equals(((YMDDate)other).toString());
    }

    /**
     * Overridden here because we're overriding equals().
     *
     * @return See Object.hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this._date);
        return hash;
    }

}
