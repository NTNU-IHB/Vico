
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TBox complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TBox">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="xExtent" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="yExtent" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="zExtent" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TBox", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TBox {

    @XmlAttribute(name = "xExtent", required = true)
    protected float xExtent;
    @XmlAttribute(name = "yExtent", required = true)
    protected float yExtent;
    @XmlAttribute(name = "zExtent", required = true)
    protected float zExtent;

    /**
     * Gets the value of the xExtent property.
     *
     */
    public float getXExtent() {
        return xExtent;
    }

    /**
     * Sets the value of the xExtent property.
     *
     */
    public void setXExtent(float value) {
        this.xExtent = value;
    }

    /**
     * Gets the value of the yExtent property.
     *
     */
    public float getYExtent() {
        return yExtent;
    }

    /**
     * Sets the value of the yExtent property.
     *
     */
    public void setYExtent(float value) {
        this.yExtent = value;
    }

    /**
     * Gets the value of the zExtent property.
     *
     */
    public float getZExtent() {
        return zExtent;
    }

    /**
     * Sets the value of the zExtent property.
     *
     */
    public void setZExtent(float value) {
        this.zExtent = value;
    }

}
