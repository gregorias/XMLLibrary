package me.gregorias.xmllibrary;

import me.gregorias.xmllibrary.interfaces.gui.MainApplication;
import me.gregorias.xmllibrary.interfaces.rest.RESTApplication;
import me.gregorias.xmllibrary.library.jaxb.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
  private static Path PATH_TO_LIBRARY_XML =
    FileSystems.getDefault().getPath("resources/config/library.xml");

  public static void main(String[] args) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Library library = (Library) unmarshaller.unmarshal(PATH_TO_LIBRARY_XML.toFile());

    MainApplication.main(args);
    final URI baseURI = URI.create(String.format("http://%s:%s/", "localhost", "8080"));
    Lock lock = new ReentrantReadWriteLock().readLock();
    RESTApplication rest = new RESTApplication(library, lock, baseURI);
    rest.run();
  }
}
