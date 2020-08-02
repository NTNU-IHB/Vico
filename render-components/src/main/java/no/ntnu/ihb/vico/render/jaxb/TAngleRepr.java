
package no.ntnu.ihb.vico.render.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TAngleRepr.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TAngleRepr">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="deg"/>
 *     &lt;enumeration value="rad"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "TAngleRepr", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
@XmlEnum
public enum TAngleRepr {

    @XmlEnumValue("deg")
    DEG("deg"),
    @XmlEnumValue("rad")
    RAD("rad");
    private final String value;

    TAngleRepr(String v) {
        value = v;
    }

    public static TAngleRepr fromValue(String v) {
        for (TAngleRepr c : TAngleRepr.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
