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
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("navbar")
@SpringView(name = BusquedaView.VIEW_NAME)
public class BusquedaView extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "buscar";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	ApartamentoService aptoService;
	
	private HorizontalLayout busquedaLayout;
	private VerticalLayout leftAptos;
	private VerticalLayout rightAptos;
	
	@PostConstruct
	void init() {
		busquedaLayout = new HorizontalLayout();
		busquedaLayout.setMargin(true);
		busquedaLayout.setWidth("100%");
		
		leftAptos = new VerticalLayout();
		leftAptos.setSpacing(true);
		rightAptos = new VerticalLayout();
		rightAptos.setSpacing(true);		
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		String ubicacionBuscada = "";
		
		try {
			ubicacionBuscada = URLDecoder.decode(event.getParameters().split("/")[0], "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			
		}
		List<Apartamento> apartamentos = aptoService.buscarPorUbicacion(ubicacionBuscada);
		int i = 0;
		
		if(apartamentos.isEmpty()) {
			
			Notification.show("No existen apartamentos", "en " + ubicacionBuscada, Notification.Type.ERROR_MESSAGE);
			Button volverInicio = new Button("Buscar otra ciudad");
			volverInicio.setIcon(VaadinIcons.ARROW_BACKWARD);
			volverInicio.setClickShortcut(KeyCode.ENTER);
			volverInicio.addClickListener(e -> getUI().getNavigator().navigateTo(""));
			leftAptos.addComponent(volverInicio);
			
		} else {
			
			for(Apartamento apto : apartamentos) {
				VerticalLayout aptoInfo = new VerticalLayout();
				
				Label ubicacion = new Label(apto.getUbicacion());
				ubicacion.setStyleName("large_text");
				
				Label precioStd = new Label("Desde " + String.valueOf(apto.getPrecioEstandar()) + "€ la noche");
				
				Label numCamas = new Label(String.valueOf(apto.getNumCamas()) + " camas");
				numCamas.setStyleName("small_text");
				
				aptoInfo.addComponents(ubicacion, precioStd, numCamas);			
				
				if(i % 2 == 0) {
					leftAptos.addComponent(aptoInfo);
				} else {
					rightAptos.addComponent(aptoInfo);
				}
				
				i++;
			}
		}
		
		busquedaLayout.addComponents(leftAptos, rightAptos);		
		addComponent(busquedaLayout);
	}
}




