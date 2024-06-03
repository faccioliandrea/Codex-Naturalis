package it.polimi.ingsw.model.parser;

import com.google.gson.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.*;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.goals.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {

    public static final Parser parser = new Parser();
    private Parser() {}

    private Corner[] getCorners(JsonObject card, String cornerName) {
        JsonArray cornerArray = card.getAsJsonArray(cornerName);
        Corner[] corners = new Corner[4];
        for(int i = 0; i < corners.length; i++) {
            Corner currentCorner;
            JsonObject cornerObject = cornerArray.get(i).getAsJsonObject();
            if(cornerObject.get("isPresent").getAsBoolean()) {
                String symbol = cornerObject.get("symbol").getAsString();
                if (symbol.isEmpty()) {
                    currentCorner = new Corner(null);
                } else {
                    try {
                        CardSymbolKingdom kingdom = CardSymbolKingdom.valueOf(symbol);
                        currentCorner = new Corner(kingdom);
                    } catch (IllegalArgumentException ignored) {
                        CardSymbolObject object = CardSymbolObject.valueOf(symbol);
                        currentCorner = new Corner(object);
                    }
                }
            } else {
                currentCorner = null;
            }
            corners[i] = currentCorner;
        }
        return  corners;
    }

    public ResourceCard[] initResourceCards() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        InputStream jsonStream = getClass().getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.RESOURCE_CARD_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.RESOURCE_CARD_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        ResourceCard[] resourceCards = new ResourceCard[40];
        for(int index = 0; index < resourceCards.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            CardSymbolKingdom centerSymbol = CardSymbolKingdom.valueOf(jsonCard.get("centerSymbol").getAsString());
            int points = jsonCard.get("points").getAsInt();
            Corner[] corners = this.getCorners(jsonCard, "corners");
            ResourceCard card = new ResourceCard(id, centerSymbol, corners, points);
            resourceCards[index] = card;
        }
        return resourceCards;
    }

    public GoldCard[] initGoldCards() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        InputStream jsonStream = getClass().getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.GOLD_CARD_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.GOLD_CARD_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        GoldCard[] goldCards = new GoldCard[40];
        for(int index = 0; index < goldCards.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            CardSymbolKingdom centerSymbol = CardSymbolKingdom.valueOf(jsonCard.get("centerSymbol").getAsString());
            int points = jsonCard.get("points").getAsInt();
            Corner[] corners = this.getCorners(jsonCard, "corners");
            ArrayList<GoldCardRequirement> requirements = new ArrayList<GoldCardRequirement>();
            JsonArray requirementsJson = jsonCard.getAsJsonArray("placementRequirements");
            for(JsonElement requirementJsonElem: requirementsJson) {
                JsonObject requirementJson = requirementJsonElem.getAsJsonObject();
                GoldCardRequirement requirement;
                if(requirementJson != null) {
                    CardSymbolKingdom requiredSymbol = CardSymbolKingdom.valueOf(requirementJson.get("requiredSymbol").getAsString());
                    int quantity = requirementJson.get("quantity").getAsInt();
                    requirement = new GoldCardRequirement(requiredSymbol, quantity);
                    requirements.add(requirement);
                }

            }
            GoldCard goldCard;
            switch (jsonCard.get("type").getAsString()) {
                case "FixedPoints":
                    goldCard = new FixedPointsGoldCard(id, centerSymbol, corners, points, requirements.toArray(new GoldCardRequirement[0]));
                    break;
                case "CornerPoints":
                    goldCard = new CornerPointsGoldCard(id, centerSymbol, corners, points, requirements.toArray(new GoldCardRequirement[0]));
                    break;
                case "SymbolPoints":
                    CardSymbolObject pointsSymbol = CardSymbolObject.valueOf(jsonCard.get("pointsSymbol").getAsString());
                    goldCard = new SymbolPointsGoldCard(id, centerSymbol, corners, points, requirements.toArray(new GoldCardRequirement[0]), pointsSymbol);
                    break;
                default:
                    goldCard = null;
            }
            goldCards[index] = goldCard;
        }
        return goldCards;
    }

    public StarterCard[] initStarterCards() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        InputStream jsonStream = Parser.class.getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.STARTER_CARD_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.STARTER_CARD_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        StarterCard[] starterCards = new StarterCard[6];
        for(int index = 0; index < starterCards.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            Corner[] frontCorners = this.getCorners(jsonCard, "frontCorners");
            Corner[] backCorners = this.getCorners(jsonCard, "backCorners");
            CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
            int j = 0;
            for(JsonElement symbolElem: jsonCard.get("centerSymbols").getAsJsonArray()) {
                CardSymbolKingdom symbol = CardSymbolKingdom.valueOf(symbolElem.getAsString());
                centerSymbols[j] = symbol;
                j++;
            }
            StarterCard starterCard = new StarterCard(id, frontCorners, backCorners, centerSymbols);
            starterCards[index] = starterCard;
        }
        return starterCards;
    }

    private PatternGoalDiagonal[] getDiagonalGoals() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        PatternGoalDiagonal[] diagonalGoals = new PatternGoalDiagonal[4];
        InputStream jsonStream = getClass().getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.GOAL_DIAGONAL_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.GOAL_DIAGONAL_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        for(int index = 0; index < diagonalGoals.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            CardSymbolKingdom kingdom = CardSymbolKingdom.valueOf(jsonCard.get("kingdom").getAsString());
            int points = jsonCard.get("points").getAsInt();
            boolean primaryDiagonal = jsonCard.get("isPrimary").getAsBoolean();
            diagonalGoals[index] = new PatternGoalDiagonal(id, points, primaryDiagonal, kingdom);
        }
        return diagonalGoals;
    }

    private PatternGoalL[] getLGoals() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        PatternGoalL[] LGoals = new PatternGoalL[4];
        InputStream jsonStream = getClass().getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.GOAL_L_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.GOAL_L_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        for(int index = 0; index < LGoals.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            CardSymbolKingdom verticalKingdom = CardSymbolKingdom.valueOf(jsonCard.get("verticalKingdom").getAsString());
            CardSymbolKingdom horizonalKingdom = CardSymbolKingdom.valueOf(jsonCard.get("horizontalKingdom").getAsString());
            LDirection direction = LDirection.valueOf(jsonCard.get("direction").getAsString());
            int points = jsonCard.get("points").getAsInt();
            LGoals[index] = new PatternGoalL(id, points, direction, verticalKingdom, horizonalKingdom);
        }
        return LGoals;
    }

    private SymbolGoal[] getSymbolGoals() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        SymbolGoal[] symbolGoals = new SymbolGoal[8];
        InputStream jsonStream = getClass().getResourceAsStream(Constants.JSON_ROOT_PATH + Constants.GOAL_SYMBOL_FILENAME);
        if (jsonStream == null) {
            throw new FileNotFoundException("Resource not found: " + Constants.JSON_ROOT_PATH + Constants.GOAL_SYMBOL_FILENAME);
        }
        JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(jsonStream), JsonObject.class);
        JsonArray jsonCardArray = jsonObject.getAsJsonArray("cards");
        for(int index = 0; index < symbolGoals.length; index++) {
            JsonObject jsonCard = jsonCardArray.get(index).getAsJsonObject();
            String id = jsonCard.get("id").getAsString();
            int points = jsonCard.get("points").getAsInt();
            ArrayList<GoalRequirement> requirements = new ArrayList<>();
            JsonArray requirementsJson = jsonCard.getAsJsonArray("goalRequirements");
            for(JsonElement requirementJsonElem: requirementsJson) {
                JsonObject requirementJson = requirementJsonElem.getAsJsonObject();
                GoalRequirement requirement;
                if(requirementJson != null) {
                    CardSymbol cardSymbol;
                    String symbol = requirementJson.get("symbol").getAsString();
                    try {
                        cardSymbol = CardSymbolKingdom.valueOf(symbol);
                    } catch (IllegalArgumentException ignored) {
                        cardSymbol = CardSymbolObject.valueOf(symbol);
                    }
                    int quantity = requirementJson.get("quantity").getAsInt();
                    requirement = new GoalRequirement(cardSymbol, quantity);
                    requirements.add(requirement);
                }

            }
            symbolGoals[index] = new SymbolGoal(id, points, requirements.toArray(new GoalRequirement[0]));
        }
        return symbolGoals;
    }

    public Goal[] initGoals() throws IllegalArgumentException, FileNotFoundException, JsonParseException {
        Goal[] goals = new Goal[16];
        int index = 0;
        for(PatternGoalDiagonal goal: this.getDiagonalGoals()) {
            goals[index] = goal;
            index++;
        }
        for(PatternGoalL goal: this.getLGoals()) {
            goals[index] = goal;
            index++;
        }
        for(SymbolGoal goal: this.getSymbolGoals()) {
            goals[index] = goal;
            index++;
        }
        return goals;
    }
}
