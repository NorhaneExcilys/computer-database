package com.excilys.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.dto.ComputerDTO;
import com.excilys.exception.DatabaseException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.service.ComputerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.exception.UnknowComputerException;
import com.excilys.mapper.ComputerMapper;
import com.excilys.model.Computer;
import com.excilys.model.Paging;


/**
 * Servlet implementation class Dashboard
 */
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {

	private Logger logger = LoggerFactory.getLogger("Dashboard");
	
	private final static int DEFAULT_COMPUTERS_PER_PAGE = 50;
	private final static int DEFAULT_CURRENT_PAGE = 1;
	private final static int DEFAULT_PAGE_STEP = 3;
	
	@Autowired
	private ComputerService computerService;
	@Autowired
	private ComputerMapper computerMapper;
	
	private List<ComputerDTO> computersDTO;
	private Paging currentPaging;
	private int totalPage;
	private int totalComputers;
	private Optional<String> searchedWord;
	
	public Dashboard() {
		super();
		
		computersDTO = new ArrayList<ComputerDTO>();
		currentPaging = new Paging(DEFAULT_COMPUTERS_PER_PAGE, DEFAULT_CURRENT_PAGE);
		totalPage = 0;
		totalComputers = 0;
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Optional<String> strComputersPerPage = Optional.ofNullable(request.getParameter("computersPerPage"));
		if (strComputersPerPage.isPresent()) {
			int computersPerPage = Integer.parseInt(strComputersPerPage.get());
			currentPaging.setComputersPerPage(computersPerPage);
		}
		
		Optional<String> strCurrentPage = Optional.ofNullable(request.getParameter("currentPage"));
		if (strCurrentPage.isPresent()) {
			int currentPage = Integer.parseInt(request.getParameter("currentPage"));
			currentPaging.setCurrentPage(currentPage);
		}
		
		searchedWord = Optional.ofNullable(request.getParameter("search"));
		
		try {
			totalComputers = computerService.getCount(searchedWord);
			totalPage = totalComputers / currentPaging.getComputersPerPage();
			if (totalComputers % currentPaging.getComputersPerPage() != 0) {
				totalPage++;
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}

		try {
			List<Computer> computers = new ArrayList<Computer>();
			computersDTO.clear();
			if (searchedWord.isPresent()) {
				currentPaging.setComputersPerPage(DEFAULT_COMPUTERS_PER_PAGE);
				currentPaging.setCurrentPage(DEFAULT_CURRENT_PAGE);
				computers = computerService.getBySearchedWord(searchedWord.get());
			}
			else {
				computers = computerService.getByPage(currentPaging);
			}
			for (int index = 0; index < computers.size(); index++) {
				ComputerDTO computer = computerMapper.computerToComputerDTO(computers.get(index), "yyyy-MM-dd");
				computersDTO.add(computer);
			}	
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		} catch (UnknowCompanyException e) {
			logger.error("Unknow company");
		}
		
		request.setAttribute("computers", computersDTO);
		request.setAttribute("computerNumber", totalComputers);
		request.setAttribute("search", searchedWord.isPresent() ? searchedWord.get() : "");
		
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("pageMin", (currentPaging.getCurrentPage() - DEFAULT_PAGE_STEP) > 0 ? currentPaging.getCurrentPage() - DEFAULT_PAGE_STEP : 1);
		request.setAttribute("pageMax", (currentPaging.getCurrentPage() + DEFAULT_PAGE_STEP) <= totalPage ? currentPaging.getCurrentPage() + DEFAULT_PAGE_STEP : totalPage);
		request.setAttribute("currentPage", currentPaging.getCurrentPage());
		request.setAttribute("computersPerPage", currentPaging.getComputersPerPage());
		
		request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String computerToDelete = request.getParameter("selection");
		
		try {
			computerService.deleteComputerByList(computerToDelete);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		} catch (UnknowComputerException e) {
			logger.error("Unknow computer");
		}
		
		doGet(request, response);
	}

}