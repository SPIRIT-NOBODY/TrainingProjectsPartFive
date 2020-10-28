package task_three;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 3. Использование cookies. Вводятся идентификационные параметры (например, имя
 * и фамилия). При следующем входе они отображаются и предлагается их изменить.
 * Экспериментировать со временем жизни (установить небольшое).
 */

public class Task03 extends HttpServlet {
	private String identForm = "<form method=\"POST\" >"
			+ "<div><input name=\"name\" value=\"\" placeholder=\"Name\"></div>"
			+ "<div><input name=\"surname\" value=\"\" placeholder=\"Surname\"></div>"
			+ "<div><button type=\"submit\">Save</button></div></form>";
	private String helloString = "<h1>%s</h1>";
	private String cookieName = "name";
	private String cookieSurname = "surname";
	private int cookieLive = 60 * 5;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		Cookie[] cookies = req.getCookies();
		String name = getCookieValue(cookies, cookieName);
		String surname = getCookieValue(cookies, cookieSurname);
		StringBuilder helloBuilder = new StringBuilder();
		if (name != null || surname != null) {
			helloBuilder.append("Hello, ");
			helloBuilder.append(name != null ? name : "");
			helloBuilder.append(" ");
			helloBuilder.append(surname != null ? surname : "");
			helloBuilder.append(". Change your data");
		} else {
			helloBuilder.append("Hello, please inpute your name and surname");
		}
		PrintWriter out = resp.getWriter();
		out.print(
				"<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Input your name and surname</title></head><body>");
		out.print(String.format(helloString, helloBuilder.toString()));
		out.print(identForm);
		out.print("</body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String reqName = req.getParameter(cookieName);
		String reqSurname = req.getParameter(cookieSurname);
		if (reqName != null && !reqName.trim().equals("")) {
			reqName = URLEncoder.encode(reqName, "UTF-8");
			Cookie name = new Cookie(cookieName, reqName);
			name.setMaxAge(cookieLive);
			resp.addCookie(name);
		}

		if (reqSurname != null && !reqSurname.trim().equals("")) {
			reqSurname = URLEncoder.encode(reqSurname, "UTF-8");
			Cookie surname = new Cookie(cookieSurname, reqSurname);
			surname.setMaxAge(cookieLive);
			resp.addCookie(surname);
		}
		resp.sendRedirect("/PartFive/Task03");

	}

	protected String getCookieValue(Cookie[] cookies, String cookieName) {
		String value = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), "UTF-8");
						break;
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return value;
	}
}
