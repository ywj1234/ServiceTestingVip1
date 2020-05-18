package com.ktt.wework.contact;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;


/**
 * 成员
 */
public class Member extends Contact{

    public Response create(HashMap<String,Object> map){
        String body = template("/data/member.json",map);
        return getDefaultRequestSpecification().body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
                .then().extract().response();
    }
}
