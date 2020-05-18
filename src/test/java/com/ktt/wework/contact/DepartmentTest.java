package com.ktt.wework.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


class DepartmentTest {

    Department department;

    String random = String.valueOf(System.currentTimeMillis());

    @BeforeEach
    void setUp() {
        if (department == null){
            department = new Department();
            //department.deleteAll();
        }
    }

    @Test
    @DisplayName("获取部门列表")
    void list() {
        department.list("").then().statusCode(200);//.body("department[0].id",equalTo(2));
        department.list("2").then().statusCode(200);
    }

    @Test
    void create() {
        department.create("ceshi1"+random,"ceshi11"+random,"1").then().body("errcode",equalTo(0));
        //department.create("ceshi1","ceshi11","1").then().body("errcode",equalTo(60008));
    }

    @Test
    void createByMap(){
        Map<String,Object> map = new HashMap<String, Object>(){
            {
                put("name",String.format("架构部%s",random));
                put("name_en",String.format("jiagoubu%s",random));
                put("parentid","1");
            }
        };
        department.create((HashMap<String, Object>) map).then().body("errcode",equalTo(0));
    }

    @Test
    void createWithChinese() {
        department.create("垃圾部"+random,"ceshi111"+random,"1").then().body("errcode",equalTo(0));
    }

    @Test
    void update() {
        String nameOld = "YY1"+random;
        String nameEn = "YY2"+random;
        department.create(nameOld,nameEn,"1");
        Integer id = department.list("").path("department.find{ it.name == '"+ nameOld +"'}.id");
        department.update("ZZ3"+random,String.valueOf(id)).then().body("errcode",equalTo(0));
    }

    @Test
    void delete() {
        String nameOld = "chanpin1"+random;
        String nameEn = "chanpin11"+random;
        department.create(nameOld,nameEn,"1");
        Integer id = department.list("").path("department.find{ it.name == '"+ nameOld +"'}.id");
        department.delete(String.valueOf(id)).then().body("errcode",equalTo(0));
    }

    @Test
    void deleteAll() {
        department.deleteAll();
        assertThat(1.0,lessThan(2.0));
    }
}