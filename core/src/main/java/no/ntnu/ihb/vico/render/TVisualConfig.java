
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TVisualConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "transforms"
})
public class TVisualConfig {

    @XmlElement(name = "Transforms", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected TTransforms transforms;

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

}
