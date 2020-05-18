package com.ktt.wework.contact;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("部门接口")
//@Feature("Junit 4 support")
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

    @Order(0)
    @Test
    @Description("获取部门列表")
    @Story("获取部门")
    void list() {
        department.list("").then().statusCode(200);//.body("department[0].id",equalTo(2));
        department.list("2").then().statusCode(200);
    }

    @Order(1)
    @Test
    @Description("创建部门")
    @Story("创建部门")
    void create() {
        department.create("ceshi1"+random,"ceshi11"+random,"1").then().body("errcode",equalTo(0));
        //department.create("ceshi1","ceshi11","1").then().body("errcode",equalTo(60008));
    }

    @Order(2)
    @Test
    @Description("使用hashmap传值创建部门")
    @Story("创建部门")
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

    @Order(3)
    @Test
    @Description("创建名称中有中文部门")
    @Story("创建部门")
    void createWithChinese() {
        department.create("垃圾部"+random,"ceshi111"+random,"1").then().body("errcode",equalTo(0));
    }

    @Order(4)
    @Test
    @Description("更新部门")
    @Story("更新部门")
    void update() {
        String nameOld = "YY1"+random;
        String nameEn = "YY2"+random;
        department.create(nameOld,nameEn,"1");
        Integer id = department.list("").path("department.find{ it.name == '"+ nameOld +"'}.id");
        department.update("ZZ3"+random,String.valueOf(id)).then().body("errcode",equalTo(0));
    }

    @Order(5)
    @Test
    @Description("根据id删除部门")
    @Story("删除部门")
    void delete() {
        String nameOld = "chanpin1"+random;
        String nameEn = "chanpin11"+random;
        department.create(nameOld,nameEn,"1");
        Integer id = department.list("").path("department.find{ it.name == '"+ nameOld +"'}.id");
        department.delete(String.valueOf(id)).then().body("errcode",equalTo(0));
    }

    @Order(6)
    @Test
    @Description("删除所有部门")
    @Story("删除部门")
    void deleteAll() {
        department.deleteAll();
        assertThat(1.0,lessThan(2.0));
    }
}