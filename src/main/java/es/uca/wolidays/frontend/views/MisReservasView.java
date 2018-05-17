package es.uca.wolidays.frontend.views;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.entities.Ubicacion;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("wolidays")
@SpringView(name = MisReservasView.VIEW_NAME)
public class MisReservasView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "mis_reservas";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient UsuarioService userService;
	
	private VerticalLayout misReservasLayout;
	private Label title;
	
	private HorizontalLayout reservaInfo;
	private VerticalLayout reservasLeft;
	private VerticalLayout reservasRight;
	
	private Usuario usuario;
	private List<Reserva> usuarioReservas;

	@PostConstruct
	void init() {
		misReservasLayout = new VerticalLayout();
		misReservasLayout.setWidth("100%");
		misReservasLayout.setMargin(false);
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Mis reservas</h1>");
		
		reservaInfo = new HorizontalLayout();
		reservaInfo.setWidth("100%");
		
		reservasLeft = new VerticalLayout();
		reservasRight = new VerticalLayout();
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		usuario = userService.loadUserByUsernameWithReservas(SecurityUtils.getUsername());
		usuarioReservas = usuario.getReservas();
		int i = 0;
		
		if(usuarioReservas.isEmpty()) {
			Notification.show("No has realizado ninguna reserva aún", Notification.Type.ERROR_MESSAGE);
		} else {
			for(Reserva rsrv : usuarioReservas) {
				VerticalLayout reserva = new VerticalLayout();
				reserva.setSpacing(false);
				
				Button fechaLlegada = new Button();
				fechaLlegada.setCaption("Fecha llegada: " + rsrv.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				fechaLlegada.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				
				Button fechaSalida = new Button();
				fechaSalida.setCaption("Fecha salida: " + rsrv.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				fechaSalida.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				
				Ubicacion aptoUbicacion = rsrv.getApartamento().getUbicacion();
				Button ubicacion = new Button();
				ubicacion.setCaption("Ubicación: " + aptoUbicacion.getDireccion() + " (" + aptoUbicacion.getCiudad() + ")");
				ubicacion.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				
				Button precioNoches = new Button();
				precioNoches.setCaption(rsrv.getPrecioFinal() + "€ - " + ChronoUnit.DAYS.between(rsrv.getFechaInicio(), rsrv.getFechaFin()) + " noches");
				precioNoches.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				
				Button estadoReserva = new Button();
				estadoReserva.setCaption("Estado: " + rsrv.getEstado());
				estadoReserva.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				
				reserva.addComponents(fechaLlegada, fechaSalida, ubicacion, precioNoches, estadoReserva);
				
				if(i % 2 == 0) {
					reservasLeft.addComponent(reserva);
				} else {
					reservasRight.addComponent(reserva);
				}
				
				i++;
			}
		}
		
		reservaInfo.addComponents(reservasLeft, reservasRight);
		
		misReservasLayout.addComponents(title, reservaInfo);
		misReservasLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		misReservasLayout.setComponentAlignment(reservaInfo, Alignment.TOP_CENTER);
		
		addComponent(misReservasLayout);
	}
	
	public static void setSuccessfulReservationNotification() {
		Notification successfulSignUp = new Notification("Reserva registrada con éxito");
		successfulSignUp.setIcon(VaadinIcons.CHECK);
		successfulSignUp.setPosition(Position.TOP_RIGHT);
		successfulSignUp.setDelayMsec(2500);
		successfulSignUp.setStyleName("success_notification");
		
		successfulSignUp.show(Page.getCurrent());
	}
	
}
