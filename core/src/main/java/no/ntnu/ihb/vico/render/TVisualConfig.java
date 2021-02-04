
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TVisualConfig complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TVisualConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CameraConfig" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TCameraConfig" minOccurs="0"/>
 *         &lt;element name="Transform" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TTransform" maxOccurs="unbounded"/>
 *         &lt;element name="Water" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TWater" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="decimationFactor" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TVisualConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "cameraConfig",
        "transform",
        "water"
})
public class TVisualConfig {

    @XmlElement(name = "CameraConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TCameraConfig cameraConfig;
    @XmlElement(name = "Transform", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected List<TTransform> transform;
    @XmlElement(name = "Water", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TWater water;
    @XmlAttribute(name = "decimationFactor")
    @XmlSchemaType(name = "unsignedInt")
    protected Long decimationFactor;

    /**
     * Gets the value of the cameraConfig property.
     *
     * @return
     *     possible object is
     *     {@link TCameraConfig }
     *
     */
    public TCameraConfig getCameraConfig() {
        return cameraConfig;
    }

    /**
     * Sets the value of the cameraConfig property.
     *
     * @param value
     *     allowed object is
     *     {@link TCameraConfig }
     *
     */
    public void setCameraConfig(TCameraConfig value) {
        this.cameraConfig = value;
    }

    /**
     * Gets the value of the transform property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transform property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransform().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TTransform }
     *
     *
     */
    public List<TTransform> getTransform() {
        if (transform == null) {
            transform = new ArrayList<TTransform>();
        }
        return this.transform;
    }

    /**
     * Gets the value of the water property.
     *
     * @return
     *     possible object is
     *     {@link TWater }
     *
     */
    public TWater getWater() {
        return water;
    }

    /**
     * Sets the value of the water property.
     *
     * @param value
     *     allowed object is
     *     {@link TWater }
     *
     */
    public void setWater(TWater value) {
        this.water = value;
    }

    /**
     * Gets the value of the decimationFactor property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public long getDecimationFactor() {
        if (decimationFactor == null) {
            return  1L;
        } else {
            return decimationFactor;
        }
    }

    /**
     * Sets the value of the decimationFactor property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setDecimationFactor(Long value) {
        this.decimationFactor = value;
    }

}
