
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
 *       &lt;attribute name="xRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="yRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" default="blue" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTrail", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TTrail {

    @XmlAttribute(name = "xRef", required = true)
    protected String xRef;
    @XmlAttribute(name = "yRef", required = true)
    protected String yRef;
    @XmlAttribute(name = "color")
    protected String color;

    /**
     * Gets the value of the xRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getXRef() {
        return xRef;
    }

    /**
     * Sets the value of the xRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXRef(String value) {
        this.xRef = value;
    }

    /**
     * Gets the value of the yRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getYRef() {
        return yRef;
    }

    /**
     * Sets the value of the yRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setYRef(String value) {
        this.yRef = value;
    }

    /**
     * Gets the value of the color property.
     *
     * @return possible object is
     * {@link String }
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
     * @param value allowed object is
     *              {@link String }
     */
    public void setColor(String value) {
        this.color = value;
    }

}
