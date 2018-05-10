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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.frontend.MainScreen;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = 9207545147516956040L;

	public static final String VIEW_NAME = "login";
	
	@Autowired
	private transient AuthenticationManager authenticationManager;
	
	@Autowired
	MainScreen mainScreen;
	
	private final VerticalLayout loginLayout = new VerticalLayout();
	
	private Label title;
	
	@PostConstruct
	void init() {
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Iniciar sesión</h1>");
		
		TextField username = new TextField("Username");
		username.setId("form_username");
		PasswordField password = new PasswordField("Password");
		password.setId("form_password");

        loginLayout.addComponents(title, username, password);
        loginLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
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
        login.setId("form_btn_login");
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        loginLayout.addComponents(login);
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
