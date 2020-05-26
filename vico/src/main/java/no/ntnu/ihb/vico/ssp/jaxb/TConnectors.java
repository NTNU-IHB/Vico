
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TConnectors complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TConnectors">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Connector" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;group ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}GTypeChoice" minOccurs="0"/>
 *                   &lt;element name="ConnectorGeometry" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="kind" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="input"/>
 *                       &lt;enumeration value="output"/>
 *                       &lt;enumeration value="parameter"/>
 *                       &lt;enumeration value="calculatedParameter"/>
 *                       &lt;enumeration value="inout"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TConnectors", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "connector"
})
public class TConnectors {

    @XmlElement(name = "Connector", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", required = true)
    protected List<Connector> connector;

    /**
     * Gets the value of the connector property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connector property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnector().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Connector }
     */
    public List<Connector> getConnector() {
        if (connector == null) {
            connector = new ArrayList<Connector>();
        }
        return this.connector;
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
     *       &lt;sequence>
     *         &lt;group ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}GTypeChoice" minOccurs="0"/>
     *         &lt;element name="ConnectorGeometry" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                 &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="kind" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="input"/>
     *             &lt;enumeration value="output"/>
     *             &lt;enumeration value="parameter"/>
     *             &lt;enumeration value="calculatedParameter"/>
     *             &lt;enumeration value="inout"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "real",
            "integer",
            "_boolean",
            "string",
            "enumeration",
            "binary",
            "connectorGeometry",
            "annotations"
    })
    public static class Connector {

        @XmlElement(name = "Real", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected Real real;
        @XmlElement(name = "Integer", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected Integer integer;
        @XmlElement(name = "Boolean", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected Boolean _boolean;
        @XmlElement(name = "String", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected String string;
        @XmlElement(name = "Enumeration", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected Enumeration enumeration;
        @XmlElement(name = "Binary", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
        protected Binary binary;
        @XmlElement(name = "ConnectorGeometry", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected ConnectorGeometry connectorGeometry;
        @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected TAnnotations annotations;
        @XmlAttribute(name = "name", required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected java.lang.String name;
        @XmlAttribute(name = "kind", required = true)
        protected java.lang.String kind;
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
         * Gets the value of the connectorGeometry property.
         *
         * @return possible object is
         * {@link ConnectorGeometry }
         */
        public ConnectorGeometry getConnectorGeometry() {
            return connectorGeometry;
        }

        /**
         * Sets the value of the connectorGeometry property.
         *
         * @param value allowed object is
         *              {@link ConnectorGeometry }
         */
        public void setConnectorGeometry(ConnectorGeometry value) {
            this.connectorGeometry = value;
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
         * Gets the value of the kind property.
         *
         * @return possible object is
         * {@link java.lang.String }
         */
        public java.lang.String getKind() {
            return kind;
        }

        /**
         * Sets the value of the kind property.
         *
         * @param value allowed object is
         *              {@link java.lang.String }
         */
        public void setKind(java.lang.String value) {
            this.kind = value;
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
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Boolean {


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
         *       &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class ConnectorGeometry {

            @XmlAttribute(name = "x", required = true)
            protected double x;
            @XmlAttribute(name = "y", required = true)
            protected double y;

            /**
             * Gets the value of the x property.
             */
            public double getX() {
                return x;
            }

            /**
             * Sets the value of the x property.
             */
            public void setX(double value) {
                this.x = value;
            }

            /**
             * Gets the value of the y property.
             */
            public double getY() {
                return y;
            }

            /**
             * Sets the value of the y property.
             */
            public void setY(double value) {
                this.y = value;
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
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Enumeration {

            @XmlAttribute(name = "name", required = true)
            protected java.lang.String name;

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
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Integer {


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
         *       &lt;attribute name="unit" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Real {

            @XmlAttribute(name = "unit")
            protected java.lang.String unit;

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
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class String {


        }

    }

}
