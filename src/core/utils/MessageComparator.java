package core.utils;


import java.util.Enumeration;

import core.message.IMessage;

public class MessageComparator implements IMessageComparator {
	
	private IMessage message;
	
	public MessageComparator(IMessage message) {
		setMessage(message);
	}
	
	public IMessage getMessage() {
		return message;
	}
	
	public void setMessage(IMessage message) {
		this.message = message;
	}
	
	public double similarity(IMessage value){
		if(value == null)
			throw new IllegalArgumentException("The parameter cannot be null");
		if(message.size() != value.size())
			throw new IllegalArgumentException(
					String.format("Message length:%d, Parameter length:%s", message.size(), value.size()));
		Enumeration<Boolean> eMessage = message.getEnumeration();
		Enumeration<Boolean> vMessage = value.getEnumeration();
		int length = 0;
		int count = 0;
		while (eMessage.hasMoreElements()) {
			Boolean m = eMessage.nextElement();
			Boolean v = vMessage.nextElement();
			length++;
			if(m.equals(v))
				count++;
		}
		return length == 0 ? 0 : ((double)count) * 100 / length;
	}

}
