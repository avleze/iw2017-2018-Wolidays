package es.uca.wolidays.frontend.views;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Imagen;
import es.uca.wolidays.backend.entities.Ubicacion;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;
import es.uca.wolidays.frontend.utils.ImageUtils;

@Theme("wolidays")
@SpringView(name = BusquedaView.VIEW_NAME)
public class BusquedaView extends VerticalLayout implements View {
	
	private static final String FILTRO_PRECIO = "Precio";
	private static final String FILTRO_FECHA = "Fechas";
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "buscar";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	private String textoBuscado = "";
	
	private VerticalLayout busquedaLayout;
	private CssLayout nuevaBusquedaLayout;
	private HorizontalLayout resultadosLayout;
	private VerticalLayout filtrosLayout;
	private HorizontalLayout buttonsLayout;
	private VerticalLayout leftAptos;
	private VerticalLayout rightAptos;
	
	private Label separator;
	private TextField searchBar;
	private Button searchButton;
	
	private Button filtroPrecio;
	private Button filtroFechas;
	private Button limpiarFiltro;
	
	private HorizontalLayout slidersLayout;
	private Slider precioMinSlider;
	private Slider precioMaxSlider;
	private Button aplicarFiltroPrecio;
	
	private HorizontalLayout fechasLayout;
	private DateField fechaInicio;
	private DateField fechaFin;
	private Button aplicarFiltroFechas;
	
	private List<Apartamento> aptosSinFiltro;
	
	private String filtroActual;
	
	@PostConstruct
	void init() {
		busquedaLayout = new VerticalLayout();
		busquedaLayout.setWidth("100%");
		busquedaLayout.setMargin(false);
		
		separator = new Label();
		separator.setCaptionAsHtml(true);
		separator.setCaption("<hr>");
		separator.setStyleName("separator");
		
		nuevaBusquedaLayout = new CssLayout();
		searchBar = new TextField();
		searchButton = new Button("Buscar");
		
		searchBar.setPlaceholder("Introduce otra ciudad ...");
		searchBar.setWidth("400px");
		searchButton.setWidth("80px");
		searchButton.setClickShortcut(KeyCode.ENTER);
		searchButton.addClickListener(e -> {
			
			if(!searchBar.isEmpty()) {
				textoBuscado = searchBar.getValue();
				aptosSinFiltro = aptoService.buscarPorUbicacion(textoBuscado);
				limpiarApartamentos();
				setApartamentos(aptosSinFiltro, false);
			}
			
		});
		
		nuevaBusquedaLayout.addComponents(searchBar, searchButton);
		nuevaBusquedaLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		resultadosLayout = new HorizontalLayout();
		resultadosLayout.setWidth("100%");
		
		filtrosLayout = new VerticalLayout();
		filtrosLayout.setWidth("100%");
		filtrosLayout.setHeight("-1");
		
		buttonsLayout = new HorizontalLayout();
		
		filtroPrecio = new Button("Precio");
		filtroFechas = new Button("Fechas");
		limpiarFiltro = new Button(VaadinIcons.CLOSE);
		limpiarFiltro.setDescription("Limpia el filtro actual");
		limpiarFiltro.addClickListener(limpiar -> { 
			limpiarApartamentos();
			setApartamentos(aptosSinFiltro, true);
		});
		buttonsLayout.addComponents(filtroPrecio, filtroFechas, limpiarFiltro);
		buttonsLayout.setComponentAlignment(filtroPrecio, Alignment.TOP_LEFT);
		buttonsLayout.setComponentAlignment(filtroFechas, Alignment.TOP_LEFT);
		buttonsLayout.setComponentAlignment(limpiarFiltro, Alignment.TOP_LEFT);
		filtrosLayout.addComponent(buttonsLayout);
		filtrosLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_LEFT);
		
		slidersLayout = new HorizontalLayout();
		slidersLayout.setWidth("100%");
		slidersLayout.setHeight("-1");
		precioMinSlider = new Slider();
		precioMinSlider.setWidth("300px");
		precioMinSlider.setCaption("Precio mínimo");
		precioMinSlider.setStyleName("sliders_precio_filter");
		precioMinSlider.setMin(0.0);
		precioMinSlider.setMax(1000.0);
		precioMinSlider.setValue(0.0);
		precioMaxSlider = new Slider();
		precioMaxSlider.setWidth("300px");
		precioMaxSlider.setCaption("Precio máximo");
		precioMaxSlider.setStyleName("sliders_precio_filter");
		precioMaxSlider.setMin(0.0);
		precioMaxSlider.setMax(1000.0);
		precioMaxSlider.setValue(1000.0);
		slidersLayout.addComponent(precioMinSlider);
		slidersLayout.addComponent(precioMaxSlider);
		
		aplicarFiltroPrecio = new Button("Aplicar filtro");
		aplicarFiltroPrecio.addClickListener(filter -> updateApartamentosPorPrecio());
		filtrosLayout.addComponent(aplicarFiltroPrecio);
		filtrosLayout.setComponentAlignment(aplicarFiltroPrecio, Alignment.TOP_CENTER);
		aplicarFiltroPrecio.setVisible(false);
		
		fechasLayout = new HorizontalLayout();
		fechasLayout.setWidth("100%");
		fechasLayout.setHeight("-1");
		fechaInicio = new DateField("Fecha inicio", LocalDate.now());
		fechaFin = new DateField("Fecha fin", LocalDate.now().plusDays(1));
		fechasLayout.addComponents(fechaInicio, fechaFin);
		
		aplicarFiltroFechas = new Button("Aplicar filtro");
		aplicarFiltroFechas.addClickListener(filter -> updateApartamentosPorFechas());
		filtrosLayout.addComponent(aplicarFiltroFechas);
		filtrosLayout.setComponentAlignment(aplicarFiltroFechas, Alignment.TOP_CENTER);
		aplicarFiltroFechas.setVisible(false);
		
		setAptosInfoColumns();
		
		filtroActual = "";
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		try {
			textoBuscado = URLDecoder.decode(event.getParameters().split("/")[0], "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			
		}
		aptosSinFiltro = aptoService.buscarPorUbicacion(textoBuscado);
		
		if(aptosSinFiltro.isEmpty()) {			
			Notification.show("No existen apartamentos", "en " + textoBuscado, Notification.Type.ERROR_MESSAGE);
		} else {			
			setApartamentos(aptosSinFiltro, false);			
		}
		
		filtroPrecio.addClickListener(e -> {
			limpiarFiltros();
			
			slidersLayout.addComponents(precioMinSlider, precioMaxSlider);
			slidersLayout.setComponentAlignment(precioMinSlider, Alignment.MIDDLE_RIGHT);
			slidersLayout.setComponentAlignment(precioMaxSlider, Alignment.MIDDLE_LEFT);
			filtrosLayout.addComponent(slidersLayout);
			filtrosLayout.setComponentAlignment(slidersLayout, Alignment.TOP_LEFT);
			
			aplicarFiltroPrecio.setVisible(false);
			aplicarFiltroPrecio.setVisible(true);		
			
			filtroActual = FILTRO_PRECIO;
		});
		
		filtroFechas.addClickListener(e -> {
			limpiarFiltros();
			
			fechasLayout.addComponents(fechaInicio, fechaFin);
			fechasLayout.setComponentAlignment(fechaInicio, Alignment.MIDDLE_RIGHT);
			fechasLayout.setComponentAlignment(fechaFin, Alignment.MIDDLE_LEFT);
			filtrosLayout.addComponent(fechasLayout);
			filtrosLayout.setComponentAlignment(fechasLayout, Alignment.TOP_LEFT);
			
			aplicarFiltroFechas.setVisible(false);
			aplicarFiltroFechas.setVisible(true);
			
			filtroActual = FILTRO_FECHA;
		});
		
		busquedaLayout.addComponents(filtrosLayout, separator, nuevaBusquedaLayout, resultadosLayout);
		busquedaLayout.setComponentAlignment(nuevaBusquedaLayout, Alignment.TOP_CENTER);
		addComponent(busquedaLayout);
	}
	
	private void limpiarFiltros() {
		if(filtroActual.equals(FILTRO_PRECIO)) {
			filtrosLayout.removeComponent(slidersLayout);
			aplicarFiltroPrecio.setVisible(false);
		} else if(filtroActual.equals(FILTRO_FECHA)) {
			filtrosLayout.removeComponent(fechasLayout);
			aplicarFiltroFechas.setVisible(false);
		}
	}
	
	private void updateApartamentosPorPrecio() {
		
		Double minPrecio = precioMinSlider.getValue();
		Double maxPrecio = precioMaxSlider.getValue();
		
		if(minPrecio > maxPrecio) {
			Notification.show("El valor mínimo es mayor que el máximo", "Introdúcelo de nuevo", Notification.Type.ERROR_MESSAGE);
		} else {
			List<Apartamento> aptosActualizados = aptoService
					.filtrarPorUbicacionyPrecioEstandar(textoBuscado, minPrecio, maxPrecio);
			
			limpiarApartamentos();
			setApartamentos(aptosActualizados, true);
		}
	}
	
	private void updateApartamentosPorFechas() {
		
		LocalDate fechaIni = fechaInicio.getValue();
		LocalDate fechaF = fechaFin.getValue();
		
		if(fechaF.isBefore(fechaIni)) {
			Notification.show("La fecha de fin es anterior a la de inicio.", "Introdúcelas de nuevo", Notification.Type.ERROR_MESSAGE);
		} else {
			List<Apartamento> aptosActualizados = aptoService.filtrarPorUbicacionyFecha(textoBuscado, fechaIni, fechaF);
			
			limpiarApartamentos();
			setApartamentos(aptosActualizados, true);
		}
	}
	
	private void limpiarApartamentos() {
		resultadosLayout.removeComponent(leftAptos);
		resultadosLayout.removeComponent(rightAptos);
		setAptosInfoColumns();
	}
	
	private void setAptosInfoColumns() {
		leftAptos = new VerticalLayout();
		leftAptos.setMargin(true);
		leftAptos.setSpacing(true);
		rightAptos = new VerticalLayout();
		rightAptos.setMargin(true);
		rightAptos.setSpacing(true);
	}
	
	private void setApartamentos(List<Apartamento> aptos, Boolean filtrando) {
		
		if(aptos.isEmpty()) {
			if(filtrando) {
				Notification.show("No existen apartamentos con el filtro introducido", "Prueba con otro", Notification.Type.ERROR_MESSAGE);
			} else {
				Notification.show("No existen apartamentos", "en " + textoBuscado, Notification.Type.ERROR_MESSAGE);
			}
			
		} else {
			int i = 0;
			
			for(Apartamento apto : aptos) {
				VerticalLayout aptoInfo = new VerticalLayout();
				aptoInfo.setSpacing(false);
				
				Ubicacion aptoUbi = apto.getUbicacion();
				Button ubicacion = new Button(aptoUbi.getDireccion() + " (" + aptoUbi.getCiudad() + ")");
				ubicacion.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
				ubicacion.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
				
				Button precioStd = new Button("Desde " + apto.getPrecioEstandar() + "€ la noche");
				precioStd.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				precioStd.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
				
				Button numCamas;
				
				if(apto.getNumCamas() == 1) {
					numCamas = new Button("1 cama");
				} else {
					numCamas = new Button(String.valueOf(apto.getNumCamas()) + " camas");
				}				
				numCamas.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "small_text");
				numCamas.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
				
				Set<Imagen> imagenes = apto.getImagenes();
				if(!imagenes.isEmpty())
				{
					Image imagen = ImageUtils.convertToImage(apto.getImagenes().iterator().next().getImagen());
					imagen.setWidth("200px");
					imagen.setStyleName("navigator-cursor");
					imagen.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
					aptoInfo.addComponents(ubicacion, imagen, precioStd, numCamas);			
				}
				else
					aptoInfo.addComponents(ubicacion, precioStd, numCamas);			
				
				
				if(i % 2 == 0) {
					leftAptos.addComponent(aptoInfo);
				} else {
					rightAptos.addComponent(aptoInfo);
				}
				
				i++;
			}
			
			resultadosLayout.addComponents(leftAptos, rightAptos);
		}
	}	
}




