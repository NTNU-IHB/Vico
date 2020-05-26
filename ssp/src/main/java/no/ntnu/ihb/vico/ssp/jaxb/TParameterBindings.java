
package no.ntnu.ihb.vico.ssp.jaxb;

import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TParameterBindings complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TParameterBindings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="ParameterBinding">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ParameterValues" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ParameterMapping" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/choice>
 *                           &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-ssp-parameter-mapping" />
 *                           &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                           &lt;attribute name="sourceBase" default="SSD">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;enumeration value="SSD"/>
 *                                 &lt;enumeration value="component"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-ssp-parameter-set" />
 *                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;attribute name="sourceBase" default="SSD">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="SSD"/>
 *                       &lt;enumeration value="component"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="prefix" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
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
@XmlType(name = "TParameterBindings", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "parameterBinding"
})
public class TParameterBindings {

    @XmlElement(name = "ParameterBinding", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", required = true)
    protected List<TParameterBindings.ParameterBinding> parameterBinding;

    /**
     * Gets the value of the parameterBinding property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterBinding property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterBinding().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TParameterBindings.ParameterBinding }
     */
    public List<TParameterBindings.ParameterBinding> getParameterBinding() {
        if (parameterBinding == null) {
            parameterBinding = new ArrayList<TParameterBindings.ParameterBinding>();
        }
        return this.parameterBinding;
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
     *         &lt;element name="ParameterValues" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ParameterMapping" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/choice>
     *                 &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-ssp-parameter-mapping" />
     *                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                 &lt;attribute name="sourceBase" default="SSD">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;enumeration value="SSD"/>
     *                       &lt;enumeration value="component"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Annotations" type="{http://ssp-standard.org/SSP1/SystemStructureCommon}TAnnotations" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-ssp-parameter-set" />
     *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *       &lt;attribute name="sourceBase" default="SSD">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="SSD"/>
     *             &lt;enumeration value="component"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="prefix" type="{http://www.w3.org/2001/XMLSchema}string" default="" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "parameterValues",
            "parameterMapping",
            "annotations"
    })
    public static class ParameterBinding {

        @XmlElement(name = "ParameterValues", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected TParameterBindings.ParameterBinding.ParameterValues parameterValues;
        @XmlElement(name = "ParameterMapping", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected TParameterBindings.ParameterBinding.ParameterMapping parameterMapping;
        @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
        protected TAnnotations annotations;
        @XmlAttribute(name = "type")
        protected String type;
        @XmlAttribute(name = "source")
        @XmlSchemaType(name = "anyURI")
        protected String source;
        @XmlAttribute(name = "sourceBase")
        protected String sourceBase;
        @XmlAttribute(name = "prefix")
        protected String prefix;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "description")
        protected String description;

        /**
         * Gets the value of the parameterValues property.
         *
         * @return possible object is
         * {@link TParameterBindings.ParameterBinding.ParameterValues }
         */
        public TParameterBindings.ParameterBinding.ParameterValues getParameterValues() {
            return parameterValues;
        }

        /**
         * Sets the value of the parameterValues property.
         *
         * @param value allowed object is
         *              {@link TParameterBindings.ParameterBinding.ParameterValues }
         */
        public void setParameterValues(TParameterBindings.ParameterBinding.ParameterValues value) {
            this.parameterValues = value;
        }

        /**
         * Gets the value of the parameterMapping property.
         *
         * @return possible object is
         * {@link TParameterBindings.ParameterBinding.ParameterMapping }
         */
        public TParameterBindings.ParameterBinding.ParameterMapping getParameterMapping() {
            return parameterMapping;
        }

        /**
         * Sets the value of the parameterMapping property.
         *
         * @param value allowed object is
         *              {@link TParameterBindings.ParameterBinding.ParameterMapping }
         */
        public void setParameterMapping(TParameterBindings.ParameterBinding.ParameterMapping value) {
            this.parameterMapping = value;
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
         * Gets the value of the type property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getType() {
            if (type == null) {
                return "application/x-ssp-parameter-set";
            } else {
                return type;
            }
        }

        /**
         * Sets the value of the type property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setType(String value) {
            this.type = value;
        }

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
         * Gets the value of the sourceBase property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getSourceBase() {
            if (sourceBase == null) {
                return "SSD";
            } else {
                return sourceBase;
            }
        }

        /**
         * Sets the value of the sourceBase property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSourceBase(String value) {
            this.sourceBase = value;
        }

        /**
         * Gets the value of the prefix property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPrefix() {
            if (prefix == null) {
                return "";
            } else {
                return prefix;
            }
        }

        /**
         * Sets the value of the prefix property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPrefix(String value) {
            this.prefix = value;
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
         *       &lt;choice>
         *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/choice>
         *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="application/x-ssp-parameter-mapping" />
         *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *       &lt;attribute name="sourceBase" default="SSD">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;enumeration value="SSD"/>
         *             &lt;enumeration value="component"/>
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
                "any"
        })
        public static class ParameterMapping {

            @XmlAnyElement(lax = true)
            protected List<Object> any;
            @XmlAttribute(name = "type")
            protected String type;
            @XmlAttribute(name = "source")
            @XmlSchemaType(name = "anyURI")
            protected String source;
            @XmlAttribute(name = "sourceBase")
            protected String sourceBase;
            @XmlAttribute(name = "id")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlID
            @XmlSchemaType(name = "ID")
            protected String id;
            @XmlAttribute(name = "description")
            protected String description;

            /**
             * Gets the value of the any property.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the any property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAny().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Element }
             * {@link Object }
             */
            public List<Object> getAny() {
                if (any == null) {
                    any = new ArrayList<Object>();
                }
                return this.any;
            }

            /**
             * Gets the value of the type property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getType() {
                if (type == null) {
                    return "application/x-ssp-parameter-mapping";
                } else {
                    return type;
                }
            }

            /**
             * Sets the value of the type property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setType(String value) {
                this.type = value;
            }

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
             * Gets the value of the sourceBase property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getSourceBase() {
                if (sourceBase == null) {
                    return "SSD";
                } else {
                    return sourceBase;
                }
            }

            /**
             * Sets the value of the sourceBase property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSourceBase(String value) {
                this.sourceBase = value;
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
         *       &lt;choice>
         *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "parameterSet"
        })
        public static class ParameterValues {

            @XmlElement(name = "ParameterSet", namespace = "http://ssp-standard.org/SSP1/SystemStructureParameterValues")
            protected List<ParameterSet> parameterSet;

            public List<ParameterSet> getParameterSet() {
                if (parameterSet == null) {
                    parameterSet = new ArrayList<ParameterSet>();
                }
                return this.parameterSet;
            }

        }

    }

}
