package ui;
import com.google.gson.Gson;
import exception.InvalidEmailException;
import exception.ResponseException;
import exception.StringTooLargeException;
import exception.WrongNumberOfArgumentsException;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade
{
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public void register(String[] params)
    {
        if(params.length != 3)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if((params[0] == null || params[0].isBlank()) || (params[1] == null || params[1].isBlank()) || (params[2] == null || params[2].isBlank()))
        {
             throw new NullPointerException("One of your fields is blank");
        }
        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1) || (params[2].length() > Integer.MAX_VALUE - 1))
        {
            throw new StringTooLargeException("Your input was too large, enter a shorter one");
        }
        else if(!params[2].contains("@") || !params[2].contains("."))
        {
            throw new InvalidEmailException("The email you input is invalid");
        }
        else
        {
            var request = buildRequest("POST", "/user", params);
            var response = sendRequest(request);
            handleResponse(response, null);
            String[] loginInfo = new String[2];
            loginInfo[0] = params[0];
            loginInfo[1] = params[1];
            login(loginInfo);
        }
    }

    public void login(String[] params) throws ResponseException
    {
        if(params.length != 2)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if((params[0] == null || params[0].isBlank()) || (params[1] == null || params[1].isBlank()))
        {
            throw new NullPointerException("One of your fields is blank");
        }
        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1))
        {
            throw new StringTooLargeException("Your input was too large, enter a shorter one");
        }
        else
        {
            var request = buildRequest("POST", "/session", params);
            var response = sendRequest(request);
            handleResponse(response, null);
        }
    }

    private HttpRequest buildRequest(String method, String path, Object body)
    {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null)
        {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException
    {
        try
        {
            return client.send(request, BodyHandlers.ofString());
        }
        catch (Exception ex)
        {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null)
        {
            return BodyPublishers.ofString(new Gson().toJson(request));
        }
        else
        {
            return BodyPublishers.noBody();
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException
    {
        var status = response.statusCode();
        if (!isSuccessful(status))
        {
            var body = response.body();
            if (body != null)
            {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null)
        {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status)
    {
        return status / 100 == 2; //why not just status == 200? Are there other codes in the 200s I should know about?
    }
}
