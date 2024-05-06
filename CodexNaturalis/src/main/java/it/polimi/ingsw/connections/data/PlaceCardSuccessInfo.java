package it.polimi.ingsw.connections.data;

public class PlaceCardSuccessInfo {
    private int cardsPoint;
    private int goalsPoints;
    private CardInfo playedCard;

    public PlaceCardSuccessInfo(int cardsPoint, int goalsPoints, CardInfo playedCard) {
        this.cardsPoint = cardsPoint;
        this.goalsPoints = goalsPoints;
        this.playedCard = playedCard;
    }

    public int getCardsPoint() {
        return cardsPoint;
    }

    public int getGoalsPoints() {
        return goalsPoints;
    }

    public CardInfo getPlayedCard() {
        return playedCard;
    }
}
