package io.nextweb.persistence.js;

import io.nextweb.persistence.PersistenceProvider;
import io.nextweb.persistence.js.internal.JsPersistenceProvider;

import com.google.gwt.core.client.JavaScriptObject;

public class NextwebPersistenceJs {

	public static PersistenceProvider wrapPersistenceProvider(
			JavaScriptObject js) {

		return new JsPersistenceProvider(js);

	}

}
