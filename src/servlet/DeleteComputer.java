package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.DatabaseException;
import exception.UnknowComputerException;
import service.ComputerService;

/**
 * Servlet implementation class DeleteComputer
 */
@WebServlet("/DeleteComputer")
public class DeleteComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	
	private ComputerService computerService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteComputer() {
        super();
        computerService = ComputerService.getInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// On récupère la liste des PC selectionné
		
		// On les supprime
		String selection = request.getParameter("selection");
		
		if (!selection.equals("")) {
			String[] parts = selection.split(",");
			for (int index = 0; index < parts.length; index++) {
				int foo = Integer.parseInt(parts[index]);
				try {
					computerService.deleteComputerById(foo);
				} catch (DatabaseException e) {
					e.printStackTrace();
				} catch (UnknowComputerException e) {
					e.printStackTrace();
				}
			}
		}
		
		doGet(request, response);
	}

}
