package com.jason.controllers;

import org.glassfish.jersey.server.mvc.Template;
import org.glassfish.jersey.server.mvc.Viewable;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jc6t on 2015/4/28.
 */
@Path("/")
public class Ctrller {

    @GET
    public Viewable index() {
        return new Viewable("/login.ftl");
    }

    private static Map<String, Object> loginTime = new HashMap<String, Object>();

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/login")
    public Response login(@FormParam("account") String account, @FormParam("password") String password) {
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("gary", "11111");
        user.put("apple", "00000");
        user.put("bob", "22222");
        user.put("使用者", "44444");
        String wrong = "";

        Map<String, Object> result = new HashMap<String, Object>();
        if (user.containsKey(account)) {
            if (password.equals(user.get(account))) {
                result.put("name", account);
                if (loginTime.containsKey(account)) {
                    result.put("lastlogintime", loginTime.get(account));
                } else {
                    DateTime dateTime = new DateTime();
                    loginTime.put(account, dateTime);
                    result.put("lastlogintime", dateTime);
                }
                return Response.status(200).entity(new Viewable("/hello.ftl", result)).header("Access-Control-Allow-Origin", "*").build();
            } else {
                wrong = "Password";
            }
        } else {
            wrong = "Account";
        }
        return Response.status(401).entity("Please Try Again , Enter Right " + wrong).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/foo1")
    public Viewable viewTemplate() {
        Map map = new HashMap();
        map.put("foo", "this is foo1.");
        map.put("bar", "this is bar1.");
        return new Viewable("/foo1.ftl", map);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/foo2")
    public Response responseTemplate() {
        Map map = new HashMap();
        map.put("foo", "this is foo2.");
        map.put("bar", "this is bar2.");
        return Response.ok(new Viewable("/foo2.ftl", map)).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/foo3")
    @Template(name = "/foo3.ftl")
    public Map returnTemplate() {
        Map map = new HashMap();
        map.put("foo", "this is foo3.");
        map.put("bar", "this is bar3.");
        return map;
    }
}
