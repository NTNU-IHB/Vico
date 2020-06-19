
package no.ntnu.ihb.acco.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TPositionRef complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TPositionRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="px" type="{http://www.w3.org/2001/XMLSchema}string" default="undefined" />
 *       &lt;attribute name="py" type="{http://www.w3.org/2001/XMLSchema}string" default="undefined" />
 *       &lt;attribute name="pz" type="{http://www.w3.org/2001/XMLSchema}string" default="undefined" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPositionRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TPositionRef {

    @XmlAttribute(name = "px")
    protected String px;
    @XmlAttribute(name = "py")
    protected String py;
    @XmlAttribute(name = "pz")
    protected String pz;

    /**
     * Gets the value of the px property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPx() {
        if (px == null) {
            return "undefined";
        } else {
            return px;
        }
    }

    /**
     * Sets the value of the px property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPx(String value) {
        this.px = value;
    }

    /**
     * Gets the value of the py property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPy() {
        if (py == null) {
            return "undefined";
        } else {
            return py;
        }
    }

    /**
     * Sets the value of the py property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPy(String value) {
        this.py = value;
    }

    /**
     * Gets the value of the pz property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPz() {
        if (pz == null) {
            return "undefined";
        } else {
            return pz;
        }
    }

    /**
     * Sets the value of the pz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPz(String value) {
        this.pz = value;
    }

}
