package command.customers;

import command.settings.Command;
import connection.Storage;
import db.company.CompanyDaoService;
import db.customer.CustomerDaoService;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetInformationAboutAllCustomers implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();

        CustomerDaoService customerDaoService = new CustomerDaoService(storage.getConnection());

        resp.setContentType("text/html; charset=utf-8");

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        resp.getWriter().write("<h1>List of all customers</h1>");

        String getAllNames = customerDaoService.getAllNames();
        resp.getWriter().write("<br>" + getAllNames + "</br>");

        out.println("<p >" +
                "<form action=/hw_servlets/customers method=GET>" +
                "<input type=submit value='Customers menu' />" +
                "</p>");

        resp.getWriter().close();
    }
}
