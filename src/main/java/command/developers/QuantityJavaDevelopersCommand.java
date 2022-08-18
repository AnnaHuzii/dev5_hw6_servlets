package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuantityJavaDevelopersCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write("<br>" + currentTime + "</br>");

        Context context = new Context();
        Storage util = Storage.getInstance();

        DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());

        int QuantityJavaDevelopers = developerDaoService.getQuantityJavaDevelopers();
        context.setVariable("QuantityJavaDevelopers", QuantityJavaDevelopers);
        engine.process("developers_quantity_java", context, resp.getWriter());
        resp.getWriter().close();

    }
}
