package inputs;

import java.util.Properties;
import java.util.Set;

import part_four.task_one.database.Database;

public class Selects {
	
	private static String selectWrap = "<div><select size=\"5\" name=\"%s\" >%s</select></div>";
	private static String optionFormat = " <option value=\"%s\">%s</option>";

	public static String getSelects(Properties properties, String selectName) {
		StringBuilder options = new StringBuilder();
		Set<String> optionsValues = properties.stringPropertyNames();
		for (String val : optionsValues) {
			options.append(String.format(optionFormat, val, val));
		}
		return String.format(selectWrap, selectName, options.toString());
	}
}
