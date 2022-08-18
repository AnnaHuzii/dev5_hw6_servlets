package command.companies;

import command.settings.Command;
import connection.Storage;
import db.company.Company;
import db.company.CompanyDaoService;
import db.developer.Developer;
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
import java.util.*;

public class GetInformationAboutAllCompanies implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        Storage util = Storage.getInstance();
        CompanyDaoService companyDaoService = new CompanyDaoService(util.getConnection());
        List <Company> getAllNamesCompany = companyDaoService.getAllNames();

        List<Company> result = new ArrayList<>();
       for (Company company: getAllNamesCompany){
           Company list = new Company();
           list.setCompanyId(company.getCompanyId());
           list.setCompanyName(company.getCompanyName());
           list.setDescription(company.getDescription());
           result.add(list);
        }
        Context  simpleContext = new Context(
                req.getLocale(),
                Map.of("list", result)
        );
        resp.setContentType("text/html; charset=utf-8");
        engine.process("companies_all", simpleContext, resp.getWriter());
        resp.getWriter().close();

    }
}
