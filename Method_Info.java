import java.util.ArrayList;

/**
 * The Method_Info class is the java bytecode representation of a the details of a MethodRef_Info object in the constant pool
 * 
 * It is used to elaborate the MethodRef_Info later in the methods[] section of the java class file format.
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u2		The access flags to the method
 * 		u2		The index of the Utf8_Info object holding the name of the method
 * 		u2		The index of the Utf8_Info object holding the name of the return type, including the library
 * 		u2		The number of attributes used by the method, which will always be 1, since only the Code attribute will be used
 * 
 * The string element of the MethodRef_Info class is optional.
 * 
 * @author Nathaniel Quan
 *
 */
public class Method_Info {
	int access_flags;								//public, public static only
	int name_index;									//index of utf8 name
	int descriptor_index;							//return type + libraries
	int attributes_count;							//only code attribute will be used
	
	/**
	 * The default constructor for the Method_Info class where it takes bytecode representation for the access flag, the index of the name of the method, 
	 * the descriptor index of the method where the return type is held, and the attribute count will be set to 1 automatically. 
	 * 
	 * 
	 * @param flags
	 * The access flag representation in java bytecode
	 *
	 * @param methodIndex
	 * The index of Utf8_Info object holding the name of the method being elaborated
	 * 
	 * @param descriptorIndex
	 * The index of the Utf8_Info object holding the return type of the method
	 */	
	//Constructor
	public Method_Info(int flags, int methodIndex, int descriptorIndex){
		access_flags = flags;
		name_index = methodIndex;
		descriptor_index = descriptorIndex;
		attributes_count = 1;									//Only using Code attribute
	}
	
	
	/**
	 * Converts the access_flags, name_index, descriptor_index, and attributes_count of the Method_Info object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form. The bytecode representation of the access_flags, name_index, descriptor_index, and attributes_count elements will 
	 * be stored in an ArrayList and that ArrayList is returned by this function.
	 * 
	 * @return
	 * Returns the bytecode of the MethodRef_Info class in an ArrayList of shorts
	 */		
	//Convert class elements to bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.addAll(TypeConverter.intToU2(access_flags));
		bytecode.addAll(TypeConverter.intToU2(name_index));
		bytecode.addAll(TypeConverter.intToU2(descriptor_index));
		bytecode.addAll(TypeConverter.intToU2(attributes_count));
		return bytecode;
	}
}
