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
	public MapConnection createMap(String id) {
		
		return null;
	}

	private final native JavaScriptObject createMapJs(String id)/*-{
																	source.createMap(id);
																	}-*/;

	public JsPersistenceProvider(JavaScriptObject source) {
		super();
		this.source = source;
	}

}
