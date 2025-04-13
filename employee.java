import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database config
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "yourpassword"; // replace with your actual password

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empId = request.getParameter("id");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            if (empId != null && !empId.trim().isEmpty()) {
                // Search by ID
                String sql = "SELECT * FROM employees WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(empId));
                ResultSet rs = pst.executeQuery();

                out.println("<h2>Employee Details</h2>");
                if (rs.next()) {
                    out.println("<p>ID: " + rs.getInt("id") + "</p>");
                    out.println("<p>Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Department: " + rs.getString("department") + "</p>");
                    out.println("<p>Email: " + rs.getString("email") + "</p>");
                } else {
                    out.println("<p>No employee found with ID: " + empId + "</p>");
                }

            } else {
                // Display all employees
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

                out.println("<h2>All Employees</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th><th>Email</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" +
                            rs.getString("name") + "</td><td>" +
                            rs.getString("department") + "</td><td>" +
                            rs.getString("email") + "</td></tr>");
                }
                out.println("</table>");
            }

            conn.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }

        out.close();
    }
}
