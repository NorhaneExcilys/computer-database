package com.excilys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.dto.ComputerDTO;
import com.excilys.exception.DatabaseException;
import com.excilys.exception.IncorrectComputerDTOException;
import com.excilys.exception.IncorrectDateException;
import com.excilys.exception.IncorrectIdException;
import com.excilys.exception.IncorrectNameException;
import com.excilys.exception.IntroducedAfterDiscontinuedException;
import com.excilys.exception.UnknowCompanyException;
import com.excilys.exception.UnknowComputerException;
import com.excilys.mapper.ComputerMapper;
import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Paging;
import com.excilys.service.CompanyService;
import com.excilys.service.ComputerService;



@Controller
@RequestMapping
public class ComputerController {
 
	private Logger logger = LoggerFactory.getLogger("ComputerController");
	
	private final static int DEFAULT_COMPUTERS_PER_PAGE = 50;
	private final static int DEFAULT_CURRENT_PAGE = 1;
	private final static int DEFAULT_PAGE_STEP = 3;
	
	private ComputerService computerService;
	private ComputerMapper computerMapper;
	private CompanyService companyService;
	
	private List<ComputerDTO> computersDTO;
	private Paging currentPaging;
	private int totalPage;
	private int totalComputers;
	private Optional<String> searchedWord;

	
	public ComputerController(ComputerService computerService, ComputerMapper computerMapper, CompanyService companyService) {
		this.computerService = computerService;
		this.computerMapper = computerMapper;
		this.companyService = companyService;
		
		computersDTO = new ArrayList<ComputerDTO>();
		currentPaging = new Paging(DEFAULT_COMPUTERS_PER_PAGE, DEFAULT_CURRENT_PAGE);
		totalPage = 0;
		totalComputers = 0;
	}
	
	
	@GetMapping("/dashboard")
    public String getDashboard(final ModelMap pModel,
    		@RequestParam(required = false) String computersPerPage,
    		@RequestParam(required = false) String currentPage,
    		@RequestParam(required = false) String search) {
		Optional<String> strComputersPerPage = Optional.ofNullable(computersPerPage);
		if (strComputersPerPage.isPresent()) {
			currentPaging.setComputersPerPage(Integer.parseInt(strComputersPerPage.get()));
		}
		
		Optional<String> strCurrentPage = Optional.ofNullable(currentPage);
		if (strCurrentPage.isPresent()) {
			currentPaging.setCurrentPage(Integer.parseInt(currentPage));
		}

		searchedWord = Optional.ofNullable(search);
		
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
				computers = computerService.getBySearchedWord(searchedWord.get(), currentPaging);
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
		
		pModel.addAttribute("computerNumber", totalComputers);
		
		pModel.addAttribute("computers", computersDTO);
		pModel.addAttribute("computerNumber", totalComputers);
		pModel.addAttribute("search", searchedWord.isPresent() ? searchedWord.get() : "");
		
		pModel.addAttribute("totalPage", totalPage);
		pModel.addAttribute("pageMin", (currentPaging.getCurrentPage() - DEFAULT_PAGE_STEP) > 0 ? currentPaging.getCurrentPage() - DEFAULT_PAGE_STEP : 1);
		pModel.addAttribute("pageMax", (currentPaging.getCurrentPage() + DEFAULT_PAGE_STEP) <= totalPage ? currentPaging.getCurrentPage() + DEFAULT_PAGE_STEP : totalPage);
		pModel.addAttribute("currentPage", currentPaging.getCurrentPage());
		pModel.addAttribute("computersPerPage", currentPaging.getComputersPerPage());

		return "dashboard";
    }
	
	@GetMapping("/addComputer")
	public String getAddComputer(final ModelMap pModel) {
		List<Company> companies = new ArrayList<Company>();
		try {
			companies = companyService.getCompanies();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		pModel.addAttribute("companies", companies);
		
		return "addComputer";
	}
	
	@PostMapping("/addComputer")
	public String addComputer(final ModelMap pModel,
			@RequestParam(required = true) String computerName,
			@RequestParam(required = false, value="introduced") String strIntroducedDate,
			@RequestParam(required = false, value="discontinued") String strDiscontinuedDate,
			@RequestParam(required = false, value="companyId") String strCompanyId) {
		
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
			return "redirect:dashboard";
		}
		else {
			return "addComputer";		
		}

	}
	
	@GetMapping("/editComputer")
	public String getEditComputer(final ModelMap pModel, @RequestParam(required = true, value="id") String strId) {
		long id = -1;
		try {
			id = Long.parseLong(strId);
		} catch(NumberFormatException e) {
			logger.error(e.getMessage());
		}
		
		Optional<Computer> computer;
		try {
			computer = computerService.getById(id);
			if (computer.isPresent()) {
				ComputerDTO computerDTO = computerMapper.computerToComputerDTO(computer.get(), "yyyy-MM-dd");
				pModel.addAttribute("id", computerDTO.getId());
				pModel.addAttribute("name", computerDTO.getName());
				pModel.addAttribute("introduced", computerDTO.getIntroducedDate());
				pModel.addAttribute("discontinued", computerDTO.getDiscontinuedDate());
				pModel.addAttribute("companyId", computerDTO.getCompanyId());
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
		
		pModel.addAttribute("companies", companies);
		
		return "editComputer";
	}
	
	
	@PostMapping("/editComputer")
	public String editComputer(final ModelMap pModel,
			@RequestParam(required = true, value="id") String strId,
			@RequestParam(required = false, value="name") String strName,
			@RequestParam(required = false, value="introduced") String strIntroducedDate,
			@RequestParam(required = false, value="discontinued") String strDiscontinuedDate,
			@RequestParam(required = false, value="companyId") String strCompanyId) {
		long id = Long.parseLong(strId);
		
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
			return "redirect:dashboard";
		}
		else {
			return "editComputer";		
		}
	}
	
	@PostMapping("/deleteComputer")
	public String deleteComputer(final ModelMap pModel,
			@RequestParam(required = true, value="selection") String computerToDelete) {
		
		if (computerToDelete != "") {
			try {
				computerService.deleteComputerByList(computerToDelete);
			} catch (DatabaseException e) {
				logger.error(e.getMessage());
			} catch (UnknowComputerException e) {
				logger.error("Unknow computer");
			}
		}
		
		return "redirect:dashboard";
	}
	

     
}