
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This is the base type for all elements, currently consisting of components and systems.
 *
 *
 * <p>Java class for TElement complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Connectors" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TConnectors" minOccurs="0"/>
 *         &lt;element name="ElementGeometry" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="x1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="y1" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="x2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="y2" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                 &lt;attribute name="rotation" type="{http://www.w3.org/2001/XMLSchema}double" default="0.0" />
 *                 &lt;attribute name="iconSource" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;attribute name="iconRotation" type="{http://www.w3.org/2001/XMLSchema}double" default="0.0" />
 *                 &lt;attribute name="iconFlip" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;attribute name="iconFixedAspectRatio" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ParameterBindings" type="{http://ssp-standard.org/SSP1/SystemStructureDescription}TParameterBindings" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://ssp-standard.org/SSP1/SystemStructureCommon}ABaseElement"/>
 *       &lt;attribute name="name" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TElement", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription", propOrder = {
        "connectors",
        "elementGeometry",
        "parameterBindings"
})
@XmlSeeAlso({
        TSystem.class,
        TComponent.class,
        TSignalDictionaryReference.class
})
public class TElement {

    @XmlElement(name = "Connectors", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TConnectors connectors;
    @XmlElement(name = "ElementGeometry", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected ElementGeometry elementGeometry;
    @XmlElement(name = "ParameterBindings", namespace = "http://ssp-standard.org/SSP1/SystemStructureDescription")
    protected TParameterBindings parameterBindings;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the connectors property.
     *
     * @return possible object is
     * {@link TConnectors }
     */
    public TConnectors getConnectors() {
        return connectors;
    }

    /**
     * Sets the value of the connectors property.
     *
     * @param value allowed object is
     *              {@link TConnectors }
     */
    public void setConnectors(TConnectors value) {
        this.connectors = value;
    }

    /**
     * Gets the value of the elementGeometry property.
     *
     * @return possible object is
     * {@link ElementGeometry }
     */
    public ElementGeometry getElementGeometry() {
        return elementGeometry;
    }

    /**
     * Sets the value of the elementGeometry property.
     *
     * @param value allowed object is
     *              {@link ElementGeometry }
     */
    public void setElementGeometry(ElementGeometry value) {
        this.elementGeometry = value;
    }

    /**
     * Gets the value of the parameterBindings property.
     *
     * @return possible object is
     * {@link TParameterBindings }
     */
    public TParameterBindings getParameterBindings() {
        return parameterBindings;
    }

    /**
     * Sets the value of the parameterBindings property.
     *
     * @param value allowed object is
     *              {@link TParameterBindings }
     */
    public void setParameterBindings(TParameterBindings value) {
        this.parameterBindings = value;
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
     *       &lt;attribute name="rotation" type="{http://www.w3.org/2001/XMLSchema}double" default="0.0" />
     *       &lt;attribute name="iconSource" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *       &lt;attribute name="iconRotation" type="{http://www.w3.org/2001/XMLSchema}double" default="0.0" />
     *       &lt;attribute name="iconFlip" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="iconFixedAspectRatio" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ElementGeometry {

        @XmlAttribute(name = "x1", required = true)
        protected double x1;
        @XmlAttribute(name = "y1", required = true)
        protected double y1;
        @XmlAttribute(name = "x2", required = true)
        protected double x2;
        @XmlAttribute(name = "y2", required = true)
        protected double y2;
        @XmlAttribute(name = "rotation")
        protected Double rotation;
        @XmlAttribute(name = "iconSource")
        @XmlSchemaType(name = "anyURI")
        protected String iconSource;
        @XmlAttribute(name = "iconRotation")
        protected Double iconRotation;
        @XmlAttribute(name = "iconFlip")
        protected Boolean iconFlip;
        @XmlAttribute(name = "iconFixedAspectRatio")
        protected Boolean iconFixedAspectRatio;

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
         * Gets the value of the rotation property.
         *
         * @return possible object is
         * {@link Double }
         */
        public double getRotation() {
            if (rotation == null) {
                return 0.0D;
            } else {
                return rotation;
            }
        }

        /**
         * Sets the value of the rotation property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setRotation(Double value) {
            this.rotation = value;
        }

        /**
         * Gets the value of the iconSource property.
         *
         * @return possible object is
         * {@link String }
         */
        public String getIconSource() {
            return iconSource;
        }

        /**
         * Sets the value of the iconSource property.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setIconSource(String value) {
            this.iconSource = value;
        }

        /**
         * Gets the value of the iconRotation property.
         *
         * @return possible object is
         * {@link Double }
         */
        public double getIconRotation() {
            if (iconRotation == null) {
                return 0.0D;
            } else {
                return iconRotation;
            }
        }

        /**
         * Sets the value of the iconRotation property.
         *
         * @param value allowed object is
         *              {@link Double }
         */
        public void setIconRotation(Double value) {
            this.iconRotation = value;
        }

        /**
         * Gets the value of the iconFlip property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isIconFlip() {
            if (iconFlip == null) {
                return false;
            } else {
                return iconFlip;
            }
        }

        /**
         * Sets the value of the iconFlip property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setIconFlip(Boolean value) {
            this.iconFlip = value;
        }

        /**
         * Gets the value of the iconFixedAspectRatio property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isIconFixedAspectRatio() {
            if (iconFixedAspectRatio == null) {
                return false;
            } else {
                return iconFixedAspectRatio;
            }
        }

        /**
         * Sets the value of the iconFixedAspectRatio property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setIconFixedAspectRatio(Boolean value) {
            this.iconFixedAspectRatio = value;
        }

    }

}
