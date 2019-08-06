package com.jason.config;

import com.jason.controllers.Ctrller;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jc6t on 2015/4/28.
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // for freemarker
        Set<Class<?>> controllerClazz=new HashSet();
        controllerClazz.add(Ctrller.class);
        registerClasses(controllerClazz);
        register(FreemarkerMvcFeature.class);
    }
}
