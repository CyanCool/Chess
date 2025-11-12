package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceUnitTests {

    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;
    private RegisterService registerService;
    private RegisterResponse regResponse;
    private LoginService loginService;
    private LogoutService logoutService;
    private CreateGameService createGameService;
    private JoinGameService joinGameService;
    private ListGamesService listGamesService;
    private ClearService clearService;
    private static Server server;

    @BeforeAll
    public static void initiate()
    {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup()
    {
        myData = new MemoryUserDAO();
        myAuth = new MemoryAuthDAO();
        myGame = new MemoryGameDAO();

        registerService = new RegisterService(myData, myAuth);
        loginService = new LoginService(myData, myAuth);
        logoutService = new LogoutService(myData, myAuth);
        createGameService = new CreateGameService(myGame, myAuth);
        joinGameService = new JoinGameService(myAuth, myGame);
        listGamesService = new ListGamesService(myAuth, myGame);
        clearService = new ClearService(myData, myAuth, myGame);

        RegisterRequest regRequest = new RegisterRequest("Steven", "password", "steven@gmail.com");
        regResponse = registerService.register(regRequest);
    }

    @AfterAll
    static void stopServer()
    {
        server.stop();
    }

@Test
    @Order(1)
    @DisplayName("Username already taken")
    public void tryUserTaken()
    {
        RegisterRequest myRequest = new RegisterRequest("Steven", "password", "steven@gmail.com");
        Assertions.assertThrowsExactly(AlreadyTakenException.class, () -> {registerService.register(myRequest); });
    }

    @Test
    @Order(2)
    @DisplayName("Bad Password Request")
    public void tryBadPassword()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", null, "rose@gmail.com");
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {registerService.register(myRequest); });
    }

    @Test
    @Order(3)
    @DisplayName("Bad Email Request")
    public void tryBadEmail()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", "password", null);
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {registerService.register(myRequest); });
    }

    @Test
    @Order(4)
    @DisplayName("Bad Username Request")
    public void tryBadUsername()
    {
        RegisterRequest myRequest = new RegisterRequest(null, "password", "rose@gmail.com");
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {registerService.register(myRequest); });
    }

    @Test
    @Order(5)
    @DisplayName("Successful Registration")
    public void registerSuccess()
    {
        RegisterRequest myRequest = new RegisterRequest("Bartholemue", "Imanerd", "bartholemue@gmail.com");
        RegisterResponse myResponse = registerService.register(myRequest);

        //make sure the username of the request matches the response
        Assertions.assertEquals(myRequest.username(), myResponse.username());
        //make sure the authtoken in the response isn't null
        Assertions.assertNotNull(myResponse.authToken());
    }

    @Test
    @Order(6)
    @DisplayName("Null Field - Unsuccessful")
    public void nullField()
    {
        LoginRequest loginRequest = new LoginRequest("Steven",null);
        Assertions.assertThrowsExactly(BlankFieldException.class, () -> {loginService.login(loginRequest); });

    }

    @Test
    @Order(7)
    @DisplayName("User does not exist")
    public void notExist()
    {
        LoginRequest loginRequest = new LoginRequest("Kirk", "jlsafj");
        Assertions.assertThrowsExactly(UnauthorizedException.class, () -> {loginService.login(loginRequest); });
    }

    @Test
    @Order(9)
    @DisplayName("Incorrect Password")
    public void badPassword()
    {
        LoginRequest loginRequest = new LoginRequest("Steven", "universe");
        Assertions.assertThrowsExactly(PasswordIncorrectException.class, () -> {loginService.login(loginRequest); });
    }

    @Test
    @Order(10)
    @DisplayName("Login Successful")
    public void successLogin()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        Assertions.assertEquals(loginRequest.username(), loginResponse.username());
        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    @Order(11)
    @DisplayName("Logout Successful")
    public void logoutSuccessful()
    {
        LogoutRequest logoutRequest = new LogoutRequest(regResponse.authToken());
        LogoutResponse logoutResponse = logoutService.logout(logoutRequest);

        Assertions.assertEquals(logoutResponse.authToken(), logoutRequest.authToken());
    }

    @Test
    @Order(12)
    @DisplayName("Not logged in")
    public void notInDatabase()
    {
        LogoutRequest logoutRequest = new LogoutRequest("123456789");
        Assertions.assertThrowsExactly(InvalidAuthDataException.class, () -> {logoutService.logout(logoutRequest); });
    }

    @Test
    @Order(13)
    @DisplayName("Authentication is null")
    public void badAuth()
    {
        LogoutRequest logoutRequest = new LogoutRequest(null);
        Assertions.assertThrowsExactly(InvalidAuthDataException.class, () -> {logoutService.logout(logoutRequest); });
    }

    @Test
    @Order(14)
    @DisplayName("Authentication is null")
    public void badCreateAuth()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Tyrone");
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {createGameService.createGame(null, createRequest); });
    }

    @Test
    @Order(15)
    @DisplayName("The session does not exist")
    public void createAuthNotExist()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Tyrone");
        Assertions.assertThrowsExactly(UnauthorizedException.class, () -> {createGameService.createGame("792749274982", createRequest); });
    }

    @Test
    @Order(16)
    @DisplayName("Game name is blank")
    public void blankName()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest(null);
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {createGameService.createGame(loginResponse.authToken(), createRequest); });
    }

    @Test
    @Order(17)
    @DisplayName("Game Name is Already Taken")
    public void alreadyTaken()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password"); //login first
        LoginResponse loginResponse = loginService.login(loginRequest);

        CreateRequest createRequestDefault = new CreateRequest("Gertrude");
        CreateResponse createResponseDefault = createGameService.createGame(loginResponse.authToken(), createRequestDefault); //create the game

        CreateRequest createRequest = new CreateRequest("Gertrude"); //create the duplicate game
        Assertions.assertThrowsExactly(UnauthorizedException.class, () -> {createGameService.createGame(loginResponse.authToken(), createRequest); });
    }

    @Test
    @Order(18)
    @DisplayName("Create Game Successful")
    public void createSuccess()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        Assertions.assertNotNull(createResponse.gameID());
    }

    @Test
    @Order(19)
    @DisplayName("Empty field")
    public void emptyFieldPlayerColor()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(null, createResponse.gameID());
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);});
    }

    @Test
    @Order(20)
    @DisplayName("Empty field")
    public void emptyFieldGameID()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 0);
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);});
    }

    @Test
    @Order(21)
    @DisplayName("Game doesn't exist")
    public void gameNotExist()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 686);
        Assertions.assertThrowsExactly(UnauthorizedException.class, () -> {joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);});
    }

    @Test
    @Order(22)
    @DisplayName("Spot Already Taken")
    public void spotTaken()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);

        RegisterRequest myRequest = new RegisterRequest("Harold", "password", "bartholemue@gmail.com");
        RegisterResponse myResponse = registerService.register(myRequest);
        LoginRequest loginRequest2 = new LoginRequest("Harold","password");
        LoginResponse loginResponse2 = loginService.login(loginRequest2);


        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        JoinGameRequest joinGameRequest2 = new JoinGameRequest("BLACK", createResponse.gameID());
        Assertions.assertThrowsExactly(AlreadyTakenException.class, () ->
        {joinGameService.updateGame(loginResponse2.authToken(), joinGameRequest2);});
    }

    @Test
    @Order(23)
    @DisplayName("Join Success")
    public void joinSuccess()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        Assertions.assertNotNull(joinGameResponse);

    }

    @Test
    @Order(24)
    @DisplayName("AuthToken Null")
    public void listAuthNull()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        ListgamesRequest listgamesRequest = new ListgamesRequest(null);
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {listGamesService.listGames(listgamesRequest);});

    }

    @Test
    @Order(25)
    @DisplayName("Session does not exist")
    public void seshNotExistList()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        ListgamesRequest listgamesRequest = new ListgamesRequest("8979729749");
        Assertions.assertThrowsExactly(UnauthorizedException.class, () -> {listGamesService.listGames(listgamesRequest);});

    }

    @Test
    @Order(26)
    @DisplayName("List Success")
    public void listSuccess()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        ListgamesRequest listgamesRequest = new ListgamesRequest(loginResponse.authToken());
        ListgamesResponse listgamesResponse= listGamesService.listGames(listgamesRequest);
        Assertions.assertNotNull(listgamesResponse.games());

    }

    @Test
    @Order(27)
    @DisplayName("Clear Not Successful")
    public void badClear()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        ListgamesRequest listgamesRequest = new ListgamesRequest(loginResponse.authToken());
        ListgamesResponse listgamesResponse= listGamesService.listGames(listgamesRequest);
        DeleteRequest deleteRequest = new DeleteRequest();
        DeleteResponse deleteResponse = clearService.clear(null);
        Assertions.assertNotNull(myData.getMap());
        Assertions.assertNotNull(myAuth.getAllAuthData());
        Assertions.assertNotNull(myGame.getList());

    }

    @Test
    @Order(28)
    @DisplayName("Clear Not Successful")
    public void clearSuccess()
    {
        LoginRequest loginRequest = new LoginRequest("Steven","password");
        LoginResponse loginResponse = loginService.login(loginRequest);
        CreateRequest createRequest = new CreateRequest("Boulevard");
        CreateResponse createResponse = createGameService.createGame(loginResponse.authToken(), createRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", createResponse.gameID());
        JoinGameResponse joinGameResponse = joinGameService.updateGame(loginResponse.authToken(), joinGameRequest);
        ListgamesRequest listgamesRequest = new ListgamesRequest(loginResponse.authToken());
        ListgamesResponse listgamesResponse= listGamesService.listGames(listgamesRequest);
        DeleteRequest deleteRequest = new DeleteRequest();
        DeleteResponse deleteResponse = clearService.clear(deleteRequest);
        Assertions.assertNull(myData.getMap());
        Assertions.assertNull(myAuth.getAllAuthData());
        Assertions.assertNull(myGame.getList());
    }

}
