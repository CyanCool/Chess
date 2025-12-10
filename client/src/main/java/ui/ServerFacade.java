package ui;
import com.google.gson.Gson;
import exception.*;
import model.GameData;
import request.*;
import response.ListgamesResponse;
import response.LoginResponse;
import response.LogoutResponse;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade
{
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public void setLoginResponse(LoginResponse loginResponse)
    {
        this.loginResponse = loginResponse;
    }

    private LoginResponse loginResponse;

    public ServerFacade(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public void register(String[] params) throws ResponseException
    {
        if(params.length != 3)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if((params[0] == null || params[0].isBlank()) || (params[1] == null || params[1].isBlank()) || (params[2] == null || params[2].isBlank()))
        {
             throw new NullPointerException("One of your fields is blank");
        }
//        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1) || (params[2].length() > Integer.MAX_VALUE - 1))
//        {
//            throw new StringTooLargeException("Your input was too large, enter a shorter one");
//        }
        else if(!params[0].matches("[A-Za-z0-9_\\-!.@?']+") || !params[1].matches("[A-Za-z0-9_\\-!.@?']+") || !params[2].matches("[A-Za-z0-9_\\-!.@?']+"))
        {
            throw new InvalidCharacterException("This game name has invalid characters." +
                    " Enter a game name with only letters, numbers, and special characters !, ., -, @, ?, '." );
        }
        else if(!params[2].contains("@") || !params[2].contains("."))
        {
            throw new InvalidEmailException("The email you input is invalid");
        }
        else
        {
            RegisterRequest myRequest = new RegisterRequest(params[0],params[1],params[2]);
            var request = buildRequest("POST", "/user", myRequest, null);
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
//        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1))
//        {
//            throw new StringTooLargeException("Your input was too large, enter a shorter one");
//        }
        else if(!params[0].matches("[A-Za-z0-9_\\-!.@?']+") || !params[1].matches("[A-Za-z0-9_\\-!.@?']+"))
        {
            throw new InvalidCharacterException("This game name has invalid characters." +
                    " Enter a game name with only letters, numbers, and special characters !, ., -, @, ?,'." );
        }
        else
        {
            LoginRequest loginRequest = new LoginRequest(params[0],params[1]);
            var request = buildRequest("POST", "/session", loginRequest, null);
            var response = sendRequest(request);
            loginResponse = new Gson().fromJson(response.body(), LoginResponse.class);
            handleResponse(response, null);
        }
    }

    public void logout() throws ResponseException
    {
        var request = buildRequest("DELETE", "/session", null, loginResponse.authToken());
        var response = sendRequest(request);
        handleResponse(response, LogoutResponse.class);
    }

    public void createGame(String[] params) throws ResponseException
    {
        if(params.length != 1)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if(params[0] == null || params[0].isBlank())
        {
            throw new NullPointerException("One of your fields is blank");
        }
//        else if((params[0].length() > Integer.MAX_VALUE - 1))
//        {
//            throw new StringTooLargeException("Your input was too large, enter a shorter one");
//        }
        else if(!params[0].matches("[A-Za-z0-9_\\-!.@?']+"))
        {
            throw new InvalidCharacterException("This game name has invalid characters." +
                    " Enter a game name with only letters, numbers, and special characters !, ., -, @, ?,'." );
        }
        else
        {
            CreateRequest createRequest = new CreateRequest(params[0]);
            var request = buildRequest("POST", "/game", createRequest, loginResponse.authToken());
            var response = sendRequest(request);
            handleResponse(response, null);
        }
    }

    public HttpResponse<String> listGames() throws ResponseException
    {
        var request = buildRequest("GET", "/game", null, loginResponse.authToken());
        var response = sendRequest(request);
        handleResponse(response, ListgamesResponse.class);

        return response;
    }

    public void joinGame(int gameID, String[] params) throws ResponseException
    {
        if(params.length != 2)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if((params[0] == null || params[0].isBlank()) || (params[1] == null || params[1].isBlank()))
        {
            throw new NullPointerException("One of your fields is blank");
        }
//        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1))
//        {
//            throw new StringTooLargeException("Your input was too large, enter a shorter one");
//        }
        else if(!params[0].matches("[0-9]+"))
        {
            throw new InvalidCharacterException("Enter a valid integer for the index");
        }
        else if(!params[1].equals("black") && !params[1].equals("white"))
        {
            throw new InvalidCharacterException("Enter a valid team color, black or white");
        }
        else
        {
            JoinGameRequest joinRequest = new JoinGameRequest(params[1].toUpperCase(), gameID);
            var request = buildRequest("PUT", "/game", joinRequest, loginResponse.authToken());
            var response = sendRequest(request);
            handleResponse(response, null);
        }
    }

    public void observeGame(int gameID, String[] params) throws ResponseException
    {
        if(params.length != 1)
        {
            throw new WrongNumberOfArgumentsException("Your input has the wrong number of arguments");
        }
        else if((params[0] == null || params[0].isBlank()))
        {
            throw new NullPointerException("One of your fields is blank");
        }
//        else if((params[0].length() > Integer.MAX_VALUE - 1) || (params[1].length() > Integer.MAX_VALUE - 1))
//        {
//            throw new StringTooLargeException("Your input was too large, enter a shorter one");
//        }
        else if(!params[0].matches("[0-9]+"))
        {
            throw new InvalidCharacterException("Enter a valid integer for the index");
        }
        else
        {
            JoinGameRequest joinRequest = new JoinGameRequest("OBSERVE", gameID);
            var request = buildRequest("PUT", "/game", joinRequest, loginResponse.authToken());
            var response = sendRequest(request);
            handleResponse(response, null);
        }
    }



    private HttpRequest buildRequest(String method, String path, Object body, String authToken)
    {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if(authToken != null)
        {
            request.setHeader("authorization", authToken);
        }
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
            //return client.send(request, BodyHandlers.ofString());
            return client.send(request, BodyHandlers.ofString());
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
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
