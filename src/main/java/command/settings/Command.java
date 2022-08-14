package command.settings;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public interface Command {
    void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException;

}
