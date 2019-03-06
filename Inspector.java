import java.util.*;
import java.lang.reflect.*;

public class Inspector {
	public Inspector() { }
	
	private Vector objectsAlreadyInspected = new Vector();
	
	public void inspect(Object obj, boolean recursive) throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
		obj = (Object) obj;
		
		objectsAlreadyInspected.addElement(obj);
		Class ObjClass = obj.getClass();
	
		System.out.println("\nInside inspector: " + obj + " (recursive = "+recursive+")");
		
		inspectClass(obj, ObjClass);
		inspectConstructors(obj, ObjClass);
		inspectMethods(obj, ObjClass);
		inspectFields(obj, ObjClass, recursive);
		if(ObjClass.getSuperclass() != null)
//			inspectSuperclass(obj, ObjClass, recursive);
		
		System.out.println();
    }
	
	/********************************************************************************************/
	private void inspectClass(Object obj, Class ObjClass)
	{
		//Inspect and print class name
		System.out.println("\nCLASS: "+ ObjClass.getSimpleName());
		//Inspect and print immediate superclass
		if (ObjClass.getSuperclass() != null)
			System.out.println("  - Immediate superclass: "+ ObjClass.getSuperclass().getName());
		//Inspect and print implemented interfaces
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
		// Handle if object is array
		if (ObjClass.isArray()) 
		{
			System.out.print("\n  - Object is an array:");
			handleArray(obj);
		}
	}
	
	/********************************************************************************************/
	private void inspectConstructors(Object obj, Class ObjClass)
	{
		for (int i=0; i<ObjClass.getDeclaredConstructors().length; i++)
		{					
			Constructor c = ObjClass.getDeclaredConstructors()[i];
			c.setAccessible(true);
			
			//Inspect 
			System.out.print("\nConstructor: ");
			
			//Inspect and print parameter types
			if (c.getParameterTypes().length == 0)
				System.out.print("\n  - No parameters.");
			else 
			{
				System.out.print("\n  - Parameter types: ");
				for (int j=0; j<c.getParameterTypes().length; j++)
				{
					Class pType = c.getParameterTypes()[j];
					System.out.print(pType.getSimpleName());

					if (j < (c.getParameterTypes().length)-1)
						System.out.print(", ");
				}
			}
			//Inspect and print modifiers
			System.out.print("\n  - Modifiers: "+ Modifier.toString(c.getModifiers()));
		}
	}

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
					System.out.print("  - Exceptions thrown: ");
					for (int j=0; j<m.getExceptionTypes().length; j++)
					{
						Class eType = m.getExceptionTypes()[j];
						System.out.print(eType.getName());
						if (j < (m.getExceptionTypes().length)-1)
							System.out.print(", ");
					}
				}
				//Inspect and print parameter types
				if (m.getParameterTypes().length == 0)
					System.out.print("\n  - No parameters.");
				else 
				{
					System.out.print("\n  - Parameter types: ");
					for (int j=0; j<m.getParameterTypes().length; j++)
					{
						Class pType = m.getParameterTypes()[j];
						System.out.print(pType.getSimpleName());
						if (j < (m.getParameterTypes().length)-1)
							System.out.print(", ");
					}
				}
				//Inspect and print return type
				System.out.println("\n  - Return type: "+ m.getReturnType().getSimpleName());
				//Inspect and print modifiers
				System.out.print("  - Modifiers: "+ Modifier.toString(m.getModifiers()));
			}
		}
	}
	
	/********************************************************************************************/
	private void inspectFields(Object obj,Class ObjClass, boolean rec) throws IllegalAccessException, IllegalArgumentException, InstantiationException
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
				System.out.println("  - Type: "+f.getType().getSimpleName());
				//Inspect and print field modifiers
				System.out.println("  - Modifiers: " +Modifier.toString(f.getModifiers()));
				
				//Inspect and print field's current value
				System.out.print("  - Current value: ");
				
				///If value is a primitive
				if(f.getType().isPrimitive())
					System.out.print(f.get(obj));
				///Else, if the value is an array
				else if (f.getType().isArray())
				{
					handleArray(f.get(obj));
				}
				///Else, if value is a non-array object
				else
				{
			 		System.out.print(f.get(obj) + ", Identity hash code: " + System.identityHashCode(f.get(obj))); 
					
			 		// Further, if it's not null and recursion is set to true
					if (rec == true && f.get(obj) != null) 
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

	/********************************************************************************************/
 	private void inspectSuperclass(Object obj,Class ObjClass, boolean rec) throws IllegalAccessException, IllegalArgumentException, InstantiationException 
	{
		if (! objectsAlreadyInspected.contains(ObjClass))
		{
			System.out.print("\n\nINSPECTING SUPERCLASS OF "+ObjClass.getName()+ ": " + ObjClass.getSuperclass().getName());
			if (ObjClass.getSuperclass().getName() == "java.lang.Object" && ObjClass.getSuperclass().getName() != "ClassC" )
				inspect(ObjClass.getSuperclass().newInstance(), rec);
			else
				inspect(ObjClass.getSuperclass(), rec);
			System.out.print("FINISHED INSPECTING "+ ObjClass.getSuperclass().getName());
		}
	}
 	
 	/********************************************************************************************/
 	private void handleArray(Object arrayObj)
 	{
 		Class arrayClass = arrayObj.getClass();
 		System.out.println("\n\tComponent type: "+ arrayClass.getComponentType().getSimpleName());
 		System.out.println("\tLength: "+ Array.getLength(arrayObj));
 		
 		System.out.print("\tContents: ");
 		printArrayContents(arrayObj);
 	}
 	
 	private void printArrayContents(Object arrayObj)
 	{
 		System.out.print("[");
 		for (int i =0; i<Array.getLength(arrayObj); i++)
 		{ 	
 			// If the element is a non-array object
 			if (Array.get(arrayObj, i) == null)
 				System.out.print(Array.get(arrayObj, i));
 			// If element is another array (multidimensional), recursively deal with it
 			else if (Array.get(arrayObj, i).getClass().isArray())
 				printArrayContents(Array.get(arrayObj, i));
 			else
 				System.out.print(Array.get(arrayObj, i));
 			
 			if (i < (Array.getLength(arrayObj))-1)
				System.out.print(", ");
 		}
 		System.out.print("]");
 	}
}

