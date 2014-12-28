package core.utils;


import core.message.IMessage;
import core.utils.enumerations.BitEnumeration;

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
		if(message.bytes() != value.bytes())
			throw new IllegalArgumentException(
					String.format("Message length:%d, Parameter length:%s", message.bytes(), value.bytes()));
		BitEnumeration eMessage = message.getEnumeration();
		BitEnumeration vMessage = value.getEnumeration();
		int length = 0;
		int count = 0;
		while (eMessage.hasMoreElements()) {
			Boolean m = (Boolean) eMessage.nextElement();
			Boolean v = vMessage.nextElement();
			length++;
			if(m.equals(v))
				count++;
		}
		return length == 0 ? 0 : ((double)count) * 100 / length;
	}

}
