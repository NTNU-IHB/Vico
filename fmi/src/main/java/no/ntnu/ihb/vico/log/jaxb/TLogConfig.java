
package no.ntnu.ihb.vico.log.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TLogConfig complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TLogConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="components" type="{http://github.com/NTNU-IHB/Vico/schema/LogConfig}TComponents"/>
 *       &lt;/sequence>
 *       &lt;attribute name="staticFileNames" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TLogConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/LogConfig", propOrder = {
        "components"
})
public class TLogConfig {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/LogConfig", required = true)
    protected TComponents components;
    @XmlAttribute(name = "staticFileNames")
    protected Boolean staticFileNames;

    /**
     * Gets the value of the components property.
     *
     * @return
     *     possible object is
     *     {@link TComponents }
     *
     */
    public TComponents getComponents() {
        return components;
    }

    /**
     * Sets the value of the components property.
     *
     * @param value
     *     allowed object is
     *     {@link TComponents }
     *
     */
    public void setComponents(TComponents value) {
        this.components = value;
    }

    /**
     * Gets the value of the staticFileNames property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isStaticFileNames() {
        if (staticFileNames == null) {
            return false;
        } else {
            return staticFileNames;
        }
    }

    /**
     * Sets the value of the staticFileNames property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setStaticFileNames(Boolean value) {
        this.staticFileNames = value;
    }

}
