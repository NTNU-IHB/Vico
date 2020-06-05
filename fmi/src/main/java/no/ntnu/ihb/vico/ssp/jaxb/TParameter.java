
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TParameter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Real">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                   &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Integer">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Boolean">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="String">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Enumeration">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                   &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Binary">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="mime-type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/octet-stream" />
 *                   &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}hexBinary" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TParameter", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues", propOrder = {
        "real",
        "integer",
        "_boolean",
        "string",
        "enumeration",
        "binary",
        "annotations"
})
public class TParameter {

    @XmlElement(name = "Real", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected Real real;
    @XmlElement(name = "Integer", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected Integer integer;
    @XmlElement(name = "Boolean", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected Boolean _boolean;
    @XmlElement(name = "String", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected String string;
    @XmlElement(name = "Enumeration", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected Enumeration enumeration;
    @XmlElement(name = "Binary", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected Binary binary;
    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
    protected TAnnotations annotations;
    @XmlAttribute(name = "name", required = true)
    protected java.lang.String name;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;
    @XmlAttribute(name = "description")
    protected java.lang.String description;

    /**
     * Gets the value of the real property.
     *
     * @return possible object is
     * {@link Real }
     */
    public Real getReal() {
        return real;
    }

    /**
     * Sets the value of the real property.
     *
     * @param value allowed object is
     *              {@link Real }
     */
    public void setReal(Real value) {
        this.real = value;
    }

    /**
     * Gets the value of the integer property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getInteger() {
        return integer;
    }

    /**
     * Sets the value of the integer property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setInteger(Integer value) {
        this.integer = value;
    }

    /**
     * Gets the value of the boolean property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean getBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setBoolean(Boolean value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the string property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setString(String value) {
        this.string = value;
    }

    /**
     * Gets the value of the enumeration property.
     *
     * @return possible object is
     * {@link Enumeration }
     */
    public Enumeration getEnumeration() {
        return enumeration;
    }

    /**
     * Sets the value of the enumeration property.
     *
     * @param value allowed object is
     *              {@link Enumeration }
     */
    public void setEnumeration(Enumeration value) {
        this.enumeration = value;
    }

    /**
     * Gets the value of the binary property.
     *
     * @return possible object is
     * {@link Binary }
     */
    public Binary getBinary() {
        return binary;
    }

    /**
     * Sets the value of the binary property.
     *
     * @param value allowed object is
     *              {@link Binary }
     */
    public void setBinary(Binary value) {
        this.binary = value;
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
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link java.lang.String }
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link java.lang.String }
     */
    public void setName(java.lang.String value) {
        this.name = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link java.lang.String }
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link java.lang.String }
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link java.lang.String }
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link java.lang.String }
     */
    public void setDescription(java.lang.String value) {
        this.description = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="mime-type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/octet-stream" />
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}hexBinary" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Binary {

        @XmlAttribute(name = "mime-type")
        protected java.lang.String mimeType;
        @XmlAttribute(name = "value", required = true)
        @XmlJavaTypeAdapter(HexBinaryAdapter.class)
        @XmlSchemaType(name = "hexBinary")
        protected byte[] value;

        /**
         * Gets the value of the mimeType property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getMimeType() {
            if (mimeType == null) {
                return "application/octet-stream";
            } else {
                return mimeType;
            }
        }

        /**
         * Sets the value of the mimeType property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setMimeType(java.lang.String value) {
            this.mimeType = value;
        }

        /**
         * Gets the value of the value property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public byte[] getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setValue(byte[] value) {
            this.value = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Boolean {

        @XmlAttribute(name = "value", required = true)
        protected boolean value;

        /**
         * Gets the value of the value property.
         */
        public boolean isValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         */
        public void setValue(boolean value) {
            this.value = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Enumeration {

        @XmlAttribute(name = "value", required = true)
        protected java.lang.String value;
        @XmlAttribute(name = "name")
        protected java.lang.String name;

        /**
         * Gets the value of the value property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setValue(java.lang.String value) {
            this.value = value;
        }

        /**
         * Gets the value of the name property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setName(java.lang.String value) {
            this.name = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Integer {

        @XmlAttribute(name = "value", required = true)
        protected int value;

        /**
         * Gets the value of the value property.
         */
        public int getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         */
        public void setValue(int value) {
            this.value = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Real {

        @XmlAttribute(name = "value", required = true)
        protected double value;
        @XmlAttribute(name = "unit")
        protected java.lang.String unit;

        /**
         * Gets the value of the value property.
         */
        public double getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         */
        public void setValue(double value) {
            this.value = value;
        }

        /**
         * Gets the value of the unit property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getUnit() {
            return unit;
        }

        /**
         * Sets the value of the unit property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setUnit(java.lang.String value) {
            this.unit = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class String {

        @XmlAttribute(name = "value", required = true)
        protected java.lang.String value;

        /**
         * Gets the value of the value property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setValue(java.lang.String value) {
            this.value = value;
        }

    }

}
