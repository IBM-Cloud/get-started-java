package wasdev.sample.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudant.client.api.Database;
import com.google.gson.Gson;

import wasdev.sample.CloudantClientMgr;
import wasdev.sample.MyNameDocument;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/getNames")
public class GetNames extends HttpServlet {
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
		response.setContentType("application/json");

		if (db == null) {
			response.getWriter().print("[]");
		} else {
			List<String> names = new ArrayList<String>();
	        List<MyNameDocument> docs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(MyNameDocument.class);
	        for (MyNameDocument doc : docs) {
				String name = doc.getUserName();
				if(name!=null)	names.add(name);
			}
	        response.getWriter().print(new Gson().toJson(names));
		}

	}

}
