
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TGeometry complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TGeometry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OffsetPosition" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TPosition" minOccurs="0"/>
 *         &lt;element name="OffsetRotation" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TEuler" minOccurs="0"/>
 *         &lt;element name="Shape" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TShape"/>
 *       &lt;/sequence>
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="opacity" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="wireframe" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TGeometry", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "offsetPosition",
        "offsetRotation",
        "shape"
})
public class TGeometry {

    @XmlElement(name = "OffsetPosition", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TPosition offsetPosition;
    @XmlElement(name = "OffsetRotation", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TEuler offsetRotation;
    @XmlElement(name = "Shape", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected TShape shape;
    @XmlAttribute(name = "color")
    protected String color;
    @XmlAttribute(name = "opacity")
    protected Double opacity;
    @XmlAttribute(name = "wireframe")
    protected Boolean wireframe;

    /**
     * Gets the value of the offsetPosition property.
     *
     * @return
     *     possible object is
     *     {@link TPosition }
     *
     */
    public TPosition getOffsetPosition() {
        return offsetPosition;
    }

    /**
     * Sets the value of the offsetPosition property.
     *
     * @param value
     *     allowed object is
     *     {@link TPosition }
     *
     */
    public void setOffsetPosition(TPosition value) {
        this.offsetPosition = value;
    }

    /**
     * Gets the value of the offsetRotation property.
     *
     * @return
     *     possible object is
     *     {@link TEuler }
     *
     */
    public TEuler getOffsetRotation() {
        return offsetRotation;
    }

    /**
     * Sets the value of the offsetRotation property.
     *
     * @param value
     *     allowed object is
     *     {@link TEuler }
     *
     */
    public void setOffsetRotation(TEuler value) {
        this.offsetRotation = value;
    }

    /**
     * Gets the value of the shape property.
     *
     * @return
     *     possible object is
     *     {@link TShape }
     *
     */
    public TShape getShape() {
        return shape;
    }

    /**
     * Sets the value of the shape property.
     *
     * @param value
     *     allowed object is
     *     {@link TShape }
     *
     */
    public void setShape(TShape value) {
        this.shape = value;
    }

    /**
     * Gets the value of the color property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the opacity property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getOpacity() {
        return opacity;
    }

    /**
     * Sets the value of the opacity property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setOpacity(Double value) {
        this.opacity = value;
    }

    /**
     * Gets the value of the wireframe property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isWireframe() {
        return wireframe;
    }

    /**
     * Sets the value of the wireframe property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setWireframe(Boolean value) {
        this.wireframe = value;
    }

}
