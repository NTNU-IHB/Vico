
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TLinearTransformation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TLinearTransformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *       &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TLinearTransformation", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig")
public class TLinearTransformation {

    @XmlAttribute(name = "offset")
    protected Double offset;
    @XmlAttribute(name = "factor")
    protected Double factor;

    /**
     * Gets the value of the offset property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public double getOffset() {
        if (offset == null) {
            return  0.0D;
        } else {
            return offset;
        }
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setOffset(Double value) {
        this.offset = value;
    }

    /**
     * Gets the value of the factor property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public double getFactor() {
        if (factor == null) {
            return  1.0D;
        } else {
            return factor;
        }
    }

    /**
     * Sets the value of the factor property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setFactor(Double value) {
        this.factor = value;
    }

}
