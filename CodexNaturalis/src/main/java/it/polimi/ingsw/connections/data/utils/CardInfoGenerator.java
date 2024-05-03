package it.polimi.ingsw.connections.data.utils;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

final public class CardInfoGenerator {
    // TODO: Use template in the future
    public static final CardInfoGenerator generator = new CardInfoGenerator();

    private CardInfoGenerator() {}

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

    private String getCornerInfo(Corner corner, int index) {
        return String.format("\n\t- %s: " + (corner == null ? "no corner" : (corner.getSymbol() == null ? "available" : "available, with " + corner.getSymbol().toString())), getCornerPosition(index));
    }

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

    private String centerSymbols(Card card) {
        StringBuilder symbols = new StringBuilder();
        if (card instanceof GoldCard) {
            symbols.append(String.format("\n\t- %s", ((GoldCard) card).getCardKingdom().toString()));
        } else if (card instanceof ResourceCard) {
            symbols.append(String.format("\n\t- %s", ((ResourceCard) card).getCardKingdom().toString()));
        } else if (card instanceof StarterCard) {
            for(CardSymbolKingdom symbol: ((StarterCard) card).getCenterSymbols()) {
                symbols.append(symbol == null ? "" : String.format("\n\t- %s", symbol.toString()));
            }
        }
        return symbols.toString();
    }

    private String pointsWhenPlaced(Card card) {
        if (card instanceof GoldCard) {
            GoldCard goldCard = (GoldCard) card;
            return String.format("\nThis card gives %d point(s)%s when placed", goldCard.getPoints(), pointsWhenPlacedGold(goldCard));
        } else if (card instanceof ResourceCard) {
            return String.format("\nThis card gives %d point(s) when placed", ((ResourceCard) card).getPoints());
        } else if (card instanceof StarterCard) {
            return "";
        }
        return "";
    }

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