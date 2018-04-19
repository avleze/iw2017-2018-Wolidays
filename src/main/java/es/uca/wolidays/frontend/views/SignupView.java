package es.uca.wolidays.frontend.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Label;

@SpringView(name = SignupView.VIEW_NAME)
public class SignupView extends Composite implements View {
	
	public static final String VIEW_NAME = "signup";
	
	public SignupView() {
		setCompositionRoot(new Label("Vista de registro"));
	}
}
