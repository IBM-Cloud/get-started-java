package wasdev.sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.Database;


import wasdev.sample.CloudantClientMgr;
import wasdev.sample.MyNameDocument;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/addName")
public class AddName extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Database db = null;
	
	static {
		try {
			db = CloudantClientMgr.getDB();
		} catch (Exception re) {
			re.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = request.getParameter("user_name");
		
		if (db == null) {
			response.getWriter().print("Hello " + userName + "!");
		} else {
			MyNameDocument mydoc = new MyNameDocument(userName);
			db.save(mydoc);
			response.getWriter().println("Hello " + userName + "! I added you to the database.");
		}

	}

}
