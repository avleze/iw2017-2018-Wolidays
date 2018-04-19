package es.uca.wolidays.frontend.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends Composite implements View {
	
	public static final String VIEW_NAME = "login";
	
	public LoginView() {
		setCompositionRoot(new Label("Vista de inicio de sesión"));
	}
	
}
