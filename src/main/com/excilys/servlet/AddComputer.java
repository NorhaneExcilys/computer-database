package com.excilys.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.exception.IntroducedAfterDiscontinuedException;
import com.excilys.mapper.ComputerMapper;
import com.excilys.model.Computer;

import com.excilys.dto.ComputerDTO;
import com.excilys.exception.DatabaseException;
import com.excilys.exception.IncorrectComputerDTOException;
import com.excilys.exception.IncorrectDateException;
import com.excilys.exception.IncorrectIdException;
import com.excilys.exception.IncorrectNameException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.model.Company;
import com.excilys.service.CompanyService;
import com.excilys.service.ComputerService;

@WebServlet("/AddComputer")
public class AddComputer extends HttpServlet {

	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerMapper computerMapper;
	
	private Logger logger = LoggerFactory.getLogger("AddComputer");
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
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
		String strIntroducedDate = request.getParameter("introduced");
		String strDiscontinuedDate = request.getParameter("discontinued");
		String strCompanyId = request.getParameter("companyId");
		
		ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(computerName).introducedDate(strIntroducedDate).discontinuedDate(strDiscontinuedDate).companyId(strCompanyId).build();

		boolean computerAdded = false;
		try {
			Computer computer = computerMapper.computerDTOToComputer(computerDTO, "yyyy-MM-dd");
			computerAdded = computerService.addComputer(computer);
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
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		} catch (UnknowCompanyException e) {
			logger.error("Unknow company");
		}

		if (computerAdded) {
			response.sendRedirect("Dashboard");
		}
		else {
			doGet(request, response);			
		}
	}

}
