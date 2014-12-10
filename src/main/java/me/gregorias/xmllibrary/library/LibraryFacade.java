package me.gregorias.xmllibrary.library;

import me.gregorias.xmllibrary.library.jaxb.Library;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by grzesiek on 10.12.14.
 */
public class LibraryFacade {
  private final Library mLibrary;
  private final Lock mLibraryLock = new ReentrantLock();
  private Account mLoggedInAccount;

  public LibraryFacade(Library library) {
    mLibrary = library;
  }

  public void acquireLock() {
    mLibraryLock.lock();
  }

  public Account getCurrentLoggedInAccount() {
    return mLoggedInAccount;
  }

  public Library getLibrary() {
    return mLibrary;
  }

  public boolean loginAsUser(String username) {
    mLibraryLock.lock();
    try {
      // TODO Check user existance
      mLoggedInAccount = new Account(username);
    } finally {
      mLibraryLock.unlock();
    }
    return true;
  }

  public void releaseLock() {
    mLibraryLock.unlock();
  }

  public static class Account {
    private final String mUsername;
    private final boolean mIsLibrarian;

    public Account(String username) {
      mUsername = username;
      mIsLibrarian = false;
    }

    public Account(boolean isLibrarian) {
      mUsername = null;
      mIsLibrarian = isLibrarian;
    }

    public String getUsername() {
      return mUsername;
    }

    public boolean isLibrarian() {
      return mIsLibrarian;
    }

    public boolean isNormalUser() {
      return !mIsLibrarian && mUsername != null;
    }
  }
}
