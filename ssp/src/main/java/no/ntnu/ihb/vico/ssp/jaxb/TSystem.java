
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * This element describes a system, which can contain components and other systems as elements,
 * connectors as an interface to the outside world, and connections connecting the connectors
 * of itself and of its elements to another.
 *
 *
 * <p>Java class for TSystem complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TSystem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ssp-standard.org/SSP1/SystemStructureDescription}TElement">
 *       &lt;sequence>
 *         &lt;element name="Elements" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="unbounded">
 *                     &lt;element name="Component" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TComponent"/>
 *                     &lt;element name="SignalDictionaryReference" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSignalDictionaryReference"/>
 *                     &lt;element name="System" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSystem"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Connections" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Connection" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;group ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}GTransformationChoice" minOccurs="0"/>
 *                             &lt;element name="ConnectionGeometry" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="pointsX" use="required">
 *                                       &lt;simpleType>
 *                                         &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                     &lt;attribute name="pointsY" use="required">
 *                                       &lt;simpleType>
 *                                         &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *                           &lt;attribute name="startElement" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="startConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="endElement" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="endConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="suppressUnitConversion" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SignalDictionaries" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSignalDictionaries" minOccurs="0"/>
 *         &lt;element name="SystemGeometry" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GraphicalElements" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="unbounded">
 *                     &lt;element name="Note">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                             &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                             &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                             &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                             &lt;attribute name="text" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSystem", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "elements",
        "connections",
        "signalDictionaries",
        "systemGeometry",
        "graphicalElements",
        "annotations"
})
public class TSystem
        extends TElement {

    @XmlElement(name = "Elements", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TSystem.Elements elements;
    @XmlElement(name = "Connections", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TSystem.Connections connections;
    @XmlElement(name = "SignalDictionaries", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TSignalDictionaries signalDictionaries;
    @XmlElement(name = "SystemGeometry", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TSystem.SystemGeometry systemGeometry;
    @XmlElement(name = "GraphicalElements", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TSystem.GraphicalElements graphicalElements;
    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TAnnotations annotations;

    /**
     * Gets the value of the elements property.
     *
     * @return possible object is
     * {@link TSystem.Elements }
     */
    public TSystem.Elements getElements() {
        return elements;
    }

    /**
     * Sets the value of the elements property.
     *
     * @param value allowed object is
     *              {@link TSystem.Elements }
     */
    public void setElements(TSystem.Elements value) {
        this.elements = value;
    }

    /**
     * Gets the value of the connections property.
     *
     * @return possible object is
     * {@link TSystem.Connections }
     */
    public TSystem.Connections getConnections() {
        return connections;
    }

    /**
     * Sets the value of the connections property.
     *
     * @param value allowed object is
     *              {@link TSystem.Connections }
     */
    public void setConnections(TSystem.Connections value) {
        this.connections = value;
    }

    /**
     * Gets the value of the signalDictionaries property.
     *
     * @return possible object is
     * {@link TSignalDictionaries }
     */
    public TSignalDictionaries getSignalDictionaries() {
        return signalDictionaries;
    }

    /**
     * Sets the value of the signalDictionaries property.
     *
     * @param value allowed object is
     *              {@link TSignalDictionaries }
     */
    public void setSignalDictionaries(TSignalDictionaries value) {
        this.signalDictionaries = value;
    }

    /**
     * Gets the value of the systemGeometry property.
     *
     * @return possible object is
     * {@link TSystem.SystemGeometry }
     */
    public TSystem.SystemGeometry getSystemGeometry() {
        return systemGeometry;
    }

    /**
     * Sets the value of the systemGeometry property.
     *
     * @param value allowed object is
     *              {@link TSystem.SystemGeometry }
     */
    public void setSystemGeometry(TSystem.SystemGeometry value) {
        this.systemGeometry = value;
    }

    /**
     * Gets the value of the graphicalElements property.
     *
     * @return possible object is
     * {@link TSystem.GraphicalElements }
     */
    public TSystem.GraphicalElements getGraphicalElements() {
        return graphicalElements;
    }

    /**
     * Sets the value of the graphicalElements property.
     *
     * @param value allowed object is
     *              {@link TSystem.GraphicalElements }
     */
    public void setGraphicalElements(TSystem.GraphicalElements value) {
        this.graphicalElements = value;
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
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Connection" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;group ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}GTransformationChoice" minOccurs="0"/>
     *                   &lt;element name="ConnectionGeometry" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="pointsX" use="required">
     *                             &lt;simpleType>
     *                               &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                           &lt;attribute name="pointsY" use="required">
     *                             &lt;simpleType>
     *                               &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
     *                 &lt;attribute name="startElement" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="startConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="endElement" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="endConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="suppressUnitConversion" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
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
    @XmlType(name = "", propOrder = {
            "connection"
    })
    public static class Connections {

        @XmlElement(name = "Connection", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", required = true)
        protected List<TSystem.Connections.Connection> connection;

        /**
         * Gets the value of the connection property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the connection property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConnection().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TSystem.Connections.Connection }
         */
        public List<TSystem.Connections.Connection> getConnection() {
            if (connection == null) {
                connection = new ArrayList<TSystem.Connections.Connection>();
            }
            return this.connection;
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
         *         &lt;group ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}GTransformationChoice" minOccurs="0"/>
         *         &lt;element name="ConnectionGeometry" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="pointsX" use="required">
         *                   &lt;simpleType>
         *                     &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *                 &lt;attribute name="pointsY" use="required">
         *                   &lt;simpleType>
         *                     &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
         *       &lt;attribute name="startElement" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="startConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="endElement" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="endConnector" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="suppressUnitConversion" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "linearTransformation",
                "booleanMappingTransformation",
                "integerMappingTransformation",
                "enumerationMappingTransformation",
                "connectionGeometry",
                "annotations"
        })
        public static class Connection {

            @XmlElement(name = "LinearTransformation", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
            protected TSystem.Connections.Connection.LinearTransformation linearTransformation;
            @XmlElement(name = "BooleanMappingTransformation", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
            protected TSystem.Connections.Connection.BooleanMappingTransformation booleanMappingTransformation;
            @XmlElement(name = "IntegerMappingTransformation", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
            protected TSystem.Connections.Connection.IntegerMappingTransformation integerMappingTransformation;
            @XmlElement(name = "EnumerationMappingTransformation", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
            protected TSystem.Connections.Connection.EnumerationMappingTransformation enumerationMappingTransformation;
            @XmlElement(name = "ConnectionGeometry", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
            protected TSystem.Connections.Connection.ConnectionGeometry connectionGeometry;
            @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
            protected TAnnotations annotations;
            @XmlAttribute(name = "startElement")
            protected String startElement;
            @XmlAttribute(name = "startConnector", required = true)
            protected String startConnector;
            @XmlAttribute(name = "endElement")
            protected String endElement;
            @XmlAttribute(name = "endConnector", required = true)
            protected String endConnector;
            @XmlAttribute(name = "suppressUnitConversion")
            protected Boolean suppressUnitConversion;
            @XmlAttribute(name = "id")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlID
            @XmlSchemaType(name = "ID")
            protected String id;
            @XmlAttribute(name = "description")
            protected String description;

            /**
             * Gets the value of the linearTransformation property.
             *
             * @return possible object is
             * {@link TSystem.Connections.Connection.LinearTransformation }
             */
            public TSystem.Connections.Connection.LinearTransformation getLinearTransformation() {
                return linearTransformation;
            }

            /**
             * Sets the value of the linearTransformation property.
             *
             * @param value allowed object is
             *              {@link TSystem.Connections.Connection.LinearTransformation }
             */
            public void setLinearTransformation(TSystem.Connections.Connection.LinearTransformation value) {
                this.linearTransformation = value;
            }

            /**
             * Gets the value of the booleanMappingTransformation property.
             *
             * @return possible object is
             * {@link TSystem.Connections.Connection.BooleanMappingTransformation }
             */
            public TSystem.Connections.Connection.BooleanMappingTransformation getBooleanMappingTransformation() {
                return booleanMappingTransformation;
            }

            /**
             * Sets the value of the booleanMappingTransformation property.
             *
             * @param value allowed object is
             *              {@link TSystem.Connections.Connection.BooleanMappingTransformation }
             */
            public void setBooleanMappingTransformation(TSystem.Connections.Connection.BooleanMappingTransformation value) {
                this.booleanMappingTransformation = value;
            }

            /**
             * Gets the value of the integerMappingTransformation property.
             *
             * @return possible object is
             * {@link TSystem.Connections.Connection.IntegerMappingTransformation }
             */
            public TSystem.Connections.Connection.IntegerMappingTransformation getIntegerMappingTransformation() {
                return integerMappingTransformation;
            }

            /**
             * Sets the value of the integerMappingTransformation property.
             *
             * @param value allowed object is
             *              {@link TSystem.Connections.Connection.IntegerMappingTransformation }
             */
            public void setIntegerMappingTransformation(TSystem.Connections.Connection.IntegerMappingTransformation value) {
                this.integerMappingTransformation = value;
            }

            /**
             * Gets the value of the enumerationMappingTransformation property.
             *
             * @return possible object is
             * {@link TSystem.Connections.Connection.EnumerationMappingTransformation }
             */
            public TSystem.Connections.Connection.EnumerationMappingTransformation getEnumerationMappingTransformation() {
                return enumerationMappingTransformation;
            }

            /**
             * Sets the value of the enumerationMappingTransformation property.
             *
             * @param value allowed object is
             *              {@link TSystem.Connections.Connection.EnumerationMappingTransformation }
             */
            public void setEnumerationMappingTransformation(TSystem.Connections.Connection.EnumerationMappingTransformation value) {
                this.enumerationMappingTransformation = value;
            }

            /**
             * Gets the value of the connectionGeometry property.
             *
             * @return possible object is
             * {@link TSystem.Connections.Connection.ConnectionGeometry }
             */
            public TSystem.Connections.Connection.ConnectionGeometry getConnectionGeometry() {
                return connectionGeometry;
            }

            /**
             * Sets the value of the connectionGeometry property.
             *
             * @param value allowed object is
             *              {@link TSystem.Connections.Connection.ConnectionGeometry }
             */
            public void setConnectionGeometry(TSystem.Connections.Connection.ConnectionGeometry value) {
                this.connectionGeometry = value;
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
             * Gets the value of the startElement property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getStartElement() {
                return startElement;
            }

            /**
             * Sets the value of the startElement property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setStartElement(String value) {
                this.startElement = value;
            }

            /**
             * Gets the value of the startConnector property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getStartConnector() {
                return startConnector;
            }

            /**
             * Sets the value of the startConnector property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setStartConnector(String value) {
                this.startConnector = value;
            }

            /**
             * Gets the value of the endElement property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getEndElement() {
                return endElement;
            }

            /**
             * Sets the value of the endElement property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setEndElement(String value) {
                this.endElement = value;
            }

            /**
             * Gets the value of the endConnector property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getEndConnector() {
                return endConnector;
            }

            /**
             * Sets the value of the endConnector property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setEndConnector(String value) {
                this.endConnector = value;
            }

            /**
             * Gets the value of the suppressUnitConversion property.
             *
             * @return possible object is
             * {@link Boolean }
             */
            public boolean isSuppressUnitConversion() {
                if (suppressUnitConversion == null) {
                    return false;
                } else {
                    return suppressUnitConversion;
                }
            }

            /**
             * Sets the value of the suppressUnitConversion property.
             *
             * @param value allowed object is
             *              {@link Boolean }
             */
            public void setSuppressUnitConversion(Boolean value) {
                this.suppressUnitConversion = value;
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
             * <p>Java class for anonymous complex type.
             *
             * <p>The following schema fragment specifies the expected content contained within this class.
             *
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="MapEntry" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
             *                 &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
            @XmlType(name = "", propOrder = {
                    "mapEntry"
            })
            public static class BooleanMappingTransformation {

                @XmlElement(name = "MapEntry", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
                protected List<TSystem.Connections.Connection.BooleanMappingTransformation.MapEntry> mapEntry;

                /**
                 * Gets the value of the mapEntry property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the mapEntry property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMapEntry().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TSystem.Connections.Connection.BooleanMappingTransformation.MapEntry }
                 */
                public List<TSystem.Connections.Connection.BooleanMappingTransformation.MapEntry> getMapEntry() {
                    if (mapEntry == null) {
                        mapEntry = new ArrayList<TSystem.Connections.Connection.BooleanMappingTransformation.MapEntry>();
                    }
                    return this.mapEntry;
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
                 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
                 *       &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class MapEntry {

                    @XmlAttribute(name = "source", required = true)
                    protected boolean source;
                    @XmlAttribute(name = "target", required = true)
                    protected boolean target;

                    /**
                     * Gets the value of the source property.
                     */
                    public boolean isSource() {
                        return source;
                    }

                    /**
                     * Sets the value of the source property.
                     */
                    public void setSource(boolean value) {
                        this.source = value;
                    }

                    /**
                     * Gets the value of the target property.
                     */
                    public boolean isTarget() {
                        return target;
                    }

                    /**
                     * Sets the value of the target property.
                     */
                    public void setTarget(boolean value) {
                        this.target = value;
                    }

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
             *       &lt;attribute name="pointsX" use="required">
             *         &lt;simpleType>
             *           &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *       &lt;attribute name="pointsY" use="required">
             *         &lt;simpleType>
             *           &lt;list itemType="{http://www.w3.org/2001/XMLSchema}double" />
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ConnectionGeometry {

                @XmlAttribute(name = "pointsX", required = true)
                protected List<Double> pointsX;
                @XmlAttribute(name = "pointsY", required = true)
                protected List<Double> pointsY;

                /**
                 * Gets the value of the pointsX property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the pointsX property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPointsX().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 */
                public List<Double> getPointsX() {
                    if (pointsX == null) {
                        pointsX = new ArrayList<Double>();
                    }
                    return this.pointsX;
                }

                /**
                 * Gets the value of the pointsY property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the pointsY property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPointsY().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 */
                public List<Double> getPointsY() {
                    if (pointsY == null) {
                        pointsY = new ArrayList<Double>();
                    }
                    return this.pointsY;
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
             *       &lt;sequence>
             *         &lt;element name="MapEntry" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                 &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
            @XmlType(name = "", propOrder = {
                    "mapEntry"
            })
            public static class EnumerationMappingTransformation {

                @XmlElement(name = "MapEntry", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
                protected List<TSystem.Connections.Connection.EnumerationMappingTransformation.MapEntry> mapEntry;

                /**
                 * Gets the value of the mapEntry property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the mapEntry property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMapEntry().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TSystem.Connections.Connection.EnumerationMappingTransformation.MapEntry }
                 */
                public List<TSystem.Connections.Connection.EnumerationMappingTransformation.MapEntry> getMapEntry() {
                    if (mapEntry == null) {
                        mapEntry = new ArrayList<TSystem.Connections.Connection.EnumerationMappingTransformation.MapEntry>();
                    }
                    return this.mapEntry;
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
                 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *       &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class MapEntry {

                    @XmlAttribute(name = "source", required = true)
                    protected String source;
                    @XmlAttribute(name = "target", required = true)
                    protected String target;

                    /**
                     * Gets the value of the source property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getSource() {
                        return source;
                    }

                    /**
                     * Sets the value of the source property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setSource(String value) {
                        this.source = value;
                    }

                    /**
                     * Gets the value of the target property.
                     *
                     * @return possible object is
                     * {@link String }
                     */
                    public String getTarget() {
                        return target;
                    }

                    /**
                     * Sets the value of the target property.
                     *
                     * @param value allowed object is
                     *              {@link String }
                     */
                    public void setTarget(String value) {
                        this.target = value;
                    }

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
             *       &lt;sequence>
             *         &lt;element name="MapEntry" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
             *                 &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
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
            @XmlType(name = "", propOrder = {
                    "mapEntry"
            })
            public static class IntegerMappingTransformation {

                @XmlElement(name = "MapEntry", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
                protected List<TSystem.Connections.Connection.IntegerMappingTransformation.MapEntry> mapEntry;

                /**
                 * Gets the value of the mapEntry property.
                 *
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the mapEntry property.
                 *
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getMapEntry().add(newItem);
                 * </pre>
                 *
                 *
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TSystem.Connections.Connection.IntegerMappingTransformation.MapEntry }
                 */
                public List<TSystem.Connections.Connection.IntegerMappingTransformation.MapEntry> getMapEntry() {
                    if (mapEntry == null) {
                        mapEntry = new ArrayList<TSystem.Connections.Connection.IntegerMappingTransformation.MapEntry>();
                    }
                    return this.mapEntry;
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
                 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
                 *       &lt;attribute name="target" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class MapEntry {

                    @XmlAttribute(name = "source", required = true)
                    protected int source;
                    @XmlAttribute(name = "target", required = true)
                    protected int target;

                    /**
                     * Gets the value of the source property.
                     */
                    public int getSource() {
                        return source;
                    }

                    /**
                     * Sets the value of the source property.
                     */
                    public void setSource(int value) {
                        this.source = value;
                    }

                    /**
                     * Gets the value of the target property.
                     */
                    public int getTarget() {
                        return target;
                    }

                    /**
                     * Sets the value of the target property.
                     */
                    public void setTarget(int value) {
                        this.target = value;
                    }

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
             *       &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1.0" />
             *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0.0" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LinearTransformation {

                @XmlAttribute(name = "factor")
                protected Double factor;
                @XmlAttribute(name = "offset")
                protected Double offset;

                /**
                 * Gets the value of the factor property.
                 *
                 * @return possible object is
                 * {@link Double }
                 */
                public double getFactor() {
                    if (factor == null) {
                        return 1.0D;
                    } else {
                        return factor;
                    }
                }

                /**
                 * Sets the value of the factor property.
                 *
                 * @param value allowed object is
                 *              {@link Double }
                 */
                public void setFactor(Double value) {
                    this.factor = value;
                }

                /**
                 * Gets the value of the offset property.
                 *
                 * @return possible object is
                 * {@link Double }
                 */
                public double getOffset() {
                    if (offset == null) {
                        return 0.0D;
                    } else {
                        return offset;
                    }
                }

                /**
                 * Sets the value of the offset property.
                 *
                 * @param value allowed object is
                 *              {@link Double }
                 */
                public void setOffset(Double value) {
                    this.offset = value;
                }

            }

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
     *       &lt;sequence>
     *         &lt;choice maxOccurs="unbounded">
     *           &lt;element name="Component" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TComponent"/>
     *           &lt;element name="SignalDictionaryReference" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSignalDictionaryReference"/>
     *           &lt;element name="System" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TSystem"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Elements {

        @XmlElement(name = "Component", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", type = TComponent.class)
        protected List<TComponent> components;

        @XmlElement(name = "SignalDictionaryReference", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", type = TSignalDictionaryReference.class)
        protected List<TSignalDictionaryReference> signalDictionaryReferences;

        @XmlElement(name = "System", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", type = TSystem.class)
        protected List<TSystem> systems;

        public List<TComponent> getComponents() {
            if (components == null) {
                components = new ArrayList<>();
            }
            return components;
        }

        public List<TSignalDictionaryReference> getSignalDictionaryReferences() {
            if (signalDictionaryReferences == null) {
                signalDictionaryReferences = new ArrayList<>();
            }
            return signalDictionaryReferences;
        }

        public List<TSystem> getSystems() {
            if (systems == null) {
                systems = new ArrayList<>();
            }
            return systems;
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
     *       &lt;sequence>
     *         &lt;choice maxOccurs="unbounded">
     *           &lt;element name="Note">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                   &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                   &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                   &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *                   &lt;attribute name="text" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "note"
    })
    public static class GraphicalElements {

        @XmlElement(name = "Note", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected List<TSystem.GraphicalElements.Note> note;

        /**
         * Gets the value of the note property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the note property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getNote().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TSystem.GraphicalElements.Note }
         */
        public List<TSystem.GraphicalElements.Note> getNote() {
            if (note == null) {
                note = new ArrayList<TSystem.GraphicalElements.Note>();
            }
            return this.note;
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
         *       &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
         *       &lt;attribute name="text" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Note {

            @XmlAttribute(name = "x1", required = true)
            protected double x1;
            @XmlAttribute(name = "y1", required = true)
            protected double y1;
            @XmlAttribute(name = "x2", required = true)
            protected double x2;
            @XmlAttribute(name = "y2", required = true)
            protected double y2;
            @XmlAttribute(name = "text", required = true)
            protected String text;

            /**
             * Gets the value of the x1 property.
             */
            public double getX1() {
                return x1;
            }

            /**
             * Sets the value of the x1 property.
             */
            public void setX1(double value) {
                this.x1 = value;
            }

            /**
             * Gets the value of the y1 property.
             */
            public double getY1() {
                return y1;
            }

            /**
             * Sets the value of the y1 property.
             */
            public void setY1(double value) {
                this.y1 = value;
            }

            /**
             * Gets the value of the x2 property.
             */
            public double getX2() {
                return x2;
            }

            /**
             * Sets the value of the x2 property.
             */
            public void setX2(double value) {
                this.x2 = value;
            }

            /**
             * Gets the value of the y2 property.
             */
            public double getY2() {
                return y2;
            }

            /**
             * Sets the value of the y2 property.
             */
            public void setY2(double value) {
                this.y2 = value;
            }

            /**
             * Gets the value of the text property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getText() {
                return text;
            }

            /**
             * Sets the value of the text property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setText(String value) {
                this.text = value;
            }

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
     *       &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *       &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SystemGeometry {

        @XmlAttribute(name = "x1", required = true)
        protected double x1;
        @XmlAttribute(name = "y1", required = true)
        protected double y1;
        @XmlAttribute(name = "x2", required = true)
        protected double x2;
        @XmlAttribute(name = "y2", required = true)
        protected double y2;

        /**
         * Gets the value of the x1 property.
         */
        public double getX1() {
            return x1;
        }

        /**
         * Sets the value of the x1 property.
         */
        public void setX1(double value) {
            this.x1 = value;
        }

        /**
         * Gets the value of the y1 property.
         */
        public double getY1() {
            return y1;
        }

        /**
         * Sets the value of the y1 property.
         */
        public void setY1(double value) {
            this.y1 = value;
        }

        /**
         * Gets the value of the x2 property.
         */
        public double getX2() {
            return x2;
        }

        /**
         * Sets the value of the x2 property.
         */
        public void setX2(double value) {
            this.x2 = value;
        }

        /**
         * Gets the value of the y2 property.
         */
        public double getY2() {
            return y2;
        }

        /**
         * Sets the value of the y2 property.
         */
        public void setY2(double value) {
            this.y2 = value;
        }

    }

}
