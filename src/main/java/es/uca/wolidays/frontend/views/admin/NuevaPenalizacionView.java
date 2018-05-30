package es.uca.wolidays.frontend.views.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.wolidays.backend.entities.Penalizacion;
import es.uca.wolidays.backend.services.ReglasDeNegocioService;

@SpringView(name = NuevaPenalizacionView.VIEW_NAME)
public class NuevaPenalizacionView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "nueva_penalizacion";
	
	@Autowired
	ReglasDeNegocioService penService;
	
	private VerticalLayout nuevaPenLayout;
	
	private HorizontalLayout fieldsAboveLayout;
	private HorizontalLayout fieldsBelowLayout;
	
	private int penId;
	private Penalizacion penalizacion;
	
	private TextField cargoField;
	private NativeSelect<String> tipoUsuarioField;
	private List<String> tiposUsuario;
	private NativeSelect<String> motivoField;
	private List<String> tiposMotivo;
	
	private NativeSelect<Integer> minNochesField;
	private NativeSelect<Integer> maxNochesField;
	private NativeSelect<Integer> minDiasAntField;
	private NativeSelect<Integer> maxDiasAntField;
	
	private String cargoRgx = "\\d+\\.\\d{1,2}|\\d+";
	
	List<Integer> data = IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList());
	
	private Button registrarPen;
	private Button volverPen;
	
	@PostConstruct
	void init() {
		
		nuevaPenLayout = new VerticalLayout();
		nuevaPenLayout.setWidth("100%");
		
		fieldsAboveLayout = new HorizontalLayout();
		fieldsBelowLayout = new HorizontalLayout();
		
		penalizacion = null;
		
		cargoField = new TextField("Cargo");
		cargoField.setWidth("60px");
		
		tipoUsuarioField = new NativeSelect<>();
		tipoUsuarioField.setCaption("Tipo de usuario");
		tipoUsuarioField.setEmptySelectionAllowed(false);
		tiposUsuario = new ArrayList<>();
		tiposUsuario.add("Huésped");
		tiposUsuario.add("Anfitrión");
		tipoUsuarioField.setItems(tiposUsuario);
		tipoUsuarioField.setSelectedItem(tiposUsuario.get(0));		
		
		motivoField = new NativeSelect<>();
		motivoField.setCaption("Motivo");
		motivoField.setEmptySelectionAllowed(false);
		tiposMotivo = new ArrayList<>();
		tiposMotivo.add("Cancelación");
		tiposMotivo.add("Modificación");
		motivoField.setItems(tiposMotivo);
		motivoField.setSelectedItem(tiposMotivo.get(0));
		
		minNochesField = new NativeSelect<>("Mín. noches", data);
		minNochesField.setSelectedItem(data.get(0));
		minNochesField.setEmptySelectionAllowed(false);
		
		maxNochesField = new NativeSelect<>("Máx. noches", data);
		maxNochesField.setSelectedItem(data.get(0));
		maxNochesField.setEmptySelectionAllowed(false);
		
		minDiasAntField = new NativeSelect<>("Mín. días antelación", data);
		minDiasAntField.setSelectedItem(data.get(0));
		minDiasAntField.setEmptySelectionAllowed(false);
		
		maxDiasAntField = new NativeSelect<>("Máx. días antelación", data);
		maxDiasAntField.setSelectedItem(data.get(0));
		maxDiasAntField.setEmptySelectionAllowed(false);
		
		registrarPen = new Button("Registrar política");
		registrarPen.addClickListener(e -> {
			
			if(cargoField.isEmpty()) {
				
				Notification.show("Debes indicar un cargo (porcentaje)");
				
			} else {
				
				String nuevoIVA = cargoField.getValue();
				Pattern patt = Pattern.compile(cargoRgx);
				Matcher mat = patt.matcher(nuevoIVA);
				
				if(mat.matches()) {
					
					Integer minNoches = minNochesField.getValue();
					Integer maxNoches = maxNochesField.getValue();
					Integer minDiasAnt = minDiasAntField.getValue();
					Integer maxDiasAnt = maxDiasAntField.getValue();
					
					if(maxNoches < minNoches || maxDiasAnt < minDiasAnt) {
						
						Notification.show("Los campos de días antelación/noches máximas deben ser igual o mayores que los de mínimo");
						
					} else {
				
						if(penalizacion == null) {
							penalizacion = new Penalizacion();
						}
						
						penalizacion.setPorcentajeCargo(Float.parseFloat(cargoField.getValue()));
						
						String tipoUsr = tipoUsuarioField.getValue();
						if(tipoUsr.equals("Anfitrión")) {
							penalizacion.setTipoUsuario(Penalizacion.TipoUsuario.Anfitrion);
						} else {
							penalizacion.setTipoUsuario(Penalizacion.TipoUsuario.Huesped);
						}
						
						String motivo = motivoField.getValue();
						if(motivo.equals("Cancelación")) {
							penalizacion.setMotivo(Penalizacion.Motivo.Cancelacion);
						} else {
							penalizacion.setMotivo(Penalizacion.Motivo.Modificacion);
						}
						
						penalizacion.setMinNoches(minNoches);
						penalizacion.setMaxNoches(maxNoches);
						penalizacion.setMinDiasAntelacion(minDiasAnt);
						penalizacion.setMaxDiasAntelacion(maxDiasAnt);				
						
						penService.guardar(penalizacion);
						PenalizacionesView.setSuccessfulPenalizacionNotification();
						getUI().getNavigator().navigateTo(PenalizacionesView.VIEW_NAME);
						
					}
					
				} else {
					Notification.show("El cargo debe ser un porcentaje (decimales separados con punto)");
				}
			}				
		});
		
		volverPen = new Button("Volver a penalizaciones");
		volverPen.setIcon(VaadinIcons.ARROW_BACKWARD);
		volverPen.addClickListener(e -> getUI().getNavigator().navigateTo(PenalizacionesView.VIEW_NAME));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		if(!event.getParameters().isEmpty()) {
			penId = Integer.parseInt(event.getParameters().split("/")[0]);
			Optional<Penalizacion> existePen = penService.buscarPenalizacionPorId(penId);
			
			if(existePen.isPresent()) {
				penalizacion = existePen.get();
			}
			
			cargoField.setValue(String.valueOf(penalizacion.getPorcentajeCargo()));
			
			String tipoUsuario = String.valueOf(penalizacion.getTipoUsuario());
			if(tipoUsuario.equals("Huesped")) {
				tipoUsuarioField.setSelectedItem(tiposUsuario.get(0));
			} else {
				tipoUsuarioField.setSelectedItem(tiposUsuario.get(1));
			}
			
			String motivo = String.valueOf(penalizacion.getMotivo());
			if(motivo.equals("Cancelacion")) {
				motivoField.setSelectedItem(tiposMotivo.get(0));
			} else {
				motivoField.setSelectedItem(tiposMotivo.get(1));
			}
			
			minNochesField.setSelectedItem(penalizacion.getMinNoches());
			maxNochesField.setSelectedItem(penalizacion.getMaxNoches());
			minDiasAntField.setSelectedItem(penalizacion.getMinDiasAntelacion());
			maxDiasAntField.setSelectedItem(penalizacion.getMaxDiasAntelacion());
			
		}
		
		fieldsAboveLayout.addComponents(cargoField, tipoUsuarioField, motivoField);
		fieldsBelowLayout.addComponents(minNochesField, maxNochesField, minDiasAntField, maxDiasAntField);		
		
		nuevaPenLayout.addComponents(fieldsAboveLayout, fieldsBelowLayout, registrarPen, volverPen);
		nuevaPenLayout.setComponentAlignment(fieldsAboveLayout, Alignment.TOP_CENTER);
		nuevaPenLayout.setComponentAlignment(fieldsBelowLayout, Alignment.TOP_CENTER);
		nuevaPenLayout.setComponentAlignment(registrarPen, Alignment.TOP_CENTER);
		nuevaPenLayout.setComponentAlignment(volverPen, Alignment.TOP_LEFT);
		
		addComponent(nuevaPenLayout);
	}	
}
