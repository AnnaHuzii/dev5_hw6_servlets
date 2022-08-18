package command.companies;

import command.settings.Command;
import connection.Storage;
import db.company.CompanyDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class AddCompanies implements Command {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        Storage util = Storage.getInstance();
        CompanyDaoService companyDaoService = new CompanyDaoService(util.getConnection());
        Context context = new Context();
        String companyName = req.getParameter("companyName");
        String description = req.getParameter("companyDescription");
       if (!companyName.equals("")&& !description.equals("")){
            String addNamesCompany = companyDaoService.addCompany(companyName, description);
            context.setVariable("addNamesCompany", addNamesCompany);
            context.setVariable("companyName", companyName);
            context.setVariable("companyDescription", description);
            engine.process("companies_add", context, resp.getWriter());
            resp.getWriter().close();
        }else {
            engine.process("error_companies_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }

    }
}
