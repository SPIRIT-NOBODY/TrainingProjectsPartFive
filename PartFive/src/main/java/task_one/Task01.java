package task_one;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Необходимо запрашивать имя. Сервлет здоровается: «Hello, имя» и дает возможность изменить имя. 
 * Использовать параметры. При пустом имени выдавать соответствующее сообщение.
 * */

/**
 * Servlet implementation class TestServlet
 */
public class Task01 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = null;
		response.setCharacterEncoding("UTF-8");
		name = request.getParameter("name");
		String identsurname = (String) request.getSession(true).getAttribute("identsurname");
		String helloFormat = "Hello, %s";
		String helloString = null;
		if (name == null) {
			helloString = "Please, send your name.";
		} else {
			if ("".equals(name.trim())) {
				helloString = "Please, send not empty name!";
			} else {
				helloString = String.format(helloFormat, URLDecoder.decode(name, "UTF-8"));
			}
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Insert title here</title></head><body>");
		out.print("<h1>" + helloString + "</h1>");
		if(identsurname != null) {
			out.print("<div>Session ident surname "+identsurname+"</div>");
		}
		out.print("<form method=\"GET\">" + "<input name=\"name\" />"
				+ "<button type=\"submit\">Send name</button>" + "</form>");
		out.print("</body></html>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("/PartFive/Task01");
	}

}
