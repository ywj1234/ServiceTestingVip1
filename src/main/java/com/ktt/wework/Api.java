package com.ktt.wework;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.restassured.RestAssured.*;


public class Api {

    HashMap<String,Object> query = new HashMap<String, Object>();


    public Api(){
        useRelaxedHTTPSValidation();
    }

    /**
     * 数据模板，根据传入的参数进行覆盖模板
     * @param path
     * @param map
     * @return
     */
    public static String template(String path,HashMap<String,Object> map){
        DocumentContext documentContext = JsonPath.parse(Api.class.getResourceAsStream(path));
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            documentContext.set((String) entry.getKey(),entry.getValue());
        }
        return documentContext.jsonString();
    }

    public RequestSpecification getDefaultRequestSpecification(){
        return given().log().all();
    }

    public Response getResponseFromHar(String path,String pattern,HashMap<String,Object> map){
        Restful restful = getApiFromHar(path,pattern);
        restful = updateApiFromMap(restful,map);
        return getResponseFromRestful(restful);
    }

    public Restful getApiFromHar(String path,String pattern){
        HarReader harReader = new HarReader();
        Har har = null;
        try {
            har = harReader.readFromFile(new File(URLDecoder.decode(getClass().getResource(path).getPath(),"utf-8")));
            HarRequest harRequest = null;

            Boolean match = false;
            for (HarEntry entry : har.getLog().getEntries()){
                harRequest = entry.getRequest();
                if (harRequest.getUrl().matches(pattern)){
                    match = true;
                    break;
                }
            }

            if (match == false){
                harRequest = null;
                throw new Exception("错误");
            }

            Restful restful = new Restful();
            restful.method = harRequest.getMethod().name().toLowerCase();
            restful.url = harRequest.getUrl();
            harRequest.getQueryString().forEach(wq->{
                restful.query.put(wq.getName(),wq.getValue());
            });
            restful.body = harRequest.getPostData().getText();
            return restful;
        } catch (HarReaderException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Restful getApiFromYaml(String path){
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            return objectMapper.readValue(WeworkConfig.class.getResourceAsStream(path), Restful.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Restful updateApiFromMap(Restful restful,HashMap<String,Object> map){

        if (map == null){
            return restful;
        }

        if (restful.method.toLowerCase().contains("get")){
            map.entrySet().forEach(entry->{
                restful.query.replace(entry.getKey(),entry.getValue().toString());
            });
        }
        if (restful.method.toLowerCase().contains("post")){
            if (map.containsKey("_body")){
                restful.body = map.get("_body").toString();
            }
            if (map.containsKey("_file")){
                String filePath = map.get("_file").toString();
                map.remove("_file");
                restful.body = template(filePath,map);
            }
        }
        return restful;
    }

    public Response getResponseFromRestful(Restful restful){
        RequestSpecification requestSpecification = getDefaultRequestSpecification();

        if (restful.query != null) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }

        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }
        String[] url=updateUrl(restful.url);
        return requestSpecification.log().all()
                .when().request(restful.method, restful.url)
                .then().log().all()
                .extract().response();
    }

    /**
     * 根据yaml文件生成接口定义并发送
     * @param path
     * @param map
     * @return
     */
    public Response getResponseFromYaml(String path, HashMap<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        //生成restful实体
        Restful restful = getApiFromYaml(path);
        restful = updateApiFromMap(restful, map);
        RequestSpecification requestSpecification = getDefaultRequestSpecification();

        if (restful.query != null) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }

        if (restful.body != null) {
            requestSpecification.body(restful.body);
        }

        String[] url=updateUrl(restful.url);

        return requestSpecification.log().all()
                .header("Host", url[0])
                .when().request(restful.method, url[1])
                .then().log().all()
                .extract().response();
    }

    /**
     * 多环境支持，替换url，更新host的header
     * @param url
     * @return
     */
    private String[] updateUrl(String url) {
        HashMap<String, String> hosts=WeworkConfig.getInstance().env.get(WeworkConfig.getInstance().current);
        String host="";
        String urlNew="";
        for(Map.Entry<String, String> entry : hosts.entrySet()){
            if(url.contains(entry.getKey())){
                host=entry.getKey();
                urlNew=url.replace(entry.getKey(), entry.getValue());
            }
        }

        return new String[]{host, urlNew};
    }
}
