
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;


/**
 * The attributes of this element, if present, specify reasonable default values
 * for running an experiment of the given system.  Any tool is free to ignore this
 * information.
 *
 *
 * <p>Java class for TDefaultExperiment complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TDefaultExperiment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="startTime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="stopTime" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDefaultExperiment", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "annotations"
})
public class TDefaultExperiment {

    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TAnnotations annotations;
    @XmlAttribute(name = "startTime")
    protected Double startTime;
    @XmlAttribute(name = "stopTime")
    protected Double stopTime;

    /**
     * Gets the value of the annotations property.
     *
     * @return possible object is
     * {@link TAnnotations }
     */
    public TAnnotations getAnnotations() {
        return annotations;
    }

    /**
     * Sets the value of the annotations property.
     *
     * @param value allowed object is
     *              {@link TAnnotations }
     */
    public void setAnnotations(TAnnotations value) {
        this.annotations = value;
    }

    /**
     * Gets the value of the startTime property.
     *
     * @return possible object is
     * {@link Double }
     */
    public Double getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setStartTime(Double value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the stopTime property.
     *
     * @return possible object is
     * {@link Double }
     */
    public Double getStopTime() {
        return stopTime;
    }

    /**
     * Sets the value of the stopTime property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setStopTime(Double value) {
        this.stopTime = value;
    }

}
