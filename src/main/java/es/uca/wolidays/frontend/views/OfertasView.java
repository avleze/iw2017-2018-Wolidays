package es.uca.wolidays.frontend.views;

import java.time.format.DateTimeFormatter;
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
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.OfertaService;
import es.uca.wolidays.frontend.MainScreen;


@Theme("navbar")
@SpringView(name = OfertasView.VIEW_NAME)
public class OfertasView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "ofertas";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	OfertaService oftaService;
	
	@Autowired
	ApartamentoService aptoService;
	
	private VerticalLayout ofertasLayout;
	private Label title;
	
	private HorizontalLayout resultadosLayout;
	private VerticalLayout ofertasLeft;
	private VerticalLayout ofertasRight;
	
	private List<Oferta> ofertas;
	
	private int id_aptoOferta = 0;

	@PostConstruct
	void init() {
		
		ofertasLayout = new VerticalLayout();
		ofertasLayout.setWidth("100%");
		title = new Label("Ofertas");
		title.addStyleName("detail_apto_title");
		ofertasLayout.setMargin(false);
		
		ofertasLeft = new VerticalLayout();
		ofertasRight = new VerticalLayout();
		
		resultadosLayout = new HorizontalLayout();
		resultadosLayout.setWidth("100%");
		
		setOfertasInfoColumns();
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		id_aptoOferta = Integer.parseInt(event.getParameters().split("/")[0]);
		
		ofertas = aptoService.buscarPorIdConOfertas(id_aptoOferta).get().getOfertas();
		
		if(ofertas.isEmpty()) {
			
			Notification.show("No existen ofertas para el apartamento seleccionado", Notification.Type.ERROR_MESSAGE);
			Button volver = new Button("Volver");
			volver.setIcon(VaadinIcons.ARROW_BACKWARD);
			volver.setClickShortcut(KeyCode.ENTER);
			volver.addClickListener(e -> getUI().getNavigator().navigateTo(""));
			ofertasLeft.addComponent(volver);
			
		} else {			
			setOfertas(ofertas);			
		}
		
		ofertasLayout.addComponents(resultadosLayout);
		addComponent(ofertasLayout);
	}
	
	private void setOfertasInfoColumns() {
		ofertasLeft = new VerticalLayout();
		ofertasLeft.setMargin(true);
		ofertasLeft.setSpacing(true);
		ofertasRight = new VerticalLayout();
		ofertasRight.setMargin(true);
		ofertasRight.setSpacing(true);
	}
	
	private void setOfertas(List<Oferta> oftas) {
			
			if(oftas.isEmpty()) {
				Notification.show("No existen ofertas para el apartamento escogido","", Notification.Type.ERROR_MESSAGE);
			} else {
				int i = 0;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy");
				
				for(Oferta ofta : oftas) {
					VerticalLayout oftaInfo = new VerticalLayout();
					oftaInfo.setSpacing(false);
					
					Button fechaInicio = new Button(ofta.getFechaInicio().format(formatter));
					fechaInicio.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
					
					Button fechaFin = new Button(ofta.getFechaInicio().format(formatter));
					fechaFin.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
					
					Button precio = new Button(String.valueOf(ofta.getPrecioOferta()));
					precio.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
					
					oftaInfo.addComponents(fechaInicio, fechaFin, precio);			
					
					if(i % 2 == 0) {
						ofertasLeft.addComponent(oftaInfo);
					} else {
						ofertasRight.addComponent(oftaInfo);
					}
					
					i++;
				}
				
				resultadosLayout.addComponents(ofertasLeft, ofertasRight);
			}
	}
}