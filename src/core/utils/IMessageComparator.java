package core.utils;

import core.message.IMessage;

public interface IMessageComparator {

	public abstract double similarity(IMessage value);

}