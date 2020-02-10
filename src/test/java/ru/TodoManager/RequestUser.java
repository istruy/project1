package ru.TodoManager;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RequestUser {

    private static String baseURI = "http://localhost:7844/api";

    public static RequestSpecification doRequest(String contentType, Cookie cookie, HashMap<String, String> parameters) {
        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
        }

        request.contentType(contentType);
        request.cookie(cookie);
        request.body(requestParams.toJSONString());

        return request;
    }

    public static RequestSpecification doRequest(String contentType, HashMap<String, String> parameters) {
        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            requestParams.put(entry.getKey(), entry.getValue());
        }

        request.contentType(contentType);
        request.body(requestParams.toJSONString());

        return request;
    }

    public static RequestSpecification doRequest(Cookie cookie) {
        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        request.cookie(cookie);

        return request;
    }


}
