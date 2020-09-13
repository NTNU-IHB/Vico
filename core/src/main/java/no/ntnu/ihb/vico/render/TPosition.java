
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TPosition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TPosition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="px" type="{http://www.w3.org/2001/XMLSchema}float" default="0" />
 *       &lt;attribute name="py" type="{http://www.w3.org/2001/XMLSchema}float" default="0" />
 *       &lt;attribute name="pz" type="{http://www.w3.org/2001/XMLSchema}float" default="0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPosition", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TPosition {

    @XmlAttribute(name = "px")
    protected Float px;
    @XmlAttribute(name = "py")
    protected Float py;
    @XmlAttribute(name = "pz")
    protected Float pz;

    /**
     * Gets the value of the px property.
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public float getPx() {
        if (px == null) {
            return 0.0F;
        } else {
            return px;
        }
    }

    /**
     * Sets the value of the px property.
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setPx(Float value) {
        this.px = value;
    }

    /**
     * Gets the value of the py property.
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public float getPy() {
        if (py == null) {
            return 0.0F;
        } else {
            return py;
        }
    }

    /**
     * Sets the value of the py property.
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setPy(Float value) {
        this.py = value;
    }

    /**
     * Gets the value of the pz property.
     *
     * @return
     *     possible object is
     *     {@link Float }
     *
     */
    public float getPz() {
        if (pz == null) {
            return 0.0F;
        } else {
            return pz;
        }
    }

    /**
     * Sets the value of the pz property.
     *
     * @param value
     *     allowed object is
     *     {@link Float }
     *
     */
    public void setPz(Float value) {
        this.pz = value;
    }

}
