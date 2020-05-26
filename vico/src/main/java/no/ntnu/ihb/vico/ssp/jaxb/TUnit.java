
package no.ntnu.ihb.vico.ssp.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TUnit complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TUnit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BaseUnit">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="kg" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="s" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="A" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="K" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="mol" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="cd" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="rad" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *                 &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
 *                 &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
@XmlType(name = "TUnit", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", propOrder = {
        "baseUnit",
        "annotations"
})
public class TUnit {

    @XmlElement(name = "BaseUnit", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon", required = true)
    protected BaseUnit baseUnit;
    @XmlElement(name = "Annotations", namespace = "http://ssp-standard.org/SSP1/SystemStructureCommon")
    protected TAnnotations annotations;
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
     * Gets the value of the baseUnit property.
     *
     * @return possible object is
     * {@link BaseUnit }
     */
    public BaseUnit getBaseUnit() {
        return baseUnit;
    }

    /**
     * Sets the value of the baseUnit property.
     *
     * @param value allowed object is
     *              {@link BaseUnit }
     */
    public void setBaseUnit(BaseUnit value) {
        this.baseUnit = value;
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
     *       &lt;attribute name="kg" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="m" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="s" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="A" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="K" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="mol" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="cd" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="rad" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
     *       &lt;attribute name="factor" type="{http://www.w3.org/2001/XMLSchema}double" default="1" />
     *       &lt;attribute name="offset" type="{http://www.w3.org/2001/XMLSchema}double" default="0" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class BaseUnit {

        @XmlAttribute(name = "kg")
        protected Integer kg;
        @XmlAttribute(name = "m")
        protected Integer m;
        @XmlAttribute(name = "s")
        protected Integer s;
        @XmlAttribute(name = "A")
        protected Integer a;
        @XmlAttribute(name = "K")
        protected Integer k;
        @XmlAttribute(name = "mol")
        protected Integer mol;
        @XmlAttribute(name = "cd")
        protected Integer cd;
        @XmlAttribute(name = "rad")
        protected Integer rad;
        @XmlAttribute(name = "factor")
        protected Double factor;
        @XmlAttribute(name = "offset")
        protected Double offset;

        /**
         * Gets the value of the kg property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getKg() {
            if (kg == null) {
                return 0;
            } else {
                return kg;
            }
        }

        /**
         * Sets the value of the kg property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setKg(Integer value) {
            this.kg = value;
        }

        /**
         * Gets the value of the m property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getM() {
            if (m == null) {
                return 0;
            } else {
                return m;
            }
        }

        /**
         * Sets the value of the m property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setM(Integer value) {
            this.m = value;
        }

        /**
         * Gets the value of the s property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getS() {
            if (s == null) {
                return 0;
            } else {
                return s;
            }
        }

        /**
         * Sets the value of the s property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setS(Integer value) {
            this.s = value;
        }

        /**
         * Gets the value of the a property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getA() {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }

        /**
         * Sets the value of the a property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setA(Integer value) {
            this.a = value;
        }

        /**
         * Gets the value of the k property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getK() {
            if (k == null) {
                return 0;
            } else {
                return k;
            }
        }

        /**
         * Sets the value of the k property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setK(Integer value) {
            this.k = value;
        }

        /**
         * Gets the value of the mol property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getMol() {
            if (mol == null) {
                return 0;
            } else {
                return mol;
            }
        }

        /**
         * Sets the value of the mol property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setMol(Integer value) {
            this.mol = value;
        }

        /**
         * Gets the value of the cd property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getCd() {
            if (cd == null) {
                return 0;
            } else {
                return cd;
            }
        }

        /**
         * Sets the value of the cd property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setCd(Integer value) {
            this.cd = value;
        }

        /**
         * Gets the value of the rad property.
         *
         * @return possible object is
         * {@link Integer }
         */
        public int getRad() {
            if (rad == null) {
                return 0;
            } else {
                return rad;
            }
        }

        /**
         * Sets the value of the rad property.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setRad(Integer value) {
            this.rad = value;
        }

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
