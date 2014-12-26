package core.utils;

import org.apache.commons.math3.exception.NullArgumentException;

import core.message.IMessage;
import core.utils.enumerations.BitEnumeration;

public class MessageComparatorByPosition extends MessageComparator{
	
	public MessageComparatorByPosition(IMessage message) {
		super(message);
	}
	
	public double similarity(IMessage value){
		if(value == null)
			throw new NullArgumentException();
		IMessage message = getMessage();
		if(message.bytes() != value.bytes())
			throw new IllegalArgumentException(
					String.format("Message length:%d, Parameter length:%s", message.bytes(), value.bytes()));
		BitEnumeration eMessage = message.getEnumeration();
		BitEnumeration vMessage = value.getEnumeration();
		int length = 0;
		int count = 0;
		while (eMessage.hasMoreElements()) {
			count <<= 1;
			Boolean m = (Boolean) eMessage.nextElement();
			Boolean v = vMessage.nextElement();
			length++;
			if(m.equals(v)){
				count++;
			}
		}
		return length == 0 ? 0 : ((double)count) * 100 / ((1 << length) - 1);
	}

}
