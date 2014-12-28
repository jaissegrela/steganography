package app.old;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import core.utils.KPoint;

public class SerializationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<KPoint> list = new ArrayList<KPoint>();
		list.add(new KPoint(1, 1, 3));
		list.add(new KPoint(2, 4, 5));
		
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream("/employee.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(list);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in employee.ser");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
		
		try
	      {
	         FileInputStream fileIn = new FileInputStream("/employee.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         ArrayList<KPoint> e = (ArrayList<KPoint>) in.readObject();
	         in.close();
	         fileIn.close();
	         for (KPoint kPoint : e) {
				System.out.println(kPoint);
			}
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         return;
	      }
	}

}
