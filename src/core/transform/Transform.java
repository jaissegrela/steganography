package core.transform;

public interface Transform {

	public abstract void transform(double[] input, int levels);

	public abstract void inverse(double[] input, int levels);

}