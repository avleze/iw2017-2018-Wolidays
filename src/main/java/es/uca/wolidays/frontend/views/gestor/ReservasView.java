package es.uca.wolidays.frontend.views.gestor;

import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.services.ReservaService;
import es.uca.wolidays.frontend.views.DetalleReservaView;

@SpringView(name = ReservasView.VIEW_NAME)
public class ReservasView extends VerticalLayout implements View {

	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "reservas";
	
	@Autowired
	ReservaService rsrvService;
	
	private VerticalLayout reservasLayout;
	
	private Label title;
	private Grid<Reserva> reservasTabla;
	private ArrayList<Reserva> reservas;

	@PostConstruct
	void init() {
		reservasLayout = new VerticalLayout();
		reservasLayout.setWidth("100%");
		reservasLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Reservas en Wolidays</h1>");
		
		reservasTabla = new Grid<>();
		reservasTabla.setWidth("900px");
		reservas = new ArrayList<>();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		rsrvService.reservas().forEach(reservas::add);
		
		reservasTabla.setItems(reservas);
		
		reservasTabla.addColumn(Reserva::getId).setCaption("ID");
		reservasTabla.addColumn(Reserva::getApartamento).setCaption("Apartamento");
		reservasTabla.addColumn(Reserva::getFechaInicio).setCaption("Fecha inicio");
		reservasTabla.addColumn(Reserva::getFechaFin).setCaption("Fecha fin");
		reservasTabla.addColumn(Reserva::getEstado).setCaption("Estado");
		
		reservasTabla.addSelectionListener(e -> {
			Optional<Reserva> rsrvSelec = e.getFirstSelectedItem();
			if(rsrvSelec.isPresent()) {
				Reserva rsrv = rsrvSelec.get();
				Apartamento apto = rsrv.getApartamento();
				
				getUI().getNavigator().navigateTo(DetalleReservaView.VIEW_NAME + "/" + rsrv.getId() + "/" + apto.getId());
			}
		});
		
		reservasLayout.addComponents(title, reservasTabla);
		reservasLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		reservasLayout.setComponentAlignment(reservasTabla, Alignment.TOP_CENTER);
		addComponent(reservasLayout);
	}
	
	public static void setSuccessfulReservationModNotification() {
		Notification successfulMod = new Notification("Reserva modificada con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(2500);
		successfulMod.setStyleName("success_notification");		
		successfulMod.show(Page.getCurrent());
	}
	
	public static void setSuccessfulReservationAcceptNotification() {
		Notification successfulMod = new Notification("Reserva validada con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(2500);
		successfulMod.setStyleName("success_notification");
		
		successfulMod.show(Page.getCurrent());
	}
	
	public static void setSuccessfulReservationRejectNotification() {
		Notification successfulMod = new Notification("Reserva rechazada con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(2500);
		successfulMod.setStyleName("success_notification");
		
		successfulMod.show(Page.getCurrent());
	}
	
	public static void setSuccessfulReservationDelNotification() {
		Notification successfulMod = new Notification("Reserva eliminada con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(2500);
		successfulMod.setStyleName("success_notification");		
		successfulMod.show(Page.getCurrent());
	}
}
