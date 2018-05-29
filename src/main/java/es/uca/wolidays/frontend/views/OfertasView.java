package es.uca.wolidays.frontend.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Oferta;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.frontend.MainScreen;


@Theme("wolidays")
@SpringView(name = OfertasView.VIEW_NAME)
public class OfertasView extends VerticalLayout implements View {
	
	private static final String LARGE_TEXT = "large_text";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "ofertas";
	
	@Autowired
	MainScreen mainScreen;
	
	@Autowired
	private transient ApartamentoService aptoService;
	
	private VerticalLayout ofertasLayout;
	private Label title;
	private Label help;
	
	private HorizontalLayout buttonsLayout;
	private HorizontalLayout resultadosLayout;
	private VerticalLayout ofertasLeft;
	private VerticalLayout ofertasRight;
	
	private Button volverButton;
	private Button nuevaOfertaButton;
	
	private List<Oferta> ofertas;
	
	private int idAptoOferta = 0;

	@PostConstruct
	void init() {
		
		ofertasLayout = new VerticalLayout();
		ofertasLayout.setWidth("100%");
		ofertasLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		help = new Label();
		help.setCaptionAsHtml(true);
		
		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setWidth("100%");
		buttonsLayout.setMargin(false);
		
		ofertasLeft = new VerticalLayout();
		ofertasRight = new VerticalLayout();
		
		nuevaOfertaButton = new Button("Nueva oferta");
		nuevaOfertaButton.setIcon(VaadinIcons.PLUS);
		volverButton = new Button("Volver al apartamento");
		volverButton.setIcon(VaadinIcons.ARROW_BACKWARD);
		
		resultadosLayout = new HorizontalLayout();
		resultadosLayout.setWidth("100%");
		
		setOfertasInfoColumns();
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
		
		idAptoOferta = Integer.parseInt(event.getParameters().split("/")[0]);
		
		Optional<Apartamento> existeApartamento = aptoService.buscarPorIdConOfertas(idAptoOferta);
		if(existeApartamento.isPresent()) {
			ofertas = existeApartamento.get().getOfertas();
			title.setCaption("<h1>Ofertas del apartamento de <i>" + existeApartamento.get().getUbicacion() + "</i></h1>");
			help.setCaption("<h3><i>Sólo se muestran las ofertas vigentes o futuras.</i></h3>");
			
			nuevaOfertaButton.addClickListener(e -> {
				getUI().getNavigator().navigateTo(NuevaOfertaView.VIEW_NAME + "/" + idAptoOferta);
			});
			volverButton.addClickListener(e -> {
				getUI().getNavigator().navigateTo(DetalleApartamentoView.VIEW_NAME + "/" + idAptoOferta);
			});
			
		}
		
		if(ofertas.isEmpty()) {			
			Notification.show("No existen ofertas para el apartamento seleccionado", Notification.Type.ERROR_MESSAGE);
		} else {			
			setOfertas(ofertas);			
		}
		
		buttonsLayout.addComponents(volverButton, nuevaOfertaButton);
		buttonsLayout.setComponentAlignment(volverButton, Alignment.TOP_LEFT);
		buttonsLayout.setComponentAlignment(nuevaOfertaButton, Alignment.TOP_RIGHT);
		
		ofertasLayout.addComponents(title, help, buttonsLayout, resultadosLayout);
		ofertasLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		ofertasLayout.setComponentAlignment(help, Alignment.TOP_CENTER);
		ofertasLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);
		ofertasLayout.setComponentAlignment(resultadosLayout, Alignment.TOP_CENTER);
		addComponent(ofertasLayout);
	}
	
	private void setOfertasInfoColumns() {
		ofertasLeft = new VerticalLayout();
		ofertasLeft.setMargin(true);
		ofertasLeft.setSpacing(true);
		ofertasRight = new VerticalLayout();
		ofertasRight.setMargin(true);
		ofertasRight.setSpacing(true);
	}
	
	private void setOfertas(List<Oferta> oftas) {
			
			if(oftas.isEmpty()) {
				Notification.show("No existen ofertas para el apartamento escogido","", Notification.Type.ERROR_MESSAGE);
			} else {
				int i = 0;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
				for(Oferta ofta : oftas) {
					if(ofta.getFechaFin().isAfter(LocalDate.now()) || ofta.getFechaFin().isEqual(LocalDate.now())) {
						VerticalLayout oftaInfo = new VerticalLayout();
						oftaInfo.setSpacing(false);
						
						Button fechaInicio = new Button("Fecha inicio: " + ofta.getFechaInicio().format(formatter));
						fechaInicio.addStyleNames(ValoTheme.BUTTON_BORDERLESS, LARGE_TEXT);
						
						Button fechaFin = new Button("Fecha fin: " + ofta.getFechaFin().format(formatter));
						fechaFin.addStyleNames(ValoTheme.BUTTON_BORDERLESS, LARGE_TEXT);
						
						Button precio = new Button("Precio: " + String.valueOf(ofta.getPrecioOferta()));
						precio.setStyleName(ValoTheme.BUTTON_BORDERLESS);
						
						oftaInfo.addComponents(fechaInicio, fechaFin, precio);			
						
						if(i % 2 == 0) {
							ofertasLeft.addComponent(oftaInfo);
						} else {
							ofertasRight.addComponent(oftaInfo);
						}
						
						i++;
					}
				}
				
				/* 
				 * Si la variable i vale 0 después del bucle anterior, significa que el apartamento
				 * tiene ofertas, pero están todas expiradas.
				 */
				if(i == 0) {
					Notification.show("Todas las ofertas para este apartamento están expiradas", "", Notification.Type.WARNING_MESSAGE);
				}
				
				resultadosLayout.addComponents(ofertasLeft, ofertasRight);
			}
	}
	
	public static void setSuccessfulRegistroOfertaNotification() {
		Notification successfulRegistroOferta = new Notification("Oferta registrada con éxito");
		successfulRegistroOferta.setIcon(VaadinIcons.CHECK);
		successfulRegistroOferta.setPosition(Position.TOP_RIGHT);
		successfulRegistroOferta.setDelayMsec(2500);
		successfulRegistroOferta.setStyleName("success_notification");
		
		successfulRegistroOferta.show(Page.getCurrent());
	}
}