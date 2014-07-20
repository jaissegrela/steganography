package core.transform;

public interface Transform2d {

	public abstract void transform(double[][] input, int levels);

	public abstract void inverse(double[][] input, int levels);

}