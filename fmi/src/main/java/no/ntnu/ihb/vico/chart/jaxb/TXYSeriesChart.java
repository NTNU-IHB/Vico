
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TXYSeriesChart complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TXYSeriesChart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="series" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TXYSeries" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="xLabel" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="yLabel" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" default="800" />
 *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}int" default="640" />
 *       &lt;attribute name="decimationFactor" type="{http://www.w3.org/2001/XMLSchema}int" default="1" />
 *       &lt;attribute name="maxLength" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="live" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TXYSeriesChart", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", propOrder = {
        "series"
})
public class TXYSeriesChart {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", required = true)
    protected List<TXYSeries> series;
    @XmlAttribute(name = "title", required = true)
    protected String title;
    @XmlAttribute(name = "xLabel", required = true)
    protected String xLabel;
    @XmlAttribute(name = "yLabel", required = true)
    protected String yLabel;
    @XmlAttribute(name = "width")
    protected Integer width;
    @XmlAttribute(name = "height")
    protected Integer height;
    @XmlAttribute(name = "decimationFactor")
    protected Integer decimationFactor;
    @XmlAttribute(name = "maxLength")
    protected Integer maxLength;
    @XmlAttribute(name = "live")
    protected Boolean live;

    /**
     * Gets the value of the series property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the series property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeries().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TXYSeries }
     */
    public List<TXYSeries> getSeries() {
        if (series == null) {
            series = new ArrayList<TXYSeries>();
        }
        return this.series;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the xLabel property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getXLabel() {
        return xLabel;
    }

    /**
     * Sets the value of the xLabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXLabel(String value) {
        this.xLabel = value;
    }

    /**
     * Gets the value of the yLabel property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getYLabel() {
        return yLabel;
    }

    /**
     * Sets the value of the yLabel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setYLabel(String value) {
        this.yLabel = value;
    }

    /**
     * Gets the value of the width property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public int getWidth() {
        if (width == null) {
            return 800;
        } else {
            return width;
        }
    }

    /**
     * Sets the value of the width property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public int getHeight() {
        if (height == null) {
            return 640;
        } else {
            return height;
        }
    }

    /**
     * Sets the value of the height property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

    /**
     * Gets the value of the decimationFactor property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public int getDecimationFactor() {
        if (decimationFactor == null) {
            return 1;
        } else {
            return decimationFactor;
        }
    }

    /**
     * Sets the value of the decimationFactor property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setDecimationFactor(Integer value) {
        this.decimationFactor = value;
    }

    /**
     * Gets the value of the maxLength property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the value of the maxLength property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxLength(Integer value) {
        this.maxLength = value;
    }

    /**
     * Gets the value of the live property.
     *
     * @return possible object is
     * {@link Boolean }
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
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setLive(Boolean value) {
        this.live = value;
    }

}
