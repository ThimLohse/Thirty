package se.umu.thlo0007.dicegame_revised;


import android.os.Parcel;
import android.os.Parcelable;
import java.util.Random;


/**
 *
 * This class is a datamodel representing a 6-sided dice. It handles representation of the dice value, and throws of the dice.
 *
 * @author Thim Lohse
 *
 */
public class Dice implements Parcelable{
    private Random randomGenerator;
    private int diceFaceID;
    private int diceValue;
    private boolean chosen;
    private boolean animate;
    private final int[] diceFaces = {R.drawable.dice_1, R.drawable.dice_2,R.drawable.dice_3,
            R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6};
    private final int[] diceFacesChosen = {R.drawable.dice_1_pressed, R.drawable.dice_2_pressed,R.drawable.dice_3_pressed,
            R.drawable.dice_4_pressed,R.drawable.dice_5_pressed,R.drawable.dice_6_pressed};

    public Dice() {
        this.diceFaceID = 0;
        this.diceValue = 1;
        this.chosen = false;
        this.animate = true;

    }

    /**
     * Method that simulates a dice throw.
     *
     * The method uses an internal boolean value in its conditional statement to decide whether the dice should be thrown or not.
     */
    public void throwDice()
    {
        if(!isChosen())
        {
            animate = true;
            randomGenerator = new Random();
            int throwValue = randomGenerator.nextInt(6);
            diceValue = throwValue + 1;
            diceFaceID = throwValue;

        }
    }

    /**
     * This method sets the boolean value deciding if the dice should animate or not.
     * @param animate The boolean value to set
     */
    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    /**
     *
     * @return The boolean value representing if the dice is chosen or not
     */
    public boolean isChosen() {
        return chosen;
    }

    /**
     *
     * @return The boolean value representing if the dice should animate or not
     */
    public boolean isAnimate() {
        return animate;
    }

    /**
     *
     * @return The integer ID of the image-resource to present in the UI.
     */
    public int getDiceFace() {

        if (isChosen())
        {
            return diceFacesChosen[diceFaceID];
        }
        return diceFaces[diceFaceID];
    }

    /**
     *
     * @return The current dice integer value
     *
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * This method is called to notify the dice when a user tap/click on the dice in the UI.
     * The method uses the internal boolean values to interpret if the dice is chosen or not and alternates between the two states.
     *
     */
    public void onClickDice()
    {
        if(chosen)
        {
            chosen = false;

        }
        else
        {
            chosen = true;
        }
        animate = false;
    }

    /**
     * This method is called when the dice is notified that a new game round is about to start.
     *
     * All dices that was chosen at any point during the previous round are restored.
     */
    public void roundRestore()
    {
        chosen = false;
        animate = false;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(diceFaceID);
        dest.writeInt(diceValue);
        dest.writeByte((byte) (chosen ? 1 : 0));

    }
    protected Dice(Parcel in) {
        diceFaceID = in.readInt();
        diceValue = in.readInt();
        if(in.readByte() == 0)
        {
            chosen = false;
            animate = true;

        }
        else
        {
            chosen = true;
            animate = false;

        }
    }

    public static final Creator<Dice> CREATOR = new Creator<Dice>() {
        @Override
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };
}
