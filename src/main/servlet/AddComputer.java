package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import mapper.DateMapper;
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

@WebServlet("/AddComputer")
public class AddComputer extends HttpServlet {

	private ComputerService computerService;
	private CompanyService companyService;
	private DateMapper dateMapper;
	
	public AddComputer() {
		super();
		computerService = ComputerService.getInstance();
		companyService = CompanyService.getInstance();
		dateMapper = DateMapper.getInstance();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyService.getCompanies();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		request.setAttribute("companies", companies);
		
		request.getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String computerName = request.getParameter("computerName");
		Optional<LocalDate> introducedDate = dateMapper.stringToLocalDate(request.getParameter("introduced"), "yyyy-MM-dd");
		Optional<LocalDate> discontinuedDate = dateMapper.stringToLocalDate(request.getParameter("discontinued"), "yyyy-MM-dd");
		Optional<String> strCompany = Optional.ofNullable(request.getParameter("companyId"));
		Optional<Company> company = Optional.empty();
		boolean computerAdded = false;
		try {
			if (strCompany.isPresent()) {
				company = companyService.getCompanyById(Integer.parseInt(request.getParameter("companyId")));
			}
			Computer computer = new Computer.ComputerBuilder(computerName).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
			computerAdded = computerService.addComputer(computer);
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
		
		
		if (computerAdded) {
			response.sendRedirect("Dashboard");
		}
		else {
			doGet(request, response);			
		}
	}

}
