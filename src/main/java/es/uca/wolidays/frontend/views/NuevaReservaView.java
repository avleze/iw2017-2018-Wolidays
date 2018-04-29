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
@SpringView(name = NuevaReservaView.VIEW_NAME)
public class NuevaReservaView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nueva_reserva";
	
	@Autowired
	MainScreen mainScreen;
	
	private int userId;
	private int aptoId;

	@PostConstruct
	void init() {
		
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		userId = Integer.parseInt(event.getParameters().split("/")[0]);
		aptoId = Integer.parseInt(event.getParameters().split("/")[1]);
		
		addComponent(new Label("Usuario " + userId + " reservando apartamento " + aptoId));
	}
	
}
