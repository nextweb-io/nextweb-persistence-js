package io.nextweb.persistence.js.internal;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.SerializationException;
import com.seanchenxi.gwt.storage.client.serializer.StorageRPCSerializerImpl;

import de.mxro.serialization.string.StringDestination;
import de.mxro.serialization.string.StringSource;
import io.nextweb.persistence.js.JsSerializer;

public class RpcSerializer implements JsSerializer {

	private final StorageRPCSerializerImpl serializerImpl;

	@Override
	public boolean serialize(Object obj, StringDestination dest) {
		if (obj instanceof String) {
			dest.getDestination().setValue( "S" + (String) obj);
			return true;
		}

		try {
			dest.getDestination().setValue( "O"
					+ serializerImpl.serialize(Serializable.class,
							(Serializable) obj));
			return true;
		} catch (SerializationException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public Object deserialize(StringSource source) {
		String data = source.getSource();
		switch (data.charAt(0)) {
		case 'S':
			return data.substring(1);

		case 'O':
			try {
				return serializerImpl.deserialize(Serializable.class,
						data.substring(1));
			} catch (SerializationException e) {
				throw new RuntimeException(e);
			}
		}

		throw new RuntimeException("Unsupported serialization format: "
				+ data.charAt(0) + " in data " + data);
	}

	

	public RpcSerializer() {
		super();
		this.serializerImpl = new StorageRPCSerializerImpl();
	}

}
