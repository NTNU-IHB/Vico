
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TAngleRef complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TAngleRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="repr" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TAngleRepr" default="deg" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TAngleRef", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
public class TAngleRef {

    @XmlAttribute(name = "ref")
    protected String ref;
    @XmlAttribute(name = "repr")
    protected TAngleRepr repr;

    /**
     * Gets the value of the ref property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the repr property.
     *
     * @return possible object is
     * {@link TAngleRepr }
     */
    public TAngleRepr getRepr() {
        if (repr == null) {
            return TAngleRepr.DEG;
        } else {
            return repr;
        }
    }

    /**
     * Sets the value of the repr property.
     *
     * @param value allowed object is
     *              {@link TAngleRepr }
     */
    public void setRepr(TAngleRepr value) {
        this.repr = value;
    }

}
