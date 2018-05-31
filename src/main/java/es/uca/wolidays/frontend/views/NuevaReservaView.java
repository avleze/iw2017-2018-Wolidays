package es.uca.wolidays.frontend.views;

import java.time.LocalDate;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.RegexpValidator;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.ReservaService;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("wolidays")
@SpringView(name = NuevaReservaView.VIEW_NAME)
public class NuevaReservaView extends VerticalLayout implements View {
	
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nueva_reserva";
	
	@Autowired
	private transient UsuarioService userService;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	@Autowired
	private transient ReservaService reservaService;
	
	@Autowired
	MainScreen mainScreen;
	
	private Optional<Apartamento> existeApartamento;
	
	private VerticalLayout nuevaReservaLayout;
	private Label title;
	
	private HorizontalLayout formLayout;
	private VerticalLayout infoLayout;
	private VerticalLayout fechaLayout;
	private Label precioFinalLabel;
	
	private Usuario usuario;
	private int aptoId;
	private Apartamento apartamento;
	private Double precioFinal;
	
	private Binder<Reserva> binder = new Binder<>();
	private String contactoRgx = "(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+"
			+ "\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-"
			+ "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]"
			+ "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
			+ "|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|"
			+ "[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f"
			+ "\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])|(\\d{9})";
	private String tarjetaRgx = "\\d{16}";

	@PostConstruct
	void init() {
		nuevaReservaLayout = new VerticalLayout();
		nuevaReservaLayout.setWidth("100%");
		nuevaReservaLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Nueva reserva</h1>");
		
		formLayout = new HorizontalLayout();
		formLayout.setWidth("100%");
		infoLayout = new VerticalLayout();
		fechaLayout = new VerticalLayout();
		precioFinalLabel = new Label("Precio final: ");
		precioFinalLabel.setStyleName("final_price");
		
		nuevaReservaLayout.addComponent(title);
		nuevaReservaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		aptoId = Integer.parseInt(event.getParameters().split("/")[1]);
		usuario = (Usuario)userService.loadUserByUsername(SecurityUtils.getUsername());
		
		existeApartamento = aptoService.buscarPorIdConOfertas(aptoId);
		
		if(existeApartamento.isPresent()) {
			apartamento = existeApartamento.get();
		}
		
		TextField contactoField = new TextField("Contacto");
		contactoField.setIcon(VaadinIcons.INFO_CIRCLE);
		contactoField.setDescription("Correo electrónico o télefono (9 dígitos) que servirán para ponerse en contacto contigo.");
		binder.forField(contactoField)
			.withValidator(new RegexpValidator("El contacto debe ser un email o un número de teléfono", contactoRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Reserva::getContacto, Reserva::setContacto);
		
		TextArea comentarioField = new TextArea("Comentario");
		comentarioField.setIcon(VaadinIcons.INFO_CIRCLE);
		comentarioField.setDescription("Máximo 250 caracteres.");
		comentarioField.setMaxLength(250);
		comentarioField.setWidth("300px");
		binder.forField(comentarioField)
			.bind(Reserva::getComentario, Reserva::setComentario);
		
		TextField tarjetaCredField = new TextField("Tarjeta de crédito");
		tarjetaCredField.setIcon(VaadinIcons.INFO_CIRCLE);
		tarjetaCredField.setDescription("Número formado por 16 dígitos.");
		tarjetaCredField.setMaxLength(16);
		binder.forField(tarjetaCredField)
			.withValidator(new RegexpValidator("La tarjeta de crédito debe tener 16 dígitos.", tarjetaRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Reserva::getTarjetaHuesped, Reserva::setTarjetaHuesped);
		
		infoLayout.addComponents(contactoField, comentarioField, tarjetaCredField);
		infoLayout.setComponentAlignment(contactoField, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(comentarioField, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(tarjetaCredField, Alignment.TOP_LEFT);
		
		DateField fechaInicioField = new DateField("Fecha de inicio");
		fechaInicioField.setValue(LocalDate.now());
		binder.forField(fechaInicioField)
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Reserva::getFechaInicio, Reserva::setFechaInicio);
		
		DateField fechaFinField = new DateField("Fecha fin");
		fechaFinField.setEnabled(false);
		fechaInicioField.addValueChangeListener(date -> {
			fechaFinField.setEnabled(true);
			fechaFinField.setValue(fechaInicioField.getValue().plusDays(1));
		});
		
		Binder.BindingBuilder<Reserva, LocalDate> fechaFinBindingBuilder = 
				binder.forField(fechaFinField)
					.withValidator(fechaFin -> !fechaFin.isBefore(fechaInicioField.getValue()), "No puedes irte antes de llegar")
					.asRequired(CAMPO_OBLIGATORIO);
		
		Binder.Binding<Reserva, LocalDate> returnBinder = 
				fechaFinBindingBuilder.bind(Reserva::getFechaFin, Reserva::setFechaFin);
		
		fechaFinField.addValueChangeListener(compDates -> returnBinder.validate());
		fechaFinField.addValueChangeListener(precio -> {
			precioFinal = reservaService.calcularPrecioFinal(apartamento, fechaInicioField.getValue(), fechaFinField.getValue());
			precioFinalLabel.setValue("Precio final: " + precioFinal + "€");
		});
		
		Button realizarReservaButton = new Button("Realizar reserva");
		realizarReservaButton.addClickListener(nuevaRes -> {
			
			existeApartamento = aptoService.buscarPorIdConOfertas(aptoId);
			
			if(existeApartamento.isPresent()) {
				apartamento = existeApartamento.get();
			}
			
			try {
				Double nuevoPrecioFinal = reservaService.calcularPrecioFinal(apartamento, fechaInicioField.getValue(), fechaFinField.getValue());
				
				if(Double.compare(precioFinal, nuevoPrecioFinal) != 0) {					
					Notification.show("El precio del apartamento ha cambiado mientras realizabas la reserva.", "Revisa los parámetros de la reserva y el nuevo precio", Notification.Type.ERROR_MESSAGE);
					
				} else {
					
					Reserva reserva = new Reserva();
					reserva.setUsuario(usuario);
					reserva.setApartamento(apartamento);
					reserva.setPrecioFinal(precioFinal);
					reserva.setEstado(Reserva.Estado.Pendiente);
					
					binder.writeBean(reserva);
					reservaService.guardar(reserva);
					MisReservasView.setSuccessfulReservationNotification();
					getUI().getNavigator().navigateTo("mis_reservas");
				}
				
			} catch (ValidationException vEx) {
				Notification.show("No se ha podido completar el registro");
			}
		});
		realizarReservaButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		fechaLayout.addComponents(fechaInicioField, fechaFinField);
		fechaLayout.setComponentAlignment(fechaInicioField, Alignment.TOP_LEFT);
		fechaLayout.setComponentAlignment(fechaFinField, Alignment.TOP_LEFT);
		
		formLayout.addComponents(infoLayout, fechaLayout);
		formLayout.setComponentAlignment(infoLayout, Alignment.TOP_LEFT);
		formLayout.setComponentAlignment(fechaLayout, Alignment.TOP_RIGHT);
		
		nuevaReservaLayout.addComponents(formLayout, precioFinalLabel, realizarReservaButton);
		nuevaReservaLayout.setComponentAlignment(formLayout, Alignment.TOP_CENTER);
		nuevaReservaLayout.setComponentAlignment(precioFinalLabel, Alignment.TOP_CENTER);
		nuevaReservaLayout.setComponentAlignment(realizarReservaButton, Alignment.TOP_CENTER);
		
		addComponent(nuevaReservaLayout);
	}
	

}
