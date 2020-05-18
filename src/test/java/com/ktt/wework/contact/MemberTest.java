package com.ktt.wework.contact;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.HashMap;
import static org.hamcrest.Matchers.equalTo;

class MemberTest {

    static Member member;

    @BeforeAll
    static void setUp(){
        member = new Member();
    }


    @ParameterizedTest
    @ValueSource(strings ={"yang","zhou","li"})
    void create(String name) {
        String random = String.valueOf(System.currentTimeMillis()).substring(5+0,5+8);
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("userid",name+member.random);
        map.put("name",name+member.random);
        map.put("mobile","158" + random);
        map.put("email",random + "@qq.com");
        member.create(map).then().statusCode(200).body("errcode",equalTo(0));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/member.csv")
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