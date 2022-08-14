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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetInformationAboutAllCompanies implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();

        CompanyDaoService companyDaoService = new CompanyDaoService(storage.getConnection());

        resp.setContentType("text/html; charset=utf-8");

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        resp.getWriter().write("<h1>List of all IT companies</h1>");

        String getAllNamesCompany = companyDaoService.getAllNames();
        resp.getWriter().write("<br>" + getAllNamesCompany + "</br>");

        out.println("<p >" +
                "<form action=/hw_servlets/companies method=GET>" +
                "<input type=submit value='Companies menu' />" +
                "</p>");

        resp.getWriter().close();
    }
}
