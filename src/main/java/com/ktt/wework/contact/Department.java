package com.ktt.wework.contact;


import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;

/**
 * 部门接口
 */
public class Department extends Contact{


    /**
     * 获取部门
     * @param id
     * @return
     */
    public Response list(String id){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        return getResponseFromYaml("/api/list.yaml",map);
    }

    /**
     * 添加部门
     * @param name
     * @param nameEn
     * @param parentid
     * @return
     */
    public Response create(String name,String nameEn,String parentid){
        HashMap<String,Object> map = new HashMap<>();
        map.put("_file","/data/create.json");
        map.put("name",name);
        map.put("name_en",nameEn);
        map.put("parentid",parentid);
        return getResponseFromYaml("/api/create.yaml",map);
    }

    public Response create(HashMap<String,Object> map){
        map.put("_file","/data/create.json");
        return getResponseFromYaml("/api/create.yaml",map);
    }

    public Response update(String name,String id){
        HashMap<String,Object> map = new HashMap<>();
        map.put("_file","/data/update.json");
        map.put("name",name);
        map.put("id",id);
        return getResponseFromYaml("/api/update.yaml",map);
    }


    public Response delete(String id){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        return getResponseFromYaml("/api/delete.yaml",map);
    }

    public void deleteAll(){
        List<Integer> idList = list("").then().extract().path("department.id");
        idList.forEach(id->delete(id.toString()));
    }
}
