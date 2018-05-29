package es.uca.wolidays.frontend.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Image;

public final class ImageUtils {
	public static Image convertToImage(final byte[] imageData) {
		StreamSource streamSource = new StreamResource.StreamSource() {

			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				return (imageData == null) ? null : new ByteArrayInputStream(imageData);
			}
		};

		return new Image(null, new StreamResource(streamSource, "streamedSourceFromByteArray"));
	}
}
