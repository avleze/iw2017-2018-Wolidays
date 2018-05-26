package es.uca.wolidays.frontend.views.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = BeneficioView.VIEW_NAME)
public class BeneficioView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "beneficio";
	
	private VerticalLayout beneficioLayout;
	
	private Label title;
	private Label instrucciones;
	
	private List<String> beneficioFile;
	private Float beneficio;
	
	private TextField beneficioField;
	private Button actualizarBeneficio;
	
	private Button volverReglas;
	
	private String benRgx = "\\d{1,2}\\.\\d{1,2}|\\d{1,2}";
	private Boolean benValido = false;
	
	@PostConstruct
	void init() {
		beneficioLayout = new VerticalLayout();
		beneficioLayout.setWidth("100%");
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Beneficio</h1>");
		
		instrucciones = new Label();
		instrucciones.setCaptionAsHtml(true);
		instrucciones.setCaption("<h3>Este es el porcentaje que se queda la empresa de cada reserva llevada a cabo<br><br>Debe ser un porcentaje entre 0.0 y 99.99</h3>");
		
		beneficioField = new TextField();
		beneficioField.setWidth("70px");
		
		actualizarBeneficio = new Button("Actualizar");
		actualizarBeneficio.setIcon(VaadinIcons.PENCIL);
		actualizarBeneficio.addClickListener(e -> {
			
			Path path = Paths.get("src/main/resources/beneficio");
			String nuevoBen = beneficioField.getValue();
			
			if(nuevoBen.equals("")) {
				nuevoBen = "0.0";
			} else {
				
				Pattern patt = Pattern.compile(benRgx);
				Matcher mat = patt.matcher(nuevoBen);
				if(mat.matches()) {
					benValido = true;
				}
				
				if(benValido) {
					try {
						Files.write(path, nuevoBen.getBytes());
						setSuccessfulBenNotification();
						getUI().getNavigator().navigateTo("beneficio");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					
				} else {
					Notification.show("El beneficio debe ser un porcentaje entre 0.0 y 99.99");
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
		
		Path path = Paths.get("src/main/resources/beneficio");
		
		try {
			
			if(Files.exists(path)) {
				beneficioFile = Files.readAllLines(path);
			} else {
				Files.createFile(path);
				Files.write(path, "5.0".getBytes());
				beneficioFile = Files.readAllLines(path);
			}
			
		} catch(IOException ex) {
			System.out.println("Error con fichero de beneficio.");
		}
		
		beneficio = Float.parseFloat(beneficioFile.get(0));
		beneficioField.setValue(String.valueOf(beneficio));
		
		beneficioLayout.addComponents(title, instrucciones, beneficioField, actualizarBeneficio, volverReglas);
		beneficioLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		beneficioLayout.setComponentAlignment(instrucciones, Alignment.TOP_CENTER);
		beneficioLayout.setComponentAlignment(beneficioField, Alignment.TOP_CENTER);
		beneficioLayout.setComponentAlignment(actualizarBeneficio, Alignment.TOP_CENTER);
		beneficioLayout.setComponentAlignment(volverReglas, Alignment.TOP_LEFT);
		
		addComponent(beneficioLayout);
	}
	
	public void setSuccessfulBenNotification() {
		Notification successfulMod = new Notification("Beneficio modificado con éxito");
		successfulMod.setIcon(VaadinIcons.CHECK);
		successfulMod.setPosition(Position.TOP_RIGHT);
		successfulMod.setDelayMsec(1500);
		successfulMod.setStyleName("success_notification");
		
		successfulMod.show(Page.getCurrent());
	}
	
}