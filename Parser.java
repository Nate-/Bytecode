import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Parser class converts natesLang into java bytecode.
 * 
 * This class performs optimizations on the natesLang language by removing any unnecessary
 * code before converting it to bytecode. This is done by storing the variable name with
 * the value of strings, numbers, and functions in their HashMaps to be referenced in future code.
 * 
 * Function code for every function is stored in the index represented by the function name in
 * its HashMap.
 * 
 * 
 * @author Nathaniel Quan
 *
 */
public class Parser {
	HashMap<String, String> strings = new HashMap<>();
	HashMap<String, Integer> numbers = new HashMap<>();
	HashMap<String, Integer> functions = new HashMap<>();
	ArrayList<ArrayList<String>> fnCode = new ArrayList<>();
	ArrayList<String> tmpCode = new ArrayList<>();
	int fn_counter = 0;
	Pattern pattern;
	Matcher m;
	Bytecode B;
	Boolean conditionMet;
	Integer iterations;
	
	/**
	 * The default constructor for Parser.
	 * 
	 * This constructor allows the Parser to output Java bytecode.
	 * 
	 * @param b
	 * A reference to a Bytecode object passed in to allow Parser to output 
	 * bytecode in the code sections of methods.
	 */
	public Parser(Bytecode b){
		B = b;
	}

	/**
	 * parseLine parses a string and converts the code into java bytecode.
	 * 
	 * Using regular expressions, each possible accepted syntax format is scanned for
	 * and depending on the syntax, an according action is performed. The types of
	 * language functionality parsed for are:
	 * 		- Printing string literal
	 * 		- Printing variable
	 * 		- Assigning a string to a variable
	 * 		- Assigning a numeric value or expression to a variable
	 * 		- If statements
	 * 		- From ... until ... statements (natesLang version of for loop)
	 * 		- Function declarations
	 * 		- Function instantiations
	 * 
	 * @param line
	 * The line of code written in natesLang
	 */
	public void parseLine(String line){	
		//Print literal
		if (line.matches("(print\\()(\")(.*)(\"\\))")){
			pattern = Pattern.compile("(print\\()(\")(.*)(\"\\))");
			m = pattern.matcher(line);
			if (m.find()){
				B.print(m.group(3));
				System.out.println("B.print("+m.group(3)+")");
			}
		}
		//Print variable
		else if (line.matches("(print\\()([A-Za-z]+[0-9]*)(\\))")){
			pattern = Pattern.compile("(print\\()([A-Za-z]+[0-9]*)(\\))");
			m = pattern.matcher(line);
			if (m.find()){
				if (numbers.get(m.group(2)) == null)
					B.print(strings.get(m.group(2)));
				else
					B.print(numbers.get(m.group(2)).toString());
			}
		}
		//String Assignment
		else if (line.matches("([A-Za-z0-9]+)( *)(\\=)( *)(\")(.*)(\")")){
			pattern = Pattern.compile("([A-Za-z0-9]+)( *)(\\=)( *)(\")(.*)(\")");
			m = pattern.matcher(line);
			if (m.find()){
				strings.put(m.group(1), m.group(6));
				System.out.println(m.group(1) + " = " + m.group(6));
			}
		}
		//Numeric assignment with mathematical operation
		else if (line.matches("([A-Za-z0-9]*)( *)(\\=)( *)([A-Za-z0-9]*)( *)([\\+\\-\\*\\/])( *)([A-Za-z0-9]*)")){
			pattern = Pattern.compile("([A-Za-z0-9]*)( *)(\\=)( *)([A-Za-z0-9]*)( *)([\\+\\-\\*\\/])( *)([A-Za-z0-9]*)");
			m = pattern.matcher(line);
			if (m.find()){
				String var = m.group(1);
				Integer resolved = doOperation(m.group(7), m.group(5), m.group(9));
				numbers.put(var,  resolved);
				System.out.println(m.group(1) + "=" + m.group(5) + m.group(7) + m.group(9));
				System.out.println(m.group(1) + "=" + resolved);
			}
		}
		//Direct numeric assignment
		else if (line.matches("([A-Za-z0-9]*)( *)(\\=)( *)([A-Za-z0-9]*)")){
			pattern = Pattern.compile("([A-Za-z0-9]*)( *)(\\=)( *)([A-Za-z0-9]*)");
			m = pattern.matcher(line);
			if (m.find()){
				numbers.put(m.group(1), getValue(m.group(5)));
				System.out.println(m.group(1) + " = " + m.group(5));
			}
		}
		//If statement
		else if (line.matches("(if)( *)([A-Za-z0-9]+)( *)(.{1,2})( *)([A-Za-z0-9]+)")){
			pattern = Pattern.compile("(if)( *)([A-Za-z0-9]+)( *)([^\\s]{1,2})( *)([A-Za-z0-9]+)");
			m = pattern.matcher(line);
			if (m.find()){
				System.out.println("IF: " + m.group(3) + m.group(5) + m.group(7));
				conditionMet = doConditional(m.group(5), m.group(3), m.group(7));
			}
		}
		//End if statement
		else if (line.matches("(end)( +)(if)")){
			pattern = Pattern.compile("(end)( +)(if)");
			m = pattern.matcher(line);
			if (m.find()){
				System.out.println("END IF");
				conditionMet = null;
			}
		}
		//Then statement
		else if (line.matches("(then)( *)(.*)")){
			if(conditionMet){
				pattern = Pattern.compile("(then)( *)(.*)");
				m = pattern.matcher(line);
				if(m.find()){
					parseLine(m.group(3));
				}
			}
		}
		//Else statement
		else if (line.matches("(else)( *)(.*)")){
			if(!conditionMet){
				pattern = Pattern.compile("(else)( *)(.*)");
				m = pattern.matcher(line);
				if (m.find()){
					parseLine(m.group(3));
				}
			}
		}
		//For loop, my version
		else if (line.matches("(from)( *)([0-9]+)( *)(until)( *)([0-9]+)")){
			pattern = Pattern.compile("(from)( *)([0-9]+)( *)(until)( *)([0-9]+)");
			m = pattern.matcher(line);
			if (m.find()){
				iterations = Integer.parseInt(m.group(3)) - Integer.parseInt(m.group(7));
				if (iterations < 0) iterations = -1 * iterations;
			}
		}
		//Content of for loop
		else if (line.matches("(do)( *)(.*)")){
			pattern = Pattern.compile("(do)( *)(.*)");
			m = pattern.matcher(line);
			if (m.find()){
				String task = m.group(3);
				for (int i = 0; i < iterations; i++){;
					parseLine(task);
				}
				iterations = 0;
			}			
		}
		//Start of function
		else if (line.matches("(fn)( *)(.*)(:)")){
			pattern = Pattern.compile("(fn)( *)(.*)(:)");
			m = pattern.matcher(line);
			if (m.find()){
				functions.put(m.group(3).trim(), fn_counter);
				System.out.println(m.group(3).trim() + " @ " + fn_counter);
				fn_counter++;
			}
		}
		//Content of function
		else if (line.matches("(\t)(.*)")){
			pattern = Pattern.compile("(\t)(.*)");
			m = pattern.matcher(line);
			if (m.find()){
				tmpCode.add(m.group(2).trim());
				System.out.println("\tcode: " + m.group(2).trim());
			}
		}
		//End of function
		else if (line.matches("(end fn)")){
			pattern = Pattern.compile("(end fn)");
			m = pattern.matcher(line);
			if (m.find()){
				ArrayList<String> tmp = new ArrayList<>();
				tmp.addAll(tmpCode);
				fnCode.add(tmp);
				tmpCode.clear();
				System.out.println("end of function");
			}
		}
		//Calling function
		else if (line.matches("(call)( *)(.*)")){
			pattern = Pattern.compile("(call)( *)(.*)");
			m = pattern.matcher(line);
			if (m.find()){
				System.out.println("Calling " + m.group(3));
				int fnIndex = functions.get(m.group(3).trim());
				System.out.println("\tindex = " + fnIndex);
				for (String s: fnCode.get(fnIndex)){
					System.out.println("\t" + s);
					parseLine(s);
				}
			}
		}
		
	}
	
	/**
	 * getType determines the type of a value stored in a string
	 * 
	 * Conditions to determine type:
	 * 		- Letter followed optionally by number(s) = variable
	 * 		- Numbers = numeric values
	 * 		- Anything surrounded by quotes - a literal string
	 * 
	 * @param s
	 * s is the string for which the type needs to be determined
	 * 
	 * @return
	 * Returns a character with the value 'v', 'n', 's', or '?"
	 * 		'v' - variable
	 * 		'n' - number
	 * 		's' - string literal
	 * 		'?' - unknown, caused by incorrect syntax
	 */
	//Test string for type
	public char getType(String s){
		char type = '?';				// ? for no initalization so no type
		
		if (s.matches("[A-Za-z]+[0-9]*"))
			type = 'v';					// v for variable
		if (s.matches("[0-9]+"))
			type = 'n';					// n for number
		else if (s.matches("(\")(.*)(\")"))
			type = 's';					// s for string
		
		return type;
	}
	
	/**
	 * getValue determines the value of a string
	 * 
	 * If the string is a variable, the value will be retrieved from the numbers HashMap for the variable name
	 * represented by the variable. Otherwise, treat the string as an integer and parse for an integer.
	 * 
	 * @param s
	 * String for which value should be retrieved
	 * 
	 * @return
	 * Returns the Integer value of the string
	 */
	public Integer getValue(String s){
		Integer value;
		if (getType(s) == 'v')
			value = numbers.get(s);
		else
			value = Integer.parseInt(s);
		return value;
	}
	
	/**
	 * doOperation performs an operation with the first and second operands
	 * 
	 * @param operator
	 * Valid operator values are +, -, *, /
	 * 
	 * @param op1
	 * The first operand
	 * 
	 * @param op2
	 * The second operand
	 * 
	 * @return
	 * Returns the value of a resolved operation
	 */
	public Integer doOperation(String operator, String op1, String op2){
		Integer result;
		if (operator.matches("\\+"))
			result = sum(op1, op2);
		else if (operator.matches("\\-"))
			result = difference(op1, op2);
		else if (operator.matches("\\*"))
			result = product(op1, op2);
		else
			result = quotient(op1, op2);
		
		return result;
	}
	
	/**
	 * doConditional performs a comparison statement with the first and second operands
	 * 
	 * @param operator
	 * Valid operators are <, >, <=, >=, ==
	 * 
	 * @param op1
	 * First operand
	 * 
	 * @param op2
	 * Second operand
	 * 
	 * @return
	 * Returns true or false depending on the result of the comparison statement
	 */
	public Boolean doConditional(String operator, String op1, String op2){
		Boolean result;

		if (operator.matches("<"))
			result = isLess(op1, op2);
		else if (operator.matches(">"))
			result = isGreater(op1, op2);
		else if (operator.matches("<="))
			result = isLessOrEqual(op1, op2);
		else if (operator.matches(">="))
			result = isGreaterOrEqual(op1, op2);
		else if (operator.matches("=="))
			result = isEqual(op1, op2);
		else
			result = null;

		return result;
	}
	
	/**
	 * sum takes the sum of two strings
	 * 
	 * The parameters of the function can be either variables or numeric.
	 * Either ways the integer value is retrieved and the sum of the two
	 * numbers is returned
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Returns the sum of two numbers
	 */
	//Parse string parameters for double values and return sum
	public Integer sum(String a, String b){
		return getValue(a) + getValue(b);
		
	}
	
	/**
	 * difference takes the difference of two strings
	 * 
	 * The parameters of the function can be either variables or numeric.
	 * Either ways the integer value is retrieved and the difference of the two
	 * numbers is returned
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Returns the difference of two numbers
	 */
	//Parse string parameters for double values and return difference
	public Integer difference(String a, String b){
		return getValue(a) - getValue(b);
	}
	
	/**
	 * product takes the product of two strings
	 * 
	 * The parameters of the function can be either variables or numeric.
	 * Either ways the integer value is retrieved and the product of the two
	 * numbers is returned
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Returns the product of two numbers
	 */
	//Parse string parameters for double values and return product
	public Integer product(String a, String b){
		return getValue(a) * getValue(b);
	}

	/**
	 * quotient takes the quotient of two strings
	 * 
	 * The parameters of the function can be either variables or numeric.
	 * Either ways the integer value is retrieved and the quotient of the two
	 * numbers is returned
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Returns the quotient of two numbers
	 */
	//Parse string for double values and return quotient
	public Integer quotient(String a, String b){
		return getValue(a) / getValue(b);
	}
	
	/**
	 * assignVarValue puts a variable with an integer value into its HashMap
	 * 	
	 * @param var
	 * The variable name, e.g. var1
	 * 
	 * @param value
	 * The integer value represented by var1
	 */
	//Update or create a new variable to add to HashMap
	public void assignVarValue(String var, String value){
		numbers.put(var, Integer.parseInt(value));
	}
	
	/**
	 * getVarValue retrieves the numeric representation of a variable
	 * 
	 * @param var
	 * The variable name, e.g. var1
	 * 
	 * @return
	 * The integer value stored in the HashMap when hashing(var1)
	 */
	//Retrieve value from HashMap
	public Integer getVarValue(String var){
		return numbers.get(var);
	}
	
	/**
	 * isEqual tests to see if two strings are equal
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */
	//tests if the values are equal
	public boolean isEqual(String a, String b){
		if (getValue(a) == getValue(b)) return true;
		else return false;
	}

	/**
	 * isNotEqual tests to see if two strings are not equal
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */
	//tests if the values are not equal
	public boolean isNotEqual(String a, String b){
		return !isEqual(a, b);
	}
	
	/**
	 * isGreater tests to see if first string is larger than second
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */	
	//tests if a numeric value is greater than another
	public boolean isGreater(String a, String b){
		if (getValue(a) > getValue(b)) return true;
		else return false;
	}

	/**
	 * isLess tests to see if first string is less than second
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */	
	//tests if a numeric value is greater than another
	public boolean isLess(String a, String b){
		if (getValue(a) < getValue(b)) return true;
		else return false;
	}
	
	/**
	 * isGreaterOrEqual tests to see if first string is larger or equal to second
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */		
	//tests if a numeric value is less than or equal to another
	public boolean isGreaterOrEqual(String a, String b){
		return !isLess(a, b);
	}
	
	/**
	 * isLessOrEqual tests to see if first string is less or equal to second
	 * 
	 * The values of the string parameters will be converted to
	 * numeric values before the comparison occurs.
	 * 
	 * @param a
	 * First string
	 * 
	 * @param b
	 * Second string
	 * 
	 * @return
	 * Boolean result of comparison
	 */		
	//test if a numeric value is less than or equal to another
	public boolean isLessOrEqual(String a, String b){
		return !isGreater(a, b);
	}
	
	
}
