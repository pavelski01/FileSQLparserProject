package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class FileLoader
{
	public static ArrayList<String> getData(
		File _file, JComponent _component
	)
	{
		final String EMPTY = "";
		String line;
		BufferedReader bufferedReader;
		ArrayList<String> dataList = new ArrayList<String>();
	    try
	    {       
	    	bufferedReader = new BufferedReader(new FileReader(_file));
	        while ((line = bufferedReader.readLine()) != null) 
	        	if (!line.equals(EMPTY)) dataList.add(line);
	        	else continue;
	        bufferedReader.close();
	     }
	    catch (IOException _ioEX)
	    { 
	    	final String ERROR = "Error: ";
	    	JOptionPane.showMessageDialog(_component, ERROR + _ioEX); 
    	}
	    return dataList;
    }
}
