package es.uca.wolidays.frontend.views.gestor;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsPie;
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

import es.uca.wolidays.backend.services.ReservaService;
import es.uca.wolidays.backend.services.TransaccionService;

@SpringView(name = FacturacionView.VIEW_NAME)
public class FacturacionView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "facturacion";
	
	@Autowired
	TransaccionService dwh;
	
	@Autowired
	ReservaService rsSer;
	
	private HorizontalLayout facturacionLayout;
	
	private VerticalLayout historicoLayout;
	private Label historicoTitle;
	private Chart historicoChart;
	private Chart reservasChart;
	private List<Object[]> datosGanancias;
	private List<Object[]> datosReservas;
	private int totalReservas;
	
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
		
		totalReservas = 0;
		
		totalReservasLayout = new VerticalLayout();
		totalTitle = new Label();
		totalTitle.setCaptionAsHtml(true);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		historicoChart = crearGraficaHistorico();
		reservasChart = crearTartaReservas();
		
		historicoLayout.addComponents(historicoTitle, historicoChart);
		historicoLayout.setComponentAlignment(historicoTitle, Alignment.TOP_CENTER);
		historicoLayout.setComponentAlignment(historicoChart, Alignment.TOP_CENTER);
		
		totalReservasLayout.addComponents(totalTitle, reservasChart);
		totalReservasLayout.setComponentAlignment(totalTitle, Alignment.TOP_CENTER);
		totalReservasLayout.setComponentAlignment(reservasChart, Alignment.TOP_CENTER);
		
		facturacionLayout.addComponents(historicoLayout, totalReservasLayout);	
		addComponent(facturacionLayout);
	}
	
	@SuppressWarnings("unchecked")
	private Chart crearGraficaHistorico() {
		Chart chart = new Chart(ChartType.COLUMN);
		Configuration conf = chart.getConfiguration();
		
		datosGanancias = (List<Object[]>) dwh.obtenerBeneficiosTotales();
		
		int nData = datosGanancias.size();
		String[] years = new String[nData];
		Float[] ganancias = new Float[nData];
		int i = 0;
		
		for(Object[] pair : datosGanancias) {
			String year = String.valueOf(pair[0]);
			years[i] = year;
			
			String gananciasStr = String.valueOf(pair[1]);
			Float ganancia = Float.parseFloat(gananciasStr);
			ganancias[i] = ganancia;
			
			i++;
		}
		
		conf.setTitle("Ganancias totales por año");
		
		XAxis x = new XAxis();
		x.setCategories(years);
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
        
        conf.addSeries(new ListSeries("Ganancias", ganancias));
        
        chart.drawChart(conf);
		
		return chart;
	}
	
	
	@SuppressWarnings("unchecked")
	private Chart crearTartaReservas() {
		Chart chart = new Chart(ChartType.PIE);		
		Configuration conf = chart.getConfiguration();
		
		datosReservas = (List<Object[]>) rsSer.obtenerNumReservasPorEstado();
		
		conf.setTitle("Reservas según estado");
		
		PlotOptionsPie plotOptions = new PlotOptionsPie();
		DataLabels dataLabels = new DataLabels();
		dataLabels.setEnabled(true);
		dataLabels.setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage +' %'");
		plotOptions.setDataLabels(dataLabels);
		conf.setPlotOptions(plotOptions);
		
		final DataSeries series = new DataSeries();
		for(Object[] pair : datosReservas) {
			String tipoReserva = String.valueOf(pair[1]);
			int numReservas = Integer.parseInt(String.valueOf(pair[0]));
			totalReservas += numReservas;
			series.add(new DataSeriesItem(tipoReserva, numReservas));
		}
		conf.setSeries(series);
		setReservasTitleCaption();
		
		chart.drawChart(conf);
		
		return chart;
	}
	
	private void setReservasTitleCaption() {
		totalTitle.setCaption("<h2>Total de reservas: " + totalReservas + "</h2>");
	}
	
}
