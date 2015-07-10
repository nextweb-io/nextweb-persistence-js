package io.nextweb.persistence.js;

import de.mxro.async.map.Store;

public interface PersistenceProvider {

	public Store<String, Object> createMap(String id);

	public void removeMap(String id);

}
