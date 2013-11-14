package io.nextweb.persistence.js.internal;

import org.timepedia.exporter.client.ExporterUtil;

import io.nextweb.fn.js.JsClosure;
import io.nextweb.persistence.connections.MapConnection;
import io.nextweb.persistence.connections.callbacks.CloseCallback;
import io.nextweb.persistence.connections.callbacks.CommitCallback;
import io.nextweb.persistence.connections.callbacks.DeleteCallback;
import io.nextweb.persistence.connections.callbacks.GetCallback;
import io.nextweb.persistence.connections.callbacks.PutCallback;
import io.nextweb.persistence.js.JsSerializer;

import com.google.gwt.core.client.JavaScriptObject;

public class JsMapConnection implements MapConnection {

	private final JavaScriptObject source;
	private final JsSerializer serializer;

	public JsMapConnection(JavaScriptObject source, JsSerializer serializer) {
		super();
		this.source = source;
		this.serializer = serializer;
	}

	@Override
	public void put(final String key, final Object value,
			final PutCallback callback) {
		putJs(key, serializer.serialize(value), /* onSuccess */
				ExporterUtil.wrap(new JsClosure() {

					@Override
					public void apply(Object result) {
						callback.onSuccess();
					}
				}), /* onFailure */ExporterUtil.wrap(new JsClosure() {

					@Override
					public void apply(Object result) {
						callback.onFailure(new Exception(result.toString()));
					}
				}));
	}

	private final native void putJs(String key, String value,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																	source.put(key, value, onSuccess, onFailure);
																	}-*/;

	@Override
	public final Object get(final String key, final GetCallback callback) {
		return getJs(key, /* onSuccess */
				ExporterUtil.wrap(new JsClosure() {

					@Override
					public void apply(Object result) {
						callback.onSuccess(result);
					}
				}), /* onFailure */ExporterUtil.wrap(new JsClosure() {

					@Override
					public void apply(Object result) {
						callback.onFailure(new Exception(result.toString()));
					}
				}));
	}

	private final native String getJs(String key, JavaScriptObject onSuccess,
			JavaScriptObject onFailure)/*-{ 
				return source.get(key, onSuccess, onFailure);
			}-*/;

	@Override
	public void delete(String key, DeleteCallback callback) {
		
	}

	private final native void delete(String key, JavaScript)
	
	@Override
	public void close(CloseCallback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit(CommitCallback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub

	}

}
