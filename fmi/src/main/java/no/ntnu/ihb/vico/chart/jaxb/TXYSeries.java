
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TXYSeries complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TXYSeries">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TVariableIdentifier"/>
 *         &lt;element name="y" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TVariableIdentifier"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TXYSeries", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", propOrder = {
        "x",
        "y"
})
public class TXYSeries {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", required = true)
    protected TVariableIdentifier x;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", required = true)
    protected TVariableIdentifier y;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the x property.
     *
     * @return
     *     possible object is
     *     {@link TVariableIdentifier }
     *
     */
    public TVariableIdentifier getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     *
     * @param value
     *     allowed object is
     *     {@link TVariableIdentifier }
     *
     */
    public void setX(TVariableIdentifier value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     *
     * @return
     *     possible object is
     *     {@link TVariableIdentifier }
     *
     */
    public TVariableIdentifier getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     *
     * @param value
     *     allowed object is
     *     {@link TVariableIdentifier }
     *
     */
    public void setY(TVariableIdentifier value) {
        this.y = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

}
