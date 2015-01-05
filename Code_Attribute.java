import java.util.ArrayList;

/**
 * The Code_Attribute class is the javabyte code repesentation of a code segment.
 * 
 * The elements that are tracked by this class are the following (listed as size and name of element):
 * 		u2		attribute_name_index
 * 		u4		attribute_length
 * 		u2 		max_stacks
 * 		u2		max_locals
 * 		u4		code_length
 * 		u1[]	the bytecode for the method/function
 * 		u2		exception table length (this will always be 0, since the exception table is not implemented)
 * 		u2		attributes_count (this will always be 0, since lineNumber is not tracked in natesLang)
 * 
 * The Code_Attribute class is always used in conjunction with the Method_Info class. The Method_Info header will have
 * its details elaborated by the Code_Attribute class.
 * 
 * @author Nathaniel Quan
 *
 */
public class Code_Attribute {
	int attribute_name_index;							//u2
	int attribute_length;								//u4
	int max_stacks;										//u2
	int max_locals;										//u2
	int code_length;									//u4
	ArrayList<Short> code = new ArrayList<>();			//variable u1
	int exception_table_length;							//u2
	int attributes_count;								//u2
	
	/**
	 * The default constructor for the Code_Attribute class.
	 * 
	 * This constructor will require the values for index of the Utf8_Object for "Code", the maximum
	 * number of stacks, the maximum number of local variables, and the actual function/method code in
	 * bytecode format. The attribute length and code length are calculated from the provided values.
	 * The exception table length and attribute count will both be 0, because the exception table isn't 
	 * implemented and the optional lineNumber is omitted
	 *  
	 * @param index
	 * The index of the Utf8_Info for "Code"
	 * 
	 * @param stacks
	 * The maximum number of stacks for a function/method
	 * 
	 * @param locals
	 * The maximum local variables to be used by a function/method
	 * 
	 * @param c
	 * The bytecode representation of code
	 */
	//Constructor
	public Code_Attribute(int index, int stacks, int locals, ArrayList<Short> c){
		attribute_name_index = index;
		attribute_length = 12 + c.size();				//Tally up all the u2 and u4s to get 18, then add size of code
		max_stacks = stacks;
		max_locals = locals;
		code = c;
		code_length = c.size();
		exception_table_length = 0;
		attributes_count = 0;		
	}

	/**
	 * Converts elements of Code_Attribute object into java bytecode.
	 * 
	 * This requires TypeConverter in order to convert an integer into a 2-element short array in
	 * ArrayList form for the following elements:
	 * 		- attribute_name_length
	 * 		- max_stacks
	 * 		- max_locals
	 * 		- exception_table length
	 * 		- attributes_count
	 * 
	 * It also requires TypeConverter in order to convert an integer into a 4-element short array in
	 * ArrayList form for the following elements:
	 * 		- attribute_length
	 * 		- code_length
	 * 
	 * Since the function/method code will already be in bytecode, it will not need to be converted. 
	 *  
	 * 
	 * @return
	 * Returns the bytecode of the Code_Attribute class in an ArrayList of shorts
	 */	
	//Converts the attributes of the class into bytecode
	public ArrayList<Short> getBytecode(){
		ArrayList<Short> bytecode = new ArrayList<>();
		bytecode.addAll(TypeConverter.intToU2(attribute_name_index));
		bytecode.addAll(TypeConverter.intToU4(attribute_length));
		bytecode.addAll(TypeConverter.intToU2(max_stacks));
		bytecode.addAll(TypeConverter.intToU2(max_locals));
		bytecode.addAll(TypeConverter.intToU4(code_length));
		bytecode.addAll(code);
		bytecode.addAll(TypeConverter.intToU2(exception_table_length));
		bytecode.addAll(TypeConverter.intToU2(attributes_count));
		return bytecode;
	}
}
