package com.excilys.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.exception.IntroducedAfterDiscontinuedException;
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
import com.excilys.validator.Validator;

@Component
public class ComputerMapper {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private DateMapper dateMapper;
	
	@Autowired
	private CompanyService companyService;
	
	public ComputerDTO computerToComputerDTO(Computer computer, String format) {
		String id = Long.toString(computer.getId());
		String name = computer.getName();
		String introducedDate = dateMapper.localDateToString(computer.getIntroducedDate(), format);
		String discontinuedDate = dateMapper.localDateToString(computer.getDiscontinuedDate(), format);
		String companyId = computer.getCompany().isPresent() ? Long.toString(computer.getCompany().get().getId()) : "";
		String companyName = computer.getCompany().isPresent() ? computer.getCompany().get().getName() : "";
		
		return new ComputerDTO.ComputerDTOBuilder(name).id(id).introducedDate(introducedDate).discontinuedDate(discontinuedDate).companyId(companyId).companyName(companyName).build();
	}
	
	public Computer computerDTOToComputer(ComputerDTO computerDTO, String format) throws IncorrectNameException, IncorrectIdException, IncorrectDateException, IntroducedAfterDiscontinuedException, IncorrectComputerDTOException, UnknowCompanyException, DatabaseException {			
		if (validator.validComputer(computerDTO, format)) {
			String name = computerDTO.getName();
			Optional<Long> id = computerDTO.getId() == null ? Optional.empty() : Optional.of(Long.parseLong(computerDTO.getId()));
			Optional<LocalDate> introducedDate = dateMapper.stringToLocalDate(computerDTO.getIntroducedDate(), format);
			Optional<LocalDate> discontinuedDate = dateMapper.stringToLocalDate(computerDTO.getDiscontinuedDate(), format);
			Optional<String> strCompanyId = Optional.ofNullable(computerDTO.getCompanyId());
			Optional<Company> company = strCompanyId.isPresent() ? companyService.getCompanyById(Long.parseLong(computerDTO.getCompanyId())) : Optional.empty();
			if (id.isPresent()) {
				return new Computer.ComputerBuilder(name).id(id.get()).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
			}
			return new Computer.ComputerBuilder(name).introducedDate(introducedDate).discontinuedDate(discontinuedDate).company(company).build();
		}
		else {
			throw new IncorrectComputerDTOException("Impossible to convert the computerDTO to computer");
		}
	}
	
}