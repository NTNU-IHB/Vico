
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TEnumerations complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TEnumerations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Enumeration" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TEnumeration" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEnumerations", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", propOrder = {
        "enumeration"
})
public class TEnumerations {

    @XmlElement(name = "Enumeration", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
    protected List<TEnumeration> enumeration;

    /**
     * Gets the value of the enumeration property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enumeration property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnumeration().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEnumeration }
     */
    public List<TEnumeration> getEnumeration() {
        if (enumeration == null) {
            enumeration = new ArrayList<TEnumeration>();
        }
        return this.enumeration;
    }

}
