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
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

@WebServlet("/AddComputer")
public class AddComputer extends HttpServlet {

	private ComputerService computerService;
	private CompanyService companyService;
	
	public AddComputer() {
		super();
		computerService = ComputerService.getInstance();
		companyService = CompanyService.getInstance();
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
		Optional<LocalDate> introducedDate = stringToLocalDate(request.getParameter("introduced"));
		Optional<LocalDate> discontinuedDate = stringToLocalDate(request.getParameter("discontinued"));
		Optional<Company> company = Optional.empty();
		try {
			if (!request.getParameter("companyId").equals("")) {
				company =  companyService.getCompanyById(Integer.parseInt(request.getParameter("companyId")));
			}
			Computer computer = new Computer(computerName, introducedDate, discontinuedDate, company);
			computerService.addComputer(computer);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("Dashboard").forward(request,response);
	}
	
	private Optional<LocalDate> stringToLocalDate(String strDate) {
		Optional<LocalDate> date = Optional.empty();
		if (!strDate.equals("")) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				date = Optional.of(LocalDate.parse(strDate, formatter));
			}
			catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		
		return date;
	}

}
