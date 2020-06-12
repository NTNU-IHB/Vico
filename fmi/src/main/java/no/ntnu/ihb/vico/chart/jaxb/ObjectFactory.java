
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the no.ntnu.ihb.vico.chart.jaxb package.
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

    private final static QName _ChartConfig_QNAME = new QName("http://github.com/NTNU-IHB/Vico/schema/ChartConfig", "ChartConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: no.ntnu.ihb.vico.chart.jaxb
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TChartConfig }
     *
     */
    public TChartConfig createTChartConfig() {
        return new TChartConfig();
    }

    /**
     * Create an instance of {@link TXYSeries }
     *
     */
    public TXYSeries createTXYSeries() {
        return new TXYSeries();
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
     * Create an instance of {@link TLinearTransform }
     */
    public TLinearTransform createTLinearTransform() {
        return new TLinearTransform();
    }

    /**
     * Create an instance of {@link TXYSeriesChart }
     */
    public TXYSeriesChart createTXYSeriesChart() {
        return new TXYSeriesChart();
    }

    /**
     * Create an instance of {@link TTimeSeries }
     */
    public TTimeSeries createTTimeSeries() {
        return new TTimeSeries();
    }

    /**
     * Create an instance of {@link TVariableIdentifier }
     *
     */
    public TVariableIdentifier createTVariableIdentifier() {
        return new TVariableIdentifier();
    }

    /**
     * Create an instance of {@link TChart }
     *
     */
    public TChart createTChart() {
        return new TChart();
    }

    /**
     * Create an instance of {@link TTimeSeriesChart }
     *
     */
    public TTimeSeriesChart createTTimeSeriesChart() {
        return new TTimeSeriesChart();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TChartConfig }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", name = "ChartConfig")
    public JAXBElement<TChartConfig> createChartConfig(TChartConfig value) {
        return new JAXBElement<TChartConfig>(_ChartConfig_QNAME, TChartConfig.class, null, value);
    }

}
