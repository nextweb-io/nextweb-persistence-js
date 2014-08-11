package io.nextweb.persistence.js;

import io.nextweb.persistence.PersistenceProvider;
import io.nextweb.persistence.js.internal.JsMapConnection;
import io.nextweb.persistence.js.internal.JsPersistenceProvider;
import io.nextweb.persistence.js.internal.RpcSerializer;

import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.map.MapConnection;

public class NextwebPersistenceJs {

	public static PersistenceProvider wrapPersistenceProvider(
			JsSerializer serializer, JavaScriptObject js) {

		return new JsPersistenceProvider(js, serializer);

	}

	public static MapConnection wrapMapConnection(JsSerializer serializer,
			JavaScriptObject mapConnection) {
		return new JsMapConnection(mapConnection, serializer);
	}

	public static JsSerializer createRpcSerializer() {
		return new RpcSerializer();
	}

}
