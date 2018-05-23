package es.uca.wolidays.frontend.views.gestor;

import javax.annotation.PostConstruct;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = FacturacionView.VIEW_NAME)
public class FacturacionView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "facturacion";
	
	private HorizontalLayout facturacionLayout;
	
	private VerticalLayout historicoLayout;
	private Label historicoTitle;
	private Chart historicoChart;
	
	private VerticalLayout totalReservasLayout;
	private Label totalTitle;

	@PostConstruct
	void init() {
		facturacionLayout = new HorizontalLayout();
		facturacionLayout.setMargin(false);
		
		historicoLayout = new VerticalLayout();
		
		historicoTitle = new Label();
		historicoTitle.setCaptionAsHtml(true);
		historicoTitle.setCaption("<h2>Histórico de facturación</h2>");
		
		totalReservasLayout = new VerticalLayout();
		totalTitle = new Label();
		totalTitle.setCaptionAsHtml(true);
		totalTitle.setCaption("<h2>Total de reservas</h2>");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		historicoChart = crearGraficaHistorico();
		
		historicoLayout.addComponents(historicoTitle, historicoChart);
		historicoLayout.setComponentAlignment(historicoTitle, Alignment.TOP_CENTER);
		historicoLayout.setComponentAlignment(historicoChart, Alignment.TOP_CENTER);
		
		totalReservasLayout.addComponents(totalTitle);
		totalReservasLayout.setComponentAlignment(totalTitle, Alignment.TOP_CENTER);
		
		facturacionLayout.addComponents(historicoLayout, totalReservasLayout);	
		addComponent(facturacionLayout);
	}
	
	private Chart crearGraficaHistorico() {
		Chart chart = new Chart(ChartType.COLUMN);
		Configuration conf = chart.getConfiguration();
		
		conf.setTitle("Ganancias totales por año");
		
		XAxis x = new XAxis();
		x.setCategories("2014", "2015", "2016", "2017", "2018 (actual)");
		conf.addxAxis(x);
		
		YAxis y = new YAxis();
		y.setMin(0);
		y.setTitle("Ganancias (euros)");
		conf.addyAxis(y);
		
		Tooltip tooltip = new Tooltip();
		tooltip.setFormatter("this.x +': '+ this.y +'€'");
        conf.setTooltip(tooltip);
        
        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);
        
        conf.addSeries(new ListSeries("Ganancias", 30000, 45000, 60000, 58000, 25000));
        
        chart.drawChart(conf);
		
		return chart;
	}
	
}
