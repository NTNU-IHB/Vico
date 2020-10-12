
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TRealRef complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TRealRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="linearTransformation" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TLinearTransformation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRealRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "linearTransformation"
})
public class TRealRef {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TLinearTransformation linearTransformation;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the linearTransformation property.
     *
     * @return possible object is
     * {@link TLinearTransformation }
     */
    public TLinearTransformation getLinearTransformation() {
        return linearTransformation;
    }

    /**
     * Sets the value of the linearTransformation property.
     *
     * @param value allowed object is
     *              {@link TLinearTransformation }
     */
    public void setLinearTransformation(TLinearTransformation value) {
        this.linearTransformation = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

}
