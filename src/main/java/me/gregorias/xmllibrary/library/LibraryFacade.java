package me.gregorias.xmllibrary.library;

import me.gregorias.xmllibrary.library.jaxb.Book;
import me.gregorias.xmllibrary.library.jaxb.HistoryItem;
import me.gregorias.xmllibrary.library.jaxb.Item;
import me.gregorias.xmllibrary.library.jaxb.ItemStatus;
import me.gregorias.xmllibrary.library.jaxb.Library;
import me.gregorias.xmllibrary.library.jaxb.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

  public Optional<Item> getItemOfGivenStatus(Book book, List<Item> items, String status) {
    return items.stream().filter((item) -> item.getStatus().equals(status)).findAny();
  }

  public BookStatus getBookStatus(Book book) {
    List<Item> items = getBookItems(book);
    Optional<Item> availableItem = items.stream().filter((item) -> item.getStatus().equals(
        ItemStatus.AVAILABLE)).findAny();
    Optional<Item> rentableItem = items.stream().filter((item) -> !item.getStatus().equals(
        ItemStatus.RENTED)).findAny();
    if (rentableItem.isPresent()) {
      BookStatus.Status status;
      if (availableItem.isPresent()) {
        status = BookStatus.Status.AVAILABLE;
      } else {
        status = BookStatus.Status.IN_STORE;
      }
      return new BookStatus(status);
    }

    Optional<Item> rentedItem = items.stream().findFirst();
    if (rentedItem.isPresent()) {
      LinkedList<HistoryItem> rents = new LinkedList<>(rentedItem.get().getHistory().getRents());
      rents.getLast().getRentedTo();
      return new BookStatus(BookStatus.Status.RENTED, rents.getLast().getRentedTo().toString());
    } else {
      return new BookStatus(BookStatus.Status.UNAVAILABLE);
    }
  }

  public List<Item> getBookItems(Book book) {
    return mLibrary.getItems().getItems().stream()
        .filter((item) -> item.getIsbn10().equals(book.getIsbn10()))
        .collect(Collectors.toList());
  }

  public Account getCurrentLoggedInAccount() {
    return mLoggedInAccount;
  }

  public Library getLibrary() {
    return mLibrary;
  }

  public List<Item> getUserItems(User user) {
    return mLibrary.getItems().getItems().stream()
        .filter((item) -> isItemRentedByUser(user, item))
        .collect(Collectors.toList());
  }

  public boolean isBookRentedByUser(User user, Book book) {
    long count = getUserItems(user).stream()
        .filter((item) -> item.getIsbn10().equals(book.getIsbn10()))
        .count();
    return count != 0;
  }

  public boolean isItemRentedByUser(User user, Item item) {
    long count = item.getHistory().getRents().stream()
        .filter((historyItem) -> historyItem.getRenteeId() == user.getId())
        .filter((historyItem) -> historyItem.getReturnDate() == null).count();
    return count != 0;
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

  public void registerNewUser(User user) {
    int newId = calculateNextFreeUserId();
    user.setId(newId);

    String yesterdayValidTo = calculateYesterdayValidToString();
    user.setValidTo(yesterdayValidTo);
    mLibrary.getAccounts().getUsers().add(user);
  }

  public void save() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Marshaller marshaller = jc.createMarshaller();
    marshaller.marshal(mLibrary, mLibraryXMLPath.toFile());
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

  private int calculateNextFreeUserId() {
    Set<Integer> userIds = new TreeSet<>(getSetOfAllUserIds());
    if (userIds.size() == 0) {
      return 1;
    } else {
      int idCandidate = 1;
      for (Integer id : userIds) {
        if (id > idCandidate) {
          break;
        }
        idCandidate = id + 1;
      }
      return idCandidate;
    }
  }

  private String calculateYesterdayValidToString() {
    GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    calendar.add(GregorianCalendar.DAY_OF_WEEK, -1);

    try {
      XMLGregorianCalendar xmlYesterdayCalendar =
          DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      return xmlYesterdayCalendar.toXMLFormat();
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("Unexpected exception.", e);
      throw new IllegalStateException(e);
    }
  }

  private Library generateJAXBLibrary() throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    return (Library) unmarshaller.unmarshal(mLibraryXMLPath.toFile());
  }

  private Set<Integer> getSetOfAllUserIds() {
    Set<Integer> allIds = new TreeSet<>();
    for (User user : mLibrary.getAccounts().getUsers()) {
      allIds.add(user.getId());
    }
    return allIds;
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
