package es.uca.wolidays.frontend.views;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("navbar")
@SpringView(name = MisApartamentosView.VIEW_NAME)
public class MisApartamentosView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "mis_apartamentos";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient UsuarioService userService;
	
	private VerticalLayout misapartamentosLayout;
	private HorizontalLayout resultadosLayout;
	private VerticalLayout leftAptos;
	private VerticalLayout rightAptos;
	
	private List<Apartamento> misAptos;

	@PostConstruct
	void init() {
		misapartamentosLayout = new VerticalLayout();
		misapartamentosLayout.setWidth("100%");
		misapartamentosLayout.setMargin(false);
		
		leftAptos = new VerticalLayout();
		rightAptos = new VerticalLayout();
		
		resultadosLayout = new HorizontalLayout();
		resultadosLayout.setWidth("100%");
		
		Usuario currentUser = userService.loadUserByUsernameWithApartamentos(SecurityUtils.getUsername());
		misAptos = currentUser.getApartamentos();
		
		setAptosInfoColumns();
	}
	
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		if(misAptos.isEmpty()) {
			
			Notification.show("Actualmente no tiene apartamentos registrados. ¿Desea registrar uno?", Notification.Type.ERROR_MESSAGE);
			Button volverRegistrar = new Button("Registrar apartamento");
			volverRegistrar.setIcon(VaadinIcons.PLUS_CIRCLE);
			volverRegistrar.setClickShortcut(KeyCode.ENTER);
			volverRegistrar.addClickListener(e -> getUI().getNavigator().navigateTo("nuevo_apartamento"));
			leftAptos.addComponent(volverRegistrar);
			
		} else {			
			setApartamentos(misAptos);			
		}
		
		misapartamentosLayout.addComponents(resultadosLayout);
		addComponent(misapartamentosLayout);
	}
	
		private void setAptosInfoColumns() {
			leftAptos = new VerticalLayout();
			leftAptos.setMargin(true);
			leftAptos.setSpacing(true);
			rightAptos = new VerticalLayout();
			rightAptos.setMargin(true);
			rightAptos.setSpacing(true);
		}
		
		private void setApartamentos(List<Apartamento> aptos) {
			
			if(aptos.isEmpty()) {
				Notification.show("No existen apartamentos registrados por usted", Notification.Type.ERROR_MESSAGE);
			} else {
				int i = 0;
				
				for(Apartamento apto : aptos) {
					VerticalLayout aptoInfo = new VerticalLayout();
					aptoInfo.setSpacing(false);
					
					Button ubicacion = new Button(apto.getUbicacion());
					ubicacion.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "large_text");
					ubicacion.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
					
					Button precioStd = new Button("Desde " + String.valueOf(apto.getPrecioEstandar()) + "€ la noche");
					precioStd.setStyleName(ValoTheme.BUTTON_BORDERLESS);
					precioStd.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
					
					Button numCamas;
					
					if(apto.getNumCamas() == 1) {
						numCamas = new Button("1 cama");
					} else {
						numCamas = new Button(String.valueOf(apto.getNumCamas()) + " camas");
					}				
					numCamas.addStyleNames(ValoTheme.BUTTON_BORDERLESS, "small_text");
					numCamas.addClickListener(e -> getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + apto.getId()));
					
					aptoInfo.addComponents(ubicacion, precioStd, numCamas);			
					
					if(i % 2 == 0) {
						leftAptos.addComponent(aptoInfo);
					} else {
						rightAptos.addComponent(aptoInfo);
					}
					
					i++;
				}
				
				resultadosLayout.addComponents(leftAptos, rightAptos);
			}
		
	}
		
	
}