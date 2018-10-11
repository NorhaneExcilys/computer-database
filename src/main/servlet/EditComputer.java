package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.DatabaseException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import mapper.DateMapper;
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;

/**
 * Servlet implementation class EditComputer
 */
@WebServlet("/EditComputer")
public class EditComputer extends HttpServlet {

	private Logger logger = LoggerFactory.getLogger("EditComputer");
	
	private ComputerService computerService;
	private CompanyService companyService;
	private DateMapper dateMapper;
	
	private long id;
	private String name;
	private Optional<LocalDate> introducedDate;
	private Optional<LocalDate> discontinuedDate;
	private Optional<Company> company;
		
	public EditComputer() {
		super();
		computerService = ComputerService.getInstance();
		companyService = CompanyService.getInstance();
		dateMapper = DateMapper.getInstance();
		
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
		} catch (UnknowComputerException e) {
			logger.error("Unknow computer");
		} catch (UnknowCompanyException e) {
			logger.error("Unknow company");
		}
		
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyService.getCompanies();
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
		
		String companyId = "";
		if (company.isPresent()) {
			companyId = Long.toString(company.get().getId());
		}

		request.setAttribute("id", id);
		request.setAttribute("name", name);
		request.setAttribute("introduced", dateMapper.localDateToString(introducedDate, "yyyy-MM-dd"));
		request.setAttribute("discontinued", dateMapper.localDateToString(discontinuedDate, "yyyy-MM-dd"));
		request.setAttribute("companyId", companyId);
		request.setAttribute("companies", companies);

		request.getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		name = request.getParameter("name");
		introducedDate = dateMapper.stringToLocalDate(request.getParameter("introduced"), "yyyy-MM-dd");
		discontinuedDate = dateMapper.stringToLocalDate(request.getParameter("discontinued"), "yyyy-MM-dd");
		if (request.getParameter("companyId") == null) {
			company = Optional.empty();
		}
		else {
			try {
				company = companyService.getCompanyById(Long.parseLong(request.getParameter("companyId")));
			} catch (NumberFormatException e) {
				logger.error(e.getMessage());;
			} catch (DatabaseException e) {
				logger.error(e.getMessage());
			} catch (UnknowCompanyException e) {
				logger.error("Unknow company");
			}
		}
		
		Computer computer = new Computer.ComputerBuilder(name).id(id).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
		boolean computerUpadated = false;
		try {
			computerUpadated = computerService.updateComputerById(computer);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		} catch (UnknowComputerException e) {
			logger.error("Unknow company");
		}
		
		if (computerUpadated) {
			response.sendRedirect("Dashboard");
		}
		else {
			doGet(request, response);			
		}
	}
	
}