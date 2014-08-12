package io.nextweb.persistence.js;

import io.nextweb.persistence.PersistenceProvider;
import io.nextweb.persistence.js.internal.JsMapConnection;
import io.nextweb.persistence.js.internal.JsPersistenceProvider;
import io.nextweb.persistence.js.internal.RpcSerializer;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.map.AsyncMap;

public class NextwebPersistenceJs {

	public static PersistenceProvider wrapPersistenceProvider(
			JsSerializer serializer, JavaScriptObject js) {

		return new JsPersistenceProvider(js, serializer);

	}

	public static AsyncMap<String, Object> wrapMapConnection(JsSerializer serializer,
			JavaScriptObject mapConnection) {
		return new JsMapConnection(mapConnection, serializer);
	}

	public static JsSerializer createRpcSerializer() {
		return new RpcSerializer();
	}

}
