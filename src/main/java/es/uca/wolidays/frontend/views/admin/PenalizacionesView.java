package es.uca.wolidays.frontend.views.admin;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
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
	
	private Label title;
	
	private Grid<Penalizacion> penTabla;
	private ArrayList<Penalizacion> penalizaciones;
	
	@PostConstruct
	void init() {
		penLayout = new VerticalLayout();
		penLayout.setWidth("100%");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("Penalizaciones");
		
		penTabla = new Grid<>();
		penTabla.setWidth("900px");
		penalizaciones = new ArrayList<>();
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		penService.penalizaciones().forEach(penalizaciones::add);
		
		penTabla.setItems(penalizaciones);
		
		penTabla.addColumn(Penalizacion::getId).setCaption("Id");
		
		penLayout.addComponents(title); 
		penLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		
		addComponent(penLayout);
	}
	
}
