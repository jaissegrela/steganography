package test.core.message;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.opencv.core.Mat;

import core.message.CacheMessage;
import core.message.ICoverMessage;

public class DummyMessage extends CacheMessage implements ICoverMessage {

	public DummyMessage(int lenght) {
		this(new byte[lenght]);
	}

	public DummyMessage(byte[] values) {
		super(values);
	}

	@Override
	public ICoverMessage duplicateMessage() {
		return new DummyMessage(cache.clone());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(cache);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DummyMessage other = (DummyMessage) obj;
		if (!Arrays.equals(cache, other.cache))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DummyMessage [values=" + Arrays.toString(cache) + "]";
	}

	@Override
	public void setByte(int index, byte value) {
		cache[index] = value;
	}

	@Override
	public void save(OutputStream stream) throws IOException {
		stream.write(getAllBytes());
	}

	@Override
	public Mat getMat() {
		throw new UnsupportedOperationException();
	}

}
