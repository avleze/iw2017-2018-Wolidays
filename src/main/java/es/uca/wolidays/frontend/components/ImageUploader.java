package es.uca.wolidays.frontend.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.FileDropTarget;

import es.uca.wolidays.backend.entities.Imagen;

public class ImageUploader extends CustomComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<Imagen> images = new HashSet<>();

	
	public ImageUploader(String message) {        
		final Label infoLabel = new Label(message);

		final VerticalLayout dropPane = new VerticalLayout(infoLabel);
		dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
		final GridLayout glDropPane = new GridLayout(3, 3);
		Responsive.makeResponsive(glDropPane);
		dropPane.addComponent(glDropPane);
		dropPane.addStyleName("drop-area");
		dropPane.setWidth(100, Unit.PERCENTAGE);
		
		ProgressBar progress = new ProgressBar();
		progress.setIndeterminate(true);
		progress.setVisible(false);
		dropPane.addComponent(progress);
		
		new FileDropTarget<>(dropPane, fileDropEvent -> {
			final int fileSizeLimit = 2 * 1024 * 1024; // 2MB

			fileDropEvent.getFiles().forEach(html5File -> {
				final String fileName = html5File.getFileName();

				if (html5File.getFileSize() > fileSizeLimit) {
					Notification.show("Fichero rechazado, máximo 2MB",
							Notification.Type.WARNING_MESSAGE);
				} else {
					final ByteArrayOutputStream bas = new ByteArrayOutputStream();
					final StreamVariable streamVariable = new StreamVariable() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public OutputStream getOutputStream() {
							return bas;
						}


						@Override
						public void streamingFinished(final StreamingEndEvent event) {
							progress.setVisible(false);
							final StreamSource streamSource = () -> {
								ByteArrayInputStream ret = null;
								if (bas != null) {
									final byte[] byteArray = bas.toByteArray();
									ret = new ByteArrayInputStream(byteArray);
								}
								return ret;
							};
							
							
							final StreamResource resource = new StreamResource(streamSource, fileName);
							Imagen image = new Imagen();
							image.setImagen(bas.toByteArray());
							images.add(image);

							
							/* Showing the file */
							
							final Embedded embedded = new Embedded(fileName, resource);

							embedded.setWidth(150, Unit.PIXELS);

							VerticalLayout imgContainer = new VerticalLayout();
							imgContainer.addComponent(embedded);
							Button remover = new Button("X");
							remover.setStyleName("img-uploader-button");
							imgContainer.addComponent(remover);
							remover.addClickListener(listener -> 
							{
								glDropPane.removeComponent(imgContainer);
								images.remove(image);
							});
							glDropPane.addComponent(imgContainer);
						}

						@Override
						public void streamingFailed(final StreamingErrorEvent event) {
							progress.setVisible(false);
						}


						@Override
						public boolean listenProgress() {
							return false;
						}


						@Override
						public void onProgress(StreamingProgressEvent event) {
							
						}


						@Override
						public void streamingStarted(StreamingStartEvent event) {
							
						}


						@Override
						public boolean isInterrupted() {
							return false;
						}
					};
					html5File.setStreamVariable(streamVariable);
					progress.setVisible(true);
				}
			});
		});
		
		

        setCompositionRoot(dropPane);
        
        setSizeUndefined();
    }


	public Set<Imagen> getImages() {
		return images;
	}


	public void setImages(Set<Imagen> images) {
		this.images = images;
	}

}