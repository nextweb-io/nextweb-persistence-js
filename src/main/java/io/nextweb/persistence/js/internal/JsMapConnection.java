package io.nextweb.persistence.js.internal;

import io.nextweb.fn.FnUtils;
import io.nextweb.fn.js.JsClosure;
import io.nextweb.persistence.connections.MapConnection;
import io.nextweb.persistence.connections.callbacks.CloseCallback;
import io.nextweb.persistence.connections.callbacks.CommitCallback;
import io.nextweb.persistence.connections.callbacks.DeleteCallback;
import io.nextweb.persistence.connections.callbacks.GetCallback;
import io.nextweb.persistence.connections.callbacks.PutCallback;
import io.nextweb.persistence.js.JsSerializer;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;

public class JsMapConnection implements MapConnection {

	private final JavaScriptObject source;
	private final JsSerializer serializer;

	public JsMapConnection(JavaScriptObject source, JsSerializer serializer) {
		super();
		if (source == null) {
			throw new IllegalArgumentException("source should not be null.");
		}
		this.source = source;
		this.serializer = serializer;
	}

	@Override
	public void put(final String key, final Object value,
			final PutCallback callback) {
		
		
		GWT.log("about to serialize: "+value);
		String serializedValue = serializer.serialize(value);
		
		GWT.log("serialized: "+serializedValue);
		
		JavaScriptObject onSuccess = ExporterUtil.wrap(new JsClosure() {

			@Override
			public void apply(final Object result) {
				callback.onSuccess();
			}
		});
		
		GWT.log("onsuccess created: "+onSuccess);
		JavaScriptObject onFailure = ExporterUtil.wrap(new JsClosure() {

			@Override
			public void apply(final Object result) {
				callback.onFailure(new Exception(result.toString()));
			}
		});
		
	
		
		GWT.log("onfailure created: "+onSuccess);
		
		GWT.log("open dummyjs");
		
		
		GWT.log("Pass params: "+source+" "+key+" "+serializedValue+" "+onSuccess.getClass()+" "+onFailure.getClass());
		
		putJs(source, key, serializedValue, 
				onSuccess, onFailure);
		
		GWT.log("put completed");
	}

	
	
	
	
	private native void putJs(JavaScriptObject source, String key,
			String value, JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																					source.put(key, value, onSuccess, onFailure);
																					}-*/;

	@Override
	public final Object get(final String key, final GetCallback callback) {
		return getJs(source, key, /* onSuccess */
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

	private native String getJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	return source.get(key, onSuccess, onFailure);
																	}-*/;

	@Override
	public final void remove(final String key, final DeleteCallback callback) {
		removeJs(source, key, /* onSuccess */
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

	private native void removeJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.remove(key, onSuccess, onFailure);
																	}-*/;

	@Override
	public final void close(final CloseCallback callback) {
		closeJs(source, /* onSuccess */
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

	private  native void closeJs(JavaScriptObject source,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.close(onSuccess, onFailure);
																	}-*/;

	@Override
	public final void commit(final CommitCallback callback) {
		commitJs(source, /* onSuccess */
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

	private  native void commitJs(JavaScriptObject source,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																	source.commit(onSuccess, onFailure);
																	}-*/;

	@Override
	public void clearCache() {
		clearCacheJs(source);
	}

	private native void clearCacheJs(JavaScriptObject source)/*-{
																	source.clearCache();
																	}-*/;

}
