package io.nextweb.persistence.js.internal;

import io.nextweb.persistence.js.JsSerializer;
import io.nextweb.persistence.js.NextwebPersistenceJs;
import io.nextweb.persistence.js.PersistenceProvider;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.map.AsyncMap;

public class JsPersistenceProvider implements PersistenceProvider {

	private final JavaScriptObject source;
	private final JsSerializer serializer;

	@Override
	public AsyncMap<String, Object> createMap(String id) {

		return NextwebPersistenceJs.wrapMapConnection(serializer,
				createMapJs(id));
	}

	private final native JavaScriptObject createMapJs(String id)/*-{
																	source.createMap(id);
																	}-*/;

	@Override
	public void removeMap(String id) {
		removeMapJs(id);
	}

	private final native JavaScriptObject removeMapJs(String id)/*-{
																source.removeMap(id);
																}-*/;

	public JsPersistenceProvider(JavaScriptObject source,
			JsSerializer serializer) {
		super();
		this.source = source;
		this.serializer = serializer;
	}

}
