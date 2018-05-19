package es.uca.wolidays.frontend.views;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.services.UsuarioService;

@SpringView(name = PerfilView.VIEW_NAME)
public class PerfilView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = 9207545147516956040L;

	public static final String VIEW_NAME = "perfil";
	
	@Autowired
	UsuarioService userService;
	
	private VerticalLayout perfilLayout;
	private VerticalLayout datosLayout;
	
	private Label title;
	private Usuario user;
	private int userId;
	
	@PostConstruct
	void init() {
		perfilLayout = new VerticalLayout();
		perfilLayout.setWidth("100%");
		perfilLayout.setMargin(false);
		
		datosLayout = new VerticalLayout();
		datosLayout.setWidth("100%");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		
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
		
		perfilLayout.addComponents(title, datosLayout);
		perfilLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		perfilLayout.setComponentAlignment(datosLayout, Alignment.TOP_CENTER);
		addComponent(perfilLayout);
	}
}
