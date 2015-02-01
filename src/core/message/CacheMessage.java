package core.message;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import core.utils.ByteInfo;
import core.utils.Converter;

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

	@Override
	public Enumeration<Boolean> getEnumeration() {
		return new BitEnumeration();
	}

	@Override
	public int size() {
		return cache.length * ByteInfo.BYTE_SIZE;
	}
	
	protected class BitEnumeration implements Enumeration<Boolean> {
		
		protected int index, subindex;
		protected boolean[] temp;

		public BitEnumeration(){
			getTemporalValues();
		}
		
		@Override
		public boolean hasMoreElements() {
			return index < cache.length ;
		}

		@Override
		public Boolean nextElement() {
			if(!hasMoreElements())
				throw new NoSuchElementException();
			boolean result = temp[subindex];
			subindex++;
			if(subindex >= ByteInfo.BYTE_SIZE)
			{
				index++;
				subindex = 0;
				getTemporalValues();
			}
			return result;
		}

		protected void getTemporalValues() {
			if(hasMoreElements())
				temp = Converter.toArrayofBoolean(cache[index]);
		}

	}

}