package core.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import core.message.IMessage;

public class BitEnumeration implements Enumeration<Boolean> {
	
	protected IMessage imessage;
	protected int index, subindex;
	protected boolean[] temp;

	public BitEnumeration(IMessage imessage){
		this.imessage = imessage;
		getTemporalValues();
	}
	
	@Override
	public boolean hasMoreElements() {
		return index < imessage.bytes() ;
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
			temp = Converter.toArrayofBoolean(imessage.getByte(index));
	}

}
