import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Source is the main program file. 
 * 
 * @author Nathaniel Quan
 *
 */
public class Source {

	/**
	 * main is the main function for the program. It will read and parse the file with fileName (default: Basics) and output natesLang.class.
	 * 
	 * This function opens from a file written in natesLang programming language with its filename stored in fileName variable. 
	 * With the file open, each line is read by the BufferedReader and sent to the Parser, which is initially passed the 
	 * working instance of a Bytecode object. Each line is parsed for recognized code fragments and optimized by the parser. 
	 * The resulting bytecode is created by the Parser and sent to the Bytecode reference. This creates the natesLang.class object.
	 * 
	 * Input: fileName (default: Basics)
	 * Output: natesLang.class
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//Variable and Object declarations
		//String fileName = "Helloworld";
		String fileName = "Basics";
		BufferedReader br = new BufferedReader(new FileReader(fileName));	
		Bytecode B = new Bytecode();
		Parser P = new Parser(B);
		

		//Parse file
		String line;
		while ((line = br.readLine()) != null){
			P.parseLine(line);
		}
		
		//Close BufferedReader and Bytecode
		B.close();
		br.close();	
	}

}
