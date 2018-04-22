package es.uca.wolidays.frontend.views;

import java.io.File;

import javax.annotation.PostConstruct;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("navbar")
@SpringView(name = HomeView.VIEW_NAME)
public class HomeView extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "";
	
	
	@PostConstruct
	void init() {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		
		String logoBasePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource logoRsc = new FileResource(new File(logoBasePath + "/resources/img/WolidaysLogo.png"));
		Image logo = new Image(null, logoRsc);
		
		CssLayout searchBar = new CssLayout();
		
		TextField searchField = new TextField();
		searchField.setPlaceholder("Introduce una ciudad ...");
		searchField.setWidth("730px");
		
		Button searchButton = new Button("Buscar");
		searchButton.setWidth("80px");
		searchButton.setClickShortcut(KeyCode.ENTER);
		searchButton.addClickListener(e -> Notification.show("Botón 'Buscar' pulsado"));
		searchButton.setStyleName("button");
		
		searchBar.addComponents(searchField, searchButton);
		searchBar.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		mainLayout.addComponents(logo, searchBar);
		mainLayout.setComponentAlignment(logo, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(searchBar, Alignment.TOP_CENTER);
		
		addComponent(mainLayout);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
}
