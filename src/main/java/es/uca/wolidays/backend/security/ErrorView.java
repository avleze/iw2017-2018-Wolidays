package es.uca.wolidays.backend.security;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Component
@UIScope
public class ErrorView extends VerticalLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1601530583077251181L;
	private Label errorLabel;

    public ErrorView() {
        setMargin(true);
        errorLabel = new Label();
        errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setSizeUndefined();
        addComponent(errorLabel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        errorLabel.setValue(String.format("No such view: %s", event.getViewName()));
    }
}
