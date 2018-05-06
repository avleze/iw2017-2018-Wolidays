package es.uca.wolidays.frontend.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.frontend.MainScreen;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9207545147516956040L;

	public static final String VIEW_NAME = "login";
	
	@Autowired
	transient AuthenticationManager authenticationManager;
	
	@Autowired
	MainScreen mainScreen;
	
	private final VerticalLayout loginLayout = new VerticalLayout();
	
	@PostConstruct
	void init() {
		
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");

        loginLayout.addComponents(username, password);
        loginLayout.setComponentAlignment(username, Alignment.TOP_CENTER);
        loginLayout.setComponentAlignment(password, Alignment.TOP_CENTER);
        
        username.focus();
        
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
			
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			
			HomeView.setSuccessfulLoginNotification();
			mainScreen.setPerfilButtonCaption();
			getUI().getNavigator().navigateTo("");
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
	}
	
	public static void setSuccessfulSignUpNotification() {
		Notification successfulSignUp = new Notification("Registro completado con éxito");
		successfulSignUp.setIcon(VaadinIcons.CHECK);
		successfulSignUp.setPosition(Position.TOP_RIGHT);
		successfulSignUp.setDelayMsec(3500);
		successfulSignUp.setStyleName("success_notification");
		
		successfulSignUp.show(Page.getCurrent());
	}
	
}
