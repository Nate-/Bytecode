import java.util.ArrayList;

/**
 * The String_Info class is the java bytecode representation of a string in the constant pool that will be referenced.
 * 
 * This class acts similar to a Class_Info object in that it stores the tag representing its type in the constant pool and
 * the index to the Utf8_Info object holding the value of the string. 
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u1		The constant pool tag for a method reference object, which will always be 8
 * 		u2		The index of the Utf8_Info object holding the contents of the string
 * 
 * A String_Info object will always be used in conjunction with a Utf8_Info object.
 * 
 * @author Nathaniel Quan
 *
 */
public class String_Info {
	short tag;					//string tag is 8 in constant pool
	int index;					//utf8 index reference

	/**
	 * This is the default constructor for a string object that will contain the index of a Utf8_Info object.
	 * 
	 * The tag of a String_Info object is identified with the tag value of 8 in the constant pool. The index
	 * of the Utf8_Info object containing the value of the string being stored in the constant pool by a 
	 * String_Info object is passed as the parameter. 
	 *   
	 * @param i
	 * The index of the Utf8_Info object containing the value of the string
	 */
	public String_Info(int i){
		tag = 0x8;
		index = i;
	}

	/**
	 * Converts the tag and index of String_Info object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form. The bytecode representation of the tag and index will be stored in an
	 * ArrayList and that ArrayList is returned by this function.
	 * 
	 * @return
	 * Returns the bytecode of the String_Info class in an ArrayList of shorts
	 */	
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.add(tag);
		bytecode.addAll(TypeConverter.intToU2(index));
		return bytecode;
	}
}
