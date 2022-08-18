package command.developers;

import command.settings.Command;
import connection.Storage;
import db.company.Company;
import db.company.CompanyDaoService;
import db.developer.Developer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GetInformationAboutAllDevelopersCommand implements Command {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        Storage util = Storage.getInstance();
        DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());
        List<Developer> allFullName = developerDaoService.getAllFullName();

        List<Developer> result = new ArrayList<>();
        for (Developer developer: allFullName){
            Developer list = new Developer();
            list.setDeveloperId(developer.getDeveloperId());
            list.setFullName(developer.getFullName());
            list.setBirthDate(developer.getBirthDate());
            result.add(list);
        }
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("list", result)
        );
        resp.setContentType("text/html; charset=utf-8");
        engine.process("developer_all", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }

}
