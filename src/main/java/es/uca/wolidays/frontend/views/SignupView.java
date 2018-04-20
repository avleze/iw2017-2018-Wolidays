package es.uca.wolidays.frontend.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.services.UsuarioService;

@SpringView(name = SignupView.VIEW_NAME)
public class SignupView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "signup";
	
	@Autowired
	private UsuarioService service;
	
	@PostConstruct
	void init() {
		final VerticalLayout registroLayout = new VerticalLayout();
		
		TextField nombre = new TextField("Nombre");
		TextField apellidos = new TextField("Apellidos");
		TextField correo = new TextField("Correo");
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");
		
		Button registro = new Button("Regístrate");
		
		registroLayout.addComponents(nombre, apellidos, correo, username, password, registro);
		registroLayout.setComponentAlignment(nombre, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(apellidos, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(correo, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(username, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(password, Alignment.TOP_CENTER);
		registroLayout.setComponentAlignment(registro, Alignment.TOP_CENTER);
		
		Usuario user = new Usuario();
		
		registro.addClickListener(e -> {
			user.setNombre(nombre.getValue());
			user.setApellidos(apellidos.getValue());
			user.setCorreo(correo.getValue());
			user.setUsername(username.getValue());
			user.setPassword(password.getValue());
			
			service.guardar(user);
		});
		
		addComponent(registroLayout);
	}
}
