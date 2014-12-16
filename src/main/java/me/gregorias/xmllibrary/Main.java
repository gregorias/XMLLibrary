package me.gregorias.xmllibrary;

import me.gregorias.xmllibrary.interfaces.gui.MainApplication;
import me.gregorias.xmllibrary.interfaces.rest.RESTApplication;
import me.gregorias.xmllibrary.library.LibraryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Main entry point to application. Expects first argument to point to XML data file.
 *
 * If only one argument is provided GUI is started {@see MainApplication}.
 * Otherwise REST interface {@see RESTApplication}.
 */
public class Main {
  public static final String APPLICATION_NAME = "XMLLibrary";
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class) ;

  public static void main(String[] args)  {
    LOGGER.info("main({})", args.length);
    if (args.length == 0) {
      LOGGER.error("main(): Expected a path to library's XML data file.");
    }

    final Path pathToLibraryXML = FileSystems.getDefault().getPath(args[0]);
    try {
      if (args.length == 1) {
        LibraryFacade facade = new LibraryFacade(pathToLibraryXML);
        facade.initialize();

        MainApplication.main(facade);
        facade.save();
      } else {
        final URI baseURI = URI.create(String.format("http://%s:%s/", "localhost", "8080"));
        RESTApplication rest = new RESTApplication(pathToLibraryXML, baseURI);
        rest.run();
      }
    } catch (RuntimeException e) {
      LOGGER.error("main(): Caught runtime exception in main.", e);
    } catch (Exception e)  {
      LOGGER.error("main(): Caught exception in main.", e);
    }
  }
}
