package es.uca.wolidays.frontend;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//import es.uca.wolidays.frontend.views.HomeView;
//import es.uca.wolidays.frontend.views.LoginView;
//import es.uca.wolidays.frontend.views.SignupView;


@Theme("navbar")
@SpringUI
@PushStateNavigation
public class WolidaysUI extends UI {
	
	@Autowired
	SpringViewProvider viewProvider;
	
	@Autowired
	MainScreen mainScreen;
	
	@Override
	protected void init(VaadinRequest request) {
		
		showMainScreen();
		
	}

	private void showMainScreen() {
		setContent(mainScreen);
	}

}
