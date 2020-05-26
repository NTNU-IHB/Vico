
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="System" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSystem"/>
 *         &lt;element name="Enumerations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TEnumerations" minOccurs="0"/>
 *         &lt;element name="Units" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TUnits" minOccurs="0"/>
 *         &lt;element name="DefaultExperiment" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TDefaultExperiment" minOccurs="0"/>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ATopLevelMetaData"/>
 *       &lt;attribute name="version" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}normalizedString">
 *             &lt;pattern value="1[.][0-9]+(-.*)?"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "system",
        "enumerations",
        "units",
        "defaultExperiment",
        "annotations"
})
@XmlRootElement(name = "SystemStructureDescription", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
public class SystemStructureDescription {

    @XmlElement(name = "System", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", required = true)
    protected TSystem system;
    @XmlElement(name = "Enumerations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TEnumerations enumerations;
    @XmlElement(name = "Units", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TUnits units;
    @XmlElement(name = "DefaultExperiment", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TDefaultExperiment defaultExperiment;
    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TAnnotations annotations;
    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String version;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "author")
    protected String author;
    @XmlAttribute(name = "fileversion")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String fileversion;
    @XmlAttribute(name = "copyright")
    protected String copyright;
    @XmlAttribute(name = "license")
    protected String license;
    @XmlAttribute(name = "generationTool")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String generationTool;
    @XmlAttribute(name = "generationDateAndTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar generationDateAndTime;

    /**
     * Gets the value of the system property.
     *
     * @return possible object is
     * {@link TSystem }
     */
    public TSystem getSystem() {
        return system;
    }

    /**
     * Sets the value of the system property.
     *
     * @param value allowed object is
     *              {@link TSystem }
     */
    public void setSystem(TSystem value) {
        this.system = value;
    }

    /**
     * Gets the value of the enumerations property.
     *
     * @return possible object is
     * {@link TEnumerations }
     */
    public TEnumerations getEnumerations() {
        return enumerations;
    }

    /**
     * Sets the value of the enumerations property.
     *
     * @param value allowed object is
     *              {@link TEnumerations }
     */
    public void setEnumerations(TEnumerations value) {
        this.enumerations = value;
    }

    /**
     * Gets the value of the units property.
     *
     * @return possible object is
     * {@link TUnits }
     */
    public TUnits getUnits() {
        return units;
    }

    /**
     * Sets the value of the units property.
     *
     * @param value allowed object is
     *              {@link TUnits }
     */
    public void setUnits(TUnits value) {
        this.units = value;
    }

    /**
     * Gets the value of the defaultExperiment property.
     *
     * @return possible object is
     * {@link TDefaultExperiment }
     */
    public TDefaultExperiment getDefaultExperiment() {
        return defaultExperiment;
    }

    /**
     * Sets the value of the defaultExperiment property.
     *
     * @param value allowed object is
     *              {@link TDefaultExperiment }
     */
    public void setDefaultExperiment(TDefaultExperiment value) {
        this.defaultExperiment = value;
    }

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
     * Gets the value of the version property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
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

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the author property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the fileversion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileversion() {
        return fileversion;
    }

    /**
     * Sets the value of the fileversion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFileversion(String value) {
        this.fileversion = value;
    }

    /**
     * Gets the value of the copyright property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the license property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the value of the license property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicense(String value) {
        this.license = value;
    }

    /**
     * Gets the value of the generationTool property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenerationTool() {
        return generationTool;
    }

    /**
     * Sets the value of the generationTool property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenerationTool(String value) {
        this.generationTool = value;
    }

    /**
     * Gets the value of the generationDateAndTime property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getGenerationDateAndTime() {
        return generationDateAndTime;
    }

    /**
     * Sets the value of the generationDateAndTime property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setGenerationDateAndTime(XMLGregorianCalendar value) {
        this.generationDateAndTime = value;
    }

}
