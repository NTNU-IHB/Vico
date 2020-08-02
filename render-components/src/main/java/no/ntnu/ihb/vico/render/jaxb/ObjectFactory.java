
package no.ntnu.ihb.vico.render.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the no.ntnu.ihb.acco.render.jaxb package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VisualConfig_QNAME = new QName("http://github.com/NTNU-IHB/Vico/schema/VisualConfig", "VisualConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: no.ntnu.ihb.acco.render.jaxb
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TVisualConfig }
     *
     */
    public TVisualConfig createTVisualConfig() {
        return new TVisualConfig();
    }

    /**
     * Create an instance of {@link TMesh }
     *
     */
    public TMesh createTMesh() {
        return new TMesh();
    }

    /**
     * Create an instance of {@link TSphere }
     *
     */
    public TSphere createTSphere() {
        return new TSphere();
    }

    /**
     * Create an instance of {@link TEuler }
     *
     */
    public TEuler createTEuler() {
        return new TEuler();
    }

    /**
     * Create an instance of {@link TTransforms }
     *
     */
    public TTransforms createTTransforms() {
        return new TTransforms();
    }

    /**
     * Create an instance of {@link TGeometry }
     *
     */
    public TGeometry createTGeometry() {
        return new TGeometry();
    }

    /**
     * Create an instance of {@link TBox }
     *
     */
    public TBox createTBox() {
        return new TBox();
    }

    /**
     * Create an instance of {@link TTransform }
     *
     */
    public TTransform createTTransform() {
        return new TTransform();
    }

    /**
     * Create an instance of {@link TCylinder }
     *
     */
    public TCylinder createTCylinder() {
        return new TCylinder();
    }

    /**
     * Create an instance of {@link TCapsule }
     *
     */
    public TCapsule createTCapsule() {
        return new TCapsule();
    }

    /**
     * Create an instance of {@link TShape }
     *
     */
    public TShape createTShape() {
        return new TShape();
    }

    /**
     * Create an instance of {@link TPlane }
     *
     */
    public TPlane createTPlane() {
        return new TPlane();
    }

    /**
     * Create an instance of {@link TPosition }
     *
     */
    public TPosition createTPosition() {
        return new TPosition();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVisualConfig }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://github.com/NTNU-IHB/Vico/schema/VisualConfig", name = "VisualConfig")
    public JAXBElement<TVisualConfig> createVisualConfig(TVisualConfig value) {
        return new JAXBElement<TVisualConfig>(_VisualConfig_QNAME, TVisualConfig.class, null, value);
    }

}
