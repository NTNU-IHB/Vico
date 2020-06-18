
package no.ntnu.ihb.acco.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TSphere complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TSphere">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="radius" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSphere", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TSphere {

    @XmlAttribute(name = "radius", required = true)
    protected double radius;

    /**
     * Gets the value of the radius property.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     */
    public void setRadius(double value) {
        this.radius = value;
    }

}
