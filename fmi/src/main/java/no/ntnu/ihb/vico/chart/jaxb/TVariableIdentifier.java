
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TVariableIdentifier complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TVariableIdentifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="component" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="variable" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TVariableIdentifier", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig")
public class TVariableIdentifier {

    @XmlAttribute(name = "component", required = true)
    protected String component;
    @XmlAttribute(name = "variable", required = true)
    protected String variable;

    /**
     * Gets the value of the component property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getComponent() {
        return component;
    }

    /**
     * Sets the value of the component property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComponent(String value) {
        this.component = value;
    }

    /**
     * Gets the value of the variable property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVariable(String value) {
        this.variable = value;
    }

}
