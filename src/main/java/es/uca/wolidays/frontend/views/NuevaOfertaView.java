package es.uca.wolidays.frontend.views;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.OfertaService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("wolidays")
@SpringView(name = NuevaOfertaView.VIEW_NAME)
public class NuevaOfertaView extends VerticalLayout implements View {
	
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nueva_oferta";
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	@Autowired 
	private transient OfertaService oftaService;
	
	@Autowired
	MainScreen mainScreen;
	
	private VerticalLayout nuevaOfertaLayout;
	private Label title;
	
	private HorizontalLayout formLayout;
	private VerticalLayout infoLayout;
	
	private int aptoId;
	private Apartamento apartamento;
	
	Binder<Oferta> binder = new Binder<>();
	private String precioStdRgx = "^\\d{0,5}(\\.\\d{1,2})?$";
	
	private Boolean precioVacio = true;
	private Boolean precioValido = false;
	
	@PostConstruct
	void init() {
		
		nuevaOfertaLayout = new VerticalLayout();
		nuevaOfertaLayout.setWidth("100%");
		nuevaOfertaLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Nueva oferta</h1>");
		
		formLayout = new HorizontalLayout();
		formLayout.setWidth("100%");
		infoLayout = new VerticalLayout();
		
		nuevaOfertaLayout.addComponent(title);
		nuevaOfertaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		mainScreen.setButtons();
		
		aptoId = Integer.parseInt(event.getParameters().split("/")[0]);
		Optional<Apartamento> existeApto = aptoService.buscarPorId(aptoId);
		
		if(existeApto.isPresent()) {
			apartamento = existeApto.get();
		}		
				
		DateField fechaInicioField = new DateField("Fecha de inicio");
		fechaInicioField.setValue(LocalDate.now());
		binder.forField(fechaInicioField)
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(Oferta::getFechaInicio, Oferta::setFechaInicio);
		
		DateField fechaFinField = new DateField("Fecha fin");
		fechaFinField.setEnabled(false);
		fechaInicioField.addValueChangeListener(date -> {
			fechaFinField.setEnabled(true);
			fechaFinField.setValue(fechaInicioField.getValue().plusDays(1));
		});
		
		Binder.BindingBuilder<Oferta, LocalDate> fechaFinBindingBuilder = 
				binder.forField(fechaFinField)
					.withValidator(fechaFin -> !fechaFin.isBefore(fechaInicioField.getValue()), "La fecha de fin debe ser posterior a la de inicio")
					.asRequired(CAMPO_OBLIGATORIO);
		
		Binder.Binding<Oferta, LocalDate> returnBinder = 
				fechaFinBindingBuilder.bind(Oferta::getFechaFin, Oferta::setFechaFin);
		fechaFinField.addValueChangeListener(compDates -> returnBinder.validate());
		
		TextField precioStdField = new TextField("Precio por noche en oferta");
		precioStdField.setRequiredIndicatorVisible(true);
		precioStdField.setWidth("85px");
		
		Button registrarOfertaButton = new Button("Registrar oferta");
		registrarOfertaButton.addClickListener(nuevaOf -> {
			Oferta oferta = new Oferta();
			oferta.setApartamento(apartamento);
						
			if (!precioStdField.isEmpty()) {
				precioVacio = false;
				
				String precioStd = precioStdField.getValue();
				Pattern patt = Pattern.compile(precioStdRgx);
				Matcher mat = patt.matcher(precioStd);
				if(mat.matches()) {
					precioValido = true;
				}
			}
			
			if(precioVacio) {
				Notification.show("Debes establecer un precio de oferta");
			} else if(!precioValido) {
				Notification.show("El precio debe contener solo números, no empezar por 0 y los decimales deben estar separados con un punto (.)");
			} else {
			
				try {
					oferta.setPrecioOferta(Double.parseDouble(precioStdField.getValue()));
					binder.writeBean(oferta);	
					oftaService.guardar(oferta);
	
					OfertasView.setSuccessfulRegistroOfertaNotification();
					getUI().getNavigator().navigateTo(OfertasView.VIEW_NAME + "/" + aptoId);
					
				} catch(ValidationException ex) {
					Notification.show("No se ha podido completar el registro");
				} catch (NumberFormatException nex) {
					Notification.show("El precio debe contener solo números y los decimales deben estar separados con un punto (.)");
				}
			}
		});
		registrarOfertaButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		infoLayout.addComponents(fechaInicioField, fechaFinField, precioStdField);
		infoLayout.setComponentAlignment(fechaInicioField, Alignment.TOP_CENTER);
		infoLayout.setComponentAlignment(fechaFinField, Alignment.TOP_CENTER);
		infoLayout.setComponentAlignment(precioStdField, Alignment.TOP_CENTER);
		
		formLayout.addComponents(infoLayout);
		
		nuevaOfertaLayout.addComponents(title, formLayout, registrarOfertaButton);
		nuevaOfertaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		nuevaOfertaLayout.setComponentAlignment(formLayout, Alignment.TOP_CENTER);
		nuevaOfertaLayout.setComponentAlignment(registrarOfertaButton, Alignment.TOP_CENTER);
		
		addComponent(nuevaOfertaLayout);
	}
		
}