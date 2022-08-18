package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ListMiddleDevelopersCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        Storage util = Storage.getInstance();
        DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());

        String ListMiddleDevelopers = developerDaoService.getListMiddleDevelopers();
        String[] words = ListMiddleDevelopers.split("<br>");
        ArrayList list = new ArrayList();
        Collections.addAll(list, words);
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("list", list)
        );
        resp.setContentType("text/html; charset=utf-8");
        engine.process("developers_list_middle", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
