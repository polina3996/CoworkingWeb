//package coworking;
//
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRegistration;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.context.support.XmlWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//
//public class CoworkingSpaceReservationApp implements WebApplicationInitializer {
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        //AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        XmlWebApplicationContext context = new XmlWebApplicationContext();
//        //context.register(WebConfig.class);
//        context.setConfigLocation("/WEB-INF/WebConfiguration.xml");
//
//        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//    }
//}