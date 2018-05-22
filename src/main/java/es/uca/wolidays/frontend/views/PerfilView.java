package es.uca.wolidays.frontend.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Rol;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.RolService;
import es.uca.wolidays.backend.services.UsuarioService;

@SpringView(name = PerfilView.VIEW_NAME)
public class PerfilView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = 9207545147516956040L;

	public static final String VIEW_NAME = "perfil";
	
	@Autowired
	UsuarioService userService;
	
	@Autowired
	RolService rolService;
	
	private VerticalLayout perfilLayout;
	private VerticalLayout datosLayout;
	
	private Label title;
	private Usuario user;
	private int userId;
	
	private NativeSelect<String> roleSelection = new NativeSelect<>();
	private List<String> roles = new ArrayList<>();
	
	@PostConstruct
	void init() {
		perfilLayout = new VerticalLayout();
		perfilLayout.setWidth("100%");
		perfilLayout.setMargin(false);
		
		datosLayout = new VerticalLayout();
		datosLayout.setWidth("100%");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		
		setRolesOptions();
		roleSelection.setCaption("Rol de usuario:");
		roleSelection.setWidth("150px");
		roleSelection.setHeight("40px");
		roleSelection.setEmptySelectionAllowed(false);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		userId = Integer.parseInt(event.getParameters().split("/")[0]);
		Optional<Usuario> existeUsuario = userService.buscarPorId(userId);
		
		if(existeUsuario.isPresent()) {
			user = existeUsuario.get();
		}
		
		title.setCaption("<h1>Perfil de <i>" + user.getUsername() + "</i></h1>");
		
		Label nombre = new Label("Nombre: " + user.getNombre());
		Label apellidos = new Label("Apellidos: " + user.getApellidos());
		Label correo = new Label("Correo electrónico: " + user.getCorreo());

		datosLayout.addComponents(nombre, apellidos, correo);
		datosLayout.setComponentAlignment(nombre, Alignment.TOP_LEFT);
		datosLayout.setComponentAlignment(apellidos, Alignment.TOP_LEFT);
		datosLayout.setComponentAlignment(correo, Alignment.TOP_LEFT);
		
		if(SecurityUtils.hasRole("ADMIN_ROL")) {
			
			List<Rol> userRoles = user.getRoles();
			String currentRole = userRoles.get(0).getNombre();
			if(currentRole.equals("ADMIN_ROL")) {
				roleSelection.setSelectedItem(roles.get(0));
			} else if(currentRole.equals("GESTOR_ROL")) {
				roleSelection.setSelectedItem(roles.get(1));
			} else {
				roleSelection.setSelectedItem(roles.get(2));
			}
			
			roleSelection.addValueChangeListener(e -> {
				String newRole = roleSelection.getValue();
				List<Rol> role = new ArrayList<>();
				Optional<Rol> opRol;
				
				// Administrador, gestor o cliente
				if(newRole.equals(roles.get(0))) {
					opRol = rolService.buscarPorNombre("ADMIN_ROL");
				} else if(newRole.equals(roles.get(1))) {
					opRol = rolService.buscarPorNombre("GESTOR_ROL");
				} else {
					opRol = rolService.buscarPorNombre("CLIENTE_ROL");
				}
				
				if(opRol.isPresent()) {
					role.add(opRol.get());
					user.setRoles(role);
				}
				
				userService.guardar(user, false);
				setSuccessfulNuevoRolNotification();
				getUI().getNavigator().navigateTo(PerfilView.VIEW_NAME + "/" + user.getId());
				
			});
			
			datosLayout.addComponent(roleSelection);
			datosLayout.setComponentAlignment(roleSelection, Alignment.TOP_LEFT);
		}
		
		perfilLayout.addComponents(title, datosLayout);
		perfilLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		perfilLayout.setComponentAlignment(datosLayout, Alignment.TOP_CENTER);
		addComponent(perfilLayout);
	}
	
	private void setRolesOptions() {
		roles.add("Administrador");
		roles.add("Gestor");
		roles.add("Cliente");
		
		roleSelection.setItems(roles);
	}
	
	private void setSuccessfulNuevoRolNotification() {
		Notification successfulSignUp = new Notification("Rol asignado con éxito");
		successfulSignUp.setIcon(VaadinIcons.CHECK);
		successfulSignUp.setPosition(Position.TOP_RIGHT);
		successfulSignUp.setDelayMsec(1500);
		successfulSignUp.setStyleName("success_notification");
		
		successfulSignUp.show(Page.getCurrent());
	}
}
