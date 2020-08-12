
package no.ntnu.ihb.vico.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TPlane complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TPlane">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="width" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="height" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPlane", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TPlane {

    @XmlAttribute(name = "width", required = true)
    protected float width;
    @XmlAttribute(name = "height", required = true)
    protected float height;

    /**
     * Gets the value of the width property.
     *
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     *
     */
    public void setWidth(float value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     *
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     *
     */
    public void setHeight(float value) {
        this.height = value;
    }

}
