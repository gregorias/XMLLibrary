//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.08 at 09:52:03 AM CET 
//


package me.gregorias.xmllibrary.library.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ItemStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ItemStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="available"/>
 *     &lt;enumeration value="in store"/>
 *     &lt;enumeration value="rented"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ItemStatus")
@XmlEnum
public enum ItemStatus {

    @XmlEnumValue("available")
    AVAILABLE("available"),
    @XmlEnumValue("in store")
    IN_STORE("in store"),
    @XmlEnumValue("rented")
    RENTED("rented");
    private final String value;

    ItemStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ItemStatus fromValue(String v) {
        for (ItemStatus c: ItemStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
