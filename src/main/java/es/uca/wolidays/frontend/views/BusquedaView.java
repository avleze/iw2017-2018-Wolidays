package es.uca.wolidays.frontend.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
	
	
	@PostConstruct
	void init() {
		final VerticalLayout busquedaLayout = new VerticalLayout();
		busquedaLayout.setMargin(true);
		
		VerticalLayout leftAptos = new VerticalLayout();
		VerticalLayout rightAptos = new VerticalLayout();
		
		
		busquedaLayout.addComponents(leftAptos, rightAptos);		
		addComponent(busquedaLayout);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		if(event.getParameters() != null) {
			String[] params = event.getParameters().split("/");
			for(String param : params) {
				addComponent(new Label(param));
			}
		}
	}
}
