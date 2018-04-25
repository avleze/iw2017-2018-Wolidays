package es.uca.wolidays.frontend.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Rol;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.services.RolService;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.MainScreen;

@SpringView(name = SignupView.VIEW_NAME)
public class SignupView extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7191299438297310094L;

	public static final String VIEW_NAME = "signup";
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	MainScreen mainScreen;
	
	private Binder<Usuario> binder = new Binder<>();
	private String nameRgx = "[a-zA-Záéíóúü\\s]+";
	private String usrnameRgx = "[a-zA-Z-_\\d]+";
	
	@PostConstruct
	void init() {
		final VerticalLayout registroLayout = new VerticalLayout();
		
		TextField nombre = new TextField("Nombre");
		binder.forField(nombre)
			.withValidator(new RegexpValidator("El nombre solo puede contener letras", nameRgx, true))
			.asRequired("Campo obligatorio")
			.bind(Usuario::getNombre, Usuario::setNombre);
		
		TextField apellidos = new TextField("Apellidos");
		binder.forField(apellidos)
			.withValidator(new RegexpValidator("El apellido solo puede contener letras", nameRgx, true))
			.asRequired("Campo obligatorio")
			.bind(Usuario::getApellidos, Usuario::setApellidos);
		
		TextField correo = new TextField("Correo");
		binder.forField(correo)
			.withValidator(new EmailValidator("Dirección de correo no válida."))
			.asRequired("Campo obligatorio")
			.bind(Usuario::getCorreo, Usuario::setCorreo);		
		
		TextField username = new TextField("Username");
		binder.forField(username)
			.withValidator(new RegexpValidator("El nombre de usuario sólo admite letras, "
					+ "números, guiones (-) y guiones bajos (_).", usrnameRgx, true))
			.asRequired("Campo obligatorio")
			.bind(Usuario::getUsername, Usuario::setUsername);
		
		PasswordField password = new PasswordField("Contraseña");
		binder.forField(password)
			.asRequired("Campo obligatorio")
			.bind(Usuario::getPassword, Usuario::setPassword);
		
		PasswordField confirmPassword = new PasswordField("Confirma la contraseña");
		binder.forField(confirmPassword)
			.asRequired("Campo obligatorio");
		
		Button registro = new Button("Regístrate");
		
		registroLayout.addComponents(nombre, apellidos, correo, username, password, confirmPassword, registro);
		registroLayout.setComponentAlignment(nombre, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(apellidos, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(correo, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(username, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(password, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(confirmPassword, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(registro, Alignment.TOP_CENTER);
		
		nombre.focus();

		registro.addClickListener(e -> {
			
			if(!password.getValue().equals(confirmPassword.getValue())) {
				Notification.show("Las contraseñas no coinciden", Notification.Type.ERROR_MESSAGE);
				
			} else {
			
				Usuario user = new Usuario();			
				
				user.setNombre(nombre.getValue());
				user.setApellidos(apellidos.getValue());
				user.setCorreo(correo.getValue());
				user.setUsername(username.getValue());
				user.setPassword(password.getValue());
				List<Rol> roles = new ArrayList<>();
				
				Optional<Rol> defaultRol = rolService.buscarPorNombre("DEFAULT_ROL");
				if(!defaultRol.isPresent())
				{
					Rol rol = new Rol();
					rol.setNombre("DEFAULT_ROL");
					rolService.guardar(rol);
					defaultRol = Optional.of(rol);
				}
				
				roles.add(defaultRol.get());
				user.setRoles(roles);
				
				try {
					
					binder.writeBean(user);				
					service.guardar(user);
					LoginView.setSuccessfulSignUpNotification();
					getUI().getNavigator().navigateTo("login");
					
				} catch(ValidationException ex) {
					Notification.show("No se ha podido completar el registro");
				}
			}
		});
		
		addComponent(registroLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
	}
}
