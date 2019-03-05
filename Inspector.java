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
	inspectMethods(obj, ObjClass, objectsToInspect);
	
//	inspectFields(obj, ObjClass,objectsToInspect);
	
//	if(recursive)
//	    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
	   
    }
	
	private void inspectClass(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		System.out.println("\nCLASS: "+ ObjClass.getName());
		System.out.println("Immediate superclass: "+ ObjClass.getSuperclass().getName());
		if (ObjClass.getInterfaces().length == 0)
			System.out.println("Does not implement any interfaces.");
		else {
			System.out.println("Implements: ");
			for (int i=0; i< ObjClass.getInterfaces().length; i++)
			{
				System.out.println("\t   "+ObjClass.getInterfaces()[i].getName());
			}
		}
		
		
	}
	
	private void inspectMethods(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		if(ObjClass.getDeclaredMethods().length >= 1)
		{
			//Inspect each method
			for (int i=0; i<ObjClass.getDeclaredMethods().length; i++)
			{					
				Method m = ObjClass.getDeclaredMethods()[i];
				
				//Inspect and print name
				m.setAccessible(true);
				System.out.println("Method: "+m.getName());
				
				//Inspect and print exception types
				if (m.getExceptionTypes().length == 0)
					System.out.println("\tNo exceptions thrown.");
				else 
				{
					System.out.println("\tExceptions thrown: ");
					for (int j=0; j<m.getExceptionTypes().length; j++)
					{
						Class eType = m.getExceptionTypes()[j];
						System.out.println("\t   "+ eType.getName());
					}
				}
				//Inspect and print parameter types
				if (m.getExceptionTypes().length == 0)
					System.out.println("\tNo parameters.");
				else 
				{
					System.out.println("\tParameter types: ");
					for (int j=0; j<m.getParameterTypes().length; j++)
					{
						Class pType = m.getParameterTypes()[j];
						System.out.println("\t   "+pType.getName());
					}
				}
				//Inspect and print return type
				System.out.println("\tReturn type: ");
				System.out.println("\t   "+m.getReturnType().getName());
				//Inspect and print modifiers
				System.out.println("\tModifiers: ");
				System.out.println("\t   "+Modifier.toString(m.getModifiers()));
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
