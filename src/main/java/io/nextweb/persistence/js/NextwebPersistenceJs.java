package io.nextweb.persistence.js;

import io.nextweb.persistence.PersistenceProvider;
import io.nextweb.persistence.connections.MapConnection;
import io.nextweb.persistence.js.internal.JsMapConnection;
import io.nextweb.persistence.js.internal.JsPersistenceProvider;
import io.nextweb.persistence.js.internal.RpcSerializer;

import com.google.gwt.core.client.JavaScriptObject;

public class NextwebPersistenceJs {

	public static PersistenceProvider wrapPersistenceProvider(
			JavaScriptObject js) {

		return new JsPersistenceProvider(js);

	}

	public static MapConnection wrapMapConnection(JsSerializer serializer,
			JavaScriptObject mapConnection) {
		return new JsMapConnection(mapConnection, serializer);
	}

	public static JsSerializer createRpcSerializer() {
		return new RpcSerializer();
	}
	
}
