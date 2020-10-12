
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="Transforms" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TTransforms"/>
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
        "transforms",
        "water"
})
public class TVisualConfig {

    @XmlElement(name = "Transforms", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected TTransforms transforms;
    @XmlElement(name = "Water", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TWater water;
    @XmlAttribute(name = "decimationFactor")
    @XmlSchemaType(name = "unsignedInt")
    protected Long decimationFactor;

    /**
     * Gets the value of the transforms property.
     *
     * @return
     *     possible object is
     *     {@link TTransforms }
     *
     */
    public TTransforms getTransforms() {
        return transforms;
    }

    /**
     * Sets the value of the transforms property.
     *
     * @param value
     *     allowed object is
     *     {@link TTransforms }
     *
     */
    public void setTransforms(TTransforms value) {
        this.transforms = value;
    }

    /**
     * Gets the value of the water property.
     *
     * @return possible object is
     * {@link TWater }
     */
    public TWater getWater() {
        return water;
    }

    /**
     * Sets the value of the water property.
     *
     * @param value allowed object is
     *              {@link TWater }
     */
    public void setWater(TWater value) {
        this.water = value;
    }

    /**
     * Gets the value of the decimationFactor property.
     *
     * @return possible object is
     * {@link Long }
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
