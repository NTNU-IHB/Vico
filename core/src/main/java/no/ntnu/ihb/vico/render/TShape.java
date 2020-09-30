
package no.ntnu.ihb.vico.render;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TShape complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TShape">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Plane" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TPlane"/>
 *         &lt;element name="Box" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TBox"/>
 *         &lt;element name="Sphere" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TSphere"/>
 *         &lt;element name="Cylinder" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TCylinder"/>
 *         &lt;element name="Capsule" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TCapsule"/>
 *         &lt;element name="Mesh" type="{http://github.com/NTNU-IHB/Vico/schema/VisualConfig}TMesh"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TShape", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", propOrder = {
        "plane",
        "box",
        "sphere",
        "cylinder",
        "capsule",
        "mesh"
})
public class TShape {

    @XmlElement(name = "Plane", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TPlane plane;
    @XmlElement(name = "Box", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TBox box;
    @XmlElement(name = "Sphere", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TSphere sphere;
    @XmlElement(name = "Cylinder", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TCylinder cylinder;
    @XmlElement(name = "Capsule", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TCapsule capsule;
    @XmlElement(name = "Mesh", namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig")
    protected TMesh mesh;

    /**
     * Gets the value of the plane property.
     *
     * @return
     *     possible object is
     *     {@link TPlane }
     *
     */
    public TPlane getPlane() {
        return plane;
    }

    /**
     * Sets the value of the plane property.
     *
     * @param value
     *     allowed object is
     *     {@link TPlane }
     *
     */
    public void setPlane(TPlane value) {
        this.plane = value;
    }

    /**
     * Gets the value of the box property.
     *
     * @return
     *     possible object is
     *     {@link TBox }
     *
     */
    public TBox getBox() {
        return box;
    }

    /**
     * Sets the value of the box property.
     *
     * @param value
     *     allowed object is
     *     {@link TBox }
     *
     */
    public void setBox(TBox value) {
        this.box = value;
    }

    /**
     * Gets the value of the sphere property.
     *
     * @return
     *     possible object is
     *     {@link TSphere }
     *
     */
    public TSphere getSphere() {
        return sphere;
    }

    /**
     * Sets the value of the sphere property.
     *
     * @param value
     *     allowed object is
     *     {@link TSphere }
     *
     */
    public void setSphere(TSphere value) {
        this.sphere = value;
    }

    /**
     * Gets the value of the cylinder property.
     *
     * @return
     *     possible object is
     *     {@link TCylinder }
     *
     */
    public TCylinder getCylinder() {
        return cylinder;
    }

    /**
     * Sets the value of the cylinder property.
     *
     * @param value
     *     allowed object is
     *     {@link TCylinder }
     *
     */
    public void setCylinder(TCylinder value) {
        this.cylinder = value;
    }

    /**
     * Gets the value of the capsule property.
     *
     * @return
     *     possible object is
     *     {@link TCapsule }
     *
     */
    public TCapsule getCapsule() {
        return capsule;
    }

    /**
     * Sets the value of the capsule property.
     *
     * @param value
     *     allowed object is
     *     {@link TCapsule }
     *
     */
    public void setCapsule(TCapsule value) {
        this.capsule = value;
    }

    /**
     * Gets the value of the mesh property.
     *
     * @return
     *     possible object is
     *     {@link TMesh }
     *
     */
    public TMesh getMesh() {
        return mesh;
    }

    /**
     * Sets the value of the mesh property.
     *
     * @param value
     *     allowed object is
     *     {@link TMesh }
     *
     */
    public void setMesh(TMesh value) {
        this.mesh = value;
    }

}
