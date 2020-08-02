
package no.ntnu.ihb.vico.render.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TTransforms complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TTransforms">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Transform" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TTransform" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTransforms", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "transform"
})
public class TTransforms {

    @XmlElement(name = "Transform", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", required = true)
    protected List<TTransform> transform;

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

}
