package me.gregorias.xmllibrary.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Resource which on POST shutsdown this server.
 */
@Path("shutdown")
public class ShutdownResource {
  private final Lock mLock;
  private final Condition mShutDownCondition;
  private final AtomicBoolean mHasShutDownBeenCalled;
  private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownResource.class);

  public ShutdownResource(Lock lock,
                          Condition shutDownCondition,
                          AtomicBoolean hasShutDownBeenCalled) {
    mLock = lock;
    mShutDownCondition = shutDownCondition;
    mHasShutDownBeenCalled = hasShutDownBeenCalled;
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response stop() {
    LOGGER.info("stop()");
    mLock.lock();
    try {
      mHasShutDownBeenCalled.set(true);
      mShutDownCondition.signal();
    } finally {
      mLock.unlock();
    }
    LOGGER.info("stop() -> ok");
    return Response.ok().build();
  }
}
