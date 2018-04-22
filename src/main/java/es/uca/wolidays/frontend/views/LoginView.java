package es.uca.wolidays.frontend.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9207545147516956040L;

	public static final String VIEW_NAME = "login";
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	
	@PostConstruct
	void init() {
		
		final VerticalLayout loginLayout = new VerticalLayout();
		
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");

        loginLayout.addComponents(username, password);
        loginLayout.setComponentAlignment(username, Alignment.TOP_CENTER);
        loginLayout.setComponentAlignment(password, Alignment.TOP_CENTER);
        
        Button login = new Button("Login", evt -> {
            String pword = password.getValue();
            password.setValue("");
            
            if (!login(username.getValue(), pword)) {
                Notification.show("Error al iniciar sesión", "Credenciales incorrectos", Notification.Type.ERROR_MESSAGE);
                username.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        loginLayout.addComponent(login);
        loginLayout.setComponentAlignment(login, Alignment.TOP_CENTER);
        
        addComponent(loginLayout);
	}
	
	private boolean login(String username, String password) {
		try {
			Authentication token = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Reinitialize the session to protect against session fixation
			// attacks. This does not work with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			
			// Show the main UI
			getUI().getNavigator().navigateTo("");
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}
	
}
