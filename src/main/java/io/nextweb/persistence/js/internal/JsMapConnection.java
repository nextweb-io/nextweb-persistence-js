package io.nextweb.persistence.js.internal;

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
		putJs(source, key, serializer.serialize(value), /* onSuccess */
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

	private final native void putJs(JavaScriptObject source, String key,
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

	private final native String getJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	return source.get(key, onSuccess, onFailure);
																	}-*/;

	@Override
	public final void delete(final String key, final DeleteCallback callback) {
		deleteJs(source, key, /* onSuccess */
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

	private final native void deleteJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.delete(key, onSuccess, onFailure);
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

	private final native void closeJs(JavaScriptObject source,
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

	private final native void commitJs(JavaScriptObject source,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																	source.commit(onSuccess, onFailure);
																	}-*/;

	@Override
	public void clearCache() {
		clearCacheJs(source);
	}

	private final native void clearCacheJs(JavaScriptObject source)/*-{
																	source.clearCache();
																	}-*/;

}
