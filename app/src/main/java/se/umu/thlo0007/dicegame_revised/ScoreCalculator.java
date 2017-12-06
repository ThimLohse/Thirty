package se.umu.thlo0007.dicegame_revised;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class handles all the calculations of the score each round in the game.
 */
public class ScoreCalculator implements Parcelable {

    private String scoreChoice;
    private ArrayList<Integer> diceValues;

    /**
     * This enum is used to provide an easy way to sort the generated power set in different ways.
     */
    public enum SortChoice
    {
        SORT_ON_SUM_ONLY,SORT_ON_SUM_AND_NUM_DICES_NEEDED
    }

    public ScoreCalculator() {
        this.diceValues = new ArrayList<>();
    }

    public void setScoreChoice(String scoreChoice) {
        this.scoreChoice = scoreChoice;
    }

    /**
     * This method extracts the dice values for the round from the Dice objects.
     * @param dices The list of Dice objects holding necessary information about the round.
     */
    public void setDiceValues(ArrayList<Dice> dices)
    {
        diceValues = new ArrayList<>();
        for (Dice d : dices)
        {
            diceValues.add(d.getDiceValue());
        }
    }

    public String getScoreChoice() { return scoreChoice; }

    public int getScore() {
        return Calculate(diceValues, scoreChoice);
    }

    /**
     * This method calculates the total score of the round depending on the score choice and the dice values.
     * The method uses {@link se.umu.thlo0007.dicegame_revised.ScoreCalculator.SortChoice} to calculate the score in two ways,
     * to ensure the order of the combinations in the subset is not affecting the outcome.
     *
     * @param diceValues The values of the dices used the current round.
     * @param scoreChoice The score choice used the current round.
     * @return The highest integer value of the two score calculations.
     */
    public int Calculate(ArrayList<Integer> diceValues, String scoreChoice)
    {

        int totalScore = 0;
        int SortOnSumOnlyScore = 0;
        int SortOnSumAndNumDicesScore = 0;
        ArrayList<SubSetAndSum> filteredSubSets;
        ArrayList<Integer> remainingDiceValues;

        if(scoreChoice.equals("Låga"))
        {
            for(int value : diceValues)
            {
                if(value < 4)
                {
                    totalScore += value;
                }
            }
        }
        else
        {
            int integerScoreChoice = Integer.valueOf(scoreChoice);

            for (SortChoice sortChoice : SortChoice.values())
            {

                remainingDiceValues = new ArrayList<>();
                remainingDiceValues.addAll(diceValues);

                filteredSubSets = FilterAndSort(GeneratePowerSet(diceValues), integerScoreChoice, sortChoice);

                for (SubSetAndSum ssp : filteredSubSets)
                {

                    if (ContainsAllElements(remainingDiceValues, ssp.getSet()))
                    {

                        for (int diceValue : ssp.getSet())
                        {
                            int indexToBeRemoved = remainingDiceValues.indexOf(diceValue);
                            remainingDiceValues.remove(indexToBeRemoved);
                        }
                        switch(sortChoice)
                        {
                            case SORT_ON_SUM_ONLY:
                            {
                                SortOnSumOnlyScore += ssp.getSum();
                                break;
                            }
                            case SORT_ON_SUM_AND_NUM_DICES_NEEDED:
                            {
                                SortOnSumAndNumDicesScore += ssp.getSum();
                                break;
                            }
                        }

                    }

                }
            }

            totalScore = ReturnLargest(SortOnSumOnlyScore, SortOnSumAndNumDicesScore);
        }

        return totalScore;
    }
    /**
     * This method generate a power set from the array list of values of the dices for the current round.
     * Each set in the power set is a SubSetAndSum object, which holds the sub set and the sum of that sub set.
     * @return The power set as an array list of {@link se.umu.thlo0007.dicegame_revised.SubSetAndSum}.
     *
     * <b>Note:</b> The GetPowerSet method is based on the following algorithm:
     * <a href="http://www.geeksforgeeks.org/finding-all-subsets-of-a-given-set-in-java/">http://www.geeksforgeeks.org/finding-all-subsets-of-a-given-set-in-java/</a>
     * <b>For more about power sets:</b>
     * <a href="https://en.wikipedia.org/wiki/Power_set">https://en.wikipedia.org/wiki/Power_set</a>
     */
    public ArrayList<SubSetAndSum> GeneratePowerSet(ArrayList<Integer> setOfValues)
    {

        int n = setOfValues.size();
        int numberOfSubsets = (int)Math.pow(2,n);
        ArrayList<SubSetAndSum> subSetList = new ArrayList<>();

        for(int i = 0; i < numberOfSubsets; i++)
        {
            subSetList.add(new SubSetAndSum());
        }
        for(int i = 0; i < (1<<n); i++)
        {
            for(int j = 0; j < n; j++)
            {
                if((i & ( 1 << j)) > 0)
                {
                    subSetList.get(i).addElement(setOfValues.get(j));
                }
            }
        }

        return subSetList;
    }

    /**
     * This method compares two lists of Integers.
     *
     * @param sourceData A first list of integers.
     * @param comparableData A second list of integers.
     * @return true if and only if, the first list of integers contains each and every one of the elements in the second list, duplicates included.
     */
    private boolean ContainsAllElements(ArrayList<Integer> sourceData, ArrayList<Integer> comparableData)
    {
        ArrayList<Integer> remainingValues = new ArrayList<>();
        remainingValues.addAll(sourceData);

        int indexOfElement;

        for(int comparableInteger : comparableData)
        {

            indexOfElement = remainingValues.indexOf(comparableInteger);
            if(indexOfElement != -1)
            {
                remainingValues.remove(indexOfElement);
            }
            else
            {

                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to filter down the power set list of {@link se.umu.thlo0007.dicegame_revised.SubSetAndSum},
     * to those having the same sum as the score choice for the round.
     * The method also sort the list depending on which {@link se.umu.thlo0007.dicegame_revised.ScoreCalculator.SortChoice} is used.
     * This is done to simplify the actual calculation.
     *
     * @param sourceData The power set for the current round.
     * @param scoreChoice The score choice for the current round, converted to Integer.
     * @param sortChoice The {@link se.umu.thlo0007.dicegame_revised.ScoreCalculator.SortChoice}. Decides which sorting should be executed.
     * @return The filtered and sorted list of {@link se.umu.thlo0007.dicegame_revised.SubSetAndSum}.
     */
    private ArrayList<SubSetAndSum> FilterAndSort(ArrayList<SubSetAndSum> sourceData, int scoreChoice, SortChoice sortChoice)
    {
        ArrayList<SubSetAndSum> filteredData = new ArrayList<>();
        for(SubSetAndSum ssp : sourceData) {
            if (ssp.getSum() / scoreChoice == 1 && ssp.getSum() % scoreChoice == 0) {
                filteredData.add(ssp);
            }
        }
        switch (sortChoice)
        {
            case SORT_ON_SUM_ONLY:
            {
                Collections.sort(filteredData, SubSetAndSum.SumComparator);
                break;
            }
            case SORT_ON_SUM_AND_NUM_DICES_NEEDED:
            {
                Collections.sort(filteredData, SubSetAndSum.SumAndNumDicesComparator);
                break;
            }
            default:
            {
                break;
            }
        }

        return filteredData;
    }

    /**
     * This method compares two integer values.
     *
     * @param i1 The first integer value.
     * @param i2 The second integer value.
     * @return The largest of the two integer values.
     */
    public int ReturnLargest(int i1, int i2)
    {
        if(i1 >= i2)
        {
            return i1;
        }
        return i2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scoreChoice);

    }
    protected ScoreCalculator(Parcel in) {
        scoreChoice = in.readString();

    }

    public static final Creator<ScoreCalculator> CREATOR = new Creator<ScoreCalculator>() {
        @Override
        public ScoreCalculator createFromParcel(Parcel in) {
            return new ScoreCalculator(in);
        }

        @Override
        public ScoreCalculator[] newArray(int size) {
            return new ScoreCalculator[size];
        }
    };
}
