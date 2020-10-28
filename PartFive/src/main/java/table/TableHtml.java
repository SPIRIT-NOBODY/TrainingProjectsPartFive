package table;

import java.util.List;

import part_four.task_one.database.Database;

public class TableHtml {

	private List<String[]> rows;
	private String[] columnsName;
	private String tableStyle = "<style>.students-table tr:nth-child(2n) {background: #f0f0f0; }.students-table thead{background: #b4fdf3;}"
			+ "</style>";
	private String tdFormat = "<td>%s</td>";
	private String checkBoxFormat = "<input type=\"checkbox\" name=\"%s\" value=\"%s\" form=\"%s\">";
	private String deleteName;
	private String formId;

	public TableHtml(String[] columnsName) {
		this.columnsName = columnsName;
	}

	public void setCheckboxForm(String formId) {
		this.formId = formId;
	}

	public void setDeleteName(String deleteName) {
		this.deleteName = deleteName;
	}

	private String headerTable() {
		StringBuilder header = new StringBuilder();
		header.append("<thead><tr><td>   </td>");
		for (int i = 0; i < columnsName.length; i++) {
			header.append(String.format(tdFormat, columnsName[i]));
		}
		header.append("</tr></thead>");
		return header.toString();
	}

	public String getTable() {
		StringBuilder table = new StringBuilder();
		table.append(tableStyle);
		table.append("<table class=\"students-table\" border=\"2\">");
		table.append(headerTable());
		table.append(getRows());
		table.append("</table>");
		return table.toString();

	}

	private String getRows() {
		StringBuilder rowBuilder = new StringBuilder();

		for (String[] rw : rows) {
			rowBuilder.append("<tr>");
			rowBuilder.append("<td>");
			rowBuilder.append(String.format(checkBoxFormat, deleteName, rw[0], formId));
			rowBuilder.append("</td>");
			for (int i = 0; i < rw.length; i++) {
				rowBuilder.append(String.format(tdFormat, rw[i]));
			}
			rowBuilder.append("</tr>");
		}

		return rowBuilder.toString();
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

}
