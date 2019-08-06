package com.jason;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;
import java.security.ProtectionDomain;

/**
 * Created by jc6t on 2015/4/28.
 */
public class JettyMain {
    public static void main(String[] args) throws Exception {
        final int port = 80;
        Server server = new Server(port);
        WebAppContext app = new WebAppContext();

        ProtectionDomain domain = JettyMain.class.getProtectionDomain();
        URL warLocation = domain.getCodeSource().getLocation();

        app.setDescriptor(warLocation.getPath() + ".idea/web.xml");
        app.setServer(server);
        app.setWar(warLocation.toExternalForm());

//        app.setResourceBase("D:\\Projects\\HelloWorld\\hello-jjf\\src\\main\\webapp");
        server.setHandler(app);
        server.start();
        server.join();
    }
}
