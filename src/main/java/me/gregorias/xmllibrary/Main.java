package me.gregorias.xmllibrary;

import me.gregorias.xmllibrary.interfaces.gui.MainApplication;
import me.gregorias.xmllibrary.interfaces.rest.RESTApplication;
import me.gregorias.xmllibrary.library.LibraryFacade;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {
  public static final String APPLICATION_NAME = "XMLLibrary";
  private static Path PATH_TO_LIBRARY_XML =
      FileSystems.getDefault().getPath("library.xml");

  public static void main(String[] args) throws JAXBException, IOException, SAXException,
      ParserConfigurationException, DatatypeConfigurationException {
    if (args.length == 0) {
      LibraryFacade facade = new LibraryFacade(PATH_TO_LIBRARY_XML);
      facade.initialize();

      MainApplication.main(facade);
      facade.save();
    } else {
      final URI baseURI = URI.create(String.format("http://%s:%s/", "localhost", "8080"));
      RESTApplication rest = new RESTApplication(PATH_TO_LIBRARY_XML, baseURI);
      rest.run();
    }
  }
}
