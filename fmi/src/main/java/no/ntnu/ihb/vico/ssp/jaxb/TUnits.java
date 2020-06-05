
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TUnits complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TUnits">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Unit" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TUnit" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TUnits", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", propOrder = {
        "unit"
})
public class TUnits {

    @XmlElement(name = "Unit", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
    protected List<TUnit> unit;

    /**
     * Gets the value of the unit property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unit property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnit().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TUnit }
     */
    public List<TUnit> getUnit() {
        if (unit == null) {
            unit = new ArrayList<TUnit>();
        }
        return this.unit;
    }

}
