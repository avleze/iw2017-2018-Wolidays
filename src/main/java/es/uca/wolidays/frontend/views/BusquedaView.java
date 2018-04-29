package es.uca.wolidays.frontend.views;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("navbar")
@SpringView(name = BusquedaView.VIEW_NAME)
public class BusquedaView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "buscar";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	ApartamentoService aptoService;
	
	private String ubicacionBuscada = "";
	
	private VerticalLayout busquedaLayout;
	private HorizontalLayout resultadosLayout;
	private VerticalLayout filtrosLayout;
	private HorizontalLayout buttonsLayout;
	private VerticalLayout leftAptos;
	private VerticalLayout rightAptos;
	
	private Button filtroPrecio;
	private Button filtroFechas;
	private Button limpiarFiltro;
	
	private HorizontalLayout slidersLayout;
	private Slider precioMinSlider;
	private Slider precioMaxSlider;
	
	private List<Apartamento> aptosSinFiltro;
	
	@PostConstruct
	void init() {
		busquedaLayout = new VerticalLayout();
		busquedaLayout.setWidth("100%");
		busquedaLayout.setMargin(false);
		
		resultadosLayout = new HorizontalLayout();
		resultadosLayout.setWidth("100%");
		
		filtrosLayout = new VerticalLayout();
		filtrosLayout.setWidth("100%");
		filtrosLayout.setHeight("-1");
		
		buttonsLayout = new HorizontalLayout();
		
		filtroPrecio = new Button("Precio");
		filtroFechas = new Button("Fechas");
		filtroFechas.setEnabled(false);
		limpiarFiltro = new Button(VaadinIcons.CLOSE);
		limpiarFiltro.setDescription("Limpia el filtro actual");
		limpiarFiltro.addClickListener(limpiar -> { 
			limpiarApartamentos();
			setApartamentos(aptosSinFiltro);
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
		
		setAptosInfoColumns();		
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		try {
			ubicacionBuscada = URLDecoder.decode(event.getParameters().split("/")[0], "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			
		}
		aptosSinFiltro = aptoService.buscarPorUbicacion(ubicacionBuscada);
		
		if(aptosSinFiltro.isEmpty()) {
			
			Notification.show("No existen apartamentos", "en " + ubicacionBuscada, Notification.Type.ERROR_MESSAGE);
			Button volverInicio = new Button("Buscar otra ciudad");
			volverInicio.setIcon(VaadinIcons.ARROW_BACKWARD);
			volverInicio.setClickShortcut(KeyCode.ENTER);
			volverInicio.addClickListener(e -> getUI().getNavigator().navigateTo(""));
			leftAptos.addComponent(volverInicio);
			
		} else {			
			setApartamentos(aptosSinFiltro);			
		}
		
		filtroPrecio.addClickListener(e -> {
			slidersLayout.addComponents(precioMinSlider, precioMaxSlider);
			slidersLayout.setComponentAlignment(precioMinSlider, Alignment.MIDDLE_RIGHT);
			slidersLayout.setComponentAlignment(precioMaxSlider, Alignment.MIDDLE_LEFT);
			filtrosLayout.addComponent(slidersLayout);
			filtrosLayout.setComponentAlignment(slidersLayout, Alignment.TOP_LEFT);
			
			Button aplicarFiltro = new Button("Aplicar filtro");
			aplicarFiltro.addClickListener(filter -> updateApartamentos());
			
			filtrosLayout.addComponent(aplicarFiltro);
			filtrosLayout.setComponentAlignment(aplicarFiltro, Alignment.TOP_CENTER);
			
		});		
		
		busquedaLayout.addComponents(filtrosLayout, resultadosLayout);
		addComponent(busquedaLayout);
	}
	
	private void updateApartamentos() {
		Double minPrecio = precioMinSlider.getValue();
		Double maxPrecio = precioMaxSlider.getValue();
		
		if(minPrecio > maxPrecio) {
			Notification.show("El valor mínimo es mayor que el máximo", "Introdúcelo de nuevo", Notification.Type.ERROR_MESSAGE);
		} else {
			List<Apartamento> aptosActualizados = aptoService
					.filtrarPorUbicacionyPrecioEstandar(ubicacionBuscada, minPrecio, maxPrecio);
			
			limpiarApartamentos();
			setApartamentos(aptosActualizados);
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
	
	private void setApartamentos(List<Apartamento> aptos) {
		
		if(aptos.isEmpty()) {
			Notification.show("No existen apartamentos con el filtro introducido", "Prueba con otro", Notification.Type.ERROR_MESSAGE);
		} else {
			int i = 0;
			
			for(Apartamento apto : aptos) {
				VerticalLayout aptoInfo = new VerticalLayout();
				aptoInfo.setSpacing(false);
				
				Button ubicacion = new Button(apto.getUbicacion());
				ubicacion.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
				ubicacion.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
				
				Button precioStd = new Button("Desde " + String.valueOf(apto.getPrecioEstandar()) + "€ la noche");
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




