package es.uca.wolidays.frontend.views.admin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.services.RolService;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.views.PerfilView;

@SpringView(name = PermisosView.VIEW_NAME)
public class PermisosView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "permisos";
	
	@Autowired
	UsuarioService userService;
	
	@Autowired
	RolService rolService;
	
	private VerticalLayout permisosLayout;
	
	private Label title;
	private Label description;
	private CssLayout searchBar;
	private TextField searchField;
	private Button searchButton;


	@PostConstruct
	void init() {
		permisosLayout = new VerticalLayout();
		permisosLayout.setWidth("100%");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Permisos</h1>");
		
		description = new Label();
		description.setCaptionAsHtml(true);
		description.setCaption("<b>Introduce un nombre de usuario para acceder a su perfil y poder otorgarle permisos.");
		
		searchBar = new CssLayout();		
		searchField = new TextField();
		searchField.setPlaceholder("Introduce un usuario ...");
		searchField.setWidth("600px");
		searchField.focus();
		
		searchButton = new Button("Buscar");
		searchButton.setWidth("80px");
		searchButton.setClickShortcut(KeyCode.ENTER);
		searchButton.addClickListener(e -> {
							
			if(searchField.isEmpty()) {
				Notification.show("Debes introducir un usuario");
				
			} else {
				String username = searchField.getValue();
				try {
					Usuario user = (Usuario) userService.loadUserByUsername(username);
					getUI().getNavigator().navigateTo(PerfilView.VIEW_NAME + "/" + user.getId());
				} catch (UsernameNotFoundException ex) {
					Notification.show("El nombre de usuario indicado no pertenece a ningún usuario en la plataforma", "", Notification.Type.ERROR_MESSAGE);
				}
			}
			
			
		});
		searchButton.setStyleName("button");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		searchBar.addComponents(searchField, searchButton);
		searchBar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		permisosLayout.addComponents(title, description, searchBar);
		permisosLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		permisosLayout.setComponentAlignment(description, Alignment.TOP_LEFT);
		permisosLayout.setComponentAlignment(searchBar, Alignment.TOP_LEFT);
		
		addComponent(permisosLayout);
	}
	
}