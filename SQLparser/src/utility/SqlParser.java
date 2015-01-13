package utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser
{
	public static String parseData(String _content)
	{
		final String GAP = " ", EMPTY = "", SINGLE_QUOTE = "'", DOUBLE_QUOTE = "\"";
		final String[] keyWords = 
		{
			"SELECT", "DELETE", "UPDATE", 
			"INSERT", "FROM", "WHERE", 
			"INNER", "ORDER", "SET",
			"INTO", "VALUES"
		};
		final String selectRegex = 
			"(?i)\\bSELECT\\b\\s+(.+)\\s+\\bFROM\\b\\s+" +
				"([\\w\\s,]+)(?:\\s+\\b(?:WHERE|INNER|ORDER)\\b" +
					"(?:[\\w\\s]+\\s*(=|<>|!=|>=|<=|>|<)+('|\")?" +
						"[\\w\\s]+\\.?[\\w\\s]*('|\")?;?))";
		final String deleteRegex = 
			"(?i)\\bDELETE\\b\\s+(.+)\\s+\\bFROM\\b\\s+" +
				"([\\w\\s,]+)(?:\\s+\\b(?:WHERE|INNER)\\b" +
					"(?:[\\w\\s]+\\s*(=|<>|!=|>=|<=|>|<)+('|\")?" +
						"[\\w\\s]+\\.?[\\w\\s]*('|\")?;?))";
		final String updateRegex = 
			"(?i)\\bUPDATE\\b\\s+(.+)\\s+\\bSET\\b\\s+" +
				"([\\w\\s]+=[\\w\\s]+)(?:\\s+\\b(?:WHERE)\\b" +
					"(?:[\\w\\s]+\\s*(=|<>|!=|>=|<=|>|<)+('|\")?" +
						"[\\w\\s]+\\.?[\\w\\s]*('|\")?;?))";
		final String insertRegex = 
			"(?i)\\bINSERT\\b\\s+\\bINTO\\b\\s+" +
				"([\\w\\s,]+)\\s+\\([\\w\\s,]+\\)\\s+\\bVALUES\\b\\s*" +
					"\\([\\w\\s,']+\\);?";
		final String regex = 
			"(" + selectRegex + ")|(" + deleteRegex + ")|(" + 
				updateRegex + ")|(" + insertRegex + ")";
		final Pattern pattern = 
			Pattern.compile(regex, Pattern.MULTILINE);		
		StringBuilder stringBuilder = new StringBuilder();
		Matcher matcher = pattern.matcher(_content);
		while (matcher.find()) 
			stringBuilder.append(
				_content.substring(matcher.start(), matcher.end()) + GAP
			);		
		String result = stringBuilder.toString();
		ArrayList<String> quotes = new ArrayList<String>();
		int start, stop;
		int singleQuoteIndex;
		int doubleQuoteIndex;
		String toFind, temp;
		boolean more = true;
		temp = result;
		while (more)
		{
			singleQuoteIndex = temp.indexOf(SINGLE_QUOTE);
			doubleQuoteIndex = temp.indexOf(DOUBLE_QUOTE);
			toFind = EMPTY;
			if (singleQuoteIndex > doubleQuoteIndex) toFind = SINGLE_QUOTE;
			else if (singleQuoteIndex < doubleQuoteIndex) toFind = DOUBLE_QUOTE;
			else
			{
				more = false;
				break;
			}
			start = temp.indexOf(toFind);
			stop = temp.indexOf(toFind, start + 1);
			quotes.add(temp.substring(start, stop + 1));
			temp = temp.substring(0, start) + temp.substring(stop + 1, temp.length());
		}
		result = result.toLowerCase();
		for (String word : keyWords) 
			if (result.contains(word.toLowerCase()))
				result = result.replace(word.toLowerCase(), word);
		for (String quote : quotes) result = result.replace(quote.toLowerCase(), quote);
		return result;
	}
}
