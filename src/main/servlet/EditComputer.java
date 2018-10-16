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

import dto.ComputerDTO;
import exception.DatabaseException;
import exception.IncorrectComputerDTOException;
import exception.IncorrectDateException;
import exception.IncorrectIdException;
import exception.IncorrectNameException;
import exception.IntroducedAfterDiscontinuedException;
import exception.UnknowCompanyException;
import exception.UnknowComputerException;
import mapper.ComputerMapper;
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
	private ComputerMapper computerMapper;
		
	public EditComputer() {
		super();
		computerService = ComputerService.getInstance();
		companyService = CompanyService.getInstance();
		computerMapper = ComputerMapper.getInstance();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = -1;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch(NumberFormatException e) {
			logger.error(e.getMessage());
		}
		
		Optional<Computer> computer;
		try {
			computer = computerService.getById(id);
			if (computer.isPresent()) {
				ComputerDTO computerDTO = computerMapper.computerToComputerDTO(computer.get(), "yyyy-MM-dd");
				request.setAttribute("id", computerDTO.getId());
				request.setAttribute("name", computerDTO.getName());
				request.setAttribute("introduced", computerDTO.getIntroducedDate());
				request.setAttribute("discontinued", computerDTO.getDiscontinuedDate());
				request.setAttribute("companyId", computerDTO.getCompanyId());
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
		
		request.setAttribute("companies", companies);
		request.getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		String strName = request.getParameter("name");
		String strIntroducedDate = request.getParameter("introduced");
		String strDiscontinuedDate = request.getParameter("discontinued");
		String strCompanyId = request.getParameter("companyId");
		
		ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(strName).id(Long.toString(id)).introducedDate(strIntroducedDate).discontinuedDate(strDiscontinuedDate).companyId(strCompanyId).build();
		
		Computer computer;
		boolean computerUpadated = false;
		try {
			computer = computerMapper.computerDTOToComputer(computerDTO, "yyyy-MM-dd");
			computerUpadated = computerService.updateComputerById(computer);
		} catch (IncorrectNameException e) {
			logger.error(e.getMessage());
		} catch (IncorrectIdException e) {
			logger.error(e.getMessage());
		} catch (IncorrectDateException e) {
			logger.error(e.getMessage());
		} catch (IntroducedAfterDiscontinuedException e) {
			logger.error(e.getMessage());
		} catch (IncorrectComputerDTOException e) {
			logger.error(e.getMessage());
		} catch (UnknowCompanyException e) {
			logger.error(e.getMessage());
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		} catch (UnknowComputerException e) {
			logger.error("Unknow computer");
		}
		
		if (computerUpadated) {
			response.sendRedirect("Dashboard");
		}
		else {
			doGet(request, response);			
		}
	}
	
}