package io.nextweb.persistence.js;

import delight.keyvalue.Store;

public interface PersistenceProvider {

	public Store<String, Object> createMap(String id);

	public void removeMap(String id);

}
