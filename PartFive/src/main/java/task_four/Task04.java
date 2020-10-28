package task_four;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Сервлет должен запрашивать идентификационную информацию (например, фамилию),
 * выбирать посредством радиокнопок, куда перейти (например, на задачу 1 или 2)
 * и перенаправлять на нее по выбору пользователя. Используя сеанс, отображать
 * на получаемых страницах идентификационные параметры.
 */
public class Task04 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		out.print("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Insert title here</title></head><body>");
		out.print("<h1>Send identification surname</h1>");
		String error = req.getParameter("error");
		if (error != null && !error.trim().equals("")) {
			out.print("<div style=\"color:red;\">" + URLDecoder.decode(error, "UTF-8") + "</div>");
		}
		out.print("<form method=\"POST\">"
				+ "<div><input name=\"identsurname\" placeholder=\"identification surname\"/></div>");
		out.print(
				"<div>Task 01: <input name=\"task_redirect\" type=\"radio\" value=\"Task01\" checked=\"checked\"></div>"
						+ "<div>Task 02: <input name=\"task_redirect\" type=\"radio\" value=\"Task02\"></div>"
						+ "<button type=\"submit\">Send</button>" + "</form>");
		out.print("</body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String identsurname = req.getParameter("identsurname");
		String task = req.getParameter("task_redirect");
		if (identsurname != null && !identsurname.trim().equals("") && task != null && !task.trim().equals("")) {
			req.getSession(true).setAttribute("identsurname", identsurname);
			switch (task) {
			case "Task01":
				req.getRequestDispatcher("/Task01").forward(req, resp);
				break;
			case "Task02":
				resp.setHeader("Request Method", "GET");
				req.getRequestDispatcher("/Task02").forward(req, resp);
				break;
			default:
				resp.sendRedirect(req.getRequestURI() + "?error=wrong task");
			}
		} else {
			resp.sendRedirect(req.getRequestURI() + "?error=wrong parameters");
		}
	}
}
