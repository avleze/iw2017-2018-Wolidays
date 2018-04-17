package es.uca.wolidays.frontend;

import java.io.File;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("navbar")
@SpringUI
public class WolidaysUI extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.addStyleName("orange");
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		layout.setHeight("60px");
		
		HorizontalLayout buttons_layout = new HorizontalLayout();
		buttons_layout.setHeight(10.0f, Unit.PERCENTAGE);
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource rsc = new FileResource(new File(basepath + "/resources/img/WolidaysIcon.png"));
		Image tl_icon = new Image(null, rsc);
		tl_icon.addStyleName("icon");
		
		Button inicio_sesion = new Button("Iniciar sesión");
		inicio_sesion.addListener(e -> Notification.show("Enlace a inicio sesión"));
		inicio_sesion.setHeight("40px");
		inicio_sesion.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		Button registrarse = new Button("Registrarse");
		registrarse.addListener(e -> Notification.show("Enlace a registro"));
		registrarse.setHeight("40px");
		registrarse.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		buttons_layout.addComponents(inicio_sesion, registrarse);
		buttons_layout.setComponentAlignment(inicio_sesion, Alignment.TOP_RIGHT);
		buttons_layout.setComponentAlignment(registrarse, Alignment.TOP_RIGHT);
		buttons_layout.addStyleName("margin_buttons");
		
		layout.addComponents(tl_icon, buttons_layout);
		layout.setComponentAlignment(tl_icon, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(buttons_layout, Alignment.MIDDLE_RIGHT);
		
		setContent(layout);
	}

}
