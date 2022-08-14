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

public class AddCustomers implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();
        CustomerDaoService customerDaoService = new CustomerDaoService(storage.getConnection());

        String customerName = "";
        int EDRPOU = 0;
        String customerProduct = "";
        try {
            customerName = req.getParameter("customerName");
            EDRPOU = Integer.parseInt(req.getParameter("EDRPOU"));

        } catch (Exception e) {
            resp.getWriter().write("<h1>You have entered incorrect data, please try again</h1>");
            out.println("<p >" +
                    "<form action=/hw_servlets/customers method=GET>" +
                    "<input type=submit value='Customers menu' />" +
                    "</p>");
            resp.getWriter().close();
        }
        customerProduct = req.getParameter("customerProduct");

        String getAllNamesCustomer = customerDaoService.addCustomer(customerName, EDRPOU, customerProduct);
        resp.getWriter().write("<h1>" + getAllNamesCustomer + "</h1>");
        resp.getWriter().write("<h1>" + "Name of the client company" + customerName + "</h1>");
        resp.getWriter().write("<h3>" + "EDRPOU -" + EDRPOU + "</h3>");
        resp.getWriter().write("<h3>" + "Deputy product: " + customerProduct + "</h3>");
        out.println("<p >" +
                "<form action=/hw_servlets/customers method=GET>" +
                "<input type=submit value='Customers menu' />" +
                "</p>");
        resp.getWriter().close();

    }
}
