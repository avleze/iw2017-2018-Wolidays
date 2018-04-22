package es.uca.wolidays.frontend;

import java.io.File;

import javax.annotation.PostConstruct;



import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.frontend.views.LoginView;
import es.uca.wolidays.frontend.views.SignupView;

@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1905072662186258460L;
	private Panel springViewDisplay;
	
	@Override
	public void attach() {
		super.attach();
		this.getUI().getNavigator().navigateTo("");
	}
	
	@PostConstruct
	void init() {
		
		this.setMargin(false);
		
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
		tl_icon.addClickListener(e -> getUI().getNavigator().navigateTo(""));
		navbarLayout.addComponent(tl_icon);
		navbarLayout.setComponentAlignment(tl_icon, Alignment.MIDDLE_LEFT);
		
		if(SecurityUtils.isLoggedIn()) {
			/*
			 * Botones para usuario registrado		
			 */		
		} else {
			Button inicio_sesion = createNavigationButton("Iniciar sesión", LoginView.VIEW_NAME);
			Button registrarse = createNavigationButton("Registrarse", SignupView.VIEW_NAME);
			
			buttons_layout.addComponents(inicio_sesion, registrarse);
			buttons_layout.setComponentAlignment(inicio_sesion, Alignment.MIDDLE_RIGHT);
			buttons_layout.setComponentAlignment(registrarse, Alignment.MIDDLE_RIGHT);	
		}
		
		buttons_layout.addStyleName("margin_buttons");
		navbarLayout.addComponent(buttons_layout);
		navbarLayout.setComponentAlignment(buttons_layout, Alignment.MIDDLE_RIGHT);	
		
		mainLayout.addComponent(navbarLayout);
		mainLayout.setComponentAlignment(navbarLayout, Alignment.TOP_CENTER);
		
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		springViewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		mainLayout.addComponent(springViewDisplay);
		mainLayout.setExpandRatio(springViewDisplay, 1.0f);
		
		
		addComponent(mainLayout);
	}
	
	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.setHeight("40px");
		button.addStyleNames(ValoTheme.BUTTON_BORDERLESS);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		
		return button;
	}
	

	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}
	
	
}
