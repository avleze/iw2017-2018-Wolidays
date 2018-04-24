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
@SpringView(name = MisApartamentosView.VIEW_NAME)
public class MisApartamentosView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "mis_apartamentos";
	
	@Autowired
	MainScreen mainScreen;

	@PostConstruct
	void init() {
		addComponent(new Label("Vista de mis apartamentos"));
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
	}
	
}