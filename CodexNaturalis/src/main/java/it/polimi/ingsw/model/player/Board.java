package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.Point;
import java.util.*;


public class Board {

    private ArrayList<Card> playedCards;
    private Map<CardSymbol, Integer> symbols;
    private Goal privateGoal;
    private ArrayList<Goal> sharedGoals;

    /**
     * Default constructor
     */
    public Board() {
        this.playedCards = new ArrayList<>();
        this.privateGoal = null;
        this.sharedGoals = null;

        this.symbols = new HashMap<>(7);
        this.symbols.put(CardSymbolKingdom.MUSHROOM,    0);
        this.symbols.put(CardSymbolKingdom.BUTTERFLY,   0);
        this.symbols.put(CardSymbolKingdom.WOLF,        0);
        this.symbols.put(CardSymbolKingdom.LEAF,        0);
        this.symbols.put(CardSymbolObject.FEATHER,      0);
        this.symbols.put(CardSymbolObject.POTION,       0);
        this.symbols.put(CardSymbolObject.SCROLL,       0);
    }

    /**
     * Constructor
     * @param privateGoal private Goal
     * @param sharedGoals lisgt of shared goals
     */
    public Board(Goal privateGoal, ArrayList<Goal> sharedGoals) {
        this.playedCards = new ArrayList<>();
        this.privateGoal = privateGoal;
        this.sharedGoals = sharedGoals;

        this.symbols = new HashMap<>(7);
        this.symbols.put(CardSymbolKingdom.MUSHROOM,    0);
        this.symbols.put(CardSymbolKingdom.BUTTERFLY,   0);
        this.symbols.put(CardSymbolKingdom.WOLF,        0);
        this.symbols.put(CardSymbolKingdom.LEAF,        0);
        this.symbols.put(CardSymbolObject.FEATHER,      0);
        this.symbols.put(CardSymbolObject.POTION,       0);
        this.symbols.put(CardSymbolObject.SCROLL,       0);
    }

    /**
     *  Returns an ArrayList of the cards currently on the board:
     *  the first card is of type StarterCard,
     *  all others are of type PlayableCard
     *  @return ArrayList of the cards currently on the board:
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Returns a Map containing each symbol and the respective count
     * @return Map<CardSymbol, Integer>
     */
    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Return the private goal
     * @return private Goal
     */
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**
     * Sets the private goal
     * @param privateGoal the private goal
     */
    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**
     * Returns the ArrayList of shared goals
     * @return ArrayList of shared goals
     */
    public ArrayList<Goal> getSharedGoals() {
        return sharedGoals;
    }

    /**
     * Sets the shared goals
     * @param sharedGoals list of goals
     */
    public void setSharedGoals(ArrayList<Goal> sharedGoals) {
        this.sharedGoals = sharedGoals;
    }

    /**
     * Sets the list of all played cards.
     * @param playedCards list of all played cards
     */
    public void setPlayedCards(ArrayList<Card> playedCards) {
        this.playedCards = playedCards;
    }

    /**
     * Sets the Map containing each symbol and the respective count
     * @param symbols Map
     */
    public void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
    }


    /**
     * Returns an ArrayList of available points where a Card might be placed
     * @return ArrayList of available points where a Card might be placed
     */
    public ArrayList<Point> availablePositions() {
        ArrayList<Point> available = new ArrayList<>();

        for (Card c: this.playedCards) {
            for (int i=0; i<c.getCorners().length; i++) {

                Point neighbor = new Point(
                        c.getCoord().x + (i % 3 == 0 ? -1 : 1),
                        c.getCoord().y + (i > 1      ? -1 : 1)
                );
                if (c.getCorners()[i] != null && !c.getCorners()[i].isCovered()) {
                    if(!available.contains(neighbor)){
                        available.add(neighbor);
                    }
                } else {
                    available.remove(neighbor);
                }
            }
        }

        return available;
    }

    /**
     * Adds the {@code card} specified to the playedCards array
     * and updates the symbol count
     * @param card the card to add to the playedCards array
     */
    protected void addPlayedCard(Card card) {
        this.playedCards.add(card);

        Arrays.stream(card.getCorners())
                .filter(Objects::nonNull)
                .map(Corner::getSymbol)
                .filter(Objects::nonNull)
                .forEach(x -> this.symbols.put(x, this.symbols.get(x) + 1));
        if (card instanceof StarterCard && !card.getFlipped()){
            Arrays.stream(((StarterCard) card).getCenterSymbols()).forEach(x -> this.symbols.put(x, this.symbols.get(x) + 1));
        } else if (card instanceof PlayableCard && card.getFlipped()) {
            CardSymbolKingdom key = ((PlayableCard) card).getCenterSymbol();
            if (this.symbols.containsKey(key)) {
                this.symbols.put(key, this.symbols.get(key) + 1);
            }
        }
    }
}
