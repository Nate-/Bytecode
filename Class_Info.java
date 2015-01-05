import java.util.ArrayList;

/**
 * The Class_Info class is an object representation of the CONST_Class_Info section of java bytecode.
 * Each Class_Info object has a tag, the index of the utf8 ojbect that will hold the name of the class,
 * and the optionally the name of the class.
 * 
 * @author Nathaniel Quan
 *
 */
public class Class_Info {
	short tag;										//u1 - constant pool tag
	int name_index;									//u2 - index of UTF8 object holding class name
	String name;									
	
	/**
	 * The default constructor for the Class_Info class where it takes the utf8 index of its name is passed in as a parameter.
	 * 
	 * The tag of a Class_Info object will always be 7, since that is the reference number identifying it as a Class_Info
	 * object in the constant pool in bytecode.
	 * 
	 * The fields of a Class_Info object are:
	 * 		u1 tag
	 * 		u2 name_index
	 * 
	 * @param i
	 * The index of the Utf8_Info object holding the name of the class
	 */
	
	//Constructor
	public Class_Info (int i){
		tag = (short) 7;							//Class tag in constant pool is 7	
		name_index = i;
	}
	
	/**
	 * The extra constructor with the added ability to remember its own class name. 
	 * 
	 * It produces the same input as the default constructor with extra functionality of remembering name. This 
	 * constructor is for possible future expansion of natesLang assuming that it may be necessary for each 
	 * object to remember its name. Also, could be useful for finding the HashMap index of the Class_Info object.
	 * 
	 * @param i
	 * The index of the Utf8_Info object holding the name of the class
	 * 
	 * @param s
	 * The name of the class
	 */
	//Constructor
	public Class_Info (int i, String s){
		tag = (short) 7;							//Class tag in constant pool is 7
		name_index = i;
		name = s;
	}
	
	/**
	 * Converts the tag and name_index of Class_Info object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form. The bytecode representation of the tag and name_index will be stored in an
	 * ArrayList and that ArrayList is returned by this function.
	 * 
	 * @return
	 * Returns the bytecode of the Class_Info class in an ArrayList of shorts
	 */
	//Convert class elements to bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.add(tag);
		bytecode.addAll(TypeConverter.intToU2(name_index));
		return bytecode;
	}
}
