package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.ComputerDTO;
import exception.DatabaseException;
import exception.UnknowCompanyException;
import model.Computer;
import service.ComputerService;

/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {

	private ComputerService computerService;
	private int currentPage;
	private int computerPerPage;
	
	public Dashboard() {
		super();
		computerService = ComputerService.getInstance();
		currentPage = 1;
		computerPerPage = 5;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<ComputerDTO> computersDTO = new ArrayList<ComputerDTO>();
		int totalComputers = -1;
		try {
			List<Computer> computers = computerService.getByPage(computerPerPage, currentPage);
			for (int index = 0; index < computers.size(); index++) {
				ComputerDTO computer = new ComputerDTO(computers.get(index));
				computersDTO.add(computer);
			}
			totalComputers = computerService.getCount();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
		request.setAttribute("computerNumber", totalComputers);
		request.setAttribute("computers", computersDTO);
		
		request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
