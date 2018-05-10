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
import com.vaadin.ui.Label;
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
	
	private static final long serialVersionUID = -7191299438297310094L;

	public static final String VIEW_NAME = "signup";
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	
	@Autowired
	private transient UsuarioService service;
	
	@Autowired
	private transient RolService rolService;
	
	@Autowired
	MainScreen mainScreen;
	
	private Label title;
	
	private Binder<Usuario> binder = new Binder<>();
	private String nameRgx = "[a-zA-ZáéíóúÁÉÍÓÚü\\s]+";
	private String usrnameRgx = "[a-zA-Z-_\\d]+";
	
	@PostConstruct
	void init() {
		final VerticalLayout registroLayout = new VerticalLayout();
		registroLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>¡Regístrate en Wolidays!</h1>");
		
		TextField nombre = new TextField("Nombre");
		nombre.setId("form_nombre");
		binder.forField(nombre)
			.withValidator(new RegexpValidator("El nombre solo puede contener letras", nameRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Usuario::getNombre, Usuario::setNombre);
		
		TextField apellidos = new TextField("Apellidos");
		apellidos.setId("form_apellidos");
		binder.forField(apellidos)
			.withValidator(new RegexpValidator("El apellido solo puede contener letras", nameRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Usuario::getApellidos, Usuario::setApellidos);
		
		TextField correo = new TextField("Correo");
		correo.setId("form_correo");
		binder.forField(correo)
			.withValidator(new EmailValidator("Dirección de correo no válida."))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Usuario::getCorreo, Usuario::setCorreo);		
		
		TextField username = new TextField("Username");
		username.setId("form_username");
		binder.forField(username)
			.withValidator(new RegexpValidator("El nombre de usuario sólo admite letras, "
					+ "números, guiones (-) y guiones bajos (_).", usrnameRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Usuario::getUsername, Usuario::setUsername);
		
		PasswordField password = new PasswordField("Contraseña");
		password.setId("form_password");
		binder.forField(password)
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Usuario::getPassword, Usuario::setPassword);
		
		PasswordField confirmPassword = new PasswordField("Confirma la contraseña");
		confirmPassword.setId("form_confirmpassword");
		binder.forField(confirmPassword)
			.asRequired(CAMPO_OBLIGATORIO);
		
		Button registro = new Button("Regístrate");
		registro.setId("form_btn_registrate");
		registroLayout.addComponents(title, nombre, apellidos, correo, username, password, confirmPassword, registro);
		registroLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
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
