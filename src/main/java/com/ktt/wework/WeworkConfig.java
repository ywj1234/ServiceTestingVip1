package com.ktt.wework;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.HashMap;

/**
 * 全局配置
 */
public class WeworkConfig {

    //应用id
    public String agentId;
    //企业id
    public String corpid;
    //secret是企业应用里面用于保障数据安全的“钥匙”
    public String secret;
    //通讯录id
    public String contacSecret;

    public String current = "test";

    public HashMap<String,HashMap<String,String>> env;

    private static WeworkConfig weworkConfig;

    public static WeworkConfig getInstance(){
        if (weworkConfig == null){
            weworkConfig = load("/config/WeworkConfig.yaml");
        }
        return weworkConfig;
    }

    /**
     * 读取yaml或json
     */
    public static WeworkConfig load(String path){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path), WeworkConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
