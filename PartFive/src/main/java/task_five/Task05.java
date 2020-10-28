package task_five;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Task05 extends HttpServlet {

	private String escapeString(String str) {
		return str.replace("&", "&amp").replace("<", "&lt").replace(">", "&gt").replace("\"", "&quot");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletConfig config = getServletConfig();
		String fileName = config.getInitParameter("Text file");
		String imgSrc = config.getInitParameter("Picture");
		PrintWriter out = resp.getWriter();
		out.print("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Task 05</title></head><body>");
		out.print("<div>");
		out.print("<img style=\"max-width: 500px;\" src=\"" + getServletContext().getContextPath() + "/" + imgSrc
				+ "\">");
		out.print("</div>");
		out.print("<div>");		
		File file = new File(getServletContext().getRealPath(fileName));
		if (file.exists()) {
			try(BufferedReader fileRd = new BufferedReader(new FileReader(file));){
				String line = fileRd.readLine();
				out.print("<p>");
				while (line != null) {
					out.print(escapeString(line) + "<br>");
					line = fileRd.readLine();
				}
				out.print("</p>");
			}catch (Exception e) {
				out.print("<p> " + e.getMessage() + "</p>");
			}			
		} else {
			out.print("<p> " + fileName + " not exist</p>");
		}
		out.print("</div>");
		out.print("</body></html>");

	}
}
