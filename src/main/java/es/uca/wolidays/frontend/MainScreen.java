package es.uca.wolidays.frontend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.vaadin.icons.VaadinIcons;
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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.frontend.views.LoginView;
import es.uca.wolidays.frontend.views.MisApartamentosView;
import es.uca.wolidays.frontend.views.MisReservasView;
import es.uca.wolidays.frontend.views.NuevoApartamentoView;
import es.uca.wolidays.frontend.views.PerfilView;
import es.uca.wolidays.frontend.views.SignupView;
import es.uca.wolidays.frontend.views.gestor.FacturacionView;
import es.uca.wolidays.frontend.views.gestor.ReservasView;


@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1905072662186258460L;
	private Panel springViewDisplay;
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	private final HorizontalLayout navbarLayout = new HorizontalLayout();
	private final HorizontalLayout logoLayout = new HorizontalLayout();
	private final HorizontalLayout buttonsLayout = new HorizontalLayout();
	
	// Botones de usuario invitado
	private Button inicioSesion = createNavigationButton("Iniciar sesión", "nav_btn_iniciosesion", LoginView.VIEW_NAME, null);
	private Button registrarse = createNavigationButton("Registrarse","nav_btn_registrarse", SignupView.VIEW_NAME, null);
	
	// Botones de usuario cliente
	private Button misReservas = createNavigationButton("Mis reservas", "nav_btn_misreservas", MisReservasView.VIEW_NAME, VaadinIcons.TAGS);
	private Button registrarApto = createNavigationButton("Nuevo apartamento", "nav_btn_nuevoapartamento", NuevoApartamentoView.VIEW_NAME, VaadinIcons.PLUS_CIRCLE);
	private Button misAptos = createNavigationButton("Mis apartamentos", "nav_btn_misapartamentos", MisApartamentosView.VIEW_NAME, VaadinIcons.HOME);
	
	// Botones de usuario gestor
	private Button facturacion = createNavigationButton("Facturación", "nav_btn_facturacion", FacturacionView.VIEW_NAME, VaadinIcons.BOOK_DOLLAR);
	private Button reservas = createNavigationButton("Reservas", "nav_btn_reservas", ReservasView.VIEW_NAME, VaadinIcons.TAGS);
	
	// Botones de usuario con sesion iniciada
	private NativeSelect<String> perfil = new NativeSelect<>();
	private List<String> perfilOptions = new ArrayList<>();
	
	@Override
	public void attach() {
		super.attach();
		this.getUI().getNavigator().navigateTo("");
	}
	
	@PostConstruct
	void init() {
		
		this.setMargin(false);		
		mainLayout.setMargin(false);	
		
		navbarLayout.addStyleName("orange");
		navbarLayout.setWidth(100.0f, Unit.PERCENTAGE);
		navbarLayout.setHeight("60px");	
		
		logoLayout.setHeight("60px");
		buttonsLayout.setHeight("60px");
		
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource rsc = new FileResource(new File(basepath + "/resources/img/WolidaysIcon.png"));
		Image tlIcon = new Image(null, rsc);
		tlIcon.addStyleName("icon");
		tlIcon.addClickListener(e -> getUI().getNavigator().navigateTo(""));
		logoLayout.addComponent(tlIcon);
		logoLayout.setComponentAlignment(tlIcon, Alignment.MIDDLE_LEFT);
		
		navbarLayout.addComponent(logoLayout);
		navbarLayout.setComponentAlignment(logoLayout, Alignment.MIDDLE_LEFT);
		
		setButtons();
		
		buttonsLayout.addStyleName("margin_buttons");
		
		mainLayout.addComponent(navbarLayout);
		mainLayout.setComponentAlignment(navbarLayout, Alignment.TOP_CENTER);
		
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		springViewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		mainLayout.addComponent(springViewDisplay);
		mainLayout.setExpandRatio(springViewDisplay, 1.0f);
		
		addComponent(mainLayout);
	}
	
	public void setButtons() {
		
		cleanButtons();
		
		if(SecurityUtils.isLoggedIn()) {
			
			if(SecurityUtils.hasRole("ADMIN_ROL")) {
				
			} else if(SecurityUtils.hasRole("GESTOR_ROL")) {
				buttonsLayout.addComponents(facturacion, reservas);
				buttonsLayout.setComponentAlignment(facturacion, Alignment.MIDDLE_RIGHT);
				buttonsLayout.setComponentAlignment(reservas, Alignment.MIDDLE_RIGHT);
				
			} else if(SecurityUtils.hasRole("CLIENTE_ROL")) {
				buttonsLayout.addComponents(misReservas, registrarApto, misAptos);
				buttonsLayout.setComponentAlignment(misReservas, Alignment.MIDDLE_RIGHT);
				buttonsLayout.setComponentAlignment(registrarApto, Alignment.MIDDLE_RIGHT);
				buttonsLayout.setComponentAlignment(misAptos, Alignment.MIDDLE_RIGHT);
			}
			
			if(!isPerfilButtonSet())
			{
				setPerfilButton();
			}
			
			perfil.setSelectedItem(perfilOptions.get(0));
			
			buttonsLayout.addComponent(perfil);			
			buttonsLayout.setComponentAlignment(perfil, Alignment.MIDDLE_RIGHT);
			
			navbarLayout.addComponentsAndExpand(buttonsLayout);	
			
		} else {			
			
			buttonsLayout.addComponents(inicioSesion, registrarse);
			buttonsLayout.setComponentAlignment(inicioSesion, Alignment.MIDDLE_RIGHT);
			buttonsLayout.setComponentAlignment(registrarse, Alignment.MIDDLE_RIGHT);
			
			navbarLayout.addComponents(buttonsLayout);			
		}
		
		navbarLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_RIGHT);	
	}
	
	public void cleanButtons() {
		
		if(!SecurityUtils.isLoggedIn()) {
			
			buttonsLayout.removeComponent(misReservas);
			buttonsLayout.removeComponent(registrarApto);
			buttonsLayout.removeComponent(misAptos);
			buttonsLayout.removeComponent(perfil);
			
			
		} else {			
			
			buttonsLayout.removeComponent(inicioSesion);
			buttonsLayout.removeComponent(registrarse);
			
		}
		
		navbarLayout.removeComponent(buttonsLayout);
	}
	
	private Button createNavigationButton(String caption, String id, final String viewName, VaadinIcons icon) {
		
		Button button = new Button(caption);
		
		button.setHeight("40px");
		button.addStyleNames(ValoTheme.BUTTON_BORDERLESS);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		button.setId(id);
		
		if(icon != null) {
			button.setIcon(icon);
		}
		
		return button;
	}
	

	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}
	
	public void setPerfilButton() {

		setPerfilOptions();
		perfil.setStyleName("perfil_select");
		perfil.setEmptySelectionAllowed(false);
		perfil.setWidth("-1");
		perfil.setHeight("40px");
		perfil.setId("nav_btn_perfil");		
		
		perfil.addValueChangeListener(e -> {
			String selectedOption = perfil.getValue();
			
			if(selectedOption.equals(perfilOptions.get(1))) {
				int userId = SecurityUtils.getUserId();
				perfil.setSelectedItem(perfilOptions.get(0));
				getUI().getNavigator().navigateTo(PerfilView.VIEW_NAME + "/" + userId);
				
			} else if (selectedOption.equals(perfilOptions.get(2))) {
				// Si el usuario elige "Cerrar sesión"
				getUI().getPage().reload();
				getSession().close();
			}
			
		});
	}
	
	public void setPerfilOptions() {
		String currentUsername = SecurityUtils.getUsername();
		
		perfilOptions.add(currentUsername);
		perfilOptions.add("Ver perfil");
		perfilOptions.add("Cerrar sesión");
		
		perfil.setItems(perfilOptions);
	}
	
	public Boolean isPerfilButtonSet() {
		return perfilOptions.size() > 0;
	}
	
}
