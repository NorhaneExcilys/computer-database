package persistance;

import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.mockito.runners.MockitoJUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class CompanyDAOTest {
	
	@Mock
	DataSource mockDataSource;
	@Mock
	Connection mockConn;
	@Mock
	PreparedStatement mockPreparedStmnt;
	@Mock
	ResultSet mockResultSet;
	int userId = 100;

	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
