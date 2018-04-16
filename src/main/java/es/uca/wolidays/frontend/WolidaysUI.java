package es.uca.wolidays.frontend;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class WolidaysUI extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		
		Button button = new Button();
		button.setClickShortcut(KeyCode.ENTER);
		button.addClickListener(e -> Notification.show("Button clicked"));
		
		layout.addComponent(button);
		
		setContent(layout);
	}

}
