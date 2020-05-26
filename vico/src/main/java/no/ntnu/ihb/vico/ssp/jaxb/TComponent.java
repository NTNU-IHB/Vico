
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TComponent complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TComponent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ssp-standard.org/SSP1/SystemStructureDescription}TElement">
 *       &lt;sequence>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-fmu-sharedlibrary" />
 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="implementation" default="any">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="any"/>
 *             &lt;enumeration value="ModelExchange"/>
 *             &lt;enumeration value="CoSimulation"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TComponent", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "annotations"
})
public class TComponent
        extends TElement {

    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TAnnotations annotations;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "source", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String source;
    @XmlAttribute(name = "implementation")
    protected String implementation;

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
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getType() {
        if (type == null) {
            return "application/x-fmu-sharedlibrary";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the source property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the implementation property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getImplementation() {
        if (implementation == null) {
            return "any";
        } else {
            return implementation;
        }
    }

    /**
     * Sets the value of the implementation property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setImplementation(String value) {
        this.implementation = value;
    }

}
