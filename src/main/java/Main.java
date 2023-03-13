import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.fasterxml.jackson.databind.ObjectMapper;

import Services.UserService;
import models.Address;
import models.User;
import repository.UserRepositoryImpl;
 
public class Main {
 
    public static void main(String[] args) throws LifecycleException {
    	try {
			DBInitializer.createDatabaseIfNotExists();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8081);
         
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
         
        Context context = tomcat.addContext(contextPath, docBase);
         
        HttpServlet servlet = new HttpServlet() {
        	
        	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                if (request.getMethod().equalsIgnoreCase("PATCH")){
                   doPatch(request, response);
                } else {
                    super.service(request, response);
                }
            }
        	
        	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        		BufferedReader reader = request.getReader();
        	    StringBuilder sb = new StringBuilder();
        	    String line;
        	    while ((line = reader.readLine()) != null) {
        	        sb.append(line);
        	    }
        	    String requestBody = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(requestBody,User.class);
                UserRepositoryImpl impl = new UserRepositoryImpl();
                try {
					impl.createUser(user);
					response.getWriter().write("Successfully created user");
				} catch (SQLException e) {
					e.printStackTrace();
					response.getWriter().write("Failed to create user");
				}
        	    response.setContentType("application/json");
            }
        	
        	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        		BufferedReader reader = request.getReader();
        	    StringBuilder sb = new StringBuilder();
        	    String line;
        	    while ((line = reader.readLine()) != null) {
        	        sb.append(line);
        	    }
        	    String requestBody = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(requestBody,User.class);
                UserRepositoryImpl impl = new UserRepositoryImpl();
                
               List<User> users = impl.listUsers(user.getName(),user.getTitle(),user.getAddress());
               String usersJson = objectMapper.writeValueAsString(users);
               response.getWriter().write(usersJson);
        	    response.setContentType("application/json");
        	}
        	
        	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        		BufferedReader reader = request.getReader();
        	    StringBuilder sb = new StringBuilder();
        	    String line;
        	    while ((line = reader.readLine()) != null) {
        	        sb.append(line);
        	    }
        	    String requestBody = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(requestBody,User.class);
                UserRepositoryImpl impl = new UserRepositoryImpl();
                impl.updateUser(user);
				response.getWriter().write("Successfully updated user");
        	    response.setContentType("application/json");
            }
        	
        	protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        		BufferedReader reader = request.getReader();
        	    StringBuilder sb = new StringBuilder();
        	    String line;
        	    while ((line = reader.readLine()) != null) {
        	        sb.append(line);
        	    }
        	    String requestBody = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(requestBody,User.class);
                UserRepositoryImpl impl = new UserRepositoryImpl();
                impl.patchUser(user);
				response.getWriter().write("Successfully patched user");
        	    response.setContentType("application/json");
            }
        	
        	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        		BufferedReader reader = request.getReader();
        	    StringBuilder sb = new StringBuilder();
        	    String line;
        	    while ((line = reader.readLine()) != null) {
        	        sb.append(line);
        	    }
        	    String requestBody = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.readValue(requestBody,User.class);
                UserRepositoryImpl impl = new UserRepositoryImpl();
                impl.deleteUser(user.getId());
				response.getWriter().write("Successfully deleted user");
        	    response.setContentType("application/json");
            }
        };
         
        String servletName = "servlet";
        String urlPattern = "/users";
         
        tomcat.addServlet(contextPath, servletName, servlet);      
        context.addServletMappingDecoded(urlPattern, servletName);
         
        tomcat.start();
        tomcat.getServer().await();
    }
}