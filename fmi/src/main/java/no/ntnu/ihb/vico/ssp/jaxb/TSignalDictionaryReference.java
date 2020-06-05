
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TSignalDictionaryReference complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TSignalDictionaryReference">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ssp-standard.org/SSP1/SystemStructureDescription}TElement">
 *       &lt;sequence>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dictionary" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSignalDictionaryReference", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "annotations"
})
public class TSignalDictionaryReference
        extends TElement {

    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TAnnotations annotations;
    @XmlAttribute(name = "dictionary", required = true)
    protected String dictionary;

    /**
     * Gets the value of the annotations property.
     *
     * @return possible object is
     * {@link TAnnotations }
     */
    public TAnnotations getAnnotations() {
        return annotations;
    }

    /**
     * Sets the value of the annotations property.
     *
     * @param value allowed object is
     *              {@link TAnnotations }
     */
    public void setAnnotations(TAnnotations value) {
        this.annotations = value;
    }

    /**
     * Gets the value of the dictionary property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDictionary() {
        return dictionary;
    }

    /**
     * Sets the value of the dictionary property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDictionary(String value) {
        this.dictionary = value;
    }

}
