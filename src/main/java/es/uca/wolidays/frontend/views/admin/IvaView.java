package es.uca.wolidays.frontend.views.admin;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.IVA;
import es.uca.wolidays.backend.services.ReglasDeNegocioService;

@SpringView(name = IvaView.VIEW_NAME)
public class IvaView extends VerticalLayout implements View {
	
	private static final String CAMPO_OBLIGATORIO = "Campo obligatorio";
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "iva";
	
	@Autowired
	ReglasDeNegocioService ivaService;
	
	private VerticalLayout ivaLayout;	
	private HorizontalLayout ivaInfoLayout;
	private VerticalLayout nuevoIvaLayout;
	
	private Label title;
	private Label instrucciones;
	
	private Grid<IVA> ivaTabla;
	private ArrayList<IVA> ivas;
	
	private TextField paisField;
	private TextField ivaField;
	
	private Button volverReglas;
	private Button nuevoIva;
	
	private Binder<IVA> binder = new Binder<>();
	private String paisRgx = "[\\w\\s,.()áéíóúÁÉÍÓÚñÑ\\/]+";
	private String ivaRgx = "\\d{1,2}\\.\\d{1,2}|\\d{1,2}";
	private Boolean ivaVacio = true;
	private Boolean ivaValido = false;
	
	@PostConstruct
	void init() {
		ivaLayout = new VerticalLayout();
		ivaLayout.setWidth("100%");
		
		ivaInfoLayout = new HorizontalLayout();
		ivaInfoLayout.setWidth("100%");
		
		nuevoIvaLayout = new VerticalLayout();
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>I.V.A.</h1>");
		
		instrucciones = new Label();
		instrucciones.setCaptionAsHtml(true);
		instrucciones.setCaption("<h2>Introduce un nuevo país o selecciona uno de la tabla y modifícalo</h2>");
		
		ivaTabla = new Grid<>();
		ivaTabla.setWidth("700px");
		ivaTabla.addSelectionListener(e -> {
			Optional<IVA> ivaSelec = e.getFirstSelectedItem();
			if(ivaSelec.isPresent()) {
				IVA iva = ivaSelec.get();
				paisField.setValue(iva.getPais());
				ivaField.setValue(String.valueOf(iva.getPorcentajeIVA()));
			}
		});
		
		ivas = new ArrayList<>();
		
		paisField = new TextField("Pais");
		binder.forField(paisField)
			.withValidator(new RegexpValidator("El país debe ser válido", paisRgx, true))
			.asRequired(CAMPO_OBLIGATORIO)
			.bind(IVA::getPais, IVA::setPais);		
		
		ivaField = new TextField("I.V.A. (0-99.99)");
		binder.forField(ivaField)
			.asRequired(CAMPO_OBLIGATORIO);
		
		nuevoIva = new Button("Añadir/Modificar");
		nuevoIva.setIcon(VaadinIcons.PLUS);
		nuevoIva.setWidth("-1");
		nuevoIva.addClickListener(e -> {
			IVA iva;
			
			if (!ivaField.isEmpty()) {
				ivaVacio = false;
				
				String nuevoIVA = ivaField.getValue();
				Pattern patt = Pattern.compile(ivaRgx);
				Matcher mat = patt.matcher(nuevoIVA);
				if(mat.matches()) {
					ivaValido = true;
				}
			}
			
			if(ivaVacio) {
				Notification.show("Debes establecer el I.V.A.");
			} else if(!ivaValido) {
				Notification.show("El I.V.A. debe ser un número real entre 0 y 99.99 (separando decimales con punto).");
			} else {
				
				try {
					
					iva = ivaService.buscarIVAPorPais(paisField.getValue());
					if(iva == null) {
						iva = new IVA();
					}
					
					iva.setPorcentajeIVA(Float.parseFloat(ivaField.getValue()));
					binder.writeBean(iva);
					ivaService.guardar(iva);
					
					setSuccessfulIVANotification();
					getUI().getNavigator().navigateTo("iva");
					
				} catch (ValidationException ve) {
					Notification.show("No se ha podido completar el registro");
				} catch (NumberFormatException nex) {
					Notification.show("El I.V.A. debe ser un número real entre 0 y 99.99 (separando decimales con punto).");
				}
			}
		});
		
		volverReglas = new Button("Volver a reglas de negocio");
		volverReglas.setIcon(VaadinIcons.ARROW_BACKWARD);
		volverReglas.setWidth("-1");
		volverReglas.addClickListener(e -> getUI().getNavigator().navigateTo(NegocioView.VIEW_NAME));
		
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		ivaService.ivas().forEach(ivas::add);
		
		ivaTabla.setItems(ivas);
		
		ivaTabla.addColumn(IVA::getPais).setCaption("País");
		ivaTabla.addColumn(IVA::getPorcentajeIVA).setCaption("I.V.A. (%)");
		
		nuevoIvaLayout.addComponents(paisField, ivaField, nuevoIva);
		nuevoIvaLayout.setComponentAlignment(paisField, Alignment.TOP_CENTER);
		nuevoIvaLayout.setComponentAlignment(ivaField, Alignment.TOP_CENTER);
		nuevoIvaLayout.setComponentAlignment(nuevoIva, Alignment.TOP_CENTER);
		
		ivaInfoLayout.addComponents(ivaTabla, nuevoIvaLayout);
		
		ivaLayout.addComponents(title, instrucciones, ivaInfoLayout, volverReglas);
		ivaLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		ivaLayout.setComponentAlignment(instrucciones, Alignment.TOP_CENTER);
		ivaLayout.setComponentAlignment(ivaInfoLayout, Alignment.TOP_CENTER);
		ivaLayout.setComponentAlignment(volverReglas, Alignment.TOP_LEFT);
		
		addComponent(ivaLayout);
	}
	
	public void setSuccessfulIVANotification() {
		Notification successfulMod = new Notification("I.V.A. añadido/modificado con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(1500);
		successfulMod.setStyleName("success_notification");
		
		successfulMod.show(Page.getCurrent());
	}
	
}
