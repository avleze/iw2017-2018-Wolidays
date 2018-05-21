package es.uca.wolidays.frontend.views.gestor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Incidencia;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.ReservaService;

@SpringView(name = DetalleReservaView.VIEW_NAME)
public class DetalleReservaView extends VerticalLayout implements View {
	
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "detalle_reserva";
	
	@Autowired
	ReservaService rsrvService;
	
	@Autowired
	ApartamentoService aptoService;
	
	private VerticalLayout detalleReservaLayout;
	private HorizontalLayout infoLayout;
	private VerticalLayout detallesLayout;
	private VerticalLayout fechasLayout;
	
	private Label title;
	
	private int rsrvId;
	private int aptoId;
	private Reserva reserva;
	private Apartamento apartamento;
	private ArrayList<Incidencia> incidencias;
	
	private Double nuevoPrecioFinal;
	
	private Binder<Reserva> binder = new Binder<>();
	
	@PostConstruct
	void init() {
		detalleReservaLayout = new VerticalLayout();
		detalleReservaLayout.setWidth("100%");
		
		infoLayout = new HorizontalLayout();
		infoLayout.setMargin(false);
		
		detallesLayout = new VerticalLayout();
		detallesLayout.setMargin(false);
		
		fechasLayout = new VerticalLayout();
		fechasLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Detalle de reserva</h1>");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		rsrvId = Integer.parseInt(event.getParameters().split("/")[0]);
		aptoId = Integer.parseInt(event.getParameters().split("/")[1]);
		Optional<Reserva> existeReserva = rsrvService.buscarPorId(rsrvId);
		Optional<Apartamento> existeApartamento = aptoService.buscarPorId(aptoId);
		
		if(existeReserva.isPresent()) {
			reserva = existeReserva.get();
		}
		
		if(existeApartamento.isPresent()) {
			apartamento = existeApartamento.get();
		}
		
		incidencias = new ArrayList<>(reserva.getIncidencias());
		
		Label anfitrion = new Label("Anfitrión: " + apartamento.getPropietario());
		Label huesped = new Label("Huésped: " + reserva.getUsuario());
		Label precio = new Label("Precio: " + reserva.getPrecioFinal());
		Label estado = new Label("Estado: " + reserva.getEstado());
		Label numIncidencias = new Label("Incidencias: " + incidencias.size());
		
		ArrayList<Label> listIncid = new ArrayList<>();
		for(Incidencia i : incidencias) {
			listIncid.add(new Label("\t" + i.getFechaIncidencia() + " - " + i.getComentario()));
		}		
		
		
		DateField fechaInicioField = new DateField("Fecha de inicio");
		fechaInicioField.setValue(reserva.getFechaInicio());
		binder.forField(fechaInicioField)
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Reserva::getFechaInicio, Reserva::setFechaInicio);
		
		DateField fechaFinField = new DateField("Fecha fin");
		fechaFinField.setValue(reserva.getFechaFin());
		
		Binder.BindingBuilder<Reserva, LocalDate> fechaFinBindingBuilder = 
				binder.forField(fechaFinField)
					.withValidator(fechaFin -> !fechaFin.isBefore(fechaInicioField.getValue()), "No puedes irte antes de llegar")
					.asRequired(CAMPO_OBLIGATORIO);
		
		Binder.Binding<Reserva, LocalDate> returnBinder = 
				fechaFinBindingBuilder.bind(Reserva::getFechaFin, Reserva::setFechaFin);
		
		fechaFinField.addValueChangeListener(compDates -> returnBinder.validate());
		
		Button modificarReservaButton = new Button("Modificar reserva");
		modificarReservaButton.addClickListener(e -> {
			try {
				nuevoPrecioFinal = rsrvService.calcularPrecioFinal(apartamento, 
																	fechaInicioField.getValue(), 
																	fechaFinField.getValue());
				
				reserva.setPrecioFinal(nuevoPrecioFinal);
				binder.writeBean(reserva);
				rsrvService.guardar(reserva);
				ReservasView.setSuccessfulReservationModNotification();
				getUI().getNavigator().navigateTo("reservas");
			} catch (ValidationException vEx) {
				Notification.show("No se ha podido modificar la reserva.");
			}
		});
		modificarReservaButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		Button volverReservas = new Button("Volver a reservas");
		volverReservas.setIcon(VaadinIcons.ARROW_BACKWARD);
		volverReservas.addClickListener(e -> getUI().getNavigator().navigateTo("reservas"));
		
		
		
		detallesLayout.addComponents(anfitrion, huesped, precio, estado, numIncidencias);
		detallesLayout.setComponentAlignment(anfitrion, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(huesped, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(precio, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(estado, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(numIncidencias, Alignment.TOP_LEFT);
		for(Label incid : listIncid) {
			detallesLayout.addComponent(incid);
			detallesLayout.setComponentAlignment(incid, Alignment.TOP_LEFT);
		}		
		detallesLayout.addComponent(volverReservas);
		detallesLayout.setComponentAlignment(volverReservas, Alignment.TOP_LEFT);
		
		fechasLayout.addComponents(fechaInicioField, fechaFinField, modificarReservaButton);
		fechasLayout.setComponentAlignment(fechaInicioField, Alignment.TOP_LEFT);
		fechasLayout.setComponentAlignment(fechaFinField, Alignment.TOP_LEFT);
		fechasLayout.setComponentAlignment(modificarReservaButton, Alignment.TOP_LEFT);
		
		infoLayout.addComponents(detallesLayout, fechasLayout);
		
		detalleReservaLayout.addComponents(title, infoLayout);
		detalleReservaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		detalleReservaLayout.setComponentAlignment(infoLayout, Alignment.TOP_CENTER);
		
		addComponent(detalleReservaLayout);
	}
}
