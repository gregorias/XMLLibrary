package me.gregorias.xmllibrary;

import me.gregorias.xmllibrary.interfaces.gui.MainApplication;
import me.gregorias.xmllibrary.library.LibraryFacade;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {
  public static final String APPLICATION_NAME = "XMLLibrary";
  private static Path PATH_TO_LIBRARY_XML =
      FileSystems.getDefault().getPath("library.xml");

  public static void main(String[] args) throws JAXBException, IOException, SAXException,
      ParserConfigurationException {
    LibraryFacade facade = new LibraryFacade(PATH_TO_LIBRARY_XML);
    facade.initialize();

    MainApplication .main(facade);
    facade.save();

    //final URI baseURI = URI.create(String.format("http://%s:%s/", "localhost", "8080"));
    //Lock lock = new ReentrantReadWriteLock().readLock();
    //RESTApplication rest = new RESTApplication(library, lock, baseURI);
    //rest.toString();
    //rest.run();
  }
}
