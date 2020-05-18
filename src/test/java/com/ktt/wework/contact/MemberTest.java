package com.ktt.wework.contact;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("成员接口")
class MemberTest {

    static Member member;

    @BeforeAll
    static void setUp(){
        member = new Member();
    }

    @Order(1)
    @Test
    @ParameterizedTest
    @ValueSource(strings ={"yang","zhou","li"})
    @Description("模板数据创建成员")
    void create(String name) {
        String random = String.valueOf(System.currentTimeMillis()).substring(5+0,5+8);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("userid",name+member.random);
        map.put("name",name+member.random);
        map.put("mobile","158" + random);
        map.put("email",random + "@qq.com");
        member.create(map).then().statusCode(200).body("errcode",equalTo(0));
    }

    @Order(2)
    @Test
    @ParameterizedTest
    @CsvFileSource(resources = "/data/member.csv")
    @Description("数据驱动创建成员")
    @Step("测试2222:{0},{1}")
    void create(String name,String alias) {
        String random = String.valueOf(System.currentTimeMillis()).substring(5+0,5+8);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("userid",name+member.random);
        map.put("name",name+member.random);
        map.put("mobile","158" + random);
        map.put("email",random + "@qq.com");
        map.put("alias",alias);
        member.create(map).then().statusCode(200).body("errcode",equalTo(0));
    }
}