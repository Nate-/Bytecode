import java.util.ArrayList;

/**
 * The FieldRef_Info class is the java bytecode representation of a field in the constant pool, that will be referenced.
 * 
 * It is used for importing java libraries, such as PrintStream.
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u1		The constant pool tag for a field reference object, which will always be 9
 * 		u2		The index of the class that the field applies to
 * 		u2		The index of a NameAndType object that holds the indices to the name of the field and the type of the field
 * 
 * The string element of the FieldRef_Info class is optional.
 * 
 * @author Nathaniel Quan
 *
 */
public class FieldRef_Info {
	short tag;								//u1 - constant pool tag to identify as method reference
	int index;								//u2 - index of class
	int name_and_type_index;				//u2 - index of NameAndType object that will store method name and type
	String name;
	
	/**
	 * The default constructor for the FieldRef_Info class where it takes the index of its dependent class and NameAndType object is passed in as a parameter.
	 * 
	 * The tag of a FieldRef_Info object will always be 9, since that is the reference number identifying it as a FieldRef_Info
	 * object in the constant pool in bytecode.
	 * 
	 * @param i
	 * The index of the Utf8_Info object holding the name of the class
	 *
	 * @param n
	 * The index of the NameAndType_Info object holding the indices to the Utf8_Info objects for the name and the type for the field
	 */
	
	public FieldRef_Info(int i, int n){
		tag = (short) 9;					//Field tag for constant pool is 9
		index = i;
		name_and_type_index = n;
	}

	/**
	 * An additional constructor for the FieldRef_Info class where it takes the optional parameter of the field name
	 * 
	 * The tag of a FieldRef_Info object will always be 9, since that is the reference number identifying it as a FieldRef_Info
	 * object in the constant pool in bytecode.
	 * 
	 * @param i
	 * The index of the Utf8_Info object holding the name of the class
	 *
	 * @param n
	 * The index of the NameAndType_Info object holding the indices to the Utf8_Info objects for the name and the type for the field
	 * 
	 * @param s
	 * The name of the FieldRef_Info object
	 */
	
	public FieldRef_Info(int i, int n, String s){
		tag = (short) 10;					//Method tag for constant pool is 10
		index = i;
		name_and_type_index = n;
		name = s;
	}

	/**
	 * Converts the tag and name_index of FieldRef_Info object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form. The bytecode representation of the tag, index, and name_and_type_index will be stored in an
	 * ArrayList and that ArrayList is returned by this function.
	 * 
	 * @return
	 * Returns the bytecode of the FieldRef_Info class in an ArrayList of shorts
	 */
	//Convert class elements to bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.add(tag);
		bytecode.addAll(TypeConverter.intToU2(index));
		bytecode.addAll(TypeConverter.intToU2(name_and_type_index));
		return bytecode;
	}
}
