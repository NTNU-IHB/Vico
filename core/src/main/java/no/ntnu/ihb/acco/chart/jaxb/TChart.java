
package no.ntnu.ihb.acco.chart.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TChart complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TChart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="xyseries" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TXYSeriesChart"/>
 *         &lt;element name="timeseries" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TTimeSeriesChart"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TChart", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", propOrder = {
        "xyseries",
        "timeseries"
})
public class TChart {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig")
    protected TXYSeriesChart xyseries;
    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig")
    protected TTimeSeriesChart timeseries;

    /**
     * Gets the value of the xyseries property.
     *
     * @return
     *     possible object is
     *     {@link TXYSeriesChart }
     *
     */
    public TXYSeriesChart getXyseries() {
        return xyseries;
    }

    /**
     * Sets the value of the xyseries property.
     *
     * @param value
     *     allowed object is
     *     {@link TXYSeriesChart }
     *
     */
    public void setXyseries(TXYSeriesChart value) {
        this.xyseries = value;
    }

    /**
     * Gets the value of the timeseries property.
     *
     * @return
     *     possible object is
     *     {@link TTimeSeriesChart }
     *
     */
    public TTimeSeriesChart getTimeseries() {
        return timeseries;
    }

    /**
     * Sets the value of the timeseries property.
     *
     * @param value
     *     allowed object is
     *     {@link TTimeSeriesChart }
     *
     */
    public void setTimeseries(TTimeSeriesChart value) {
        this.timeseries = value;
    }

}
