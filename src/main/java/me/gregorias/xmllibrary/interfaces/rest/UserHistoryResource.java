package me.gregorias.xmllibrary.interfaces.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * Created by grzesiek on 16.12.14.
 */
@Path("/user/{userId}/history")
public class UserHistoryResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserHistoryResource.class);
  private static final String XSLT_USER_HISTORY_PATH = "/userHistory.xsl";
  private final DocumentBuilderFactory mDocumentBuilderFactory;
  private final java.nio.file.Path mLibraryXMLPath;
  private final String mXsltSpec;

  public UserHistoryResource(java.nio.file.Path libraryXMLPath) {
    mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    mLibraryXMLPath = libraryXMLPath;
    try {
      URL specUrl = CatalogueResource.class.getResource(XSLT_USER_HISTORY_PATH);
      mXsltSpec = Resources.toString(specUrl, Charsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @GET
  @Produces({"application/xml", "text/xml"})
  public String getUserHistory(@PathParam("userId") int userId)
      throws ParserConfigurationException, IOException, SAXException, TransformerException {
    LOGGER.info("getUserHistory({})", userId);

    DocumentBuilder documentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
    Document doc = documentBuilder.parse(mLibraryXMLPath.toFile());
    Transformer transformer = createXsltTransformer(String.format(mXsltSpec, userId, userId));
    LOGGER.info("getUserHistory({})", String.format(mXsltSpec, userId, userId));
    return transformNodeWithXslt(doc, transformer);
  }

  private static Transformer createXsltTransformer(String xsltSpec)
      throws TransformerConfigurationException {
    StreamSource xsltSource = new StreamSource(new StringReader(xsltSpec));
    Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private String transformNodeWithXslt(Node node, Transformer transformer)
      throws TransformerException {
    LOGGER.trace("transformNodeWithXslt()");
    StringWriter sw = new StringWriter();
    transformer.transform(new DOMSource(node), new StreamResult(sw));
    return sw.toString();
  }
}
