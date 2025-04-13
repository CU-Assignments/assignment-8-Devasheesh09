
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_portal";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "yourpassword"; // replace with your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentId = request.getParameter("studentId");
        String studentName = request.getParameter("studentName");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "INSERT INTO attendance (student_id, student_name, date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, studentName);
            ps.setString(3, date);
            ps.setString(4, status);

            int row = ps.executeUpdate();

            if (row > 0) {
                out.println("<h2>Attendance recorded successfully!</h2>");
            } else {
                out.println("<h2>Failed to record attendance.</h2>");
            }

            conn.close();
        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }

        out.println("<br><a href='attendance.jsp'>Go Back</a>");
        out.close();
    }
}
