package io.nextweb.persistence.js.internal;

import delight.async.AsyncCommon;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;
import delight.gwt.console.Console;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.StoreImplementation;
import delight.keyvalue.internal.v01.StoreEntryData;
import delight.keyvalue.operations.StoreOperation;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void put(final String key, final Object value, final SimpleCallback callback) {

        if (ENABLE_LOG) {
            Console.log(this + ".put(" + key + ", " + value + ":" + value.getClass() + ")");
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
            Console.log(this + ".putSync(" + key + ", " + value + ":" + value.getClass() + ")");
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
            Console.log(this + ".get(" + key + ")");
        }

        final JavaScriptObject jscallback = JsStringValueCallbackWrapper.wrap(new ValueCallback<String>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final String value) {
                if (ENABLE_LOG) {
                    Console.log(this + ".got(" + value + ")");
                }
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
            Console.log(this + ".getSync(" + key + ")");
        }
        final String value = getSyncJs(source, key);
        if (value == null) {
            if (ENABLE_LOG) {
                Console.log(this + ".getSync(" + key + ")->" + null);
            }
            return null;
        }

        if (ENABLE_LOG) {
            Console.log(this + ".getSync(" + key + ")->deserializing" + value);
        }

        final Object res = serializer.deserialize(Serialization.createStringSource(value));
        if (ENABLE_LOG) {
            Console.log(this + ".getSync(" + key + ")->" + res);
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
            Console.log(this + ".removeSync(" + key + ")");
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
            Console.log(this + ".commit()");
        }

        final JavaScriptObject jscallback = JsSimpleCallbackWrapper.wrap(callback);

        commitJs(source, jscallback);
    }

    private native void commitJs(JavaScriptObject source, JavaScriptObject callback)/*-{
                                                                                                                 source.commit(callback);
                                                                                                                 }-*/;

    @Override
    public void performOperation(final StoreOperation<String, Object> operation, final ValueCallback<Object> callback) {
        if (ENABLE_LOG) {
            Console.log(this + ": perform " + operation);
        }
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
        if (ENABLE_LOG) {
            Console.log(this + ": removeCall " + keyStartsWith);
        }
        getAll(keyStartsWith, 0, -1, AsyncCommon.embed(AsyncCommon.asValueCallback(callback),
                new Closure<List<StoreEntry<String, Object>>>() {

                    @Override
                    public void apply(final List<StoreEntry<String, Object>> matches) {
                        for (final StoreEntry<String, Object> m : matches) {
                            removeSync(m.key());
                        }
                        if (ENABLE_LOG) {
                            Console.log(this + ": removed " + matches);
                        }
                        callback.onSuccess();
                    }
                }));

    }

    @Override
    public void getAll(final String keyStartsWith, final int fromIdx, final int toIdx,
            final ValueCallback<List<StoreEntry<String, Object>>> callback) {
        if (ENABLE_LOG) {
            Console.log(this + ": getAll " + keyStartsWith);
        }
        try {
            final JSONArray jsonArray = new JSONArray(getAllKeysJs(source));

            final List<String> keys = new ArrayList<String>(jsonArray.size());

            for (int i = 0; i < jsonArray.size(); i++) {
                final JSONValue val = jsonArray.get(i);
                final JSONString str = val.isString();
                keys.add(str.stringValue());
            }

            int found = 0;
            final int toFind = toIdx - fromIdx + 1;
            int idx = fromIdx;
            final List<StoreEntry<String, Object>> res = new ArrayList<StoreEntry<String, Object>>(toFind);

            if (ENABLE_LOG) {
                Console.log(this + ": getAll got JSONArray " + jsonArray.toString());
            }

            while (idx < keys.size() && (found <= toFind || toIdx == -1)) {

                final String key = keys.get(idx);

                if (key.startsWith(keyStartsWith)) {

                    res.add(new StoreEntryData<String, Object>(key, getSync(key)));
                    found++;
                }

                idx++;

            }

            if (ENABLE_LOG) {
                Console.log(this + ": getAll got entries " + res);
            }

            callback.onSuccess(res);
        } catch (final Throwable t) {
            callback.onFailure(t);
        }
    }

    @Override
    public void getSize(final String keyStartsWith, final ValueCallback<Integer> callback) {
        getAll(keyStartsWith, 0, -1, AsyncCommon.embed(callback, new Closure<List<StoreEntry<String, Object>>>() {

            @Override
            public void apply(final List<StoreEntry<String, Object>> entries) {

                int size = 0;
                for (final StoreEntry<String, Object> e : entries) {
                    size += size + e.key().length();
                    size += size + e.value().toString().length();
                }

                callback.onSuccess(size);

            }

        }));
    }

    @Override
    public void count(final String keyStartsWith, final ValueCallback<Integer> callback) {
        getAll(keyStartsWith, 0, -1, AsyncCommon.embed(callback, new Closure<List<StoreEntry<String, Object>>>() {

            @Override
            public void apply(final List<StoreEntry<String, Object>> o) {
                callback.onSuccess(o.size());
            }

        }));
    }

    @Override
    public void get(final List<String> keys, final ValueCallback<List<Object>> callback) {
        final List<Object> results = new ArrayList<Object>(keys.size());

        for (final String key : keys) {
            results.add(getSync(key));
        }

        callback.onSuccess(results);

    }

    public JsAsyncMap(final JavaScriptObject source, final JsSerializer serializer) {
        super();
        if (source == null) {
            throw new IllegalArgumentException("source should not be null.");
        }
        this.source = source;
        this.serializer = serializer;
    }

}
