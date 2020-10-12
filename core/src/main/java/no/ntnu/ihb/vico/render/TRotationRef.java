
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TRotationRef complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TRotationRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xRef" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TAngleRef" minOccurs="0"/>
 *         &lt;element name="yRef" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TAngleRef" minOccurs="0"/>
 *         &lt;element name="zRef" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TAngleRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRotationRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "xRef",
        "yRef",
        "zRef"
})
public class TRotationRef {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TAngleRef xRef;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TAngleRef yRef;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TAngleRef zRef;

    /**
     * Gets the value of the xRef property.
     *
     * @return possible object is
     * {@link TAngleRef }
     */
    public TAngleRef getXRef() {
        return xRef;
    }

    /**
     * Sets the value of the xRef property.
     *
     * @param value allowed object is
     *              {@link TAngleRef }
     */
    public void setXRef(TAngleRef value) {
        this.xRef = value;
    }

    /**
     * Gets the value of the yRef property.
     *
     * @return possible object is
     * {@link TAngleRef }
     */
    public TAngleRef getYRef() {
        return yRef;
    }

    /**
     * Sets the value of the yRef property.
     *
     * @param value allowed object is
     *              {@link TAngleRef }
     */
    public void setYRef(TAngleRef value) {
        this.yRef = value;
    }

    /**
     * Gets the value of the zRef property.
     *
     * @return possible object is
     * {@link TAngleRef }
     */
    public TAngleRef getZRef() {
        return zRef;
    }

    /**
     * Sets the value of the zRef property.
     *
     * @param value allowed object is
     *              {@link TAngleRef }
     */
    public void setZRef(TAngleRef value) {
        this.zRef = value;
    }

}
