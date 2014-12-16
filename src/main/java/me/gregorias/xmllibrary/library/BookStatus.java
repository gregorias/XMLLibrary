package me.gregorias.xmllibrary.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Status of a book.
 */
public class BookStatus {
  private static final Logger LOGGER = LoggerFactory.getLogger(BookStatus.class);
  private final Status mStatus;
  private final String mRentDuration;
  private final boolean mIsOverdue;

  public BookStatus(Status status) {
    this(status, null);
  }

  public BookStatus(Status status, XMLGregorianCalendar rentedTo) {
    mStatus = status;
    if (rentedTo != null) {
      mRentDuration = rentedTo.toString();
      mIsOverdue = !Utils.isDateInFuture(rentedTo);
    } else {
      mRentDuration = null;
      mIsOverdue = false;
    }
  }

  public static enum Status {
    AVAILABLE,
    IN_STORE,
    RENTED,
    UNAVAILABLE
  }

  public Status getStatus() {
    return mStatus;
  }

  public boolean isOverdue() {
    return mIsOverdue;
  }

  public boolean isRentable() {
    return mStatus != Status.RENTED && mStatus != Status.UNAVAILABLE;
  }

  @Override
  public String toString() {
    switch (mStatus) {
      case AVAILABLE:
        return "available";
      case IN_STORE:
        return "in store";
      case RENTED:
        if (isOverdue()) {
          return "overdue since " + mRentDuration;
        } else {
          return "rented to " + mRentDuration;
        }
      default:
        return "unavailable";
    }
  }
}
