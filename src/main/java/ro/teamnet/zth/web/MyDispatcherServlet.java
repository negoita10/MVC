package ro.teamnet.zth.web;

import oracle.net.aso.e;
import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.appl.domain.Department;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandru.Negoita on 7/20/2017.
 */
public class MyDispatcherServlet extends HttpServlet {
    Map<String, MethodAttributes> allowedMethods = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            Iterable<Class> classIterable = AnnotationScanUtils.getClasses("ro.teamnet.zth.appl.controller");
            for (Class a : classIterable) {
                Method[] methods = a.getDeclaredMethods();
                for (Method method : methods) {
                    MethodAttributes methodAttributes = new MethodAttributes();
                    methodAttributes.setMethodName(method.getName());
                    methodAttributes.setMethodType(method.getAnnotation(MyRequestMethod.class).methodType());
                    methodAttributes.setControllerClass(a.getName());
                    MyController my = (MyController) a.getAnnotation(MyController.class);
                    String key = a.getClass().getAnnotation(MyController.class).urlPath() +
                            method.getAnnotation(MyRequestMethod.class).urlPath() +
                            method.getAnnotation(MyRequestMethod.class).methodType();
                    allowedMethods.put(key, methodAttributes);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodType = "POST";
        dispatchReply(request, response, methodType);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodType = "GET";
        dispatchReply(request, response, methodType);
    }

    public void dispatchReply(HttpServletRequest request, HttpServletResponse response, String methodType) {
        try {
            Object resultToDisplay = dispatch(request, methodType);
            reply(response, resultToDisplay);
        } catch (Exception e) {
            sendExceptionError(e);
        }
    }

    private void sendExceptionError(Exception e) {
        System.out.println("eroareeeeee" + e.toString());
    }

    private void reply(HttpServletResponse response, Object result) {
        try {
            response.getWriter().write((String) result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object dispatch(HttpServletRequest request, String methodType) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

//        StringBuffer url = request.getRequestURL();
//
//        String resp="";
//        if(url.toString().contains("/employees"))
//            if(url.toString().contains("/all")) {
//                EmployeeController ec = new EmployeeController();
//                resp = ec.getAllEmployees();
//            }
//            else if(url.toString().contains("/departments")) {
//                DepartmentController ec = new DepartmentController();
//                resp = ec.getAllDepartments();
//            }
//        return resp;
        String url = request.getPathInfo();
        MethodAttributes attributes = null;
        String path;
        if (!url.startsWith("/departments") && !url.startsWith("/employees") && !url.startsWith("/jobs") && !url.startsWith("/locations"))
            throw new RuntimeException("!");
        path = url + "..." + methodType;
        attributes = allowedMethods.get(path);
        if (attributes != null) {

                Class returnedClass = Class.forName(attributes.getControllerClass());
                Method returnedMethod = returnedClass.getMethod(attributes.getMethodName());
                return returnedMethod.invoke(returnedClass.newInstance());

        }
//
//
//        if(attributes != null){
//                try {
//            Class returnedClass = Class.forName(attributes.getControllerClass());
//           Method returnedMethod = returnedClass.getMethod(attributes.getMethodName());
//            return returnedMethod.invoke(returnedClass.newInstance());
//       } catch (ClassNotFoundException e){
//            e.printStackTrace();
//       } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//       } catch (IllegalAccessException e) {
//            e.printStackTrace();
//       } catch (InstantiationException e) {
//            e.printStackTrace();
//       } catch (InvocationTargetException e) {
//            e.printStackTrace();
//       }
//        }
//        return null;
    }
}
