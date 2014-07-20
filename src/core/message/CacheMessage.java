package core.message;

/**
 * This class can be useful for all kind of messages that can cache its content
 * in memory.
 * 
 * @author Jaisse Grela
 */
public class CacheMessage implements IMessage {

	protected byte[] cache;
	protected String type;

	public CacheMessage(byte[] cache) {
		this(cache, null);
	}
	
	public CacheMessage(byte[] cache, String type) {
		this.cache = cache;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.IMessage#bytes()
	 */
	@Override
	public int bytes() {
		return cache.length;
	}

	/**
	 * Get all bytes of the message
	 * 
	 * @return the content message
	 */
	public byte[] getAllBytes() {
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.IMessage#getByte(int)
	 */
	@Override
	public byte getByte(int index) {
		return cache[index];
	}

	@Override
	public String getType() {
		return type;
	}

}