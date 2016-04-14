/**
 * <copyright>
 * Copyright Squid Solutions SAS 2010
 * </copyright>
 */
package com.squid.core.jdbc.formatter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Since we don't have a relationship between Web module and POI third party. We
 * have to copy this class from DurationFormatUtils
 * (com.squid.kraken.webapp.gwt.client.util). Please refer to this class for
 * more information.
 *
 * @see com.squid.kraken.webapp.gwt.client.util.DurationFormatUtils
 * @author danhddv
 *
 */
public class DurationFormatUtils {

  /**
   * Number of seconds in one day (24 * 60 * 60).
   */
  public static final int SECONDS_IN_DAY = 86400;

  /**
   * Number of seconds in one hour (60 * 60).
   */
  public static final int SECONDS_IN_HOUR = 3600;

  /**
   * Number of seconds in one minute.
   */
  public static final int SECONDS_IN_MINUTE = 60;

  /**
   * Characters accepted in duration format pattern.
   */
  public static final List<String> ACCEPTED_CHAR_DURATION_PATTERN = Arrays
      .asList("dd", "hh", "mm", "ss");

  /**
   * Determine if the duration format pattern is valid.
   * 
   * @param formatPattern
   *          pattern
   * @return true --> valid, otherwise false
   */
  public static boolean isDurationFormatPattern(String formatPattern) {
    if (formatPattern == null) {
      return false;
    }

    final int length = formatPattern.length();
    if (length < 2) {
      return false;
    }

    formatPattern = formatPattern.toLowerCase();
    final String first = formatPattern.substring(0, 2);
    final String last = formatPattern.substring(length - 2);
    if (ACCEPTED_CHAR_DURATION_PATTERN.contains(first)
        && ACCEPTED_CHAR_DURATION_PATTERN.contains(last)) {
      return true;
    }
    return false;
  }

  /**
   * Format the duration.
   * 
   * @param pattern
   *          pattern
   * @param secondInDouble
   *          seconds
   * @return formatted string
   */
  public static String format(String pattern, final double secondInDouble) {
    if ((Double.isNaN(secondInDouble)) || (secondInDouble == 0)) {
      return "0 second";
    }
    // determine if day/hour/minute/second should be computed or not
    boolean isDayCalculated = false;
    boolean isHourCalculated = false;
    boolean isMinuteCalculated = false;
    boolean isSecondCalculated = false;
    pattern = pattern.toLowerCase();
    final String[] parsedPattern = pattern.split(":");
    for (final String str : parsedPattern) {
      if (str.equals("dd")) {
        isDayCalculated = true;
      } else if (str.equals("hh")) {
        isHourCalculated = true;
      } else if (str.equals("mm")) {
        isMinuteCalculated = true;
      } else if (str.equals("ss")) {
        isSecondCalculated = true;
      }
    }

    // start the transformation process
    final BigDecimal second = new BigDecimal(String.valueOf(secondInDouble));
    BigDecimal dayRemainder = new BigDecimal(0);
    BigDecimal day = new BigDecimal(0);
    if (isDayCalculated) {
      BigDecimal[] dayArr = second
          .divideAndRemainder(new BigDecimal(SECONDS_IN_DAY));
      day = dayArr[0];
      dayRemainder = dayArr[1];
    } else {
      dayRemainder = second;
    }

    BigDecimal hourRemainder = new BigDecimal(0);
    BigDecimal hour = new BigDecimal(0);
    if (isHourCalculated) {
      BigDecimal[] hourArr = dayRemainder
          .divideAndRemainder(new BigDecimal(SECONDS_IN_HOUR));
      hour = hourArr[0];
      hourRemainder = hourArr[1];
    } else {
      hourRemainder = dayRemainder;
    }

    BigDecimal minuteRemainder = new BigDecimal(0);
    BigDecimal minute = new BigDecimal(0);
    if (isMinuteCalculated) {
      BigDecimal[] minuteArr = hourRemainder
          .divideAndRemainder(new BigDecimal(SECONDS_IN_MINUTE));
      minute = minuteArr[0];
      minuteRemainder = minuteArr[1];
    } else {
      minuteRemainder = hourRemainder;
    }

    // build and return the formatted string
    final StringBuilder result = buildResult(isDayCalculated, isHourCalculated,
        isMinuteCalculated, isSecondCalculated, day, hour, minute,
        minuteRemainder);

    return result.toString().trim();
  }

  /**
   * @param isDayCalculated
   *          must calculate day
   * @param isHourCalculated
   *          must calculate hour
   * @param isMinuteCalculated
   *          must calculate minute
   * @param isSecondCalculated
   *          must calculate second
   * @param day
   *          day
   * @param hour
   *          hour
   * @param minute
   *          minute
   * @param second
   *          second
   * @return the transformation of duration
   */
  private static StringBuilder buildResult(final boolean isDayCalculated,
      final boolean isHourCalculated, final boolean isMinuteCalculated,
      final boolean isSecondCalculated, final BigDecimal day,
      final BigDecimal hour, final BigDecimal minute, final BigDecimal second) {
    final int dayInt = day.intValue();
    final int hourInt = hour.intValue();
    final int minuteInt = minute.intValue();
    final int secondInt = second.intValue();

    final StringBuilder result = new StringBuilder("");
    // TODO internationalize these static texts
    if ((dayInt != 0) && isDayCalculated) {
      result.append(dayInt).append(" ").append((dayInt > 1) ? "days" : "day")
          .append(" ");
    }
    if ((hourInt != 0) && isHourCalculated) {
      result.append(hourInt).append(" ")
          .append((hourInt > 1) ? "hours" : "hour").append(" ");
    }
    if ((minuteInt != 0) && isMinuteCalculated) {
      result.append(minuteInt).append(" ")
          .append((minuteInt > 1) ? "minutes" : "minute").append(" ");
    }
    if ((secondInt != 0) && isSecondCalculated) {
      result.append(secondInt).append(" ")
          .append((secondInt > 1) ? "seconds" : "second");
    }
    return result;
  }
}
