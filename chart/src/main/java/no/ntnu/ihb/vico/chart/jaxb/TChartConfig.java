
package no.ntnu.ihb.vico.chart.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for TChartConfig complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TChartConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="chart" type="{http://github.com/NTNU-IHB/Vico/schema/ChartConfig}TChart" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TChartConfig", namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", propOrder = {
        "chart"
})
public class TChartConfig {

    @XmlElement(namespace = "http://github.com/NTNU-IHB/Vico/schema/ChartConfig", required = true)
    protected List<TChart> chart;

    /**
     * Gets the value of the chart property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chart property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChart().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TChart }
     *
     *
     */
    public List<TChart> getChart() {
        if (chart == null) {
            chart = new ArrayList<TChart>();
        }
        return this.chart;
    }

}
