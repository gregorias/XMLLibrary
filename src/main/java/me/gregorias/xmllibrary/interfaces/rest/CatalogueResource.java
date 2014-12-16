package me.gregorias.xmllibrary.interfaces.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
 * Book Catalogue resource which allows querying via XPATH.
 */
//TODO historia, raporty
//pozostałe menu
//
@Path("catalogue")
public class CatalogueResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueResource.class);
  private static final String XSLT_XML_SPEC_PATH = "/catalogueXML.xsl";
  private static final String XSLT_HTML_SPEC_PATH = "/catalogueHTML.xsl";
  private final DocumentBuilderFactory mDocumentBuilderFactory;
  private final java.nio.file.Path mLibraryXMLPath;
  private final String mXsltXmlSpec;
  private final String mXsltHtmlSpec;

  public CatalogueResource(java.nio.file.Path libraryXMLPath) {
    mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    mLibraryXMLPath = libraryXMLPath;
    try {
      URL xmlUrl = CatalogueResource.class.getResource(XSLT_XML_SPEC_PATH);
      mXsltXmlSpec = Resources.toString(xmlUrl, Charsets.UTF_8);
      URL htmlUrl = CatalogueResource.class.getResource(XSLT_HTML_SPEC_PATH);
      mXsltHtmlSpec = Resources.toString(htmlUrl, Charsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @GET
  @Produces({"application/xml", "text/xml"})
  public String getCatalogueAsXML(@DefaultValue("") @QueryParam("path") String query)
      throws ParserConfigurationException, IOException, SAXException, TransformerException {
    LOGGER.info("getCatalogueAsXML({})", query);
    return getCatalogue(query, mXsltXmlSpec);
  }

  @GET
  @Produces({"text/html"})
  public String getCatalogueAsHTML(@DefaultValue("") @QueryParam("path") String query)
      throws ParserConfigurationException, IOException, SAXException, TransformerException {
    LOGGER.info("getCatalogueAsHTML({})", query);
    return getCatalogue(query, mXsltHtmlSpec);
  }

  private static Transformer createXsltTransformer(String xsltSpec)
      throws TransformerConfigurationException {
    StreamSource xsltSource = new StreamSource(new StringReader(xsltSpec));
    Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private String getCatalogue(String query, String xsltSpec)
      throws ParserConfigurationException, IOException, SAXException, TransformerException {
    if (query.length() != 0) {
      query = String.format("//book[%s]", query);
    } else {
      query = "//book";
    }

    DocumentBuilder documentBuilder = mDocumentBuilderFactory.newDocumentBuilder();
    Document doc = documentBuilder.parse(mLibraryXMLPath.toFile());
    Transformer transformer = createXsltTransformer(String.format(xsltSpec, query));
    return transformNodeWithXslt(doc, transformer);

  }

  private String transformNodeWithXslt(Node node, Transformer transformer)
      throws TransformerException {
    LOGGER.trace("transformNodeWithXslt()");
    StringWriter sw = new StringWriter();
    transformer.transform(new DOMSource(node), new StreamResult(sw));
    return sw.toString();
  }

}
