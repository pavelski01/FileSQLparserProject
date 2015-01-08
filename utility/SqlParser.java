package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser
{
	public static String parseData(String _content)
	{
		final String GAP = " ";
		final String selectRegex = 
			"(?i)\\bSELECT\\b\\s+(.+)\\s+\\bFROM\\b\\s+" +
				"([\\w\\s,]+?)(?:\\s+\\b(?:WHERE|INNER|ORDER)\\b|;?$);";
		final Pattern pattern = 
			Pattern.compile(selectRegex, Pattern.MULTILINE);		
		StringBuilder stringBuffer = new StringBuilder();
		Matcher matcher = pattern.matcher(_content);
		while (matcher.find()) 
			stringBuffer.append(
				_content.substring(matcher.start(), matcher.end()) + GAP
			);
		return stringBuffer.toString();
	}
}
