package task_six;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Task06 extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Refresh", "5");

		PrintWriter out = resp.getWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());

		out.print("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Current time</title></head><body>");
		out.print("<h1>Current time</h1>");
		out.print("<div>" + formatter.format(date) + "</div>");
		out.print("</body></html>");
	}
}
