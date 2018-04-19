package es.uca.wolidays.frontend.views;

import java.io.File;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class HomeView extends Composite implements View {
	
	public HomeView() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		
		String logoBasePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoRsc = new FileResource(new File(logoBasePath + "/resources/img/WolidaysLogo.png"));
		Image logo = new Image(null, logoRsc);
		
		TextField searchBar = new TextField();
		searchBar.setPlaceholder("Introduce una ciudad ...");
		searchBar.setWidth("800px");
		
		mainLayout.addComponents(logo, searchBar);
		mainLayout.setComponentAlignment(logo, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(searchBar, Alignment.TOP_CENTER);
		
		setCompositionRoot(mainLayout);
		
	}
}
