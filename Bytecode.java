import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Bytecode class creates the natesLang.class file.
 * 
 * The class keeps track of the required functions such as the <init> and main functions
 * as well as imports the required libraries. By default, the String and Printstream libraries are
 * imported to allow for console output.
 * 
 * @author Nathaniel Quan
 *
 */
public class Bytecode {
	//Names of required classes and methods
	final String INIT = "<init>";
	final String J_OBJ = "java/lang/Object";
	final String VOID_RTYPE = "()V";
	final String THIS_CLASS = "natesLang";
	final String CODE = "Code";
	final String MAIN = "main";
	final String STRING_LIB = "([Ljava/lang/String;)V";
	final String J_SYS = "java/lang/System";
	final String OUT = "out";
	final String OUT_RTYPE = "Ljava/io/PrintStream;";
	final String PRINTLN = "println";
	final String J_PRINTSTREAM = "java/io/PrintStream";
	final String PRINT_RTYPE = "(Ljava/lang/String;)V";

	/*
	final String CONSOLE = "console";
	final String CONSOLE_RTYPE = "()Ljava/io/Console";
	final String J_CONSOLE = "java/io/Console";
	*/
	
	//Counters and constants
	static int cp_counter = 1;										//Counter for constant pool line number
	static int method_counter = 2;									//Counter for number of methods (Default 2: Init, Main)
	static int field_counter = 0;									//Counter for number of fields
	static int fn_counter = 0;										//Counter for functions index into code
	static int maxStacks = 2;										//For main function need number of stacks
	static int maxLocals = 1;										//For main function need number of local variables
	final static int global_access_flag = 0x21;		 				//to allow public and static
	final static int public_access_flag = 0x1;		 				//for public methods
	final static int public_static_access_flag = 0x9; 				//for public static methods
	
	//ArrayLists to store different sections of class file bytecodes
	static ArrayList<Short> bytecode = new ArrayList<>();
	static ArrayList<Short> constPool = new ArrayList<>();
	static ArrayList<Short> methodPool = new ArrayList<>();
	static ArrayList<Short> mainCode = new ArrayList<>();
	static ArrayList<Short> functionPool = new ArrayList<>();
	static ArrayList<Short> functionCode = new ArrayList<>();
	
	//HashMaps to keep track of line indices
	static HashMap<String, Integer> class_map = new HashMap<>();
	static HashMap<String, Integer> field_map = new HashMap<>();
	static HashMap<String, Integer> method_map = new HashMap<>();
	static HashMap<String, Integer> utf8_map = new HashMap<>();
	
	/**
	 * The default constructor for Bytecode.
	 * 
	 * The Bytecode constructor initializes the required bytecode upon being instantiated.
	 * 
	 * Items automatically added to the bytecode:
	 * 		- Magic Number
	 * 		- Major Version
	 * 		- Minor Version
	 * 
	 * Items added to a temporary constant pool ArrayList
	 * 		- <init> method reference
	 * 		- PrintStream method reference
	 * 		- natesLang class
	 * 		- main class
	 * 		- String library
	 */
	public Bytecode(){	
		//Add magic 
		bytecode.addAll(TypeConverter.intToU2(0xCAFE));
		bytecode.addAll(TypeConverter.intToU2(0xBABE));
		
		//Add Minor and Major Versions
		bytecode.addAll(TypeConverter.intToU2(0x0));
		bytecode.addAll(TypeConverter.intToU2(0x34));
		
		//Initialize Constant Pool
		methodToCP(INIT, J_OBJ, VOID_RTYPE);
		classToCP(THIS_CLASS);
		utf8ToCP(CODE);
		utf8ToCP(MAIN);
		utf8ToCP(STRING_LIB);
		fieldToCP(OUT, J_SYS, OUT_RTYPE);
		methodToCP(PRINTLN, J_PRINTSTREAM, PRINT_RTYPE);
		System.out.println();
		System.out.println();
	}

	/**
	 * getInitCode returns the code for <init>, which remains the same for all programs
	 * that will be created under natesLang language.
	 * 
	 * @return Bytecode equivalent to aload_0, invokespecial #1, return
	 */
	//Code for the required init function
	private ArrayList<Short> getInitCode(){
		ArrayList<Short> code = new ArrayList<>();
		code.add((short)0x2A);						//aload_0
		code.add((short)0xB7);						//invokespecial #1
		code.add((short)0x0);						
		code.add((short)0x1);
		code.add((short)0xB1);						//return
		return code;
	}
	
	/**
	 * writeFile creates or replaces a file named natesLang.class in the working directory.
	 * The created file will be a binary file with the bytecode written to the file. 
	 * 
	 * Output: natesLang.class
	 * 
	 * @throws IOException if file cannot be created or replaced
	 */
	
	//Open file and write bytecode to file
	private void writeFile() throws IOException{
		//Open a file for output
		DataOutputStream os = new DataOutputStream(new FileOutputStream("C:/Users/Bohokiyo/Desktop/natesLang.class"));
		
		//Write each short as a byte
		for (short i: bytecode)
			os.writeByte(i);
		
		//Close file
		os.close();		
	}
	
	/**
	 * createUtf8 is a function that will create an instance of a Utf8_Info object and add it to the constant pool.
	 * 
	 * The constant pool index for the object is also added to a HashMap of Utf8 objects for future referencing by
	 * the parameter value.
	 * 
	 * @param s
	 * s is the String that will hold the value of the UT8 class. 
	 * 
	 * @return Utf8_Info object
	 */

	//Create a Utf8_Info constant pool object and add its index to a hashmap	
	private Utf8_Info createUtf8(String s){
		utf8_map.put(s, cp_counter);
		System.out.println("Utf8: " + s + " at " + cp_counter);
		Utf8_Info u = new Utf8_Info(s);
		return u;
	}
	
	/**
	 * createClass is a function that will create a class Object and add it to the constant pool.
	 * 
	 * This function will also add the constant pool index, cp_counter, to a HashMap along with the
	 * className for future referencing.
	 * 
	 * @param name
	 * name is a String that will hold the name of the class
	 * @param utf8Index
	 * utf8 Index holds the index of the Utf8_Info object that will be referenced when using the newly
	 * created class.
	 * @return
	 */
	
	//Create a Class_Info constant pool object and add its index to a hashmap
	private Class_Info createClass(String name, int utf8Index){
		class_map.put(name, cp_counter);
		System.out.println("Class: " + name + " at " + cp_counter);
		Class_Info c = new Class_Info(utf8Index);
		return c;
	}

	/**
	 * methodToCP is a function that will create a method and its corresponding dependencies and add them to the constant pool.
	 * 
	 * This function will add to the constant pool at first unused index, i:
	 * 		#i		Method Reference with methodName
	 * 		#i+1	Class Reference with className that will be called by method
	 * 		#i+2	Utf8 Reference that will store the above className
	 * 		#i+3	Name and Type Reference, which will store the indices for the method name and method type
	 * 		#i+4	Utf8 Reference that will store methodName
	 * 		#i+5	Utf8 Reference that will store the method type
	 * 
	 * The last used index will be incremented by 6 by the end of the function.
	 * 
	 * The method will bad added to a HashMap for methods, using the methodName and index of when the Method is created. 
	 *
	 * @param methodName
	 * methodName is the name of the method
	 * 
	 * @param className
	 * className is the name of the class
	 * 
	 * @param returnType
	 * returnType is the return type of the method including any libraries necessary.
	 * For example:
	 * 		()V is a void return type
	 * 		([Ljava/lang/String;)V is a void return type with the String library used in method
	 */
	
	//Create a new method reference and all its dependencies and add their bytecode to constant pool
	public void methodToCP(String methodName, String className, String returnType){
		System.out.println("Method: " + methodName + " at " + cp_counter);
		method_map.put(methodName, cp_counter);

		//Create const pool objects
		MethodRef_Info m = new MethodRef_Info(cp_counter+1, cp_counter+3);		//#1 Methodref #2, #4
		cp_counter = cp_counter + 1;											//Next line
		Class_Info c = createClass(className, cp_counter+1);					//#2 Class #3
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info cName = createUtf8(className);								//#3 Utf8 className
		cp_counter = cp_counter + 1;											//Next line
		NameAndType_Info n = new NameAndType_Info(cp_counter+1, cp_counter+2);	//#4 NameAndType #5, #6
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info mName = createUtf8(methodName);								//#5 Utf8 methodName
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info rType = createUtf8(returnType);								//#6 Utf8 returnType
		cp_counter = cp_counter + 1;											//Next line
		
		//Add object bytecodes to constant pool
		constPool.addAll(m.getBytecode());
		constPool.addAll(c.getBytecode());
		constPool.addAll(cName.getBytecode());
		constPool.addAll(n.getBytecode());
		constPool.addAll(mName.getBytecode());
		constPool.addAll(rType.getBytecode());
	}
	
	/**
	 * fieldToCP is a function that will create a new field reference ad its corresponding dependencies and add it to the constant pool.
	 * 
	 * This function will add to the constant pool at first unused index, i:
	 * 		#i		Field Reference with fieldName
	 * 		#i+1	Class Reference with className that will be called by field
	 * 		#i+2	Utf8 Reference that will store the above className
	 * 		#i+3	Name and Type Reference, which will store the indices for the field name and type
	 * 		#i+4	Utf8 Reference that will store fieldName
	 * 		#i+5	Utf8 Reference that will store the field type
	 * 
	 * The last used index will be incremented by 6 by the end of the function.
	 * 
	 * The field will be added to the HashMap with the index of when it was created for future referencing.
	 * 
	 *  
	 * @param fieldName
	 * fieldName is the name of the field
	 * 
	 * @param className
	 * className is the name of the dependent class
	 * 
	 * @param returnType
	 * returnType is the return type of the field, usually a reference to a java library
	 */
	
	//Create a new field reference and all its dependencies and add their bytecode to constant pool
	public void fieldToCP(String fieldName, String className, String returnType){
		System.out.println("Field: " + fieldName + " at " + cp_counter);
		field_map.put(fieldName, cp_counter);

		//Create const pool objects
		FieldRef_Info m = new FieldRef_Info(cp_counter+1, cp_counter+3);		//#1 Fieldref #2, #4
		cp_counter = cp_counter + 1;											//Next line
		Class_Info c = createClass(className, cp_counter+1);					//#2 Class #3
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info cName = createUtf8(className);								//#3 Utf8 className
		cp_counter = cp_counter + 1;											//Next line
		NameAndType_Info n = new NameAndType_Info(cp_counter+1, cp_counter+2);	//#4 NameAndType #5, #6
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info fName = createUtf8(fieldName);								//#5 Utf8 fieldName
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info rType = createUtf8(returnType);								//#6 Utf8 returnType
		cp_counter = cp_counter + 1;											//Next line
		
		//Add object bytecodes to constant pool
		constPool.addAll(m.getBytecode());
		constPool.addAll(c.getBytecode());
		constPool.addAll(cName.getBytecode());
		constPool.addAll(n.getBytecode());
		constPool.addAll(fName.getBytecode());
		constPool.addAll(rType.getBytecode());
	}	
	
	/**
	 * classToCP is a function that will create a class and its dependencies and add them to the constant pool.
	 * 
	 * The class will be created by calling the createClass function. Its dependent Utf8_Info object will be created
	 * by createUtf8 function. Thus, both will have HashMap references in their respective HashMaps to their line
	 * number in the constant pool.
	 *  
	 * The class will be created at the first unused index, i, along with its dependency where:
	 * 		#i		Class Info Object with className and index of i+1 for index of Utf8_Info object
	 * 		#i+1	Utf8_Info object to store name of the class
	 * 
	 * Overall, the first unused index will be incremented by 2.
	 *
	 * NOTE: classToCP should not be used to create classes that are part of methods or fields, which automatically generate
	 * the class when their functions, methodToCP and fieldToCP, are called.
	 * 
	 * @param className
	 * className is the name of the class
	 */
	
	//Create a new class and add its bytecode to constant pool
	public void classToCP(String className){
		//Create const pool objects
		Class_Info c = createClass(className, cp_counter+1);					//#1 Class #2
		cp_counter = cp_counter + 1;											//Next line
		Utf8_Info u = createUtf8(className);									//#2 Utf8 className
		cp_counter = cp_counter + 1;											//Next line
		
		//Add object bytecodes to constant pool
		constPool.addAll(c.getBytecode());
		constPool.addAll(u.getBytecode());
	}
	
	/**
	 * utf8ToCP will create a UTF8 object and add it to the constant pool.
	 * 
	 * The created Utf8_Info object wlll be created using Utf8_Info, so the String stored and its index in the constant pool
	 * will be automatically added. The first unused index in the constant pool will be incremented by one to account for the
	 * newly created Utf8_Info object.
	 * 
	 * NOTE: utf8ToCP should not be used to create a dependent class. Methods, Fields, and Classes all have respective functions,
	 * methodToCP, fieldToCP, and classToCP, that will automatically create their dependent Utf8_Info objects.
	 * 
	 * @param s
	 * s is the value that should be stored by the Utf8_Info object
	 */
	
	//Create a new utf8 object and add its bytecode to constant pool
	public void utf8ToCP(String s){
		Utf8_Info u = createUtf8(s);											//Create new utf8 object
		cp_counter = cp_counter + 1;											//Next line
		constPool.addAll(u.getBytecode());										//Add to constant pool
	}
	
	/**
	 * stringToCP will create a String_Info object its dependent Utf8_Info object and add them to the constant pool.
	 * 
	 * Using the first unused index in constant pool, i, the function will add to the constant pool as such:
	 * 		#i		String_Info object with index to the Utf8_Info
	 * 		#i+1	Utf8_Info object that will hold the name/value of the string
	 * 
	 * The first unused index in constant pool is incremented by 2 by the end of the function.
	 *  
	 * @param s
	 * s is the value of the string 
	 */
	
	public static void stringToCP(String s){
		utf8_map.put(s, cp_counter);
		String_Info si = new String_Info(cp_counter+1);
		cp_counter = cp_counter + 1;
		Utf8_Info u = new Utf8_Info(s);
		cp_counter = cp_counter + 1;
		constPool.addAll(si.getBytecode());
		constPool.addAll(u.getBytecode());	
	}
	
	/**
	 * methodToMP is a function that produces a Method_Info object and adds it to the methodPool. 
	 * 
	 * The Method_Info contains the header to the method, such as the type of access the function has, which
	 * is always public static, and the return type for the function. The methodToMP function creates the
	 * Method_Info by gathering the indices of the methodName and the descriptor from their respective HashMaps
	 * 
	 * @param access
	 * access for the functions in natesLang should all be public static
	 * 
	 * @param methodName
	 * the methodName is passed in to retreive the method index from the constant pool
	 * 
	 * @param descriptor
	 * the descriptor is the return type
	 */
	
	//Add method to method pool, required methods are init and main
	public void methodToMP(int access, String methodName, String descriptor){
		Method_Info m = new Method_Info(access, utf8_map.get(methodName), utf8_map.get(descriptor));
		methodPool.addAll(m.getBytecode()); 
	}
	
	/**
	 * codeToMP creates a Code_Attribute object and stores it in the methodPool ArrayList.
	 * 
	 * The Code_Attribute contains the details to the method, such as the number of stack variables, 
	 * local variables, the code and attributes. To simplify the Java bytecode, attributes have been
	 * omitted, so there will be no lineNumber attribute that typically comes with Java class files
	 * 
	 * @param stacks
	 * The number of variables added to the stack by Java bytecode 
	 * 
	 * @param locals
	 * The number of local variables used by the Java bytecode
	 * 
	 * @param code
	 * The translated java bytecode for a function or class
	 */
	//Write code to method pool
	public void codeToMP(int stacks, int locals, ArrayList<Short> code){
		Code_Attribute c = new Code_Attribute(utf8_map.get("Code"), stacks, locals, code);
		methodPool.addAll(c.getBytecode());
	}	
	
	/**
	 * print is a function in Java bytecode equivalent to Java's println() function
	 * 
	 * This function is the primary Java bytecode function used in natesLang. The bytecode
	 * is the equivalent to the following java bytecode sequence:
	 * 		getstatic		<u2 constant pool index of out field from System.out>
	 * 		ldc				<u1 constant pool index of string to be printed>
	 * 		invokevirtual	<u2 constant pool index of the println function>
	 * 
	 * This function will add the string to be printed into the constant pool, then reference
	 * the constant pool indices of the string, method, and function related to console output.
	 * 
	 * @param printThis
	 * This is the string to output to console
	 * 
	 */
	public void print(String printThis){
		//To print, in bytecode:
		//	getstatic 		<index of field holding printstream>
		//	ldc				<index of string>
		//	invokevirtual 	<index of method holding printstream>
		int fieldIndex = field_map.get(OUT);
		stringToCP(printThis);
		int stringIndex = utf8_map.get(printThis);
		int methodIndex = method_map.get(PRINTLN);
				
		mainCode.add((short)0xB2);			//B2 - bytecode representation of getstatic
		mainCode.addAll(TypeConverter.intToU2(fieldIndex));
		mainCode.add((short)0x12);			//12 - bytecode representation of ldc
		mainCode.add(TypeConverter.intToU1(stringIndex));
		mainCode.add((short)0xB6);			//B6 - bytecode representation of invokevirtual
		mainCode.addAll(TypeConverter.intToU2(methodIndex));
	}
	
	/**
	 * close is used to add all the pools and necessary information into the bytecode.
	 * 
	 * This function does the following to the bytecode:
	 * 		- Add the number of constants in constant pool
	 * 		- Add constant pool
	 * 		- Add the global access flag
	 * 		- Add the current class (natesLang)
	 * 		- Add the super class
	 * 		- Add the number of interfaces (0) and consequently ignores interfaces[] since there are no interfaces
	 * 		- Add the number of fields (0) and consequently ignores the fields[] since there are no fields
	 * 		- Add the number of methods (default: 2) and their corresponding code details
	 * 		- Add the number of class attributes (0), which will be none since the sourcefile and linenumbers are omitted from class file
	 * 
	 * After adding all the items to bytecode, write the bytecode to file.
	 * 
	 * Outputs: natesLang.class
	 * 	  
	 * @throws IOException
	 * IOException in the case that file cannot be replaced or created.
	 */
	
	public void close() throws IOException{
		//Add constant pool to byte code
		bytecode.addAll(TypeConverter.intToU2(cp_counter));	//Constant pool size
		bytecode.addAll(constPool);							//Constant pool data
		
		//Add class access flag
		bytecode.addAll(TypeConverter.intToU2(global_access_flag));
		
		//Add class references
		bytecode.addAll(TypeConverter.intToU2(class_map.get(THIS_CLASS)));		//The current class
		bytecode.addAll(TypeConverter.intToU2(class_map.get(J_OBJ)));			//Super class
		
		//Add interface count and interfaces[]
		bytecode.addAll(TypeConverter.intToU2(0));								//No interfaces
		
		//Add fields count and fields[]
		bytecode.addAll(TypeConverter.intToU2(field_counter));					//Number of fields
		System.out.println("#Fields: " + field_counter);
		
		//Add methods count and methods[]
		bytecode.addAll(TypeConverter.intToU2(method_counter));
		System.out.println("#Methods: " + method_counter);
		
		//Init method
		methodToMP(public_access_flag, INIT, VOID_RTYPE);
		codeToMP(1, 1, getInitCode());
		
		//Main method
		methodToMP(public_static_access_flag, MAIN, STRING_LIB);
		
		//Add method pool to bytecode
		mainCode.add((short)0xB1);			//B1 - bytecode representation of return
		codeToMP(maxStacks, maxLocals, mainCode);
		bytecode.addAll(methodPool);
		
		//Add class attributes count
		bytecode.addAll(TypeConverter.intToU2(0));									//No class file attributes
		
		writeFile();
	}

	
}
