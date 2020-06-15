
package no.ntnu.ihb.vico.log.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the no.ntnu.ihb.vico.log.jaxb package.
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

    private final static QName _LogConfig_QNAME = new QName("http://github.com/NTNU-IHB/Vico/schema/LogConfig", "LogConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: no.ntnu.ihb.vico.log.jaxb
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TLogConfig }
     *
     */
    public TLogConfig createTLogConfig() {
        return new TLogConfig();
    }

    /**
     * Create an instance of {@link TComponent }
     *
     */
    public TComponent createTComponent() {
        return new TComponent();
    }

    /**
     * Create an instance of {@link TVariable }
     *
     */
    public TVariable createTVariable() {
        return new TVariable();
    }

    /**
     * Create an instance of {@link TLinearTransformation }
     */
    public TLinearTransformation createTLinearTransformation() {
        return new TLinearTransformation();
    }

    /**
     * Create an instance of {@link TComponents }
     */
    public TComponents createTComponents() {
        return new TComponents();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TLogConfig }{@code >}}
     */
    @XmlElementDecl(namespace = "http://github.com/NTNU-IHB/Vico/schema/LogConfig", name = "LogConfig")
    public JAXBElement<TLogConfig> createLogConfig(TLogConfig value) {
        return new JAXBElement<TLogConfig>(_LogConfig_QNAME, TLogConfig.class, null, value);
    }

}
