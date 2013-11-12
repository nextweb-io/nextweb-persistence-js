package io.nextweb.persistence.js;

public interface JsSerializer {

	public String serialize(Object obj);

	public Object deserialize(String data);

}
