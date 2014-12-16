package me.gregorias.xmllibrary.library;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

/**
 * Utilities used by library.
 */
public class Utils {
  public static XMLGregorianCalendar getTodayDate() {
    GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException();
    }
  }

  public static boolean isDateInFuture(XMLGregorianCalendar calendar) {
    return calendar.compare(getTodayDate()) > 0;

  }
}
