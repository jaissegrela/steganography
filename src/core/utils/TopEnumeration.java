package core.utils;

import java.util.Enumeration;

public class TopEnumeration<E> implements Enumeration<E> {

	private Enumeration<E> source;
	private int top;

	public TopEnumeration(Enumeration<E> source, int top){
		this.source = source;
		this.top = top;
	}
	@Override
	public boolean hasMoreElements() {
		return top > 0 && source.hasMoreElements();
	}

	@Override
	public E nextElement() {
		if(!hasMoreElements())
			throw new java.util.NoSuchElementException();
		top--;
		return source.nextElement();
	}

}
