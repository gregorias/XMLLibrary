package me.gregorias.xmllibrary.interfaces.rest;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.gregorias.xmllibrary.library.jaxb.Library;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST interface for kademlia. See resources in this package for available commands.
 *
 * @author Grzegorz Milka
 */
public class RESTApplication implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(RESTApplication.class);
  private final Library mLibrary;
  private final Lock mLibraryLock;
  private final URI mUri;
  private final Lock mLock;
  private final Condition mShutDownCondition;
  private final AtomicBoolean mHasShutDownBeenCalled;

  public RESTApplication(Library library, Lock libraryLock, URI uri) {
    mLibrary = library;
    mLibraryLock = libraryLock;
    mUri = uri;
    mLock = new ReentrantLock();
    mShutDownCondition = mLock.newCondition();
    mHasShutDownBeenCalled = new AtomicBoolean(false);
  }

  @Override
  public void run() {
    LOGGER.info("run()");
    ResourceConfig config = createConfig();
    final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(mUri, config);
    try {
      server.start();
      mLock.lock();
      try {
        while (!mHasShutDownBeenCalled.get()) {
          mShutDownCondition.await();
        }
        server.shutdown();
      } catch (InterruptedException e) {
        server.shutdownNow();
        LOGGER.error("run() -> Unexpected interrupt exception.", e);
        return;
      } finally {
        mLock.unlock();
      }
    } catch (IOException e) {
      LOGGER.error("run() -> IOException.", e);
    }
    LOGGER.info("run() -> void");
  }

  private ResourceConfig createConfig() {
    ResourceConfig config = new ResourceConfig();
    /*config.register(new KademliaStartResource(mKademlia));
    config.register(new GetLocalKeyResource(mKademlia));
    config.register(new FindNodesResource(mKademlia));
    config.register(new KademliaGetRoutingTableResource(mKademlia));
    config.register(new KademliaStopResource(mKademlia));
    config.register(new ServerShutDownResource(mLock, mShutDownCondition,
     mHasShutDownBeenCalled));*/
    return config;
  }
}
