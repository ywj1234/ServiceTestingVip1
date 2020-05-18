package com.ktt.wework;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    void getApiFromHar() {
        Api api = new Api();
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=41.*").url);
    }

    @Test
    void getResponseFromHar() {
        Api api = new Api();
        System.out.println(api.getResponseFromHar("/api/app.har.json",".*tid=41.*",null));
    }

    @Test
    void mustache() throws IOException {
        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("name", "Mustache");

        Writer writer = new OutputStreamWriter(System.out);
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader("name: ddddddddddd {{name}} "), "example");
        mustache.execute(writer, scopes);
        writer.flush();
    }
}