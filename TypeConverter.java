import java.util.ArrayList;

/**
 * The TypeConverter class used to convert integers and strings into shorts to be printed as bytes for bytecode.
 * 
 * There is no need to instantiate this class, since all the function of this class are static.
 * 
 * @author Nathaniel Quan
 *
 */
public class TypeConverter {

	/**
	 * intToU1 is a function that converts an integer to an unsigned byte in the form of a short
	 * 
	 * This function is for all values of integers assumed to be within the size constraints of a short.
	 * It is required for some java bytecode instructions to have a constant pool index to be represented
	 * by a single unsigned byte, for example, ldc.
	 *   
	 * @param x
	 * The integer value to be converted to an unsigned byte
	 * 
	 * @return
	 * Returns a short representing an integer
	 */
	public static short intToU1(int x){
		return (short)(x & 0xFF);
	}
	
	/**
	 * intToU2 is a function that converts an integer to two unsigned bytes in the form of shorts
	 * 
	 * This is the standard function used for most of java bytecode conversions, since most indices
	 * are represented by two unsigned bytes.
	 * 
	 * @param x
	 * The integer value to be converted to two unsigned bytes
	 * 
	 * @return
	 * Returns an ArrayList of 2-length representing an integer
	 */
	//Converts integer to unsigned 2 bytes
	public static ArrayList<Short> intToU2(int x){
		ArrayList<Short> result = new ArrayList<>();
		result.add((short)((x>>8) & 0xFF));				//Shift int to get first half
		result.add((short)(x & 0xFF));					//Get second half of int
		return result;
		
	}
	
	/**
	 * intToU4 is a function that converts an integer to four unsigned bytes in the form of shorts.
	 * 
	 * This function is used to convert code_length and attribute_length into U4.
	 * 
	 * @param x
	 * The integer value to be converted into four unsigned bytes
	 * 
	 * @return
	 * Returns an ArrayList of 4-length representing an integer
	 */
	//Converts integer to unsigned 4 bytes
	public static ArrayList<Short> intToU4(int x){
		ArrayList<Short> result = new ArrayList<>();
		result.add((short)((x>>24) & 0xFF));			//Fill first byte
		result.add((short)((x>>16) & 0xFF));			//Fill second byte
		result.add((short)((x>>8) & 0xFF));				//Fill third byte
		result.add((short)(x & 0xFF));					//Fill fourth byte	
		return result;
	}
	
	/**
	 * stringToByte converts a string into an array of ASCII values for each character int the string.
	 * 
	 * This function is used by Utf8_Info during string to bytecode conversions.
	 * 
	 * @param s
	 * This is the string that should be converted to ASCII bytecode.
	 * 
	 * @return
	 * Returns an ArrayList of ASCII values
	 */
	//Converts a string into array of ASCII char values 
	public static ArrayList<Short> stringToByte(String s){
		ArrayList<Short> result = new ArrayList<>();
		for (int i = 0; i < s.length(); i++)			//Iterate thru string
			result.add((short)(s.charAt(i)));			//Add ASCII value for each char to arraylist
		return result;
	}
}
