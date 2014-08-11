package io.nextweb.persistence.js.internal;

import io.nextweb.persistence.js.JsSerializer;
import io.nextweb.promise.js.FnJs;
import io.nextweb.promise.js.callbacks.EmptyCallback;
import io.nextweb.promise.js.exceptions.ExceptionUtils;
import nx.serializer.NxSerializer;
import nx.serializer.utils.StringDestination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.FailureCallback;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.map.MapConnection;
import de.mxro.fn.Closure;

public class JsMapConnection implements MapConnection<Object> {

	private final static boolean ENABLE_LOG = false;

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

	private JavaScriptObject createFailureCallback(
			final FailureCallback callback) {
		return FnJs.exportCallback(new Closure<Object>() {

			@Override
			public void apply(Object o) {
				if (ENABLE_LOG) {
					GWT.log(this + "->Operation failed: " + o);
				}
				callback.onFailure(ExceptionUtils.convertJavaScriptException(o));
			}
		});
	}

	@Override
	public void put(final String key, final Object value,
			final SimpleCallback callback) {

		if (ENABLE_LOG) {
			GWT.log(this + ".put(" + key + ", " + value + ":"
					+ value.getClass() + ")");
		}

		StringDestination stringDestination = NxSerializer
				.createStringDestination();
		serializer.serialize(value, stringDestination);
		String serializedValue = stringDestination.getDestination().getValue();

		JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

			@Override
			public void call() {
				if (ENABLE_LOG) {
					GWT.log(this + ".put(" + key + ", " + value
							+ ")->onSuccess");
				}
				callback.onSuccess();
			}
		});

		JavaScriptObject onFailure = createFailureCallback(callback);

		putJs(source, key, serializedValue, onSuccess, onFailure);

	}

	private native void putJs(JavaScriptObject source, String key,
			String value, JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																					source.put(key, value, onSuccess, onFailure);
																					}-*/;

	@Override
	public final void get(final String key, final ValueCallback<Object> callback) {

		if (ENABLE_LOG) {
			GWT.log(this + ".get(" + key + ")");
		}

		JavaScriptObject onSuccess = FnJs.exportCallback(new Closure<Object>() {

			@Override
			public void apply(Object o) {
				if (ENABLE_LOG) {
					GWT.log(this + ".get(" + key + ")->onSuccess=" + o);
				}
				callback.onSuccess(serializer.deserialize(NxSerializer.createStringSource((String) o)));
			}

		});

		JavaScriptObject onFailure = createFailureCallback(callback);

		getJs(source, key, onSuccess, onFailure);
	}

	private native void getJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.get(key, onSuccess, onFailure);
																	}-*/;

	@Override
	public Object getSync(String key) {
		if (ENABLE_LOG) {
			GWT.log(this + ".getSync(" + key + ")");
		}
		String value = getSyncJs(source, key);
		if (value == null) {
			if (ENABLE_LOG) {
				GWT.log(this + ".getSync(" + key + ")->" + null);
			}
			return null;
		}

		if (ENABLE_LOG) {
			GWT.log(this + ".getSync(" + key + ")->deserializing" + value);
		}

		Object res = serializer.deserialize(NxSerializer.createStringSource(value));
		if (ENABLE_LOG) {
			GWT.log(this + ".getSync(" + key + ")->" + res);
		}

		return res;
	}

	private native String getSyncJs(JavaScriptObject source, String key)/*-{ 
																		var value = source.getSync(key);
																		
																		return value;
																		}-*/;

	@Override
	public final void remove(final String key, final SimpleCallback callback) {
		if (ENABLE_LOG) {
			GWT.log(this + ".remove(" + key + ")");
		}
		final JavaScriptObject onSuccess = FnJs
				.exportCallback(new EmptyCallback() {

					@Override
					public void call() {
						if (ENABLE_LOG) {
							GWT.log(this + ".remove(" + key + ")->onSuccess");
						}
						callback.onSuccess();
					}
				});

		final JavaScriptObject onFailure = createFailureCallback(callback);

		removeJs(source, key, onSuccess, onFailure);
	}

	private native void removeJs(JavaScriptObject source, String key,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.remove(key, onSuccess, onFailure);
																	}-*/;

	@Override
	public final void close(final SimpleCallback callback) {
		if (ENABLE_LOG) {
			GWT.log(this + ".close()");
		}
		final JavaScriptObject onSuccess = FnJs
				.exportCallback(new EmptyCallback() {

					@Override
					public void call() {
						if (ENABLE_LOG) {
							GWT.log(this + ".close()->onSuccess");
						}
						callback.onSuccess();
					}
				});

		final JavaScriptObject onFailure = createFailureCallback(callback);

		closeJs(source, onSuccess, onFailure);
	}

	private native void closeJs(JavaScriptObject source,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
																	source.close(onSuccess, onFailure);
																	}-*/;

	@Override
	public final void commit(final SimpleCallback callback) {
		if (ENABLE_LOG) {
			GWT.log(this + ".commit()");
		}
		final JavaScriptObject onSuccess = FnJs
				.exportCallback(new EmptyCallback() {

					@Override
					public void call() {
						if (ENABLE_LOG) {
							GWT.log(this + ".commit()->onSuccess");
						}
						callback.onSuccess();
					}
				});

		final JavaScriptObject onFailure = createFailureCallback(callback);

		commitJs(source, onSuccess, onFailure);
	}

	private native void commitJs(JavaScriptObject source,
			JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
																	source.commit(onSuccess, onFailure);
																	}-*/;

	@Override
	public void clearCache() {
		if (ENABLE_LOG) {
			GWT.log(this + ".clearCache()");
		}
		clearCacheJs(source);
	}

	private native void clearCacheJs(JavaScriptObject source)/*-{
																	source.clearCache();
																	}-*/;

}
