import java.util.*;
import java.lang.reflect.*;

public class Inspector {
	public Inspector() { }
	
	public void inspect(Object obj, boolean recursive)
    {
	Vector objectsToInspect = new Vector();
	Class ObjClass = obj.getClass();

//	System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
	
	//inspect the current class
	
	inspectClass(obj, ObjClass, objectsToInspect);
	inspectFields(obj, ObjClass,objectsToInspect, recursive);
	inspectMethods(obj, ObjClass, objectsToInspect);
	
	
//	if(recursive)
//	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
	   
    }
	
	private void inspectClass(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		System.out.println("\nCLASS: "+ ObjClass.getName());
		System.out.println("  - Immediate superclass: \n    "+ ObjClass.getSuperclass().getName());
		if (ObjClass.getInterfaces().length == 0)
			System.out.println("  - Does not implement any interfaces.");
		else {
			System.out.println("  - Implements: ");
			for (int i=0; i< ObjClass.getInterfaces().length; i++)
			{
				System.out.println("    "+ObjClass.getInterfaces()[i].getName());
			}
		}
	}
	
	private void inspectMethods(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		if(ObjClass.getDeclaredMethods().length == 0)
			System.out.println("No methods");
		else
		{
			//Inspect each method
			for (int i=0; i<ObjClass.getDeclaredMethods().length; i++)
			{					
				Method m = ObjClass.getDeclaredMethods()[i];
				m.setAccessible(true);
				
				//Inspect and print name
				System.out.println("Method: "+m.getName());
				
				//Inspect and print exception types
				if (m.getExceptionTypes().length == 0)
					System.out.println("  - No exceptions thrown.");
				else 
				{
					System.out.println("  - Exceptions thrown: ");
					for (int j=0; j<m.getExceptionTypes().length; j++)
					{
						Class eType = m.getExceptionTypes()[j];
						System.out.println("    "+ eType.getName());
					}
				}
				//Inspect and print parameter types
				if (m.getExceptionTypes().length == 0)
					System.out.println("  - No parameters.");
				else 
				{
					System.out.println("  - Parameter types: ");
					for (int j=0; j<m.getParameterTypes().length; j++)
					{
						Class pType = m.getParameterTypes()[j];
						System.out.println("    "+pType.getName());
					}
				}
				//Inspect and print return type
				System.out.println("  - Return type: ");
				System.out.println("    "+m.getReturnType().getName());
				//Inspect and print modifiers
				System.out.println("  - Modifiers: ");
				System.out.println("    "+Modifier.toString(m.getModifiers()));
			}
		}
	}
	
	private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect, boolean rec)
	{
		if(ObjClass.getDeclaredFields().length == 0)
			System.out.println("No fields.");
		else
		{
			for (int i=0; i<ObjClass.getDeclaredFields().length; i++)
			{
				Field f = ObjClass.getDeclaredFields()[i];
				f.setAccessible(true);
				
				//Inspect and print field name
				System.out.println("Field: "+f.getName());
				//Inspect and print field type
				System.out.println("  - Type: ");
				System.out.println("    "+ f.getType().getName());
				//Inspect and print field modifiers
				System.out.println("  - Modifiers: ");
				System.out.println("    "+Modifier.toString(f.getModifiers()));
			}
		}
	}
	
/*	private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)  
    {
	
	if(ObjClass.getDeclaredFields().length >= 1)
	    {
		Field f = ObjClass.getDeclaredFields()[0];
		
		f.setAccessible(true);
		
		if(! f.getType().isPrimitive() ) 
		    objectsToInspect.addElement( f );
		
		try
		    {
			
			System.out.println("Field: " + f.getName() + " = " + f.get(obj));
		    }
		catch(Exception e) {}    
	    }

	if(ObjClass.getSuperclass() != null)
	    inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
    }*/
}
