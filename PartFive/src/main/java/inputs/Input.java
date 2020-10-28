package inputs;

public class Input {
	private static String inputFormat = "<div><input type=\"text\" name=\"%s\" placeholder=\"%s\" %s value=\"%s\"></div>";
	public static boolean inputValid(String value,String pattern) {
		boolean isValid = false;
		try {
			isValid = value.trim().matches(pattern);
		} catch (Exception e) {}
		return isValid;		
	}
	
	public static String getInputsBlock(String[] columnNames, String[] errors, String[] values) {
		StringBuilder inputs = new StringBuilder();
		for (int i = 1; i < columnNames.length; i++) {
			inputs.append(String.format(inputFormat, columnNames[i], columnNames[i], errors[i], values[i]));
		}
		return inputs.toString();
	}	
}
