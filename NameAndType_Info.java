import java.util.ArrayList;

/**
 * The NameAndType_Info class stores the name and type indices of Utf8_Info classes of a method or field.
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u1		The constant pool tag for a Name And Type object, which will always be 12
 * 		u2		The index a UTF8_Info object holding the name of the method or field creating this class
 * 		u2		The index a UTF8_Info object holding the type of the method or field creating this class 
 * 
 * This class is never created alone and must be used with either a MethodRef_Info class or a FieldRef_Info class.
 * 
 * Typically, this class is used within a function that creates the MethodRef_Info or FieldRef_Info objects.
 * If the index of the MethodRef_Info or FieldRef_Info is i, then the index of the NameAndType_Info object will be i+4
 * 
 * @author Nathaniel Quan
 *
 */
public class NameAndType_Info {
	short tag;								//u1 - constant pool tag
	int nameIndex;							//u2 - index of UTF8 object holding name
	int typeIndex;							//u2 - index of UTF8 object holding return type

	
	/**
	 * This is the default constructor for the NameAndType_Info class.
	 * 
	 * The tag for a NameAndType_Info object is always 12 in the constant pool.
	 * The index of a method or field name and the index of the method or field type is passed in as
	 * parameters.
	 *  
	 * @param n
	 * The index of the Utf8_Info object holding the 
	 * 
	 * @param t
	 */
	//Constructor
	public NameAndType_Info(int n, int t){
		tag = (short) 12;
		nameIndex = n;
		typeIndex = t;
	}
	
	/**
	 * Converts the tag, nameIndex, and typeIndex of a NameAndType_Info object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form. This applies to the nameIndex and typeIndex elements.
	 * 
	 * The bytecode representation of the tag, nameIndex, and typeIndex will be stored in an
	 * ArrayList and that ArrayList is returned by this function.
	 * 
	 * @return
	 * Returns the bytecode of the MethodRef_Info class in an ArrayList of shorts
	 */	
	//Convert class elements to bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.add(tag);
		bytecode.addAll(TypeConverter.intToU2(nameIndex));
		bytecode.addAll(TypeConverter.intToU2(typeIndex));
		return bytecode;
	}
}
