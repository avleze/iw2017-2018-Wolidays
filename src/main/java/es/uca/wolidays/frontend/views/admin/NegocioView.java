package es.uca.wolidays.frontend.views.admin;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = NegocioView.VIEW_NAME)
public class NegocioView extends VerticalLayout implements View {
	
	private static final long serialVersionUID = -3089381541889114455L;
	public static final String VIEW_NAME = "negocio";
	
	private VerticalLayout negocioLayout;
	
	private Label title;
	
	private Button ivaButton;
	private Button beneficioButton;
	private Button penalizacionesButton;

	@PostConstruct
	void init() {
		negocioLayout = new VerticalLayout();
		negocioLayout.setWidth("100%");
		negocioLayout.setMargin(false);
		
		title = new Label();
		title.setCaptionAsHtml(true);
		title.setCaption("<h1>Reglas de negocio</h1>");
		
		ivaButton = new Button("I.V.A.");
		ivaButton.setWidth("200px");
		ivaButton.addClickListener(e -> {
			getUI().getNavigator().navigateTo(IvaView.VIEW_NAME);
		});
		
		beneficioButton = new Button("Beneficio por reserva");
		beneficioButton.setWidth("200px");
		beneficioButton.addClickListener(e -> {
			getUI().getNavigator().navigateTo(BeneficioView.VIEW_NAME);
		});
		
		penalizacionesButton = new Button("Penalizaciones");
		penalizacionesButton.setWidth("200px");
		penalizacionesButton.addClickListener(e -> {
			getUI().getNavigator().navigateTo(PenalizacionesView.VIEW_NAME);
		});
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		negocioLayout.addComponents(title, ivaButton, beneficioButton, penalizacionesButton);
		negocioLayout.setComponentAlignment(title, Alignment.TOP_CENTER);
		negocioLayout.setComponentAlignment(ivaButton, Alignment.TOP_CENTER);
		negocioLayout.setComponentAlignment(beneficioButton, Alignment.TOP_CENTER);
		negocioLayout.setComponentAlignment(penalizacionesButton, Alignment.TOP_CENTER);
		
		addComponent(negocioLayout);
	}
	
}