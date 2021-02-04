
package no.ntnu.ihb.vico.render;

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
 *         &lt;element name="Geometry" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TGeometry"/>
 *         &lt;element name="PositionRef" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TPositionRef" minOccurs="0"/>
 *         &lt;element name="RotationRef" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TRotationRef" minOccurs="0"/>
 *         &lt;element name="Trail" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TTrail" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parent" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTransform", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "geometry",
        "positionRef",
        "rotationRef",
        "trail"
})
public class TTransform {

    @XmlElement(name = "Geometry", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected TGeometry geometry;
    @XmlElement(name = "PositionRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TPositionRef positionRef;
    @XmlElement(name = "RotationRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TRotationRef rotationRef;
    @XmlElement(name = "Trail", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TTrail trail;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "parent")
    protected String parent;

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
     * Gets the value of the positionRef property.
     *
     * @return
     *     possible object is
     *     {@link TPositionRef }
     *
     */
    public TPositionRef getPositionRef() {
        return positionRef;
    }

    /**
     * Sets the value of the positionRef property.
     *
     * @param value
     *     allowed object is
     *     {@link TPositionRef }
     *
     */
    public void setPositionRef(TPositionRef value) {
        this.positionRef = value;
    }

    /**
     * Gets the value of the rotationRef property.
     *
     * @return
     *     possible object is
     *     {@link TRotationRef }
     *
     */
    public TRotationRef getRotationRef() {
        return rotationRef;
    }

    /**
     * Sets the value of the rotationRef property.
     *
     * @param value
     *     allowed object is
     *     {@link TRotationRef }
     *
     */
    public void setRotationRef(TRotationRef value) {
        this.rotationRef = value;
    }

    /**
     * Gets the value of the trail property.
     *
     * @return
     *     possible object is
     *     {@link TTrail }
     *
     */
    public TTrail getTrail() {
        return trail;
    }

    /**
     * Sets the value of the trail property.
     *
     * @param value
     *     allowed object is
     *     {@link TTrail }
     *
     */
    public void setTrail(TTrail value) {
        this.trail = value;
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

    /**
     * Gets the value of the parent property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParent(String value) {
        this.parent = value;
    }

}
