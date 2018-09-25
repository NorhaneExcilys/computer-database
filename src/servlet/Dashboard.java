package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {

	private ComputerService computerService;
	
	public Dashboard() {
		super();
		computerService = ComputerService.getInstance();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Computer> computers = new ArrayList<Computer>();
		int computerNumber = -1;
		try {
			computers = computerService.getComputers();
			computerNumber = computerService.getCount();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
		request.setAttribute("computerNumber", computerNumber);
		request.setAttribute("computers", computers);
		
		request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
