package command.companies;

import command.settings.Command;
import connection.Storage;
import db.company.CompanyDaoService;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;

public class AddCompanies implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();
        CompanyDaoService companyDaoService = new CompanyDaoService(storage.getConnection());

        String companyName = "";
        String description = "";
        try {
            companyName = req.getParameter("companyName");
            description = req.getParameter("companyDescription");

        } catch (Exception e) {
            resp.getWriter().write("<h1>You have entered incorrect data, please try again</h1>");
            out.println("<p >" +
                    "<form action=/hw_servlets/companies method=GET>" +
                    "<input type=submit value='Сompanies menu' />" +
                    "</p>");
            resp.getWriter().close();
        }

        String getAllNamesCompany = companyDaoService.addCompany(companyName, description);
        resp.getWriter().write("<h1>" + getAllNamesCompany + "</h1>");
        resp.getWriter().write("<h3>" + companyName + "</h3>");
        resp.getWriter().write("<h3>" + "Company description: " + description + "</h3>");
        out.println("<p >" +
                "<form action=/hw_servlets/companies method=GET>" +
                "<input type=submit value='Сompanies menu' />" +
                "</p>");
        resp.getWriter().close();

    }
}
