/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dostojic.njt.util.rewrite;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.logging.Logger.Level;
import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Log;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

/**
 *
 * @author dostojic
 */
@RewriteConfiguration
public class ApplicationConfigurationProvider extends HttpConfigurationProvider {

    
    @Override
    public Configuration getConfiguration(ServletContext context) {
        
        
        return ConfigurationBuilder.begin()
//                .addRule().when(Direction.isInbound().and(Path.matches("{path}")).and(new HttpCondition() {
//
//            @Override
//            public boolean evaluateHttp(HttpServletRewrite hsr, EvaluationContext ec) {
//                System.out.println("DEBUG :: INFO ::: scheme: " + hsr.getRequest().getScheme());
//                System.out.println("DEBUG :: INFO ::: server name: " + hsr.getRequest().getServerName());
//                System.out.println("DEBUG :: INFO ::: port: " + hsr.getRequest().getServerPort());
//                System.out.println("DEBUG :: INFO ::: requestURI:" + hsr.getRequest().getRequestURI());
//                
//                return "http".equals(hsr.getRequest().getScheme());
//            }
//        })).perform(Log.message(Level.INFO, "Client requested path: {path}"))
                .addRule(Join.path("/").to("/admin/play/index.xhtml"))
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
