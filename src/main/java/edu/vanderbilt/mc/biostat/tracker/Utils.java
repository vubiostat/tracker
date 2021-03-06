package edu.vanderbilt.mc.biostat.tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Utils {

  public static Date today() {
    return todayCalendar().getTime();
  }

  public static Date tomorrow() {
    Calendar cal = todayCalendar();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    return cal.getTime();
  }
  
  public static String shortTime(Date d) {
    return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(d);
  }

  private static Calendar todayCalendar() {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;
  }
}
