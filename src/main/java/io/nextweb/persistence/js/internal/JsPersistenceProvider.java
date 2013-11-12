package io.nextweb.persistence.js.internal;

import org.timepedia.exporter.client.ExporterUtil;

import io.nextweb.fn.Result;
import io.nextweb.fn.js.JsClosure;
import io.nextweb.persistence.PersistenceProvider;
import io.nextweb.persistence.connections.MapConnection;

import com.google.gwt.core.client.JavaScriptObject;

public class JsPersistenceProvider implements PersistenceProvider {

	private final JavaScriptObject source;

	@Override
	public final Result<MapConnection> createMap(String id) {

		createMapJs(id, /* onSuccess */ExporterUtil.wrap(new JsClosure() {

			@Override
			public void apply(Object result) {

			}
		}), /* onFailure */ExporterUtil.wrap(new JsClosure() {

			@Override
			public void apply(Object result) {

			}
		}));

		return null;
	}

	private final native void createMapJs(String id,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																	source.createMap(id, onSuccess, onFailure);
																	}-*/;

	public JsPersistenceProvider(JavaScriptObject source) {
		super();
		this.source = source;
	}

}
