
package no.ntnu.ihb.acco.chart.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for TTimeSeriesChart complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TTimeSeriesChart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="series" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TTimeSeries"/>
 *       &lt;/sequence>
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" default="800" />
 *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}int" default="640" />
 *       &lt;attribute name="decimationFactor" type="{http://www.w3.org/2001/XMLSchema}int" default="1" />
 *       &lt;attribute name="maxDuration" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="live" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTimeSeriesChart", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", propOrder = {
        "series"
})
public class TTimeSeriesChart {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", required = true)
    protected TTimeSeries series;
    @XmlAttribute(name = "title", required = true)
    protected String title;
    @XmlAttribute(name = "label", required = true)
    protected String label;
    @XmlAttribute(name = "width")
    protected Integer width;
    @XmlAttribute(name = "height")
    protected Integer height;
    @XmlAttribute(name = "decimationFactor")
    protected Integer decimationFactor;
    @XmlAttribute(name = "maxDuration")
    protected Double maxDuration;
    @XmlAttribute(name = "live")
    protected Boolean live;

    /**
     * Gets the value of the series property.
     *
     * @return
     *     possible object is
     *     {@link TTimeSeries }
     *
     */
    public TTimeSeries getSeries() {
        return series;
    }

    /**
     * Sets the value of the series property.
     *
     * @param value
     *     allowed object is
     *     {@link TTimeSeries }
     *
     */
    public void setSeries(TTimeSeries value) {
        this.series = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the label property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the width property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getWidth() {
        if (width == null) {
            return  800;
        } else {
            return width;
        }
    }

    /**
     * Sets the value of the width property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getHeight() {
        if (height == null) {
            return  640;
        } else {
            return height;
        }
    }

    /**
     * Sets the value of the height property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

    /**
     * Gets the value of the decimationFactor property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public int getDecimationFactor() {
        if (decimationFactor == null) {
            return  1;
        } else {
            return decimationFactor;
        }
    }

    /**
     * Sets the value of the decimationFactor property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setDecimationFactor(Integer value) {
        this.decimationFactor = value;
    }

    /**
     * Gets the value of the maxDuration property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getMaxDuration() {
        return maxDuration;
    }

    /**
     * Sets the value of the maxDuration property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setMaxDuration(Double value) {
        this.maxDuration = value;
    }

    /**
     * Gets the value of the live property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isLive() {
        if (live == null) {
            return false;
        } else {
            return live;
        }
    }

    /**
     * Sets the value of the live property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setLive(Boolean value) {
        this.live = value;
    }

}
