package Mortar.Servlet;

import Mortar.DB.DBSupport;
import Mortar.Model.AmmunitionType;
import Mortar.Model.MortarData;
import Mortar.Model.MortarType;
import Mortar.Service.CalculatorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MortarCalculatorServlet extends HttpServlet {

    private CalculatorService calculatorService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize your CalculatorService here
        calculatorService = new CalculatorService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("fetchData".equals(action)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            try {
                List<MortarData> mortarDataList = fetchDataFromDB();
                StringBuilder htmlBuilder = new StringBuilder();
                for (MortarData data : mortarDataList) {
                    htmlBuilder.append("<tr>")
                            .append("<td>").append(data.getMortarType()).append("</td>")
                            .append("<td>").append(data.getAmmunitionType()).append("</td>")
                            .append("<td>").append(data.getMinElevation()).append("</td>")
                            .append("<td>").append(data.getMaxElevation()).append("</td>")
                            .append("<td>").append(data.getMinRange()).append("</td>")
                            .append("<td>").append(data.getMaxRange()).append("</td>")
                            .append("<td>").append(data.getFiringRange()).append("</td>")
                            .append("<td>").append(data.getElevationMils()).append("</td>")
                            .append("</tr>");
                }
                out.print(htmlBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace(); // Replace with proper logging
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred processing your request");
            }
            return; // Stop further processing since the data has been sent
        }

        // Serve the index.html file only if the action is not 'fetchData'
        if (action == null || action.isEmpty() || !action.equals("fetchData")) {
            String htmlFilePath = getServletContext().getRealPath("/index.html");
            String htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath)));
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(htmlContent);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "calculate":
                performCalculation(request, response);
                break;
            case "saveData":
                saveData(request, response);
                break;
            case "deleteData":
                deleteData(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    private void performCalculation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String mortarTypeString = request.getParameter("mortarType");
            String ammunitionTypeString = request.getParameter("ammunitionType");
            String firingRangeString = request.getParameter("firingRange");

            if (mortarTypeString == null || mortarTypeString.isEmpty()) {
                throw new IllegalArgumentException("mortar parameters are missing.");
            }
            if (ammunitionTypeString == null || ammunitionTypeString.isEmpty()) {
                throw new IllegalArgumentException("ammunition parameters are missing.");
            }
            if (firingRangeString == null || firingRangeString.isEmpty()) {
                throw new IllegalArgumentException("Range parameters are missing.");
            }

            // Validate mortar type
            if (!isValidEnumValue(MortarType.class, mortarTypeString)) {
                throw new IllegalArgumentException("Invalid mortar type: " + mortarTypeString);
            }

            // Validate ammunition type
            if (!isValidEnumValue(AmmunitionType.class, ammunitionTypeString)) {
                throw new IllegalArgumentException("Invalid ammunition type: " + ammunitionTypeString);
            }

            int firingRange = Integer.parseInt(firingRangeString);
            MortarType mortarType = MortarType.valueOf(mortarTypeString);
            AmmunitionType ammunitionType = AmmunitionType.valueOf(ammunitionTypeString);

            // Validate firing range
            if (firingRange < mortarType.getMinRange() || firingRange > mortarType.getMaxRange()) {
                throw new IllegalArgumentException("Firing range out of bounds for selected mortar type.");
            }

            int elevation = calculatorService.calculateElevation(mortarType, ammunitionType, firingRange);

            // Send the elevation value as plain text response
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.valueOf(elevation));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: Invalid number format for firing range.");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: An error occurred: " + e.getMessage());
        }
    }

    private <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumClass, String enumValue) {
        try {
            Enum.valueOf(enumClass, enumValue);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void saveData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String mortarType = request.getParameter("mortarType");
            String ammunitionType = request.getParameter("ammunitionType");
            int minElevation = Integer.parseInt(request.getParameter("minElevation"));
            int maxElevation = Integer.parseInt(request.getParameter("maxElevation"));
            int minRange = Integer.parseInt(request.getParameter("minRange"));
            int maxRange = Integer.parseInt(request.getParameter("maxRange"));
            int firingRange = Integer.parseInt(request.getParameter("firingRange"));
            int elevationMils = Integer.parseInt(request.getParameter("elevationMils"));

            Connection conn = DBSupport.establishConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO MortarData (mortar_type, ammunition_type, minElevation, maxElevation, minRange, maxRange, firing_range, elevation_mils) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, mortarType);
            preparedStatement.setString(2, ammunitionType);
            preparedStatement.setInt(3, minElevation);
            preparedStatement.setInt(4, maxElevation);
            preparedStatement.setInt(5, minRange);
            preparedStatement.setInt(6, maxRange);
            preparedStatement.setInt(7, firingRange);
            preparedStatement.setInt(8, elevationMils);
            int rowsAffected = preparedStatement.executeUpdate();
            conn.close();

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            if (rowsAffected > 0) {
                out.print("Data saved successfully.");
            } else {
                out.print("No data saved.");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving data: " + e.getMessage());
        }
    }

    private void deleteData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String mortarType = request.getParameter("mortarType");
            String ammunitionType = request.getParameter("ammunitionType");
            int minElevation = Integer.parseInt(request.getParameter("minElevation"));
            int maxElevation = Integer.parseInt(request.getParameter("maxElevation"));
            int minRange = Integer.parseInt(request.getParameter("minRange"));
            int maxRange = Integer.parseInt(request.getParameter("maxRange"));
            int firingRange = Integer.parseInt(request.getParameter("firingRange"));
            int elevationMils = Integer.parseInt(request.getParameter("elevationMils"));

            Connection conn = DBSupport.establishConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM MortarData WHERE mortar_type = ? AND ammunition_type = ? AND minElevation = ? AND maxElevation = ? AND minRange = ? AND maxRange = ? AND firing_range = ? AND elevation_mils = ?");
            preparedStatement.setString(1, mortarType);
            preparedStatement.setString(2, ammunitionType);
            preparedStatement.setInt(3, minElevation);
            preparedStatement.setInt(4, maxElevation);
            preparedStatement.setInt(5, minRange);
            preparedStatement.setInt(6, maxRange);
            preparedStatement.setInt(7, firingRange);
            preparedStatement.setInt(8, elevationMils);

            int rowsAffected = preparedStatement.executeUpdate();
            conn.close();

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            if (rowsAffected > 0) {
                out.print("Data deleted successfully.");
            } else {
                out.print("No data deleted. Record may not exist.");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting data: " + e.getMessage());
        }
    }

    private List<MortarData> fetchDataFromDB() throws ClassNotFoundException {
        List<MortarData> mortarDataList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBSupport.establishConnection();
            stmt = conn.createStatement();
            String query = "SELECT * FROM MortarData";
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                MortarData data = new MortarData();
                data.setMortarType(rs.getString("mortar_type"));
                data.setAmmunitionType(rs.getString("ammunition_type"));
                data.setMinElevation(rs.getInt("minElevation"));
                data.setMaxElevation(rs.getInt("maxElevation"));
                data.setMinRange(rs.getInt("minRange"));
                data.setMaxRange(rs.getInt("maxRange"));
                data.setFiringRange(rs.getInt("firing_range"));
                data.setElevationMils(rs.getInt("elevation_mils"));
                mortarDataList.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with better error handling
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace(); // Replace with better error handling
            }
        }
        return mortarDataList;
    }

}
