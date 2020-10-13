
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TTrail complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TTrail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="length" type="{http://www.w3.org/2001/XMLSchema}float" default="10" />
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" default="blue" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTrail", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TTrail {

    @XmlAttribute(name = "length")
    protected Float length;
    @XmlAttribute(name = "color")
    protected String color;

    /**
     * Gets the value of the length property.
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public float getLength() {
        if (length == null) {
            return 10.0F;
        } else {
            return length;
        }
    }

    /**
     * Sets the value of the length property.
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setLength(Float value) {
        this.length = value;
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
        if (color == null) {
            return "blue";
        } else {
            return color;
        }
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

}
