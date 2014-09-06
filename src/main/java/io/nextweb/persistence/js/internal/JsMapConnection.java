package io.nextweb.persistence.js.internal;

import io.nextweb.persistence.js.JsSerializer;
import io.nextweb.promise.js.FnJs;
import io.nextweb.promise.js.callbacks.EmptyCallback;
import io.nextweb.promise.js.exceptions.ExceptionUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

import de.mxro.async.callbacks.FailureCallback;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.operations.MapOperation;
import de.mxro.fn.Closure;
import de.mxro.serialization.Serialization;
import de.mxro.serialization.string.StringDestination;

public class JsMapConnection implements AsyncMap<String, Object> {

    private final static boolean ENABLE_LOG = true;

    private final JavaScriptObject source;
    private final JsSerializer serializer;

    public JsMapConnection(final JavaScriptObject source, final JsSerializer serializer) {
        super();
        if (source == null) {
            throw new IllegalArgumentException("source should not be null.");
        }
        this.source = source;
        this.serializer = serializer;
    }

    private JavaScriptObject createFailureCallback(final FailureCallback callback) {
        return FnJs.exportCallback(new Closure<Object>() {

            @Override
            public void apply(final Object o) {
                if (ENABLE_LOG) {
                    GWT.log(this + "->Operation failed: " + o);
                }
                callback.onFailure(ExceptionUtils.convertJavaScriptException(o));
            }
        });
    }

    @Override
    public void put(final String key, final Object value, final SimpleCallback callback) {

        if (ENABLE_LOG) {
            GWT.log(this + ".put(" + key + ", " + value + ":" + value.getClass() + ")");
        }

        final StringDestination stringDestination = Serialization.createStringDestination();
        serializer.serialize(value, stringDestination);
        final String serializedValue = stringDestination.getDestination().getValue();

        final JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

            @Override
            public void call() {
                if (ENABLE_LOG) {
                    GWT.log(this + ".put(" + key + ", " + value + ")->onSuccess");
                }
                callback.onSuccess();
            }
        });

        final JavaScriptObject onFailure = createFailureCallback(callback);

        putJs(source, key, serializedValue, onSuccess, onFailure);

    }

    private native void putJs(JavaScriptObject source, String key, String value, JavaScriptObject onSuccess,
            JavaScriptObject onFailure)/*-{
                                       source.put(key, value, onSuccess, onFailure);
                                       }-*/;

    @Override
    public void putSync(final String key, final Object value) {
        if (ENABLE_LOG) {
            GWT.log(this + ".putSync(" + key + ", " + value + ":" + value.getClass() + ")");
        }

        final StringDestination stringDestination = Serialization.createStringDestination();
        serializer.serialize(value, stringDestination);
        final String serializedValue = stringDestination.getDestination().getValue();

        putSyncJs(source, key, serializedValue);
    }

    private native void putSyncJs(JavaScriptObject source, String key, String value)/*-{
                                                                                    														source.putSync(key, value);
                                                                                    														}-*/;

    @Override
    public final void get(final String key, final ValueCallback<Object> callback) {

        if (ENABLE_LOG) {
            GWT.log(this + ".get(" + key + ")");
        }

        final JavaScriptObject onSuccess = FnJs.exportCallback(new Closure<Object>() {

            @Override
            public void apply(final Object o) {
                if (ENABLE_LOG) {
                    GWT.log(this + ".get(" + key + ")->onSuccess=" + o);
                }
                callback.onSuccess(serializer.deserialize(Serialization.createStringSource((String) o)));
            }

        });

        final JavaScriptObject onFailure = createFailureCallback(callback);

        getJs(source, key, onSuccess, onFailure);
    }

    private native void getJs(JavaScriptObject source, String key, JavaScriptObject onSuccess,
            JavaScriptObject onFailure)/*-{ 
                                       source.get(key, onSuccess, onFailure);
                                       }-*/;

    @Override
    public Object getSync(final String key) {
        if (ENABLE_LOG) {
            GWT.log(this + ".getSync(" + key + ")");
        }
        final String value = getSyncJs(source, key);
        if (value == null) {
            if (ENABLE_LOG) {
                GWT.log(this + ".getSync(" + key + ")->" + null);
            }
            return null;
        }

        if (ENABLE_LOG) {
            GWT.log(this + ".getSync(" + key + ")->deserializing" + value);
        }

        final Object res = serializer.deserialize(Serialization.createStringSource(value));
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
        final JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

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

    private native void removeJs(JavaScriptObject source, String key, JavaScriptObject onSuccess,
            JavaScriptObject onFailure)/*-{ 
                                       source.remove(key, onSuccess, onFailure);
                                       }-*/;

    @Override
    public void removeSync(final String key) {
        if (ENABLE_LOG) {
            GWT.log(this + ".removeSync(" + key + ")");
        }
        removeSyncJs(source, key);

    }

    private native void removeSyncJs(JavaScriptObject source, String key)/*-{ 
                                                                         source.removeSync(key);

                                                                         }-*/;

    @Override
    public final void stop(final SimpleCallback callback) {
        if (ENABLE_LOG) {
            GWT.log(this + ".stop()");
        }
        final JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

            @Override
            public void call() {
                if (ENABLE_LOG) {
                    GWT.log(this + ".stop()->onSuccess");
                }
                callback.onSuccess();
            }
        });

        final JavaScriptObject onFailure = createFailureCallback(callback);

        stopJs(source, onSuccess, onFailure);
    }

    private native void stopJs(JavaScriptObject source, JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
                                                                                                               source.stop(onSuccess, onFailure);
                                                                                                               }-*/;

    @Override
    public void start(final SimpleCallback callback) {
        if (ENABLE_LOG) {
            GWT.log(this + ".start()");
        }
        final JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

            @Override
            public void call() {
                if (ENABLE_LOG) {
                    GWT.log(this + ".start()->onSuccess");
                }
                callback.onSuccess();
            }
        });

        final JavaScriptObject onFailure = createFailureCallback(callback);

        startJs(source, onSuccess, onFailure);
    }

    private native void startJs(JavaScriptObject source, JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{ 
                                                                                                                source.start(onSuccess, onFailure);
                                                                                                                }-*/;

    @Override
    public final void commit(final SimpleCallback callback) {
        if (ENABLE_LOG) {
            GWT.log(this + ".commit()");
        }
        final JavaScriptObject onSuccess = FnJs.exportCallback(new EmptyCallback() {

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

    private native void commitJs(JavaScriptObject source, JavaScriptObject onSuccess, JavaScriptObject onFailure)/*-{
                                                                                                                 source.commit(onSuccess, onFailure);
                                                                                                                 }-*/;

    @Override
    public void performOperation(final MapOperation operation) {
        if (ENABLE_LOG) {
            GWT.log(this + ".performOperation() XXXX> Ignored");
        }
    }

}
