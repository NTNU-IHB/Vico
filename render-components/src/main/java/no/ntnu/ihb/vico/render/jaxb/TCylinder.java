
package no.ntnu.ihb.vico.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TCylinder complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TCylinder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="radius" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCylinder", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TCylinder {

    @XmlAttribute(name = "radius", required = true)
    protected double radius;
    @XmlAttribute(name = "height", required = true)
    protected double height;

    /**
     * Gets the value of the radius property.
     *
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     *
     */
    public void setRadius(double value) {
        this.radius = value;
    }

    /**
     * Gets the value of the height property.
     *
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     *
     */
    public void setHeight(double value) {
        this.height = value;
    }

}
