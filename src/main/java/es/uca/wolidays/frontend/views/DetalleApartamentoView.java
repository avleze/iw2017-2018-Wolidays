package es.uca.wolidays.frontend.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("navbar")
@SpringView(name = DetalleApartamentoView.VIEW_NAME)
public class DetalleApartamentoView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "detalle_apartamento";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	ApartamentoService aptoService;
	
	private VerticalLayout detalleLayout;
	
	private HorizontalLayout infoLayout;
	private VerticalLayout aptoParamsLayout;
	private VerticalLayout desc_buttons_Layout;
	private HorizontalLayout descLayout;
	private VerticalLayout buttonsLayout;
	
	private Apartamento apartamento;
	private String huesped_username;
	private int id_apto;
	private Label detalleTitulo;

	@PostConstruct
	void init() {
		detalleLayout = new VerticalLayout();
		detalleLayout.setWidth("100%");
		
		infoLayout = new HorizontalLayout();
		infoLayout.setWidth("100%");
		aptoParamsLayout = new VerticalLayout();
		
		desc_buttons_Layout = new VerticalLayout();
		desc_buttons_Layout.setHeight("100%");
		descLayout = new HorizontalLayout();
		descLayout.setHeight("100%");
		buttonsLayout = new VerticalLayout();
		buttonsLayout.setHeight("100%");
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		id_apto = Integer.parseInt(event.getParameters().split("/")[0]);
		apartamento = aptoService.buscarPorId(id_apto).get();
		
		huesped_username = apartamento.getPropietario().getUsername();
		
		detalleTitulo = new Label("Apartamento de " + apartamento.getUbicacion());
		detalleTitulo.setStyleName("detail_apto_title");
		
		// Parametros del apartamento
		Label contacto = new Label("Contacto: " + apartamento.getContacto());
		Label ubicacion = new Label("Ubicación: " + apartamento.getUbicacion());
		Label precioStd = new Label("Precio por noche estándar: " + apartamento.getPrecioEstandar() + "€");
		Label numCamas = new Label("Número de camas: " + apartamento.getNumCamas());
		Label numDormitorios = new Label("Número de dormitorios: " + apartamento.getNumDormitorios());
		Label aireAcondicionado = apartamento.getAireAcondicionado() ? new Label("Aire acondicionado: Sí") : new Label("Aire acondicionado: No");
		
		aptoParamsLayout.addComponents(contacto, ubicacion, precioStd, numCamas, numDormitorios, aireAcondicionado);
		aptoParamsLayout.setComponentAlignment(contacto, Alignment.TOP_LEFT);
		aptoParamsLayout.setComponentAlignment(ubicacion, Alignment.TOP_LEFT);
		aptoParamsLayout.setComponentAlignment(precioStd, Alignment.TOP_LEFT);
		aptoParamsLayout.setComponentAlignment(numCamas, Alignment.TOP_LEFT);
		aptoParamsLayout.setComponentAlignment(numDormitorios, Alignment.TOP_LEFT);
		aptoParamsLayout.setComponentAlignment(aireAcondicionado, Alignment.TOP_LEFT);
		
		// Descripcion
		String aptoDesc = apartamento.getDescripcion();
		Label descripcion;
		descripcion = aptoDesc.isEmpty() ? new Label("Descripcion: No hay descripcion disponible") : new Label("Descripcion: " + aptoDesc);
		
		descLayout.addComponent(descripcion);
		descLayout.setComponentAlignment(descripcion, Alignment.TOP_LEFT);
		
		// Botones
		
		if(SecurityUtils.isLoggedIn() && huesped_username.equals(SecurityUtils.getUsername())) {
			/*
			 * Aquí se establecen los botones en el caso de que un usuario haya iniciado sesión
			 * y sea el propietario del apartamento en cuestión.
			 */
		} else {
			/*
			 * El usuario no ha iniciado sesión o ha iniciado sesión y no es propietario del 
			 * apartamento en cuestión.
			 */
			Button reservarAptoButton = new Button("Reservar");
			reservarAptoButton.addClickListener(e -> {
				if(SecurityUtils.isLoggedIn()) {
					int userid = SecurityUtils.getUserId();
					getUI().getNavigator().navigateTo(NuevaReservaView.VIEW_NAME + "/" + userid + "/" + id_apto);
				} else {
					Notification.show("Debes iniciar sesión", "para poder reservar apartamentos.", Notification.Type.ERROR_MESSAGE);
				}
				
			});
			
			buttonsLayout.addComponent(reservarAptoButton);
			buttonsLayout.setComponentAlignment(reservarAptoButton, Alignment.BOTTOM_RIGHT);
		}
		
		desc_buttons_Layout.addComponents(descLayout, buttonsLayout);
		desc_buttons_Layout.setComponentAlignment(descLayout, Alignment.TOP_LEFT);
		desc_buttons_Layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
		
		infoLayout.addComponents(aptoParamsLayout, desc_buttons_Layout);
		infoLayout.setComponentAlignment(aptoParamsLayout, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(desc_buttons_Layout, Alignment.TOP_RIGHT);
		
		detalleLayout.addComponents(detalleTitulo, infoLayout);
		detalleLayout.setComponentAlignment(detalleTitulo, Alignment.TOP_CENTER);
		detalleLayout.setComponentAlignment(infoLayout, Alignment.TOP_CENTER);
		
		addComponent(detalleLayout);
	}
	
}
