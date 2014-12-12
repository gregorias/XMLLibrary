package me.gregorias.xmllibrary.library;

import me.gregorias.xmllibrary.library.jaxb.Library;
import me.gregorias.xmllibrary.library.jaxb.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Observable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Facade to library data which handles synchronization and authentication.
 */
public class LibraryFacade extends Observable {
  private static final Logger LOGGER = LoggerFactory.getLogger(LibraryFacade.class);
  private final Path mLibraryXMLPath;
  private final Lock mLibraryLock = new ReentrantLock();

  // private Document mLibraryDocument;
  private Library mLibrary;
  private Account mLoggedInAccount;

  public LibraryFacade(Path libraryXMLPath) {
    mLibraryXMLPath = libraryXMLPath;
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

  public void initialize() throws ParserConfigurationException, SAXException, IOException,
      JAXBException {
    //mLibraryDocument = parseLibraryDocument();
    mLibrary = generateJAXBLibrary();
  }

  public boolean isLoggedIn() {
    mLibraryLock.lock();
    try {
      return mLoggedInAccount != null;
    } finally {
      mLibraryLock.unlock();
    }
  }

  public void loginAsLibrarian() {
    mLibraryLock.lock();
    try {
      if (mLoggedInAccount != null) {
        throw new IllegalStateException("Already logged in.");
      }
      mLoggedInAccount = new Account(true);
    } finally {
      mLibraryLock.unlock();
    }
  }

  public boolean loginAsUser(int id) {
    mLibraryLock.lock();
    try {
      if (mLoggedInAccount != null) {
        throw new IllegalStateException("Already logged in.");
      }
      for (User user : mLibrary.getAccounts().getUsers()) {
        if (user.getId() == id) {
          mLoggedInAccount = new Account(user);
          return true;
        }
      }
    } finally {
      mLibraryLock.unlock();
    }
    return false;
  }

  public void logout() {
    mLibraryLock.lock();
    try {
      mLoggedInAccount = null;
    } finally {
      mLibraryLock.unlock();
    }
  }

  @Override
  public void setChanged() {
    LOGGER.debug("setChanged()");
    super.setChanged();
  }

  public void releaseLock() {
    mLibraryLock.unlock();
  }

  public static class Account {
    private final User mUser;
    private final boolean mIsLibrarian;

    public Account(User user) {
      mUser = user;
      mIsLibrarian = false;
    }

    public Account(boolean isLibrarian) {
      mUser = null;
      mIsLibrarian = isLibrarian;
    }

    public User getUser() {
      return mUser;
    }

    public boolean isLibrarian() {
      return mIsLibrarian;
    }

    public boolean isNormalUser() {
      return !mIsLibrarian && mUser != null;
    }
  }

  private Library generateJAXBLibrary() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    return (Library) unmarshaller.unmarshal(mLibraryXMLPath.toFile());
  }

  /* private Document parseLibraryDocument() throws IOException, ParserConfigurationException,
      SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(mLibraryXMLPath.toFile());
  } */

  /* private NodeList runXPathQuery(Document document, String query) throws IOException,
      XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    return (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
  } */
}
