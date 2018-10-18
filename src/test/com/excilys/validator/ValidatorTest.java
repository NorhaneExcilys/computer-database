package com.excilys.validator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.exception.IntroducedAfterDiscontinuedException;

import com.excilys.dto.ComputerDTO;
import com.excilys.exception.IncorrectDateException;
import com.excilys.exception.IncorrectIdException;
import com.excilys.exception.IncorrectNameException;

@Component
public class ValidatorTest {

	@Autowired
	private Validator validator;

	@Test
	public void testValidComputer() {
		String name = "My Name";
		String id = "1";
		String introducedDate = "03-01-1993";
		String discontinuedDate = "16-12-1994";
		String companyId = "5";
		String format = "dd-MM-yyyy";
		
		try {
			Validator mockedValidator = Mockito.mock(Validator.class);
			Mockito.when(mockedValidator.isValidName(name)).thenReturn(true);
			Mockito.when(mockedValidator.introducedIsBeforeDiscontinued(introducedDate, discontinuedDate, format)).thenReturn(true);
			Mockito.when(mockedValidator.isValidId(companyId)).thenReturn(true);
			ComputerDTO computerDTO = new ComputerDTO.ComputerDTOBuilder(name).id(id).introducedDate(introducedDate).discontinuedDate(discontinuedDate).companyId(companyId).build();
			Mockito.when(mockedValidator.validComputer(computerDTO, format)).thenCallRealMethod();
			assertTrue(mockedValidator.validComputer(computerDTO, format));
		} catch (IncorrectNameException e) {
			fail("IncorrectNameException unexpected");
		} catch (IntroducedAfterDiscontinuedException e) {
			fail("IntroducedAfterDiscontinuedException unexpected");
		} catch (IncorrectDateException e) {
			fail("IncorrectDateException unexpected");
		} catch (IncorrectIdException e) {
			fail("IncorrectIdException unexpected");
		}
	}
	
	@Test
	public void testIsValidName() {
		try {
			assertTrue(validator.isValidName(" My sweet Computer   "));
			assertTrue(validator.isValidName("159!!"));
		} catch (IncorrectNameException e) {
			fail("IncorrectNameException unexpected");
		}
		
		try {
			assertTrue(validator.isValidName("   "));
			fail("IncorrectNameException expected");
		} catch (IncorrectNameException e) {
			assert(true);
		}
	}
	
	@Test
	public void testIsValidId() {
		try {
			assertTrue(validator.isValidId("0"));
			assertTrue(validator.isValidId("18"));
		} catch (IncorrectIdException e) {
			fail("IncorrectIdException unexpected");
		}
		
		try {
			assertTrue(validator.isValidId("5A"));
			fail("IncorrectIdException expected");
		} catch (IncorrectIdException e) {
			assert(true);
		}
		
		try {
			assertTrue(validator.isValidId("test"));
			fail("IncorrectIdException expected");
		} catch (IncorrectIdException e) {
			assert(true);
		}
		
		try {
			assertTrue(validator.isValidId("-5"));
			fail("IncorrectIdException expected");
		} catch (IncorrectIdException e) {
			assert(true);
		}
	}

	@Test
	public void testIsValidDate() {
		try {
			assertTrue(validator.isValidDate("25-09-1992", "dd-MM-yyyy"));
			assertTrue(validator.isValidDate("01/01/1956", "dd/MM/yyyy"));
			assertTrue(validator.isValidDate("2003-11-16", "yyyy-MM-dd"));
		} catch (IncorrectDateException e) {
			fail("IncorrectDateException unexpected");
		}
		
		try {
			assertTrue(validator.isValidDate("01-13-1956", "dd-MM-yyyy"));
			fail("IncorrectDateException expected");
		} catch (IncorrectDateException e) {
			assert(true);
		}
		
		try {
			assertTrue(validator.isValidDate("Bad date", "dd-MM-yyyy"));
			fail("IncorrectDateException expected");
		} catch (IncorrectDateException e) {
			assert(true);
		}
	}
	
	@Test
	public void testIntroducedIsBeforeDiscontinued() {
		try {
			assertTrue(validator.introducedIsBeforeDiscontinued("", "", "dd/MM/yyyy"));
			assertTrue(validator.introducedIsBeforeDiscontinued("25/09/1992", "", "dd/MM/yyyy"));
			assertTrue(validator.introducedIsBeforeDiscontinued("", "18-01-2001", "dd-MM-yyyy"));
			assertTrue(validator.introducedIsBeforeDiscontinued("25/09/1992", "25/09/1992", "dd/MM/yyyy"));
			assertTrue(validator.introducedIsBeforeDiscontinued("1992-09-25", "1992-09-26", "yyyy-MM-dd"));			
		} catch (IncorrectDateException e) {
			fail("IncorrectDateException unexpected");
		} catch (IntroducedAfterDiscontinuedException e) {
			fail("IntroducedAfterDiscontinuedException unexpected");
		}
		
		try {
			assertTrue(validator.introducedIsBeforeDiscontinued("18/03/2017", "02/21/2003", "dd/MM/yyyy"));
			fail("IncorrectDateException expected");
		} catch (IncorrectDateException e) {
			assertTrue(true);
		} catch (IntroducedAfterDiscontinuedException e) {
			fail("IntroducedAfterDiscontinuedException unexpected");
		}
		
		try {
			assertTrue(validator.introducedIsBeforeDiscontinued("18/01/2013", "17/01/2013", "dd/MM/yyyy"));
			fail("IntroducedAfterDiscontinuedException expected");
		} catch (IncorrectDateException e) {
			fail("IncorrectDateException unexpected");
		} catch (IntroducedAfterDiscontinuedException e) {
			assertTrue(true);
		}
	}

}
