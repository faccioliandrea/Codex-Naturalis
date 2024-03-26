package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

import java.util.*;

/**
 * Class representing  the diagonal Goal card
 */
public class PatternGoalDiagonal extends Goal{
    private boolean isPrimaryDiagonal;
    private CardSymbolKingdom kingdom;

    /**
     * Default constructor
     *
     * @param id     Goal ID
     * @param points : points given by exceeding the goal
     * @param isPrimaryDiagonal : direction of the pattern
     * @param kingdom : kingdom of the cards
     */
    public PatternGoalDiagonal(String id, int points, boolean isPrimaryDiagonal, CardSymbolKingdom kingdom) {
        super(id, points);
        this.isPrimaryDiagonal = isPrimaryDiagonal;
        this.kingdom = kingdom;
    }

    /**
     *
     * calculate the points scored by the player with this Goal
     * @param board : board of the current player
     * @return points scored
     */
    @Override
    public int checkGoal(Board board) {
        Set<PlayableCard> usedCard = new HashSet<>();
        ArrayList<PlayableCard> tmpArr = new ArrayList<>();
        ArrayList<Card> cards = new ArrayList<>(board.getPlayedCards());
        cards.sort((o1, o2) -> o1.getCoord().y == o2.getCoord().y ? (o1.getCoord().x- o2.getCoord().x) : (o1.getCoord().y - o2.getCoord().y));
        for(Card c : cards){
            tmpArr.clear();
            if(c instanceof PlayableCard && ((PlayableCard) c).getCardKingdom().equals(getKingdom())){
                Optional<Card> cc1, cc2;
                if(isPrimaryDiagonal()){
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-1 && x.getCoord().x == c.getCoord().x+1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-1 && x.getCoord().x == c.getCoord().x+1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getKingdom())).findFirst();
                } else {
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-1 && x.getCoord().x == c.getCoord().x-1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y+1 && x.getCoord().x == c.getCoord().x+1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getKingdom())).findFirst();
                }
                if (cc1.isPresent() && cc2.isPresent()) {
                    tmpArr.add((PlayableCard) cc1.get());
                    tmpArr.add((PlayableCard) cc2.get());
                    tmpArr.add((PlayableCard) c);
                    try {
                        usedCard.addAll(tmpArr);
                    } catch (IllegalArgumentException e){
                        //the cards are not added to the set, because one of them is already in
                    }
                }
            }
        }
        return usedCard.size()/3*getPoints();
    }

    /**
     * Getter for isPrimaryDiagonal attribute
     * @return isPrimaryDiagonal
     */
    public boolean isPrimaryDiagonal() {
        return isPrimaryDiagonal;
    }

    /**
     * Getter for kingdom attribute
     * @return kingdom
     */
    public CardSymbolKingdom getKingdom() {
        return kingdom;
    }
}
