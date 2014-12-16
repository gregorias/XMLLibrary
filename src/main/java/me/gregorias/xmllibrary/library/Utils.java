package me.gregorias.xmllibrary.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.Optional;

/**
 * Utilities used by library.
 */
public class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
  public static Optional<XMLGregorianCalendar> getDateFromString(String date) {
    LOGGER.trace("getDateFromString({})", date);
    try {
      return Optional.of(DatatypeFactory.newInstance().newXMLGregorianCalendar(date));
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException();
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

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
