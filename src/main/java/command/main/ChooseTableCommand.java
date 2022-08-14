package command.main;

import command.settings.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChooseTableCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        Context context = new Context();
        context.setVariable("table", req.getParameter("table"));
        switch(req.getParameter("table")) {
            case "developers":
                resp.sendRedirect(req.getRequestURI() + "developers");
                break;
            case "project":
                resp.sendRedirect(req.getRequestURI() + "project");
                break;
            case "companies":
                resp.sendRedirect(req.getRequestURI() + "companies");
                break;
            case "customers":
                resp.sendRedirect( req.getRequestURI() + "customers");
                break;
        }
    }
}
