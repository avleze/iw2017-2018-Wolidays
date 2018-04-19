package es.uca.wolidays.frontend.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

public class SignupView extends Composite implements View {
	
	public SignupView() {
		setCompositionRoot(new Label("Vista de registro"));
	}
}
