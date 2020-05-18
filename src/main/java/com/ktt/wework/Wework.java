package com.ktt.wework;

import io.restassured.RestAssured;

public class Wework {

    private static String token;

    public static String getWechatToken(String secret){
        return RestAssured.given()
                .queryParam("corpid", WeworkConfig.getInstance().corpid)
                .queryParam("corpsecret", secret)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then().statusCode(200).log().all()
                .extract().path("access_token");
    }

//    public static String getWeworkTokenForContact(){
//
//    }

    public static String getToken(){
        //todo: 支持两种类型token
        if (token == null){
            token = getWechatToken(WeworkConfig.getInstance().contacSecret);
        }
        return token;
    }
}
