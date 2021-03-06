package io.nextweb.persistence.js.internal;

import delight.keyvalue.Store;

import com.google.gwt.core.client.JavaScriptObject;

import io.nextweb.persistence.js.JsSerializer;
import io.nextweb.persistence.js.NextwebPersistenceJs;
import io.nextweb.persistence.js.PersistenceProvider;

public class JsPersistenceProvider implements PersistenceProvider {

	private final JavaScriptObject source;
	private final JsSerializer serializer;

	@Override
	public Store<String, Object> createMap(String id) {

		return NextwebPersistenceJs.createJsStore(serializer,
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
