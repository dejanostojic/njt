/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.app.rewrite;
import com.dostojic.njt.app.login.LoginContext;
import javax.servlet.ServletContext;
import org.ocpsoft.logging.Logger.Level;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Log;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.param.Constraint;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

/**
 *
 * @author dostojic
 */
@RewriteConfiguration
public class ApplicationConfigurationProvider extends HttpConfigurationProvider {

    
    Constraint<String> isasdf = new Constraint<String>() {
      @Override
      public boolean isSatisfiedBy(Rewrite event, 
                            EvaluationContext context, String value)
      {
         return true;
      }
   };
//    
    
    @Override
    public Configuration getConfiguration(ServletContext context) {
        Condition notLoggedIn = new Condition() {
            @Override
            public boolean evaluate(Rewrite event, EvaluationContext context) {
               return !LoginContext.getInstance().isLoggedIn();
            }
        };

        
        return ConfigurationBuilder.begin()
                //Join.path("/admin/.*").to("/admin/loginForm.xhtml")
//                  .addRule()
//                .when(Direction.isInbound().and(Path.matches("/{path}")))
//                .perform()
//                .where("path").matches(".*")
//                .addRule().when(Direction.isInbound().and(notLoggedIn).and(Path.matches("/{path}").andNot(Path.matches("/admin/loginForm.xhtml"))))
//                .perform(Log.message(Level.INFO, "Client requested path: {path}").and(Redirect.permanent(context.getContextPath() + "/admin/loginForm.xhtml")))
//                .where("path").matches("^/admin/.*")
                
                .addRule(Join.path("/").to("/admin/play/index.xhtml"))
                .addRule(Join.path("/admin/{type}/$").to("/admin/{type}/index.xhtml"))
                .addRule(Join.path("/admin/{type}/index$").to("/admin/{type}/index.xhtml"))
                .addRule(Join.path("/admin/play/form/{playId}").to("/admin/play/form.xhtml")).where("playId").matches("[0-9]*")
                .addRule(Join.path("/admin/perf/form/{perfId}").to("/admin/perf/form.xhtml")).where("perfId").matches("[0-9]*")
                .addRule(Join.path("/admin/perf/tickets/{perfId}").to("/admin/perf/tickets.xhtml")).where("perfId").matches("[0-9]*")
                .addRule(Join.path("/dost/tickets/{perfId}").to("/dost/tickets/{perfId}")).where("perfId").matches("[0-9]*")
                
                .addRule(Join.path("/{path}").to("/{path}.xhtml")).when(Direction.isInbound().andNot(Path.matches("^admin/.*/[0-9]*")).andNot(Path.matches("^dost/.*"))).where("path").matches("(?!.*.xhtml).*");

    }

    @Override
    public int priority() {
        return 10;
    }
    
}
