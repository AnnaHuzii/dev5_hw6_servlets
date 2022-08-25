package command.companies;

import command.settings.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompaniesMenuCommand implements Command {

    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Context context = new Context();
        context.setVariable("table", req.getParameter("table"));

        engine.process("companies", context, resp.getWriter());
        resp.getWriter().close();
    }


}