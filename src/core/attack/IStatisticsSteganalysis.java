
package core.attack;

import core.message.IMessage;

/**
 * Represents an steganalysis strategy
 * 
 * @author Jaisse Grela
 */
public interface IStatisticsSteganalysis {

	/**
	 * Estimates the probability of a data being hidden in the message.
	 * @param message the message to be examined
	 * @return the probability
	 */
	public double calculate(IMessage message);
}
