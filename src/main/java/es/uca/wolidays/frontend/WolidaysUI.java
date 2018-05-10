package es.uca.wolidays.frontend;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import es.uca.wolidays.backend.security.AccessDeniedView;
import es.uca.wolidays.backend.security.ErrorView;


@Theme("wolidays")
@SpringUI
@PushStateNavigation
public class WolidaysUI extends UI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1714516744778221408L;

	@Autowired
	SpringViewProvider viewProvider;
	
	@Autowired
	MainScreen mainScreen;
	
	@Override
	protected void init(VaadinRequest request) {
		this.getUI().getNavigator().setErrorView(ErrorView.class);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		
		showMainScreen();
		
	}

	private void showMainScreen() {
		setContent(mainScreen);
	}

}
