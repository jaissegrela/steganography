package core.utils;

import java.io.Serializable;

import org.bytedeco.javacpp.opencv_features2d.KeyPoint;

public class KPoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 130732514266393985L;
	
	private int x, y, radio;
	
	public KPoint(){}
	
	public KPoint(int x, int y, int radio){
		setX(x);
		setY(y);
		setRadio(radio);
	}
	
	public KPoint(KeyPoint keyPoint){
		this((int)keyPoint.pt().x(), (int)keyPoint.pt().y(), (int)keyPoint.size());
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRadio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}
	
	public KeyPoint getKeyPoint(){
		return new KeyPoint(getX(), getY(), getRadio());
	}
	
	@Override
	public String toString() {
		return String.format("(%d; %d) [%s]", getX(), getY(), getRadio());
	}
	
}
