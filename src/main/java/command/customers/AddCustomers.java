package command.customers;

import command.settings.Command;
import connection.Storage;
import db.customer.CustomerDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class AddCustomers implements Command {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {

        Storage util = Storage.getInstance();
        CustomerDaoService customerDaoService = new CustomerDaoService(util.getConnection());
        Context context = new Context();
        String customerName = req.getParameter("customerName");
        String customerProduct = req.getParameter("customerProduct");
        int edrpou = 0;
        try {
            edrpou = Integer.parseInt(req.getParameter("edrpou"));
        }catch (Exception e){
            engine.process("error_customer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
        if (!customerName.equals("") && edrpou != 0) {
            String getAllNamesCustomer = customerDaoService.addCustomer(customerName, edrpou, customerProduct);
            context.setVariable("getAllNamesCustomer", getAllNamesCustomer);
            context.setVariable("customerName", customerName);
            context.setVariable("edrpou", edrpou);
            context.setVariable("customerProduct", customerProduct);
            engine.process("customers_add", context, resp.getWriter());
            resp.getWriter().close();
        } else {
            engine.process("error_customer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
    }
}
