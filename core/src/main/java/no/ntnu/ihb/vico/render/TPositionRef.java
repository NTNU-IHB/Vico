
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element name="xRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="yRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TPositionRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "xRef",
        "yRef",
        "zRef"
})
public class TPositionRef {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected String xRef;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected String yRef;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected String zRef;

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
     * Gets the value of the zRef property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getZRef() {
        return zRef;
    }

    /**
     * Sets the value of the zRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setZRef(String value) {
        this.zRef = value;
    }

}
