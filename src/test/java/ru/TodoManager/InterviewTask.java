package ru.TodoManager;

import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class InterviewTask {

    public static void getLogin(WebDriver driver, String username, String password) {
        String url = "http://localhost:7844/login";
        driver.get(url);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id='login-page']/div/button")).click();
    }

    public static Response getLoginAPI(String username, String password) {

        HashMap<String, String> hashmapRarams = new HashMap<String, String>();
        hashmapRarams.put("username", username);
        hashmapRarams.put("password", password);

        RequestSpecification requestLogin = RequestUser.doRequest("application/json", hashmapRarams);
        Response response = requestLogin.post("/login");

        return response;
    }

    public static Response getLogoutAPI(String username, String password) {
        Response responseLogin = InterviewTask.getLoginAPI(username, password);
        Cookie cookie = responseLogin.getDetailedCookie("Authorization");
        RequestSpecification requestLogout = RequestUser.doRequest(cookie);
        Response responseLogout = requestLogout.post("/logout");
        return responseLogout;
    }

    public static void checkResponseStatus(Response response) {
        String statusCode = Integer.toString(response.getStatusCode());
        Assert.assertEquals(statusCode, "200");
    }

    public static Response CreateToDoApi(Cookie cookie) {
        HashMap<String, String> hashmapRarams = new HashMap<String, String>();
        hashmapRarams.put("description", "some text");
        RequestSpecification requestCreateTodo = RequestUser.doRequest("application/json", cookie, hashmapRarams);
        Response response = requestCreateTodo.post("/create");
        return response;
    }

    public static ArrayList getRandomTodo(Cookie cookie) {
        ArrayList listJsonTodo = getListTodo(cookie);
        int jsonListSize = listJsonTodo.size();
        int randomJsonElement = new Random().nextInt(jsonListSize) + 1;
        String randomTodo = listJsonTodo.get(randomJsonElement).toString();

        String newStringListTodo = randomTodo.replaceAll("[{}]", "");
        String[] arrayTodoList = newStringListTodo.split(", ");
        int sizeArr = arrayTodoList.length;
        ArrayList<String> arrTodoElement = new ArrayList<String>();

        for (int i = 0; i < sizeArr; i++) {
            String[] arrayKeyValueTodo = arrayTodoList[i].split("=");
            arrTodoElement.add(arrayKeyValueTodo[1]);
        }

        return arrTodoElement;
    }

    public static ArrayList getListTodo(Cookie cookie) {
        RequestSpecification requestGetAllTodo = RequestUser.doRequest(cookie);
        Response response = requestGetAllTodo.get("/getAll");
        ArrayList<ArrayList<String>> jsonResponse = response.jsonPath().getJsonObject("todoList");

        return jsonResponse;
    }

    public static Response RemoveToDoApi(Cookie cookie) {
        ArrayList<String> randomTodo = getRandomTodo(cookie);
        HashMap<String, String> hashmapRequestBody = new HashMap<String, String>();
        hashmapRequestBody.put("id", randomTodo.get(0));

        RequestSpecification requestRemoveTodo = RequestUser.doRequest("application/json", cookie, hashmapRequestBody);
        Response response = requestRemoveTodo.post("/remove");

        return response;
    }

    /**
     * Возвращает количество элементов до создания, количество после создания, фактическое описание дела и заданное.
     *
     * @param driver
     * @return
     */
    public static String[] CreateToDoUI(WebDriver driver) {
        //InterviewTask.getLogin(driver, username, password);
        String randomNumber = String.valueOf((int) Math.round((Math.random() * 10)));
        String textForTodo = "random text " + randomNumber;

        String numberLastTodoBeforeCreate = "";
        try {
            numberLastTodoBeforeCreate = driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > div"))
                    .getText();
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            numberLastTodoBeforeCreate = "0";
        }

        driver.findElement(By.xpath("//*[@class='create-todo-block']/div/input")).sendKeys(textForTodo);

        driver.findElement(By.xpath("//*[@class='create-todo-block']/div/button")).click();
        driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
        String numberLastTodoAfterCreate = driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > div"))
                .getText();
        String descriptionLastTodoAfterCreate = driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > div:nth-child(2)"))
                .getText();
        String[] arrayResultLastTodo = {numberLastTodoBeforeCreate, numberLastTodoAfterCreate, descriptionLastTodoAfterCreate, textForTodo};

        return arrayResultLastTodo;

    }

    /**
     * Возвращает количество элементов до удаления и после
     *
     * @param driver
     * @param username
     * @param password
     * @return
     */
    public static String[] removeToDoUI(WebDriver driver, String username, String password) {
        InterviewTask.getLogin(driver, username, password);
        //добавить проверку на 10 элементов

        String numberLastTodoBeforeRemove = "";
        try {
            numberLastTodoBeforeRemove = driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > div"))
                    .getText();
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            numberLastTodoBeforeRemove = "0";
            CreateToDoUI(driver);
        }

        driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > button")).click();
        String numberLastTodoAfterRemove = driver.findElement(By.cssSelector(".view-container>.single-todo:last-child > div"))
                .getText();

        String[] arrayResultLastTodo = {numberLastTodoBeforeRemove, numberLastTodoAfterRemove};

        return arrayResultLastTodo;
    }

}
