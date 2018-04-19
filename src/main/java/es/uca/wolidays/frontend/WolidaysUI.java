package es.uca.wolidays.frontend;

import java.io.File;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.frontend.views.HomeView;
import es.uca.wolidays.frontend.views.LoginView;
import es.uca.wolidays.frontend.views.SignupView;


@Theme("navbar")
@SpringUI
@PushStateNavigation
public class WolidaysUI extends UI {
	
	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		
		final HorizontalLayout navbarLayout = new HorizontalLayout();
		navbarLayout.setMargin(false);
		navbarLayout.addStyleName("orange");
		navbarLayout.setWidth(100.0f, Unit.PERCENTAGE);
		navbarLayout.setHeight("60px");
		
		HorizontalLayout buttons_layout = new HorizontalLayout();
		buttons_layout.setHeight("60px");
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource rsc = new FileResource(new File(basepath + "/resources/img/WolidaysIcon.png"));
		Image tl_icon = new Image(null, rsc);
		tl_icon.addStyleName("icon");
		tl_icon.addClickListener(e -> getNavigator().navigateTo(""));
		
		Button inicio_sesion = new Button("Iniciar sesión", e -> getNavigator().navigateTo("login"));
		inicio_sesion.setHeight("40px");
		inicio_sesion.addStyleNames(ValoTheme.BUTTON_BORDERLESS);
		
		Button registrarse = new Button("Registrarse", e -> getNavigator().navigateTo("signup"));
		registrarse.setHeight("40px");
		registrarse.addStyleNames(ValoTheme.BUTTON_BORDERLESS);
		
		buttons_layout.addComponents(inicio_sesion, registrarse);
		buttons_layout.setComponentAlignment(inicio_sesion, Alignment.MIDDLE_RIGHT);
		buttons_layout.setComponentAlignment(registrarse, Alignment.MIDDLE_RIGHT);
		buttons_layout.addStyleName("margin_buttons");
		
		navbarLayout.addComponents(tl_icon, buttons_layout);
		navbarLayout.setComponentAlignment(tl_icon, Alignment.MIDDLE_LEFT);
		navbarLayout.setComponentAlignment(buttons_layout, Alignment.MIDDLE_RIGHT);
		
		CssLayout viewContainer = new CssLayout();
		
		mainLayout.addComponents(navbarLayout, viewContainer);
		mainLayout.setComponentAlignment(navbarLayout, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(viewContainer, Alignment.TOP_CENTER);
		
		setContent(mainLayout);
		
		Navigator navigator = new Navigator(this, viewContainer);
		navigator.addView("", HomeView.class);
		navigator.addView("login", LoginView.class);
		navigator.addView("signup", SignupView.class);
	}

}
