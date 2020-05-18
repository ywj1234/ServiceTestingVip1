package com.ktt.wework.contact;

import com.ktt.wework.Api;
import com.ktt.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * 统一初始化类
 */
public class Contact extends Api {

    String random = String.valueOf(System.currentTimeMillis());

    @Override
    public RequestSpecification getDefaultRequestSpecification(){
        RequestSpecification requestSpecification = super.getDefaultRequestSpecification();
        requestSpecification.queryParam("access_token", Wework.getToken()).contentType(ContentType.JSON);
        requestSpecification.filter((req,res,ctx)->{
            return ctx.next(req,res);
        });
        return requestSpecification;
    }
}
