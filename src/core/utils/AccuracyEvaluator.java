package core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import core.message.IMessage;
import core.utils.enumerations.BitEnumeration;

public class AccuracyEvaluator {
	
	private IMessage message;
	private int step = 1;
	private ArrayList<Boolean> minorStep, sincronizationStep;
	
	public AccuracyEvaluator(IMessage message, int step){
		setMessage(message);
		setStep(step);
		setMinorStep(new ArrayList<Boolean>(step * message.bytes() * ByteInfo.BYTE_SIZE));
		setSincronizationStep(new ArrayList<Boolean>(message.bytes() * ByteInfo.BYTE_SIZE));
	}

	public IMessage getMessage() {
		return message;
	}

	public void setMessage(IMessage message) {
		this.message = message;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		if(step > 0)
			this.step = step;
		else
			System.out.println("Warning: step < 0");
	}

	protected ArrayList<Boolean> getSincronizationStep() {
		return sincronizationStep;
	}

	protected void setSincronizationStep(ArrayList<Boolean> sincronizationStep) {
		this.sincronizationStep = sincronizationStep;
	}

	protected ArrayList<Boolean> getMinorStep() {
		return minorStep;
	}

	protected void setMinorStep(ArrayList<Boolean> minorStep) {
		this.minorStep = minorStep;
	}
	
	public void addMinorStepResult(boolean b){
		this.minorStep.add(b);
	}
	
	public void addSincronizationResult(Boolean b){
		sincronizationStep.add(b);
	}
	
	public void reset(){
		this.sincronizationStep.clear();
		this.minorStep.clear();
	}
	
	public double minorStepResultErrorAccurancy() {
		if(minorStep.size() != step * message.bytes() * ByteInfo.BYTE_SIZE)
			throw new IllegalStateException(String.format("Step:%s Lenght1:%s Length2:%s", step, minorStep.size(),step * message.bytes() * ByteInfo.BYTE_SIZE ));
		int count = 0, i = 0;
		Enumeration<Boolean> vMessage = Collections.enumeration(minorStep);
		BitEnumeration eMessage = message.getEnumeration();
		Boolean m = false;
		while (vMessage.hasMoreElements()) {
			if(i == 0)
				m = eMessage.nextElement();
			if(!m.equals(vMessage.nextElement()))
				count++;
			i = (i + 1) % step;
		}
		return count * 100d / minorStep.size() ;
	}
	
	public double sincronizationResultAccurancy() {
		if(sincronizationStep.size() != message.bytes() * ByteInfo.BYTE_SIZE)
			throw new IllegalStateException();
		Enumeration<Boolean> vMessage = Collections.enumeration(sincronizationStep);
		BitEnumeration eMessage = message.getEnumeration();
		int count = 0;
		while (eMessage.hasMoreElements()) {
			if(eMessage.nextElement().equals(vMessage.nextElement()))
				count++;
		}
		return count * 100d / sincronizationStep.size();
	}
}
