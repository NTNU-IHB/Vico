
package no.ntnu.ihb.vico.log.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TComponent complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="variable" type="{http://github.com/NTNU-IHB/Vico/schema/LogConfig}TVariable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="decimationFactor" type="{http://www.w3.org/2001/XMLSchema}int" default="1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TComponent", namespace = "http://github.com/NTNU-IHB/Vico/schema/LogConfig", propOrder = {
        "variable"
})
public class TComponent {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/LogConfig")
    protected List<TVariable> variable;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "decimationFactor")
    protected Integer decimationFactor;

    /**
     * Gets the value of the variable property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the variable property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVariable().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TVariable }
     *
     *
     */
    public List<TVariable> getVariable() {
        if (variable == null) {
            variable = new ArrayList<TVariable>();
        }
        return this.variable;
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

    /**
     * Gets the value of the decimationFactor property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getDecimationFactor() {
        if (decimationFactor == null) {
            return  1;
        } else {
            return decimationFactor;
        }
    }

    /**
     * Sets the value of the decimationFactor property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setDecimationFactor(Integer value) {
        this.decimationFactor = value;
    }

}
