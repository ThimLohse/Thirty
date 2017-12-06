package se.umu.thlo0007.dicegame_revised;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class handles the overall game data, such as Throws, Rounds and Scores.
 * The class is also responsible for holding the score choice of each round,
 * and the image-resource references of the dice faces used each round.
 *
 * **/
public class GamePlayModel implements Parcelable {

    private int throwCounter;
    private int roundCounter;
    private int scoreTotal;
    private boolean gameOver;
    private ArrayList<Integer> scores;
    private ArrayList<String> scoreChoices;
    private HashMap<String, ArrayList<Integer>> roundDiceImageData;

    public GamePlayModel() {
        throwCounter = 0;
        roundCounter = 1;
        scoreTotal = 0;
        gameOver = false;
        scores = new ArrayList<>();
        scoreChoices = new ArrayList<>();
        roundDiceImageData = new HashMap<>();
    }


    public void newThrow()
    {
        throwCounter += 1;
    }

    /**
     * Updates the data-model when notified of new round. Only possible for 10 rounds, as it is the game limit.
     */
    public void newRound()
    {
        if(roundCounter < 10)
        {
            throwCounter = 0;
            roundCounter += 1;
        }
        else
        {
            gameOver = true;
        }
    }

    /**
     * The score for the current round is added both to the total and in the score list as a separate round score.
     *
     * @param score Round score.
     */
    public void addScore(int score)
    {
        scores.add(score);
        scoreTotal += score;

    }
    public ArrayList<Integer> getScores() { return scores; }

    public void addScoreChoice(String scoreChoice)
    {
        scoreChoices.add(scoreChoice);
    }

    /**
     * This method collects all the image-resource reference ids from the dices facing up.
     * The method then combines the list of dice faces in a Key-Value pair,
     * with the Key being the score choice for the round, and the Value being the list of dice faces.
     *
     * @param diceValues The dices used in the current round.
     * @param scoreChoice The score choice used to calculate the score the current round.
     */
    public void addDiceImageData(ArrayList<Dice> diceValues, String scoreChoice)
    {
        ArrayList<Integer> diceImageIds = new ArrayList();
        for(Dice d : diceValues)
        {
            diceImageIds.add(d.getDiceFace());
        }
        this.roundDiceImageData.put(scoreChoice, diceImageIds);
    }

    public ArrayList<String> getScoreChoices(){ return scoreChoices; }

    public HashMap<String, ArrayList<Integer>> getRoundDiceImageData()
    {
        return roundDiceImageData;
    }

    public int getScoreTotal() { return scoreTotal; }

    public int getThrowCounter() {
        return throwCounter;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public boolean isGameOver() {
        return gameOver;
    }


    protected GamePlayModel(Parcel in) {
        throwCounter = in.readInt();
        roundCounter = in.readInt();
        scoreTotal = in.readInt();
        if(in.readByte() == 0)
        {
            gameOver = false;
        }
        else
        {
            gameOver = true;
        }
        scores = new ArrayList<>();
        in.readList(scores, null);
        scoreChoices = new ArrayList<>();
        in.readList(scoreChoices, null);
        roundDiceImageData = new HashMap<>();
        in.readMap(roundDiceImageData, null);

    }

    public static final Creator<GamePlayModel> CREATOR = new Creator<GamePlayModel>() {
        @Override
        public GamePlayModel createFromParcel(Parcel in) {
            return new GamePlayModel(in);
        }

        @Override
        public GamePlayModel[] newArray(int size) {
            return new GamePlayModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(throwCounter);
        dest.writeInt(roundCounter);
        dest.writeInt(scoreTotal);
        dest.writeByte((byte) (gameOver ? 1 : 0));
        dest.writeList(scores);
        dest.writeList(scoreChoices);
        dest.writeMap(roundDiceImageData);




    }
}
