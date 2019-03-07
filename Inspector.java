import java.util.*;
import java.lang.reflect.*;

public class Inspector {
	public Inspector() { }
	
	private Vector objectsAlreadyInspected = new Vector();
	
	public void inspect(Object obj, boolean recursive) throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
		obj = (Object) obj;
		
		Class ObjClass = obj.getClass();
	
		System.out.println("\nInside inspector: " + obj + " (recursive = "+recursive+")");
		
		inspectClass(obj, ObjClass);
		inspectConstructors(obj, ObjClass);
		inspectMethods(obj, ObjClass);
		inspectFields(obj, ObjClass, recursive);
		System.out.print("\n\n------ FINISHED INSPECTING "+ObjClass.getName()+" ------");
		//Traverse hierarchy
		if(ObjClass.getSuperclass() != null)
			inspectSuperclass(obj, ObjClass, recursive);
		if(ObjClass.getInterfaces().length > 0) 
			inspectSuperInterfaces(obj, ObjClass, recursive);
		
		
		objectsAlreadyInspected.addElement(ObjClass);
		objectsAlreadyInspected.addElement(obj);
		System.out.println();
		
    }
	public void inspectAbstract(Object obj, Class ObjClass, boolean recursive) throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
		System.out.println("\nInside inspector: " + ObjClass + " (recursive = "+recursive+")");
		
		inspectClass(null, ObjClass);
		inspectConstructors(null, ObjClass);
		inspectMethods(null, ObjClass);
		inspectFields(obj, ObjClass, recursive);
		System.out.print("\n\n------ FINISHED INSPECTING "+ObjClass.getName()+" ------");
		//Traverse hierarchy
		if(ObjClass.getSuperclass() != null)
			inspectSuperclass(obj, ObjClass, recursive);
		if(ObjClass.getInterfaces().length > 0)
			inspectSuperInterfaces(obj, ObjClass, recursive);
		
		objectsAlreadyInspected.addElement(ObjClass);
		objectsAlreadyInspected.addElement(obj);

		System.out.println();
		
    }

	public void inspectInterface(Object obj, Class ObjClass, boolean recursive) throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
		System.out.print("\nInside inspector: " + obj + " (recursive = "+recursive+")");
		
		inspectClass(null, ObjClass);
		try { inspectConstructors(null, ObjClass);}
		catch (Exception e) { }
		inspectMethods(null, ObjClass);
		inspectFields(obj, ObjClass, recursive);
		System.out.print("\n\n------ FINISHED INSPECTING "+ObjClass.getName()+" ------");
		//Traverse hierarchy
		if(ObjClass.getSuperclass() != null)
			inspectSuperclass(obj, ObjClass, recursive);
		if(ObjClass.getInterfaces().length > 0)
			inspectSuperInterfaces(obj, ObjClass, recursive);
		
		objectsAlreadyInspected.addElement(ObjClass);
		objectsAlreadyInspected.addElement(obj);

		System.out.println();
		
    }
	/********************************************************************************************/
	private void inspectClass(Object obj, Class ObjClass)
	{
		//Inspect and print class name
		System.out.println("\nCLASS: "+ ObjClass.getSimpleName());
		//Inspect and print immediate superclass
		if (ObjClass.getSuperclass() == null)
			System.out.println("  - Does not have a superclass.");
		else 
			System.out.println("  - Immediate superclass: "+ ObjClass.getSuperclass().getName());
		//Inspect and print implemented interfaces
		if (ObjClass.getInterfaces().length == 0)
			System.out.print("  - Does not implement any interfaces.");
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
			try {
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
			}catch (Exception e) {System.out.print("\nConstructor: Cannot access constructor.");}
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
				System.out.print("\nMethod: ");
				try{m.setAccessible(true);}
				catch(Exception e) {System.out.println(" Cannot access method.");}
				
				//Inspect and print name
				System.out.println(m.getName());
				
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
				System.out.print("  - Modifiers: ");
				try {
					System.out.print(Modifier.toString(m.getModifiers()));
				}catch(Exception e) {System.out.print(" Cannot access modifiers.");}
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
				System.out.print("\nField: ");
				
				try{f.setAccessible(true);}
				catch(Exception e) {System.out.println(" Cannot access field.");}
				
				//Inspect and print field name
				System.out.println(f.getName());
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
					try {
				 		System.out.print(f.get(obj) + ", Identity hash code: " + System.identityHashCode(f.get(obj))); 
						
				 		// Further, if it's not null and recursion is set to true
						if (rec == true && f.get(obj) != null) 
						{
							System.out.print("\n\n------ RECURSIVELY INSPECTING FIELD: " + f.getName()+ " ------");
							if (! objectsAlreadyInspected.contains(f.get(obj)))
							{	
								objectsAlreadyInspected.addElement(f.get(obj));
								inspect(f.get(obj), true);
							}
							else {System.out.print("\n------ Already inspected this object ------");}
						}
					}catch(Exception e) {System.out.println("Cannot access field");}
				}
			}
		}
	}

	/********************************************************************************************/
 	private void inspectSuperclass(Object obj, Class ObjClass, boolean rec) throws IllegalAccessException, IllegalArgumentException, InstantiationException 
	{
 		System.out.print("\n\n------ INSPECTING SUPERCLASS OF "+ObjClass.getName()+ ": " + ObjClass.getSuperclass().getName()+ " ------");
		if (! objectsAlreadyInspected.contains(ObjClass.getSuperclass()))
		{
			if (!Modifier.isAbstract(ObjClass.getSuperclass().getModifiers())) 
				inspect(ObjClass.getSuperclass().newInstance(), rec);
			else 
				inspectAbstract(obj, ObjClass.getSuperclass(), rec);
		}
		else {System.out.print("\n------ Already inspected this object ------");}
	}
 	
 	private void inspectSuperInterfaces(Object obj, Class ObjClass, boolean rec) throws IllegalAccessException, IllegalArgumentException, InstantiationException 
 	{ 		
 		for (int i=0; i< ObjClass.getInterfaces().length; i++)
		{
 			Object superInterface = ObjClass.getGenericInterfaces()[i];
 			System.out.print("\n\n------ INSPECTING SUPERINTERFACE OF "+ObjClass.getName()+ ": " + superInterface + " ------");
 			if (! objectsAlreadyInspected.contains(superInterface.getClass()) && !objectsAlreadyInspected.contains(superInterface))
			{
				objectsAlreadyInspected.addElement(superInterface);
				objectsAlreadyInspected.addElement(superInterface.getClass());
				inspectInterface(superInterface, superInterface.getClass(), rec);
			}
 			else {System.out.print("\n------ Already inspected this object ------");}
	 	} 
	}
 	
 	/********************************************************************************************/
 	private void handleArray(Object arrayObj)
 	{
 		try {
 		Class arrayClass = arrayObj.getClass();
 		System.out.println("\n\tComponent type: "+ arrayClass.getComponentType().getSimpleName());
 		System.out.println("\tLength: "+ Array.getLength(arrayObj));
 		
 		System.out.print("\tContents: ");
 		printArrayContents(arrayObj);}
 		catch(Exception e) { }
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

