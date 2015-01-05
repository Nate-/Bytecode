import java.util.ArrayList;

/**
 * The Utf8_Info class is the java bytecode representation of string values, such as names and string values.
 * 
 * The Utf8_Info class is the fundamental building block of the java bytecode constant pool. It is the only 
 * unit that can be used by itself. It is commonly used in conjection with MethodRef_Info, FieldRef_Info,
 * Class_Info, and String_Info to store their values, since they primarily store indices.
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u1		The constant pool tag for a Utf8 object which will always be 1
 * 		u2		The length of the string, which can the string representing the name of a method, class, or field.
 * 		u1[]	The ASCII values of the character array, with each character being represented by an unsigned byte value
 * 
 * @author Nathaniel Quan
 *
 */
public class Utf8_Info {
	Short tag;					//u1 - constant pool tag
	Integer length;				//u2 - length of the string
	String value;				//u1*- string that will be converted to ASCII char array
	
	/**
	 * The default constructor for a Utf8_Info object.
	 * 
	 * This constructor requires the string that it will be stored. From the string,
	 * the length is automatically calculated. The tag is assigned the value of 1 to
	 * represent a Utf8_Info object in the constant pool. 
	 * 
	 * @param s
	 * The string to be stored in the constant pool
	 */
	//Constructor
	Utf8_Info(String s){
		tag = (short) 1;		//Utf8 tag in contant pool is 1
		value = s;				//Holds string
		length = s.length();	//Length of string
	}
	
	/**
	 * Converts the tag, length, and value of a string to bytecode to be represented in the constant pool.
	 * 
	 * This requires TypeConverter in order to convert the integer value of length into a 2-element short array 
	 * ArrayList representation. TypeConverter is also required to convert the string into an array of ASCII
	 * values for each letter that is represented. 
	 * 
	 * @return
	 * Returns the bytecode of the Utf8_Info class in an ArrayList of shorts
	 */	
	//Convert class elements to bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.add(tag);
		bytecode.addAll(TypeConverter.intToU2(length));
		bytecode.addAll(TypeConverter.stringToByte(value));
		return bytecode;
	}
}
