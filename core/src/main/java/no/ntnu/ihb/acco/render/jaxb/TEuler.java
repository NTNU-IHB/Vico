
package no.ntnu.ihb.acco.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TEuler complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TEuler">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="x" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *       &lt;attribute name="y" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *       &lt;attribute name="z" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *       &lt;attribute name="repr" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TAngleRepr" default="deg" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEuler", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TEuler {

    @XmlAttribute(name = "x")
    protected Double x;
    @XmlAttribute(name = "y")
    protected Double y;
    @XmlAttribute(name = "z")
    protected Double z;
    @XmlAttribute(name = "repr")
    protected TAngleRepr repr;

    /**
     * Gets the value of the x property.
     *
     * @return possible object is
     * {@link Double }
     */
    public double getX() {
        if (x == null) {
            return 0.0D;
        } else {
            return x;
        }
    }

    /**
     * Sets the value of the x property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setX(Double value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     *
     * @return possible object is
     * {@link Double }
     */
    public double getY() {
        if (y == null) {
            return 0.0D;
        } else {
            return y;
        }
    }

    /**
     * Sets the value of the y property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setY(Double value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     *
     * @return possible object is
     * {@link Double }
     */
    public double getZ() {
        if (z == null) {
            return 0.0D;
        } else {
            return z;
        }
    }

    /**
     * Sets the value of the z property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setZ(Double value) {
        this.z = value;
    }

    /**
     * Gets the value of the repr property.
     *
     * @return possible object is
     * {@link TAngleRepr }
     */
    public TAngleRepr getRepr() {
        if (repr == null) {
            return TAngleRepr.DEG;
        } else {
            return repr;
        }
    }

    /**
     * Sets the value of the repr property.
     *
     * @param value allowed object is
     *              {@link TAngleRepr }
     */
    public void setRepr(TAngleRepr value) {
        this.repr = value;
    }

}
