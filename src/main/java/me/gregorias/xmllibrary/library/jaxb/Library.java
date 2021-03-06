//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.10 at 11:12:52 AM CET 
//


package me.gregorias.xmllibrary.library.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Library complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Library">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="positions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="book" type="{http://xmllibrary.gregorias.me/Library}Book" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="items">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{http://xmllibrary.gregorias.me/Library}Item" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="accounts">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="user" type="{http://xmllibrary.gregorias.me/Library}User" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Library", propOrder = {
    "positions",
    "items",
    "accounts"
})
@XmlRootElement(name = "library")
public class Library {

    @XmlElement(required = true)
    protected Library.Positions positions;
    @XmlElement(required = true)
    protected Library.Items items;
    @XmlElement(required = true)
    protected Library.Accounts accounts;

    /**
     * Gets the value of the positions property.
     * 
     * @return
     *     possible object is
     *     {@link Library.Positions }
     *     
     */
    public Library.Positions getPositions() {
        return positions;
    }

    /**
     * Sets the value of the positions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Library.Positions }
     *     
     */
    public void setPositions(Library.Positions value) {
        this.positions = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link Library.Items }
     *     
     */
    public Library.Items getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link Library.Items }
     *     
     */
    public void setItems(Library.Items value) {
        this.items = value;
    }

    /**
     * Gets the value of the accounts property.
     * 
     * @return
     *     possible object is
     *     {@link Library.Accounts }
     *     
     */
    public Library.Accounts getAccounts() {
        return accounts;
    }

    /**
     * Sets the value of the accounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Library.Accounts }
     *     
     */
    public void setAccounts(Library.Accounts value) {
        this.accounts = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="user" type="{http://xmllibrary.gregorias.me/Library}User" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "users"
    })
    public static class Accounts {

        @XmlElement(name = "user")
        protected List<User> users;

        /**
         * Gets the value of the users property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the users property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUsers().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link User }
         * 
         * 
         */
        public List<User> getUsers() {
            if (users == null) {
                users = new ArrayList<User>();
            }
            return this.users;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="item" type="{http://xmllibrary.gregorias.me/Library}Item" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "items"
    })
    public static class Items {

        @XmlElement(name = "item")
        protected List<Item> items;

        /**
         * Gets the value of the items property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the items property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItems().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Item }
         * 
         * 
         */
        public List<Item> getItems() {
            if (items == null) {
                items = new ArrayList<Item>();
            }
            return this.items;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="book" type="{http://xmllibrary.gregorias.me/Library}Book" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "books"
    })
    public static class Positions {

        @XmlElement(name = "book")
        protected List<Book> books;

        /**
         * Gets the value of the books property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the books property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBooks().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Book }
         * 
         * 
         */
        public List<Book> getBooks() {
            if (books == null) {
                books = new ArrayList<Book>();
            }
            return this.books;
        }

    }

}
