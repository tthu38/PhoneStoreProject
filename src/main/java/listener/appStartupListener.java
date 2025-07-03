/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import service.WebConfigLoader;

/**
 * Web application lifecycle listener.
 *
 * @author Admin
 */
public class appStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
         ServletContext context = sce.getServletContext();
        WebConfigLoader.init(context); // Gọi init ở đây
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
