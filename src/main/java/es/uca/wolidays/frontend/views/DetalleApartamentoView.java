package es.uca.wolidays.frontend.views;

import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Imagen;
import es.uca.wolidays.backend.entities.Ubicacion;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;
import es.uca.wolidays.frontend.utils.ImageUtils;

@Theme("wolidays")
@SpringView(name = DetalleApartamentoView.VIEW_NAME)
public class DetalleApartamentoView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "detalle_apartamento";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	private VerticalLayout detalleLayout;
	
	private HorizontalLayout infoLayout;
	private VerticalLayout aptoParamsLayout;
	private VerticalLayout descButtonsLayout;
	private HorizontalLayout descLayout;
	private VerticalLayout buttonsLayout;
	
	private Apartamento apartamento;
	private String huespedUsername;
	private int idApto;
	private Label title;

	@PostConstruct
	void init() {
		detalleLayout = new VerticalLayout();
		detalleLayout.setWidth("100%");
		detalleLayout.setMargin(false);
		
		infoLayout = new HorizontalLayout();
		infoLayout.setWidth("100%");
		aptoParamsLayout = new VerticalLayout();
		
		title = new Label();
		title.setCaptionAsHtml(true);
		
		descButtonsLayout = new VerticalLayout();
		descButtonsLayout.setHeight("100%");
		descLayout = new HorizontalLayout();
		descLayout.setHeight("100%");
		buttonsLayout = new VerticalLayout();
		buttonsLayout.setHeight("100%");
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		idApto = Integer.parseInt(event.getParameters().split("/")[0]);
		
		Optional<Apartamento> existeApartamento = aptoService.buscarPorIdConImagenes(idApto);
		
		if(existeApartamento.isPresent()) {
			apartamento = existeApartamento.get();
		}
		
		huespedUsername = apartamento.getPropietario().getUsername();
		
		Ubicacion aptoUbi = apartamento.getUbicacion();
		title.setCaption("<h1>Apartamento de <i>" + aptoUbi.getDireccion() + " (" + aptoUbi.getCiudad() + ")</i></h1>");
		
		// Parametros del apartamento
		Label contacto = new Label("Contacto: " + apartamento.getContacto());
		Label ubicacion = new Label("Ubicación: " + aptoUbi.getDireccion() + " (" + aptoUbi.getCiudad() + ")");
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
		
		if(SecurityUtils.isLoggedIn() && huespedUsername.equals(SecurityUtils.getUsername())) {
			/*
			 * El usuario ha iniciado sesión y es el propietario del apartamento en cuestión.
			 */
			Button ofertasAptoButton = new Button("Ver ofertas");
			ofertasAptoButton.addClickListener(e -> {
				getUI().getNavigator().navigateTo(OfertasView.VIEW_NAME + "/" + idApto);
			});
			
			Button reservasAptoButton = new Button("Ver reservas");
			reservasAptoButton.addClickListener(e -> {
				getUI().getNavigator().navigateTo(SolicitudesView.VIEW_NAME + "/" + idApto);
			});
			
			buttonsLayout.addComponents(ofertasAptoButton, reservasAptoButton);
			buttonsLayout.setComponentAlignment(ofertasAptoButton, Alignment.BOTTOM_RIGHT);
			buttonsLayout.setComponentAlignment(reservasAptoButton, Alignment.BOTTOM_RIGHT);
		}
		else {
			/*
			 * El usuario no ha iniciado sesión o ha iniciado sesión y no es propietario del 
			 * apartamento en cuestión.
			 */
			Button reservarAptoButton = new Button("Reservar");
			reservarAptoButton.addClickListener(e -> {
				if(SecurityUtils.isLoggedIn()) {
					int userid = SecurityUtils.getUserId();
					getUI().getNavigator().navigateTo(NuevaReservaView.VIEW_NAME + "/" + userid + "/" + idApto);
				} else {
					Notification.show("Debes iniciar sesión", "para poder reservar apartamentos.", Notification.Type.ERROR_MESSAGE);
				}
				
			});
			
			buttonsLayout.addComponent(reservarAptoButton);
			buttonsLayout.setComponentAlignment(reservarAptoButton, Alignment.BOTTOM_RIGHT);
		}
		
		descButtonsLayout.addComponents(descLayout, buttonsLayout);
		descButtonsLayout.setComponentAlignment(descLayout, Alignment.TOP_LEFT);
		descButtonsLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
		
		infoLayout.addComponents(aptoParamsLayout, descButtonsLayout);
		infoLayout.setComponentAlignment(aptoParamsLayout, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(descButtonsLayout, Alignment.TOP_RIGHT);
		
		Set<Imagen> imagenes = apartamento.getImagenes();
		GridLayout imagenesLayout = new GridLayout(4,4);
		if(!imagenes.isEmpty())
		{
			for(Imagen i : imagenes)
			{
				Image image = ImageUtils.convertToImage(i.getImagen());
				image.setWidth(200, Unit.PIXELS);
				imagenesLayout.addComponent(image);
				imagenesLayout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
			}
			
			detalleLayout.addComponents(title, imagenesLayout, infoLayout);
			detalleLayout.setComponentAlignment(imagenesLayout, Alignment.TOP_CENTER);
		}
		else
			detalleLayout.addComponents(title, infoLayout);
		
		detalleLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		detalleLayout.setComponentAlignment(title, Alignment.TOP_CENTER);

		detalleLayout.setComponentAlignment(infoLayout, Alignment.TOP_CENTER);
		
		addComponent(detalleLayout);
	}
	
}
