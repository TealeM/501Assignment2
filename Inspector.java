import java.util.*;
import java.lang.reflect.*;

public class Inspector {
	public Inspector() { }
	
	private Vector objectsAlreadyInspected = new Vector();
	
	public void inspect(Object obj, boolean recursive) throws IllegalArgumentException, IllegalAccessException
    {
		objectsAlreadyInspected.addElement(obj);
		Class ObjClass = obj.getClass();
	
		System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
		
		inspectClass(obj, ObjClass);
//		inspectConstructors(obj, ObjClass);
		inspectMethods(obj, ObjClass);
		inspectFields(obj, ObjClass, recursive);
		
		System.out.println();
		   
    }
	
	/********************************************************************************************/
	private void inspectClass(Object obj, Class ObjClass)
	{
		System.out.println("\nCLASS: "+ ObjClass.getName());
		System.out.println("  - Immediate superclass: "+ ObjClass.getSuperclass().getName());
		if (ObjClass.getInterfaces().length == 0)
			System.out.println("  - Does not implement any interfaces.");
		else {
			System.out.print("  - Implements: ");
			for (int i=0; i< ObjClass.getInterfaces().length; i++)
			{
				System.out.print(ObjClass.getInterfaces()[i].getName());
				if (i < (ObjClass.getInterfaces().length)-1)
					System.out.print(", ");
			}
		}
	}
	
	/********************************************************************************************/
/*	private void inspectConstructors(Object obj, Class ObjClass)
	{
		
	}
*/	
	/********************************************************************************************/
	private void inspectMethods(Object obj, Class ObjClass)
	{
		if(ObjClass.getDeclaredMethods().length == 0)
			System.out.print("\nNo methods.");
		else
		{
			//Inspect each method
			for (int i=0; i<ObjClass.getDeclaredMethods().length; i++)
			{					
				Method m = ObjClass.getDeclaredMethods()[i];
				m.setAccessible(true);
				
				//Inspect and print name
				System.out.println("\nMethod: "+m.getName());
				
				//Inspect and print exception types
				if (m.getExceptionTypes().length == 0)
					System.out.print("  - No exceptions thrown.");
				else 
				{
					System.out.print("\n  - Exceptions thrown: ");
					for (int j=0; j<m.getExceptionTypes().length; j++)
					{
						Class eType = m.getExceptionTypes()[j];
						System.out.print(eType.getName());
						if (i < (m.getExceptionTypes().length)-1)
							System.out.print(", ");
					}
				}
				//Inspect and print parameter types
				if (m.getExceptionTypes().length == 0)
					System.out.print("\n  - No parameters.");
				else 
				{
					System.out.print("\n  - Parameter types: ");
					for (int j=0; j<m.getParameterTypes().length; j++)
					{
						Class pType = m.getParameterTypes()[j];
						System.out.print(pType.getName());
						if (i < (m.getParameterTypes().length)-1)
							System.out.print(", ");
					}
				}
				//Inspect and print return type
				System.out.println("\n  - Return type: "+ m.getReturnType().getName());
				//Inspect and print modifiers
				System.out.print("  - Modifiers: "+ Modifier.toString(m.getModifiers()));
			}
		}
	}
	
	/********************************************************************************************/
	private void inspectFields(Object obj,Class ObjClass, boolean rec) throws IllegalArgumentException, IllegalAccessException
	{
		if(ObjClass.getDeclaredFields().length == 0)
			System.out.println("\nNo fields.");
		else
		{
			for (int i=0; i<ObjClass.getDeclaredFields().length; i++)
			{
				Field f = (Field) ObjClass.getDeclaredFields()[i];
				f.setAccessible(true);
				
				//Inspect and print field name
				System.out.println("\nField: "+f.getName());
				//Inspect and print field type
				System.out.println("  - Type: " + f.getType().getName());
				//Inspect and print field modifiers
				System.out.println("  - Modifiers: " +Modifier.toString(f.getModifiers()));
				
				//Inspect and print field's current value
				System.out.print("  - Current value: ");
				if(f.getType().isPrimitive())
					System.out.print(f.get(obj));
				else if (! f.getType().isPrimitive())
			 		System.out.print(f.get(obj) + ", Identity hash code: " + System.identityHashCode(f.get(obj))); 
				if (rec == true && ! f.getType().isPrimitive() && f.get(obj) != null) 
				{
					if (! objectsAlreadyInspected.contains(f.get(obj)))
					{	
						System.out.print("\n\n------ RECURSIVELY INSPECTING FIELD: " + f.getName()+ " ------");
						inspect(f.get(obj), true);
						System.out.println("\n------ FINISHED RECURSION FOR FIELD: " + f.getName()+ " ------");
					}
				}		
			}
		}
	}
}
