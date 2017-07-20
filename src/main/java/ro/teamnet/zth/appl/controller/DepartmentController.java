package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyRequestMethod;

/**
 * Created by Alexandru.Negoita on 7/20/2017.
 */
public class DepartmentController {
    @MyRequestMethod(urlPath="/all",methodType="GET")
    public String getAllDepartments(){
        return "allDepartments";
    }

    @MyRequestMethod(urlPath = "/one", methodType = "GET")
    public String getOneDepartments(){
        return "OneDepartment";
    }
}
