package task_two;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseExt;
import inputs.Input;
import inputs.Selects;
import part_four.task_one.database.Database;
import table.TableHtml;

/**
 * 2. Решить задачу 4.1 (т.е. добавлять, удалять). Оформить отсортированную базу
 * в виде красивой таблицы, разными цветами.
 */

public class Task02 extends HttpServlet {
	
	private static final long serialVersionUID = 4740724953224611929L;
	private String databaseSessionAttr = "database";
	private String tableSessionAttr = "table";
	private String deleteFormId = "deleteForm";
	private String deleteCheckboxName = "deleteIds";
	private String selectedFormId = "selectedForm";
	private String selectActionName = "selectedQuery";
	private String deleteForm = "<br><form id=\"%s\" method=\"POST\"><button type=\"submit\">Delete selected</button></form>";
	private String addNewStudentsForm = "<br><h2>Inputs new student</h2>"
			+ "<form method=\"POST\">%s<button type=\"submit\" name=\"addNewStudent\" value=\"Y\">Add new student</button></form>";
	private String selectedForm = "<br><h2>Select action</h2><form id=\"%s\" method=\"POST\" >" + "%s" + "%s"
			+ "<div>Select:<input name=\"action\" type=\"radio\" value=\"select\" checked=\"checked\"></div>"
			+ "<div>Delete:<input name=\"action\" type=\"radio\" value=\"delete\" ></div>"
			+ "<div>Show All:<input name=\"action\" type=\"radio\" value=\"showAll\" ></div>"
			+ "<div><button type=\"submit\">Do selected action</button></div></form>";

	private String inputErroSessionAttr = "errors";
	private String addStudentsInputsError = "addStudentsInputsError";

	private String lastValuesSessionAttr = "lastValues";
	private String selectsPropertiesSessiontAttr = "selects";
	private String currSelectQuerySessionAttr = "curentSelectValue";
	private String inputQueryError = "inputQueryError";

	private String errorInputStyle = "style=\"background-color: #ff00003b;\"";

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		DatabaseExt db = (DatabaseExt) session.getAttribute(databaseSessionAttr);
		TableHtml table = (TableHtml) session.getAttribute(tableSessionAttr);
		// String[] errors = (String[]) session.getAttribute(inputErroSessionAttr);
		HashMap<String, String[]> errorHash = (HashMap<String, String[]>) session.getAttribute(inputErroSessionAttr);
		String[] lastValues = (String[]) session.getAttribute(lastValuesSessionAttr);
		String[] currentSelectQuery = (String[]) session.getAttribute(currSelectQuerySessionAttr);
		Properties selectsProperties = (Properties) session.getAttribute(selectsPropertiesSessiontAttr);
		String identsurname = (String) session.getAttribute("identsurname");
		if (db == null) {
			try {
				db = new DatabaseExt();
				session.setAttribute(databaseSessionAttr, db);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (table == null) {
			table = new TableHtml(db.getColumnNames());
			table.setCheckboxForm(deleteFormId);
			table.setDeleteName(deleteCheckboxName);

			session.setAttribute(tableSessionAttr, table);
			try {
				table.setRows(db.showAll());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (errorHash == null) {
			errorHash = new HashMap<String, String[]>();
			String[] arrAddStudentsInputsError = new String[db.getColumnNames().length];
			Arrays.fill(arrAddStudentsInputsError, "");
			errorHash.put(addStudentsInputsError, arrAddStudentsInputsError);
			String[] arrInputQueryError = new String[] { "", "" };
			errorHash.put(inputQueryError, arrInputQueryError);
			session.setAttribute(inputErroSessionAttr, errorHash);
		}

		if (lastValues == null) {
			lastValues = new String[db.getColumnNames().length];
			Arrays.fill(lastValues, "");
			session.setAttribute(lastValuesSessionAttr, lastValues);
		}

		if (selectsProperties == null) {
			selectsProperties = new Properties();
			db.getProperties("selectdialog.properties", selectsProperties);
			session.setAttribute(selectsPropertiesSessiontAttr, selectsProperties);
		}

		PrintWriter out = resp.getWriter();

		resp.setContentType("text/html");
		out.print("<!DOCTYPE html><html><body><h2>Students Table</h2>");
		if(identsurname != null) {
			out.print("<div>Session ident surname "+identsurname+"</div>");
		}
		out.print(table.getTable());
		out.print(String.format(deleteForm, deleteFormId));
		out.print(String.format(addNewStudentsForm,
				Input.getInputsBlock(db.getColumnNames(), errorHash.get(addStudentsInputsError), lastValues)));
		if (currentSelectQuery != null) {
			out.print(currentSelectQuery[0]);
		}
		out.print(String.format(selectedForm, selectedFormId, Selects.getSelects(selectsProperties, selectActionName),
				Input.getInputsBlock(new String[] { "", selectActionName + "Value" }, errorHash.get(inputQueryError),
						new String[] { "", "" })));
		out.print("</body><html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		DatabaseExt db = (DatabaseExt) session.getAttribute(databaseSessionAttr);
		TableHtml table = (TableHtml) session.getAttribute(tableSessionAttr);

		String[] lastValues = (String[]) session.getAttribute(lastValuesSessionAttr);
		Properties selectsProperties = (Properties) session.getAttribute(selectsPropertiesSessiontAttr);
		String[] currentSelectQuery = (String[]) session.getAttribute(currSelectQuerySessionAttr);

		HashMap<String, String[]> errorHash = (HashMap<String, String[]>) session.getAttribute(inputErroSessionAttr);

		if (db != null) {
			String[] deletedIds = req.getParameterValues(deleteCheckboxName);
			String[] addNewStudents = req.getParameterValues("addNewStudent");
			String[] actionValue = req.getParameterValues("action");
			String[] actionSelectedValue = req.getParameterValues(selectActionName);
			if (deletedIds != null) {
				List<String> ids = new ArrayList<String>();
				String deleteCondition = "`Id` IN";
				for (int i = 0; i < deletedIds.length; i++) {
					try {
						Integer.parseInt(deletedIds[i]);
						ids.add(deletedIds[i]);
					} catch (Exception e) {
					}
				}

				if (ids.size() > 0) {
					try {
						String[] values = new String[ids.size()];
						ids.toArray(values);
						db.deleteByCondition(deleteCondition, values);
						table.setRows(db.showAll());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			if (addNewStudents != null && addNewStudents[0].equals("Y")) {
				String[] getColumnNames = db.getColumnNames();
				String[] columnParams = db.getColumnParams();
				String[] values = new String[getColumnNames.length];
				String[] errors = errorHash.get(addStudentsInputsError);
				int errorCount = 0;
				for (int i = 0; i < getColumnNames.length; i++) {
					if (i == 0) {
						errorCount--;
					}
					String patternValidator = columnParams[i].matches(".*INT.*") ? "[0-9]|10" : ".+";
					String tmpVal = req.getParameter(getColumnNames[i]);
					if (Input.inputValid(tmpVal, patternValidator)) {
						values[i] = tmpVal;
						errors[i] = "";
					} else {
						errors[i] = errorInputStyle;
						errorCount++;
					}
					lastValues[i] = tmpVal == null ? "" : tmpVal;
				}
				errorHash.put(addStudentsInputsError, errors);
				if (errorCount <= 0) {
					try {
						lastValues = new String[lastValues.length];
						Arrays.fill(lastValues, "");
						db.addValues(Arrays.copyOfRange(values, 1, values.length));
						table.setRows(db.showAll());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

			}

			if (actionValue != null && selectsProperties != null) {
				String[] actionValError = new String[] { "", "" };

				switch (actionValue[0]) {
				case "select":
					if (actionSelectedValue != null && !actionSelectedValue[0].equals("")) {
						String val = actionSelectedValue[0];
						String conditionValue = req.getParameter(selectActionName + "Value");
						String condition = selectsProperties.getProperty(val);

						if (condition != null && conditionValue != null
								&& Input.inputValid(conditionValue, "[0-9]|10")) {
							try {
								table.setRows(db.selectByCondition(condition, new String[] { conditionValue }, ""));
								currentSelectQuery = new String[] {
										"<br><h3>Current selected query: " + val + " " + conditionValue + "</h3>" };
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							actionValError[1] = errorInputStyle;
						}
					}
					break;
				case "delete":
					if (actionSelectedValue != null && !actionSelectedValue[0].equals("")) {
						currentSelectQuery = null;
						String val = actionSelectedValue[0];
						String conditionValue = req.getParameter(selectActionName + "Value");
						String condition = selectsProperties.getProperty(val);
						if (condition != null && conditionValue != null
								&& Input.inputValid(conditionValue, "[0-9]|10")) {
							try {
								db.deleteByCondition(condition, new String[] { conditionValue });
								table.setRows(db.showAll());
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							actionValError[1] = errorInputStyle;
						}
					}
					break;
				default:
					try {
						table.setRows(db.showAll());
						currentSelectQuery = null;
						actionValError[1] = "";
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				}

				errorHash.put(inputQueryError, actionValError);
			}
		}
		session.setAttribute(inputErroSessionAttr, errorHash);
		session.setAttribute(lastValuesSessionAttr, lastValues);
		session.setAttribute(tableSessionAttr, table);
		session.setAttribute(currSelectQuerySessionAttr, currentSelectQuery);
		resp.sendRedirect("/PartFive/Task02");
	}
}
