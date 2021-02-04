
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TCameraConfig complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TCameraConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="initialPosition" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TPosition"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCameraConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "initialPosition"
})
public class TCameraConfig {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected TPosition initialPosition;

    /**
     * Gets the value of the initialPosition property.
     *
     * @return possible object is
     * {@link TPosition }
     */
    public TPosition getInitialPosition() {
        return initialPosition;
    }

    /**
     * Sets the value of the initialPosition property.
     *
     * @param value allowed object is
     *              {@link TPosition }
     */
    public void setInitialPosition(TPosition value) {
        this.initialPosition = value;
    }

}
