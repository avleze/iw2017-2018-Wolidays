package es.uca.wolidays.backend.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

@Component
public class SampleViewAccessControl implements ViewAccessControl{

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		
		List<String> clienteViews = Arrays.asList(
				"DetalleApartamentoView","MisApartamentosView","MisReservasView","NuevaOfertaView","NuevaReservaView","NuevoApartamentoView","OfertasView"
		);
		
		List<String> adminViews = Arrays.asList(
		);
		
		List<String> gestorViews = Arrays.asList(
		);
		
		if (clienteViews.contains(beanName)) {
            return SecurityUtils.hasRole("CLIENTE_ROL");
        } else if (adminViews.contains(beanName)) {
            return SecurityUtils.hasRole("ADMIN_ROL");
        } else if (gestorViews.contains(beanName)) {
        	return SecurityUtils.hasRole("GESTOR_ROL");
        } else {
        	return true;
        }
	}

	
}
 