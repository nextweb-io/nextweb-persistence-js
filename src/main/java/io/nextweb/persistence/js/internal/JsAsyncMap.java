package io.nextweb.persistence.js.internal;

import delight.async.Value;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.internal.v01.StoreEntryData;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import de.mxro.serialization.Serialization;
import de.mxro.serialization.string.StringDestination;
import io.nextweb.persistence.js.JsSerializer;
import io.nextweb.promise.js.callbacks.JsSimpleCallbackWrapper;
import io.nextweb.promise.js.callbacks.JsStringValueCallbackWrapper;

public class JsAsyncMap implements StoreImplementation<String, Object> {

    private final static boolean ENABLE_LOG = false;

    private final JavaScriptObject source;
    private final JsSerializer serializer;

    public JsAsyncMap(final JavaScriptObject source, final JsSerializer serializer) {
        super();
        if (source == null) {
            throw new IllegalArgumentException("source should not be null.");
        }
        this.source = source;
        this.serializer = serializer;
    }

    @Override
    public void put(final String key, final Object value, final SimpleCallback callback) {

        if (ENABLE_LOG) {
            GWT.log(this + ".put(" + key + ", " + value + ":" + value.getClass() + ")");
        }

        final StringDestination stringDestination = Serialization.createStringDestination();
        serializer.serialize(value, stringDestination);
        final String serializedValue = stringDestination.getDestination().getValue();

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        putJs(source, key, serializedValue, jscallback);

    }

    private native void putJs(JavaScriptObject source, String key, String value, JavaScriptObject callback)/*-{
                                                                                                           source.put(key, value, callback);
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

    private native void putSyncJs(JavaScriptObject source, String key,
            String value)/*-{
                         														source.putSync(key, value);
                         														}-*/;

    @Override
    public final void get(final String key, final ValueCallback<Object> callback) {

        if (ENABLE_LOG) {
            GWT.log(this + ".get(" + key + ")");
        }

        final JavaScriptObject jscallback = JsStringValueCallbackWrapper.wrap(new ValueCallback<String>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final String value) {
                if (value == null) {
                    callback.onSuccess(null);
                    return;
                }
                callback.onSuccess(serializer.deserialize(Serialization.createStringSource(value)));
            }
        });

        getJs(source, key, jscallback);
    }

    private native void getJs(JavaScriptObject source, String key, JavaScriptObject callback)/*-{ 
                                                                                             source.get(key, callback);
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

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        removeJs(source, key, jscallback);
    }

    private native void removeJs(JavaScriptObject source, String key, JavaScriptObject callback)/*-{ 
                                                                                                source.remove(key, callback);
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

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        stopJs(source, jscallback);
    }

    private native void stopJs(JavaScriptObject source, JavaScriptObject callback)/*-{ 
                                                                                                               source.stop(callback);
                                                                                                               }-*/;

    @Override
    public void start(final SimpleCallback callback) {

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        startJs(source, jscallback);
    }

    private native void startJs(JavaScriptObject source, JavaScriptObject callback)/*-{ 
                                                                                                                source.start(callback);
                                                                                                                }-*/;

    @Override
    public final void commit(final SimpleCallback callback) {
        if (ENABLE_LOG) {
            GWT.log(this + ".commit()");
        }

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        commitJs(source, jscallback);
    }

    private native void commitJs(JavaScriptObject source, JavaScriptObject callback)/*-{
                                                                                                                 source.commit(callback);
                                                                                                                 }-*/;

    @Override
    public void performOperation(final StoreOperation<String, Object> operation, final ValueCallback<Object> callback) {
        operation.applyOn(this, callback);
    }

    @Override
    public void clearCache() {
        // DO NOTHING
    }

    private native JavaScriptObject getAllKeysJs(JavaScriptObject source)/*-{
                                                                                                    return source.getAllKeys();
                                                                                                    }-*/;

    @Override
    public void removeAll(final String keyStartsWith, final SimpleCallback callback) {
        getAll(keyStartsWith, new Closure<StoreEntry<String, Object>>() {

            @Override
            public void apply(final StoreEntry<String, Object> o) {
                removeSync(o.key());
            }
        }, callback);
    }

    @Override
    public void getAll(final String keyStartsWith, final Closure<StoreEntry<String, Object>> onEntry,
            final SimpleCallback onCompleted) {
        final JSONArray jsonArray = new JSONArray(getAllKeysJs(source));

        final List<String> keys = new ArrayList<String>(jsonArray.size());

        for (int i = 0; i < jsonArray.size(); i++) {
            final JSONValue val = jsonArray.get(i);
            final JSONString str = val.isString();
            keys.add(str.stringValue());
        }

        // runs in one thread anyway therefore no special async logic
        for (final String key : keys) {
            if (key.startsWith(keyStartsWith)) {

                onEntry.apply(new StoreEntryData<String, Object>(key, getSync(key)));

            }
        }

        onCompleted.onSuccess();
    }

    @Override
    public void count(final String keyStartsWith, final ValueCallback<Integer> callback) {
        final Value<Integer> count = new Value<Integer>(0);
        getAll(keyStartsWith, new Closure<StoreEntry<String, Object>>() {

            @Override
            public void apply(final StoreEntry<String, Object> o) {
                count.set(count.get() + 1);
            }
        }, new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(count.get());
            }
        });
    }

    @Override
    public void get(final List<String> keys, final ValueCallback<List<Object>> callback) {
        final List<Object> results = new ArrayList<Object>(keys.size());

        for (final String key : keys) {
            results.add(getSync(key));
        }

        callback.onSuccess(results);

    }

}
