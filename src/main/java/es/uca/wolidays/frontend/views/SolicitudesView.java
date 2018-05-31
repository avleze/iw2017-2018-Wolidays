package es.uca.wolidays.frontend.views;

import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Ubicacion;
import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;
import es.uca.wolidays.frontend.views.DetalleReservaView;

@Theme("wolidays")
@SpringView(name = SolicitudesView.VIEW_NAME)
public class SolicitudesView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "solicitudes";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	private VerticalLayout solicitudesLayout;
	private Grid<Reserva> reservasTabla;
		
	private Label title;
	
	private Set<Reserva> reservas;
	private int idApto;
	private Apartamento apartamento;
	
	private Button volverApto;
	
	@PostConstruct
	void init() {
		
		solicitudesLayout = new VerticalLayout();
		solicitudesLayout.setWidth("100%");
		solicitudesLayout.setMargin(false);
		
		reservasTabla = new Grid<>();
		reservasTabla.setWidth("100%");
		reservasTabla.setWidth("900px");
				
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Solicitudes de reservas</h1>");
		
		volverApto = new Button("Volver al apartamento");
		volverApto.setIcon(VaadinIcons.ARROW_BACKWARD);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		mainScreen.setButtons();
		
		idApto = Integer.parseInt(event.getParameters().split("/")[0]);
		volverApto.addClickListener(e -> {
			getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + idApto);
		});
		
		Optional<Apartamento> existenAptosReservas = aptoService.buscarPorIdConReservas(idApto);
		
		if(existenAptosReservas.isPresent()) {
			apartamento = existenAptosReservas.get();
		}
		reservas = apartamento.getReservas();
		
		Ubicacion aptoUbi = apartamento.getUbicacion();
		title.setCaption("<h1>Apartamento de <i>" + aptoUbi.getDireccion() + " (" + aptoUbi.getCiudad() + ")</i></h1>");
		
		reservasTabla.setItems(reservas);
		
		reservasTabla.addColumn(Reserva::getId).setCaption("ID");
		reservasTabla.addColumn(Reserva::getUsuario).setCaption("Usuario solicitante");
		reservasTabla.addColumn(Reserva::getContacto).setCaption("Contacto");
		reservasTabla.addColumn(Reserva::getPrecioFinal).setCaption("Precio");
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
		
		solicitudesLayout.addComponents(title, reservasTabla, volverApto);
		solicitudesLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		solicitudesLayout.setComponentAlignment(reservasTabla, Alignment.TOP_CENTER);
		solicitudesLayout.setComponentAlignment(volverApto, Alignment.TOP_LEFT);
		addComponent(solicitudesLayout);
	}
	
}