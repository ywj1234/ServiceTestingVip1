package com.ktt.wework.devtools;

import com.ktt.wework.Api;
import com.ktt.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class App extends Api {

    /*@Override
    public RequestSpecification getDefaultRequestSpecification(){
        RequestSpecification requestSpecification = super.getDefaultRequestSpecification();
        requestSpecification.queryParam("access_token", Wework.getToken()).contentType(ContentType.JSON);
        requestSpecification.filter((req,res,ctx)->{
            return ctx.next(req,res);
        });
        return requestSpecification;
    }

    public Response listApp(){
        return getResponseFromHar("/api/app.har.json",".*tid=41.*",null);
    }*/
}
