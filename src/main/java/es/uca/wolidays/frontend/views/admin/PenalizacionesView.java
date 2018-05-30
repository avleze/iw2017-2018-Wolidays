package es.uca.wolidays.frontend.views.admin;

import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.services.ReglasDeNegocioService;

@SpringView(name = PenalizacionesView.VIEW_NAME)
public class PenalizacionesView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "penalizaciones";
	
	@Autowired
	ReglasDeNegocioService penService;
	
	private VerticalLayout penLayout;
	private VerticalLayout tablaLayout;
	
	private Label title;
	private Label instrucciones;
	
	private Button volverReglas;
	private Button nuevaPen;
	
	private Grid<Penalizacion> penTabla;
	private ArrayList<Penalizacion> penalizaciones;
	
	@PostConstruct
	void init() {
		penLayout = new VerticalLayout();
		penLayout.setWidth("100%");
		
		tablaLayout = new VerticalLayout();
		tablaLayout.setWidth("-1");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Penalizaciones</h1>");
		
		instrucciones = new Label();
		instrucciones.setCaptionAsHtml(true);
		instrucciones.setCaption("<h2>Introduce una nueva política de penalización o selecciona una para modificarla</h2>");
		
		volverReglas = new Button("Volver a reglas de negocio");
		volverReglas.setIcon(VaadinIcons.ARROW_BACKWARD);
		volverReglas.setWidth("-1");
		volverReglas.addClickListener(e -> getUI().getNavigator().navigateTo(NegocioView.VIEW_NAME));
		
		nuevaPen = new Button("Nueva política");
		nuevaPen.setIcon(VaadinIcons.PLUS);
		nuevaPen.setWidth("-1");
		nuevaPen.addClickListener(e -> getUI().getNavigator().navigateTo(NuevaPenalizacionView.VIEW_NAME));
		
		penTabla = new Grid<>();
		penTabla.setWidth("1100px");
		penTabla.addSelectionListener(e -> {
			Optional<Penalizacion> penSelec = e.getFirstSelectedItem();
			if(penSelec.isPresent()) {
				Penalizacion pen = penSelec.get();
				getUI().getNavigator().navigateTo(NuevaPenalizacionView.VIEW_NAME + "/" + pen.getId());
			}
		});
		
		penalizaciones = new ArrayList<>();
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		penService.penalizaciones().forEach(penalizaciones::add);
		
		penTabla.setItems(penalizaciones);
		
		penTabla.addColumn(Penalizacion::getId).setCaption("Id");
		penTabla.addColumn(Penalizacion::getPorcentajeCargo).setCaption("Cargo (%)");
		penTabla.addColumn(Penalizacion::getTipoUsuario).setCaption("Tipo de usuario");
		penTabla.addColumn(Penalizacion::getMotivo).setCaption("Motivo");
		penTabla.addColumn(Penalizacion::getMinNoches).setCaption("Noches mínimas");
		penTabla.addColumn(Penalizacion::getMaxNoches).setCaption("Noches máximas");
		penTabla.addColumn(Penalizacion::getMinDiasAntelacion).setCaption("Mín. días antelación");
		penTabla.addColumn(Penalizacion::getMaxDiasAntelacion).setCaption("Máx. días antelación");
		
		tablaLayout.addComponents(penTabla, nuevaPen, volverReglas);
		tablaLayout.setComponentAlignment(penTabla, Alignment.TOP_CENTER);
		tablaLayout.setComponentAlignment(nuevaPen, Alignment.TOP_RIGHT);
		tablaLayout.setComponentAlignment(volverReglas, Alignment.TOP_LEFT);
		
		penLayout.addComponents(title, instrucciones, tablaLayout); 
		penLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		penLayout.setComponentAlignment(instrucciones, Alignment.TOP_CENTER);
		penLayout.setComponentAlignment(tablaLayout, Alignment.TOP_CENTER);
		
		addComponent(penLayout);
	}
	
	public static void setSuccessfulPenalizacionNotification() {
		Notification successfulMod = new Notification("Penalización añadida/modificada con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(1500);
		successfulMod.setStyleName("success_notification");
		
		successfulMod.show(Page.getCurrent());
	}
	
}
