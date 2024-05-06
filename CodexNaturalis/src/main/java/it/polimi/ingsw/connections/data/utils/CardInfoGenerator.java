package it.polimi.ingsw.connections.data.utils;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

/**
 * Class to generate description for cards
 */
final public class CardInfoGenerator {
    // TODO: Use template in the future
    public static final CardInfoGenerator generator = new CardInfoGenerator();

    private CardInfoGenerator() {}

    /**
     * Utility to translate index position of a card corner to String
     * @param index of corners
     * @return Description of corner position
     */
    private String getCornerPosition(int index) {
        switch(index) {
            case 0:
                return "Top left";
            case 1:
                return "Top right";
            case 2:
                return "Bottom right";
            case 3:
                return "Bottom left";
            default:
                return "";
        }
    }

    /**
     * Utility to get all info of a card corner
     * @param corner Corner of the card
     * @param index Position of the corner inside corner array
     * @return Description of corner
     */
    private String getCornerInfo(Corner corner, int index) {
        if (corner == null)
            return "";
        if (corner.isCovered())
            return String.format("\n\t- %s: corner covered", getCornerPosition(index));
        return String.format("\n\t- %s: " + (corner.getSymbol() == null ? "available" : "available, with " + corner.getSymbol().toString()), getCornerPosition(index));
    }

    /**
     * Utility to get card type description
     * @param card Card to get type description
     * @return Description of card type
     */
    private String cardType(Card card) {
        if (card instanceof GoldCard) {
            return "Gold Card";
        } else if (card instanceof ResourceCard) {
            return "Resource Card";
        } else if (card instanceof StarterCard) {
            return "Starter Card";
        }
        return "";
    }

    /**
     * Utility to get info of center symbols in a card
     * @param card Card to get info
     * @return Description of center symbols
     */
    private String centerSymbols(Card card) {
        StringBuilder symbols = new StringBuilder();
        if (card instanceof GoldCard) {
            symbols.append(String.format("\n\t- %s", ((GoldCard) card).getCardKingdom().toString()));
        } else if (card instanceof ResourceCard) {
            symbols.append(String.format("\n\t- %s", ((ResourceCard) card).getCardKingdom().toString()));
        } else if (card instanceof StarterCard) {
            for(CardSymbolKingdom symbol: ((StarterCard) card).getCenterSymbols()) {
                symbols.append(symbol == null ? "" : String.format("\n\t- %s", symbol));
            }
        }
        return symbols.toString();
    }

    /**
     * Utility to get info of points when placing card
     * @param card Card to get info
     * @return Description of points user gets when placing the card based on its type
     */
    private String pointsWhenPlaced(Card card) {
        if (card instanceof GoldCard) {
            GoldCard goldCard = (GoldCard) card;
            return String.format("\nThis card gives %d point(s)%s when placed", goldCard.getPoints(), pointsWhenPlacedGold(goldCard));
        } else if (card instanceof ResourceCard) {
            ResourceCard resourceCard = (ResourceCard) card;
            if (resourceCard.getPoints() == 0) {
                return "";
            }
            return String.format("\nThis card gives %d point(s) when placed", resourceCard.getPoints());
        } else if (card instanceof StarterCard) {
            return "";
        }
        return "";
    }

    /**
     * Utility to get info of points when placing gold card
     * @param card Card to get info
     * @return Description of points user gets when placing gold card based on its type
     */
    private String pointsWhenPlacedGold(GoldCard card) {
        if (card instanceof FixedPointsGoldCard) {
            return "";
        } else if (card instanceof CornerPointsGoldCard) {
            return " for each corner covered by this card";
        } else if (card instanceof SymbolPointsGoldCard) {
            return String.format(" for each %s symbol on the game field", ((SymbolPointsGoldCard) card).getPointsSymbol().toString());
        }
        return "";
    }

    /**
     * Utility to get info of requirements to place a card
     * @param card Card to get info
     * @return Description of requirements for placement
     */
    private String cardRequirements(Card card) {
        if (card instanceof GoldCard) {
            StringBuilder requirements = new StringBuilder();
            requirements.append("\nNeeds the following requirement(s) to be placed: ");
            for(GoldCardRequirement requirement: ((GoldCard) card).getRequirements()) {
                requirements.append(requirement == null ? "" : String.format("\n\t- %dx %s", requirement.getQuantity(), requirement.getRequiredSymbol().toString()));
            }
            return requirements.toString();
        }
        return "";
    }

    /**
     * Creates the description of the card
     * @param card Card to get info
     * @return Description of the card
     */
    public String getCardDescription(Card card) {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Card %s", card.getId()));
        description.append(String.format("\nType: %s", cardType(card)));
        description.append("\nOn the front has the following corners:");
        for(int i = 0; i < card.getFrontCorners().length; i++) {
            description.append(getCornerInfo(card.getFrontCorners()[i], i));
        }
        description.append("\nOn the back has the following corners:");
        for(int i = 0; i < card.getBackCorners().length; i++) {
            description.append(getCornerInfo(card.getBackCorners()[i], i));
        }
        description.append(String.format("\nOn the back has center symbol(s): %s", centerSymbols(card)));
        description.append(pointsWhenPlaced(card));
        description.append(cardRequirements(card));
        return description.toString();
    }
}