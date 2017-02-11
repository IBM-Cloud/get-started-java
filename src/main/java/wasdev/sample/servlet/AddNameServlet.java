/*
 * Copyright IBM Corp. 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wasdev.sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wasdev.sample.Visitor;
import wasdev.sample.store.VisitorStore;
import wasdev.sample.store.VisitorStoreFactory;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/addName")
public class AddNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Our database store
	VisitorStore store = VisitorStoreFactory.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userName = request.getParameter("user_name");
		
		if (store == null) {
			response.getWriter().print("Hello " + userName + "!");
		} else {
			Visitor mydoc = new Visitor(userName);
			store.persist(mydoc);
			response.getWriter().println("Hello " + userName + "! I added you to the database.");
		}

	}

}
