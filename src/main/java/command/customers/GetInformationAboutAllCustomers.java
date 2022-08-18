package command.customers;

import command.settings.Command;
import connection.Storage;
import db.customer.CustomerDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class GetInformationAboutAllCustomers implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        Storage util = Storage.getInstance();
        CustomerDaoService customerDaoService = new CustomerDaoService(util.getConnection());

        resp.setContentType("text/html; charset=utf-8");
        String getAllNames = customerDaoService.getAllNames();
        String[] words = getAllNames.split("<br>");
        ArrayList list = new ArrayList();
        Collections.addAll(list, words);
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("list", list)
        );
        engine.process("customers_all", simpleContext, resp.getWriter());

        resp.getWriter().close();
    }
}
