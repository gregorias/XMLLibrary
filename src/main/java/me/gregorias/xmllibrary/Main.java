package me.gregorias.xmllibrary;

import me.gregorias.xmllibrary.library.jaxb.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {
  private static Path PATH_TO_LIBRARY_XML =
    FileSystems.getDefault().getPath("resources/config/library.xml");
  public static void main(String[] args) throws JAXBException {
    JAXBContext jc = JAXBContext.newInstance(Library.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Library library = (Library) unmarshaller.unmarshal(PATH_TO_LIBRARY_XML.toFile());
    System.out.println(library.getPositions());
  }
}
