package me.gregorias.xmllibrary.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by grzesiek on 14.12.14.
 */
public class BookStatus {
  private static final Logger LOGGER = LoggerFactory.getLogger(BookStatus.class);
  private final Status mStatus;
  private final String mRentDuration;

  public BookStatus(Status status) {
    this(status, "");
  }

  public BookStatus(Status status, String rentDuration) {
    LOGGER.debug("BookStatus({}, {})", status, rentDuration);
    mStatus = status;
    mRentDuration = rentDuration;
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
        return "rented to " + mRentDuration;
      default:
        return "unavailable";
    }
  }
}
