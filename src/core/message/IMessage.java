package core.message;

import java.util.Enumeration;

/**
 * Represents a message to be hidden
 * 
 * @author Jaisse Grela
 */
public interface IMessage {
	
	public Enumeration<Boolean> getEnumeration();
	
	public int size();
}
