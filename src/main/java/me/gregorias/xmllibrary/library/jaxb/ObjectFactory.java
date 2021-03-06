//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.10 at 11:12:52 AM CET 
//


package me.gregorias.xmllibrary.library.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the me.gregorias.xmllibrary.library.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Isbn10_QNAME = new QName("http://xmllibrary.gregorias.me/Library", "isbn-10");
    private final static QName _Isbn13_QNAME = new QName("http://xmllibrary.gregorias.me/Library", "isbn-13");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: me.gregorias.xmllibrary.library.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Library }
     * 
     */
    public Library createLibrary() {
        return new Library();
    }

    /**
     * Create an instance of {@link Book }
     * 
     */
    public Book createBook() {
        return new Book();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link Library.Positions }
     * 
     */
    public Library.Positions createLibraryPositions() {
        return new Library.Positions();
    }

    /**
     * Create an instance of {@link Library.Items }
     * 
     */
    public Library.Items createLibraryItems() {
        return new Library.Items();
    }

    /**
     * Create an instance of {@link Library.Accounts }
     * 
     */
    public Library.Accounts createLibraryAccounts() {
        return new Library.Accounts();
    }

    /**
     * Create an instance of {@link Passport }
     * 
     */
    public Passport createPassport() {
        return new Passport();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link Position }
     * 
     */
    public Position createPosition() {
        return new Position();
    }

    /**
     * Create an instance of {@link HistoryItem }
     * 
     */
    public HistoryItem createHistoryItem() {
        return new HistoryItem();
    }

    /**
     * Create an instance of {@link Book.Authors }
     * 
     */
    public Book.Authors createBookAuthors() {
        return new Book.Authors();
    }

    /**
     * Create an instance of {@link Item.History }
     * 
     */
    public Item.History createItemHistory() {
        return new Item.History();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmllibrary.gregorias.me/Library", name = "isbn-10")
    public JAXBElement<String> createIsbn10(String value) {
        return new JAXBElement<String>(_Isbn10_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmllibrary.gregorias.me/Library", name = "isbn-13")
    public JAXBElement<String> createIsbn13(String value) {
        return new JAXBElement<String>(_Isbn13_QNAME, String.class, null, value);
    }

}
