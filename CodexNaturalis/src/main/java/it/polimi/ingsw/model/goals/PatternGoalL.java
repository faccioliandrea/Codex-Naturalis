package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.player.Board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class representing the LGoal Card
 */
public class PatternGoalL extends Goal{

    final private LDirection direction;
    final private CardSymbolKingdom verticalKingdom;
    final private CardSymbolKingdom horizontalKingdom;

    /**
     * Default constructor
     *
     * @param id : Goal ID
     * @param points : points given by exceeding the goal
     * @param verticalKingdom : kingdom of the 2 vertical cards
     * @param horizontalKingdom : kingdom of the horizontal card
     */
    public PatternGoalL(String id, int points, LDirection direction, CardSymbolKingdom verticalKingdom, CardSymbolKingdom horizontalKingdom) {
        super(id, points);
        this.direction = direction;
        this.verticalKingdom = verticalKingdom;
        this.horizontalKingdom = horizontalKingdom;
    }

    /**
     * calculate the points scored by the player with this Goal
     * @param board : board of the current player
     * @return points
     */
    @Override
    public int checkGoal(Board board) {
        Set<PlayableCard> usedCard = new HashSet<>();
        Set<PlayableCard> tmpSet = new HashSet<>();
        ArrayList<Card> cards = new ArrayList<>(board.getPlayedCards());
        cards.sort((o1, o2) -> o1.getCoord().y == o2.getCoord().y ? (o1.getCoord().x - o2.getCoord().x) : (o1.getCoord().y - o2.getCoord().y));
        for(Card c : cards){
            tmpSet.clear();
            if(c instanceof PlayableCard && ((PlayableCard) c).getCardKingdom().equals(getVerticalKingdom())){
                Optional<Card> cc1, cc2;
                if(direction.equals(LDirection.BOTTOMLEFT)){
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y+2 && x.getCoord().x == c.getCoord().x && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getVerticalKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-1 && x.getCoord().x == c.getCoord().x-1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getHorizontalKingdom())).findFirst();
                } else if(direction.equals(LDirection.BOTTOMRIGHT)){
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y+2 && x.getCoord().x == c.getCoord().x && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getVerticalKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-1 && x.getCoord().x == c.getCoord().x+1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getHorizontalKingdom())).findFirst();
                }
                else if(direction.equals(LDirection.TOPLEFT)){
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y+1 && x.getCoord().x == c.getCoord().x-1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getHorizontalKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-2 && x.getCoord().x == c.getCoord().x && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getVerticalKingdom())).findFirst();
                }
                else {
                    cc1 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y+1 && x.getCoord().x == c.getCoord().x+1 && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getHorizontalKingdom())).findFirst();
                    cc2 = cards.stream().filter(x -> x.getCoord().y == c.getCoord().y-2 && x.getCoord().x == c.getCoord().x && x instanceof PlayableCard && ((PlayableCard) x).getCardKingdom().equals(getVerticalKingdom())).findFirst();
                }
                if (cc1.isPresent() && cc2.isPresent()) {
                    tmpSet.add((PlayableCard) cc1.get());
                    tmpSet.add((PlayableCard) cc2.get());
                    tmpSet.add((PlayableCard) c);
                    try {
                        usedCard.addAll(tmpSet);
                    } catch (IllegalArgumentException e){
                        //the cards are not added to the set, because one of them is already in
                    }
                }
            }
        }
        return usedCard.size()/3*getPoints();
    }

    /**
     *  Getter for direction attribute
     * @return direction
     */
    public LDirection getDirection() {
        return direction;
    }

    /**
     * Getter for verticalKingdom attribute
     * @return verticalKingdom
     */
    public CardSymbolKingdom getVerticalKingdom() {
        return verticalKingdom;
    }

    /**
     * Getter for horizontalKingdom
     * @return horizontalKingdom
     */
    public CardSymbolKingdom getHorizontalKingdom() {
        return horizontalKingdom;
    }
}
