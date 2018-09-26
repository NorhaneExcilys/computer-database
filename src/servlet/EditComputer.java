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
import exception.UnknowComputerException;
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

/**
 * Servlet implementation class EditComputer
 */
@WebServlet("/EditComputer")
public class EditComputer extends HttpServlet {

	
	private ComputerService computerService;
	private CompanyService companyService;
	
	private long id;
	private String name;
	private Optional<LocalDate> introducedDate;
	private Optional<LocalDate> discontinuedDate;
	private Optional<Company> company;
	
	
	public EditComputer() {
		super();
		computerService = ComputerService.getInstance();
		companyService = CompanyService.getInstance();
		
		id = -1;
		name = "";
		introducedDate = Optional.empty();
		discontinuedDate = Optional.empty();
		company = Optional.empty();
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		Optional<Computer> computer;
		try {
			computer = computerService.getById(id);
			if (computer.isPresent()) {
				name = computer.get().getName();
				introducedDate = computer.get().getIntroducedDate();
				discontinuedDate = computer.get().getDiscontinuedDate();
				company = computer.get().getCompany();
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnknowComputerException e) {
			e.printStackTrace();
		} catch (UnknowCompanyException e) {
			e.printStackTrace();
		}
		
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyService.getCompanies();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		String companyId = "";
		if (company.isPresent()) {
			companyId = Long.toString(company.get().getId());
		}

		request.setAttribute("id", id);
		request.setAttribute("name", name);
		request.setAttribute("introduced", localDateToString(introducedDate));
		request.setAttribute("discontinued", localDateToString(discontinuedDate));
		request.setAttribute("companyId", companyId);
		request.setAttribute("companies", companies);

		request.getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		name = request.getParameter("name");
		introducedDate = stringToLocalDate(request.getParameter("introduced"));
		discontinuedDate = stringToLocalDate(request.getParameter("discontinued"));
		if (request.getParameter("companyId") == null) {
			company = Optional.empty();
		}
		else {
			try {
				company = companyService.getCompanyById(Long.parseLong(request.getParameter("companyId")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			} catch (UnknowCompanyException e) {
				e.printStackTrace();
			}
		}
		
		Computer computer = new Computer(id, name, introducedDate, discontinuedDate, company);
		try {
			computerService.updateComputerById(computer);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		doGet(request, response);
	}
	
	private String localDateToString(Optional<LocalDate> date) {
		String strDate = "";
		if (date.isPresent()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			strDate = date.get().format(formatter);
		}
		return strDate;
	}
	
	private Optional<LocalDate> stringToLocalDate(String strDate) {
		Optional<LocalDate> date = Optional.empty();
		if (!strDate.equals("")) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				date = Optional.of(LocalDate.parse(strDate, formatter));
			}
			catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		
		return date;
	}


}
