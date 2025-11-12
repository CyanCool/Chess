package response;

import model.GameData;

import java.util.ArrayList;

public record ListgamesResponse(ArrayList<GameData> games) {}
