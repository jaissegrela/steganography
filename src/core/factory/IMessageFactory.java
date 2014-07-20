package core.factory;

import core.message.ICoverMessage;
import core.message.IMessage;

public interface IMessageFactory {
	
	public ICoverMessage createMessage(IMessage message);

}
