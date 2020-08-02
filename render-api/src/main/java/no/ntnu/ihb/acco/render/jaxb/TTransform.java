
package no.ntnu.ihb.acco.render.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TTransform complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TTransform">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Geometry" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TGeometry" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTransform", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "geometry"
})
public class TTransform {

    @XmlElement(name = "Geometry", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TGeometry geometry;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the geometry property.
     *
     * @return
     *     possible object is
     *     {@link TGeometry }
     *
     */
    public TGeometry getGeometry() {
        return geometry;
    }

    /**
     * Sets the value of the geometry property.
     *
     * @param value
     *     allowed object is
     *     {@link TGeometry }
     *
     */
    public void setGeometry(TGeometry value) {
        this.geometry = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

}
