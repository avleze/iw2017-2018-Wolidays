package es.uca.wolidays.frontend.views;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
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

@Theme("navbar")
@SpringView(name = NuevaReservaView.VIEW_NAME)
public class NuevaReservaView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nueva_reserva";
	
	@Autowired
	UsuarioService userService;
	
	@Autowired
	ApartamentoService aptoService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	MainScreen mainScreen;
	
	private VerticalLayout nuevaReservaLayout;
	private Label title;
	
	private HorizontalLayout formLayout;
	private VerticalLayout infoLayout;
	private VerticalLayout fechaLayout;
	
	private int userId;
	private Usuario usuario;
	private int aptoId;
	private Apartamento apartamento;
	
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
		title = new Label("Nueva reserva");
		title.addStyleName("detail_apto_title");
		
		formLayout = new HorizontalLayout();
		formLayout.setWidth("100%");
		infoLayout = new VerticalLayout();
		fechaLayout = new VerticalLayout();
		
		nuevaReservaLayout.addComponent(title);
		nuevaReservaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		userId = Integer.parseInt(event.getParameters().split("/")[0]);
		aptoId = Integer.parseInt(event.getParameters().split("/")[1]);
		usuario = (Usuario)userService.loadUserByUsername(SecurityUtils.getUsername());
		apartamento = aptoService.buscarPorId(aptoId).get();
		
		TextField contactoField = new TextField("Contacto");
		binder.forField(contactoField)
			.withValidator(new RegexpValidator("El contacto debe ser un email o un número de teléfono", contactoRgx, true))
			.asRequired("Campo obligatorio")
			.bind(Reserva::getContacto, Reserva::setContacto);
		
		TextArea comentarioField = new TextArea("Comentario");
		comentarioField.setWidth("300px");
		binder.forField(comentarioField)
			.bind(Reserva::getComentario, Reserva::setComentario);
		
		TextField tarjetaCredField = new TextField("Tarjeta de crédito");
		binder.forField(tarjetaCredField)
			.withValidator(new RegexpValidator("La tarjeta de crédito debe tener 16 dígitos.", tarjetaRgx, true))
			.asRequired("Campo obligatorio")
			.bind(Reserva::getTarjeta, Reserva::setTarjeta);
		
		infoLayout.addComponents(contactoField, comentarioField, tarjetaCredField);
		infoLayout.setComponentAlignment(contactoField, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(comentarioField, Alignment.TOP_LEFT);
		infoLayout.setComponentAlignment(tarjetaCredField, Alignment.TOP_LEFT);
		
		DateField fechaInicioField = new DateField("Fecha de inicio");
		fechaInicioField.setValue(LocalDate.now());
		binder.forField(fechaInicioField)
			.asRequired("Campo obligatorio")
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
					.asRequired("Campo obligatorio");
		
		Binder.Binding<Reserva, LocalDate> returnBinder = 
				fechaFinBindingBuilder.bind(Reserva::getFechaFin, Reserva::setFechaFin);
		
		fechaFinField.addValueChangeListener(compDates -> returnBinder.validate());
		
		Button realizarReservaButton = new Button("Realizar reserva");
		realizarReservaButton.addClickListener(nuevaRes -> {
			Reserva reserva = new Reserva();
			
			Double precioFinal = 150.0;
			
			reserva.setUsuario(usuario);
			reserva.setApartamento(apartamento);
			reserva.setPrecioFinal(precioFinal);
			
			try {
				binder.writeBean(reserva);
				reservaService.guardar(reserva);
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
		
		nuevaReservaLayout.addComponents(formLayout, realizarReservaButton);
		nuevaReservaLayout.setComponentAlignment(formLayout, Alignment.TOP_CENTER);
		nuevaReservaLayout.setComponentAlignment(realizarReservaButton, Alignment.TOP_CENTER);
		
		addComponent(nuevaReservaLayout);
	}
	
	/**
	 * Método que calcula el precio final de una reserva,
	 * teniendo en cuenta el precio estándar del apartamento,
	 * y todas las posibles ofertas que puede tener definidas.
	 * @return Precio final de una reserva
	 */
	private Double calcularPrecioFinal() {
		Double precioFinal = 150.0;
		
		
		return precioFinal;
	}
	
}
