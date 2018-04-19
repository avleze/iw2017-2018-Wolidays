package es.uca.wolidays.frontend.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

public class LoginView extends Composite implements View {
	
	public LoginView() {
		setCompositionRoot(new Label("Vista de inicio de sesión"));
	}
	
}
