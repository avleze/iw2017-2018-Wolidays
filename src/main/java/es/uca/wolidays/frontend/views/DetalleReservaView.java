package es.uca.wolidays.frontend.views;

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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Incidencia;
import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.entities.TransaccionReserva;
import es.uca.wolidays.backend.entities.TransaccionPenalizacion;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.ReglasDeNegocioService;
import es.uca.wolidays.backend.services.ReservaService;
import es.uca.wolidays.backend.services.TransaccionService;
import es.uca.wolidays.frontend.views.gestor.ReservasView;

@SpringView(name = DetalleReservaView.VIEW_NAME)
public class DetalleReservaView extends VerticalLayout implements View {
	
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "detalle_reserva";
	
	@Autowired
	private transient ReservaService rsrvService;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	@Autowired
	private transient TransaccionService transacService;
	
	@Autowired
	private transient ReglasDeNegocioService reglasNegService;
	
	private VerticalLayout detalleReservaLayout;
	private HorizontalLayout infoLayout;
	private VerticalLayout detallesLayout;
	private VerticalLayout fechasLayout;
	private CssLayout botonesReserva;
	
	private Label title;
	
	private int rsrvId;
	private int aptoId;
	private Reserva reserva;
	private Apartamento apartamento;
	private ArrayList<Incidencia> incidencias;
	
	private Double nuevoPrecioFinal;
	
	private Binder<Reserva> binder = new Binder<>();
	private Binder<TransaccionReserva> binderTransacRes= new Binder<>();
	private Binder<TransaccionPenalizacion> binderTransac = new Binder<>();
	
	@PostConstruct
	void init() {
		detalleReservaLayout = new VerticalLayout();
		detalleReservaLayout.setWidth("100%");
		
		infoLayout = new HorizontalLayout();
		infoLayout.setWidth("100%");
		infoLayout.setMargin(false);
		
		detallesLayout = new VerticalLayout();
		detallesLayout.setMargin(false);
		
		fechasLayout = new VerticalLayout();
		fechasLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Detalle de reserva</h1>");
		
		botonesReserva = new CssLayout();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		rsrvId = Integer.parseInt(event.getParameters().split("/")[0]);
		aptoId = Integer.parseInt(event.getParameters().split("/")[1]);
		Optional<Reserva> existeReserva = rsrvService.buscarReservaPorIdConIncidencias(rsrvId);
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
		Label numIncidencias = new Label("Incidencias: " + incidencias.size());
		Label estado = new Label("Estado: " + reserva.getEstado());
		
		ArrayList<Label> listIncid = new ArrayList<>();
		for(Incidencia i : incidencias) {
			Label incidencia = new Label();
			incidencia.setCaptionAsHtml(true);
			incidencia.setCaption("&emsp;" + i.getFechaIncidencia() + " - " + i.getComentario());
			
			listIncid.add(incidencia);
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
				if(SecurityUtils.hasRole("CLIENTE_ROL") && apartamento.getPropietario().toString().equals(SecurityUtils.getUsername())) {
					TransaccionPenalizacion transPenaliz = new TransaccionPenalizacion();
					transPenaliz.setCuentaOrigen(apartamento.getPropietario().getCuentaBancaria());
					transPenaliz.setCuentaDestino(reserva.getUsuario().getCuentaBancaria());
					transPenaliz.setCosteAdicional(reglasNegService.calcularCosteAdicionalPorPenalizacion(Penalizacion.TipoUsuario.Anfitrion, Penalizacion.Motivo.Modificacion, reserva));
					transPenaliz.setUsuarioAfectado(reserva.getUsuario());
					transPenaliz.setUsuarioPenalizado(apartamento.getPropietario());
					binderTransac.writeBean(transPenaliz);
					transacService.guardar(transPenaliz);
					
				}
				else if(SecurityUtils.hasRole("CLIENTE_ROL") && reserva.getUsuario().toString().equals(SecurityUtils.getUsername())) {
					TransaccionPenalizacion transPenaliz = new TransaccionPenalizacion();
					transPenaliz.setCuentaOrigen(reserva.getUsuario().getCuentaBancaria());
					transPenaliz.setCuentaDestino(apartamento.getPropietario().getCuentaBancaria());
					transPenaliz.setCosteAdicional(reglasNegService.calcularCosteAdicionalPorPenalizacion(Penalizacion.TipoUsuario.Huesped, Penalizacion.Motivo.Modificacion, reserva));
					transPenaliz.setUsuarioAfectado(apartamento.getPropietario());
					transPenaliz.setUsuarioPenalizado(reserva.getUsuario());
					binderTransac.writeBean(transPenaliz);
					transacService.guardar(transPenaliz);
				}
				if(SecurityUtils.hasRole("GESTOR_ROL"))
					getUI().getNavigator().navigateTo("reservas");
				else {
					getUI().getNavigator().navigateTo("mis_reservas");
				}
			} catch (ValidationException vEx) {
				Notification.show("No se ha podido modificar la reserva.");
			}
		});
		modificarReservaButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		Label modificarReservaLabel = new Label("* Modificar las fechas de la reserva puede derivar en penalizaciones");
		
		detallesLayout.addComponents(anfitrion, huesped, precio, numIncidencias, estado);
		detallesLayout.setComponentAlignment(anfitrion, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(huesped, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(precio, Alignment.TOP_LEFT);
		detallesLayout.setComponentAlignment(numIncidencias, Alignment.TOP_LEFT);
		for(Label incid : listIncid) {
			detallesLayout.addComponent(incid);
			detallesLayout.setComponentAlignment(incid, Alignment.TOP_LEFT);
		}
		
	    if(reserva.getEstado().toString().equals("Pendiente") && SecurityUtils.isLoggedIn() 
	    		&& SecurityUtils.hasRole("CLIENTE_ROL") && apartamento.getPropietario().toString().equals(SecurityUtils.getUsername())) {
	    	Button aceptarReservaButton = new Button("Aceptar reserva");
	    	aceptarReservaButton.setIcon(VaadinIcons.CHECK);
	    	Button rechazarReservaButton = new Button("Rechazar reserva");
	    	rechazarReservaButton.setIcon(VaadinIcons.CLOSE);
	    	aceptarReservaButton.addClickListener(e -> {
	    		try {
	    			reserva.setEstado(Reserva.Estado.Validada);
	    			binder.writeBean(reserva);
					rsrvService.guardar(reserva);
					TransaccionReserva transacReserva = new TransaccionReserva();
					transacReserva.setCuentaAnfitrion(apartamento.getPropietario().getCuentaBancaria());
					transacReserva.setReservaAsociada(reserva);
					transacReserva.setTarjetaHuesped(reserva.getTarjetaHuesped());
					transacReserva.setBeneficioEmpresa(reserva.getPrecioFinal()*transacService.obtenerPctBeneficioActual());
					binderTransacRes.writeBean(transacReserva);
					transacService.guardar(transacReserva);
		    		ReservasView.setSuccessfulReservationAcceptNotification();
		    		getUI().getNavigator().navigateTo(DetalleReservaView.VIEW_NAME + "/" + reserva.getId() + "/" + apartamento.getId());
	    		} catch (ValidationException vEx) {
	    			Notification.show("No se ha podido aceptar la reserva");
	    		}
	    		
	    	});
	    	rechazarReservaButton.addClickListener(e -> {
	    		try {
	    			reserva.setEstado(Reserva.Estado.Rechazada);
	    			binder.writeBean(reserva);
					rsrvService.guardar(reserva);
		    		ReservasView.setSuccessfulReservationRejectNotification();
		    		getUI().getNavigator().navigateTo(DetalleReservaView.VIEW_NAME + "/" + reserva.getId() + "/" + apartamento.getId());
	    		} catch (ValidationException vEx){
	    			Notification.show("No se ha podido rechazar la reserva");		
	    		}
	    		
	    	});
	    	
	    	botonesReserva.addComponents(aceptarReservaButton, rechazarReservaButton);
	    	botonesReserva.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
	    	detallesLayout.addComponent(botonesReserva);
	    	detallesLayout.setComponentAlignment(botonesReserva, Alignment.TOP_LEFT);
			
		}
		detallesLayout.setComponentAlignment(estado, Alignment.TOP_LEFT);
		
		if(SecurityUtils.isLoggedIn() && SecurityUtils.hasRole("GESTOR_ROL")) {
			Button volverReservas = new Button("Volver a reservas");
			volverReservas.setIcon(VaadinIcons.ARROW_BACKWARD);
			volverReservas.addClickListener(e -> getUI().getNavigator().navigateTo("reservas"));
			
			detallesLayout.addComponent(volverReservas);
			detallesLayout.setComponentAlignment(volverReservas, Alignment.TOP_LEFT);
		}
						
		fechasLayout.addComponents(fechaInicioField, fechaFinField, modificarReservaButton);
		if(!SecurityUtils.hasRole("GESTOR_ROL")) {
			fechasLayout.addComponents(modificarReservaLabel);
			fechasLayout.setComponentAlignment(modificarReservaLabel, Alignment.TOP_LEFT);
		}
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
