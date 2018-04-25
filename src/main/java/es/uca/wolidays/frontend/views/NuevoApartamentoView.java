package es.uca.wolidays.frontend.views;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Apartamento;
import es.uca.wolidays.backend.entities.Usuario;
import es.uca.wolidays.backend.security.SecurityUtils;
import es.uca.wolidays.backend.services.ApartamentoService;
import es.uca.wolidays.backend.services.UsuarioService;
import es.uca.wolidays.frontend.MainScreen;

@Theme("navbar")
@SpringView(name = NuevoApartamentoView.VIEW_NAME)
public class NuevoApartamentoView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nuevo_apartamento";
	
	@Autowired
	UsuarioService userService;
	
	@Autowired
	ApartamentoService aptoService;
	
	@Autowired
	MainScreen mainScreen;

	@PostConstruct
	void init() {
		
		final VerticalLayout nuevoAptoLayout = new VerticalLayout();
		final HorizontalLayout fieldsLayout = new HorizontalLayout();
		final VerticalLayout leftFields = new VerticalLayout();
		final VerticalLayout rightFields = new VerticalLayout();
		Usuario currentUser = (Usuario)userService.loadUserByUsername(SecurityUtils.getUsername());
		
		TextField contactoField = new TextField("Contacto *");
		TextField ubicacionField = new TextField("Ubicación *");
		List<Integer> data = IntStream.range(1, 11).mapToObj(i -> i).collect(Collectors.toList());
        NativeSelect<Integer> numCamasField = new NativeSelect<>("Número de camas *", data);
        numCamasField.setEmptySelectionAllowed(false);
        numCamasField.setSelectedItem(data.get(0));
		numCamasField.setWidth("50px");
		NativeSelect<Integer> numDormitoriosField = new NativeSelect<>("Número de dormitorios *", data);
        numDormitoriosField.setEmptySelectionAllowed(false);
        numDormitoriosField.setSelectedItem(data.get(0));
		numDormitoriosField.setWidth("50px");
		CheckBox aireAcondCB = new CheckBox("Aire acondicionado", false);
		
		leftFields.addComponents(contactoField, ubicacionField, numCamasField, numDormitoriosField, aireAcondCB);
		leftFields.setComponentAlignment(contactoField, Alignment.TOP_LEFT);
		leftFields.setComponentAlignment(ubicacionField, Alignment.TOP_LEFT);
		leftFields.setComponentAlignment(numCamasField, Alignment.TOP_LEFT);
		leftFields.setComponentAlignment(numDormitoriosField, Alignment.TOP_LEFT);
		leftFields.setComponentAlignment(aireAcondCB, Alignment.TOP_LEFT);
		
		TextArea descripcionField = new TextArea("Descripción");
		descripcionField.setWidth("300px");
		TextField precioStdField = new TextField("Precio estándar por noche *");
		precioStdField.setWidth("85px");
		
		
		rightFields.addComponents(descripcionField, precioStdField);
		rightFields.setComponentAlignment(descripcionField, Alignment.TOP_LEFT);
		rightFields.setComponentAlignment(precioStdField, Alignment.TOP_LEFT);
		
		fieldsLayout.addComponents(leftFields,rightFields);
		fieldsLayout.setComponentAlignment(leftFields, Alignment.MIDDLE_CENTER);
		fieldsLayout.setComponentAlignment(rightFields, Alignment.MIDDLE_CENTER);
		fieldsLayout.setWidth("40%");
		
		Button registrarAptoButton = new Button("Registrar apartamento");
		registrarAptoButton.addClickListener(e -> {
			Apartamento apartamento = new Apartamento();
			
			apartamento.setPropietario(currentUser);
			apartamento.setContacto(contactoField.getValue());
			apartamento.setUbicacion(ubicacionField.getValue());
			apartamento.setNumCamas(numCamasField.getValue());
			apartamento.setNumDormitorios(numDormitoriosField.getValue());
			apartamento.setAireAcondicionado(aireAcondCB.getValue());
			apartamento.setDescripcion(descripcionField.getValue());
			apartamento.setPrecioEstandar(Double.parseDouble(precioStdField.getValue()));
			
			aptoService.guardar(apartamento);
		});
		registrarAptoButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		
		nuevoAptoLayout.addComponents(fieldsLayout, registrarAptoButton);
		nuevoAptoLayout.setComponentAlignment(fieldsLayout, Alignment.TOP_CENTER);
		nuevoAptoLayout.setComponentAlignment(registrarAptoButton, Alignment.TOP_CENTER);
		
		addComponent(nuevoAptoLayout);
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		mainScreen.setButtons();
	}
	
}
