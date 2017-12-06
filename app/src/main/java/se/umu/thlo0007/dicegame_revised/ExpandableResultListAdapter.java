package se.umu.thlo0007.dicegame_revised;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a custom Expandable List Adapter, and the Controller between the result list view and the result data-model.
 * It is responsible for populating an expandable list with information about the rounds,
 * such as score, score choice, and as expandable child view, the dice values/faces used to calculate the score.
 *
 * @author Thim Lohse
 */
public class ExpandableResultListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<Integer> mScoreDataSource;
    private ArrayList<String> mScoreChoiceDataSource;
    private HashMap<String, ArrayList<Integer>> mDiceImages;

    /**
     *
     * @param mContext The parent/creating activity context.
     * @param mScoreDataSource A list holding the score for each round.
     * @param mScoreChoiceDataSource A list holding the score choice for each round.
     * @param mDiceImages A hashmap holding Key-Value pairs with Key: Score choice, Value: The dice value/faces used to calculate the score for the specific score choice.
     */
    public ExpandableResultListAdapter(Context mContext, ArrayList<Integer> mScoreDataSource,
                                       ArrayList<String> mScoreChoiceDataSource, HashMap<String, ArrayList<Integer>> mDiceImages) {
        this.mContext = mContext;
        this.mScoreDataSource = mScoreDataSource;
        this.mScoreChoiceDataSource = mScoreChoiceDataSource;
        this.mDiceImages = mDiceImages;
    }

    /**
     * @return The size of the list of scores, equivalent with the number of rounds.
     */
    @Override
    public int getGroupCount() {
        return mScoreDataSource.size();
    }

    /**
     *
     * @param groupPosition The position of the group in the list.
     * @return Always return 1, as this implementation only allows one child view per group.
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /**
     *
     * @param groupPosition The position of the group.
     * @return The score value of the specific group position.
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.mScoreDataSource.get(groupPosition);
    }

    /**
     *
     * @param groupPosition The position of the group which determines which dices should be fetched from the hashmap mDiceImages.
     * @param childPosition unused, as the score choice is used as key for the hashmap mDiceImages.
     * @return An array of image resource ids corresponding to the group position.
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mDiceImages.get(this.mScoreChoiceDataSource.get(groupPosition));
    }

    /**
     * @param groupPosition
     * @return group position equivalent to group Id.
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * @param groupPosition The position of the group.
     * @param childPosition unused.
     * @return group position equivalent to child Id, as every group only have 1 child.
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * This method populates each group view cell with data about round number, score choice and score.
     * If the view has not already been created, the view-cell layout is inflated and the references to the view elements are hooked up.
     * The method uses the class GroupViewHolder to minimize calls to findViewById and reduce memory usage.
     *
     * @param groupPosition The current group position to populate with data.
     * @param isExpanded The boolean value representing if the group is expanded or not.
     * @param convertView The view cell to inflate and populate.
     * @param parent The expandable list view.
     * @return The inflated view cell, populated with data.
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Integer score = mScoreDataSource.get(groupPosition);
        final int round = groupPosition + 1;
        final String scoreChoice = mScoreChoiceDataSource.get(groupPosition);

        if(convertView == null)
        {
            LayoutInflater mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.results_group_layout, parent, false);


            final TextView roundNumber = (TextView)convertView.findViewById(R.id.round_number);
            final TextView roundScore = (TextView)convertView.findViewById(R.id.round_score);
            final TextView roundChoice = (TextView)convertView.findViewById(R.id.round_score_choice);
            final GroupViewHolder viewHolder = new GroupViewHolder(roundNumber,roundScore, roundChoice);

            convertView.setTag(viewHolder);

        }

        final GroupViewHolder groupViewHolder = (GroupViewHolder) convertView.getTag();
        groupViewHolder.roundNumber.setText(String.valueOf(round));
        groupViewHolder.roundScore.setText(String.valueOf(score));
        groupViewHolder.roundChoice.setText(scoreChoice);

        return convertView;
    }

    /**
     * This method inflates and populates each child view cell with the dice values/faces used to calculate the score of that round.
     * If the view has not already been created, the view-cell layout is inflated and the references to the view elements are hooked up.
     * This method uses the class ChildViewHolder to minimize calls to findViewById and reduce memory usage.
     *
     * @param groupPosition The position of the group of which child view cell to inflate and populate.
     * @param childPosition The position of the child. Always 1, in this implementation.
     * @param isLastChild The boolean value representing if the child is the last child of the group.
     * @param convertView The view cell to inflate and populate.
     * @param parent The group in which the child view resides.
     * @return The inflated view cell, populated with data.
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final List<Integer> diceImages = (List) getChild(groupPosition, childPosition);

        if(convertView == null)
        {
            LayoutInflater mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.results_child_layout, parent, false);

            final ImageView diceOne = (ImageView)convertView.findViewById(R.id.child_item_1);
            final ImageView diceTwo = (ImageView)convertView.findViewById(R.id.child_item_2);
            final ImageView diceThree = (ImageView)convertView.findViewById(R.id.child_item_3);
            final ImageView diceFour = (ImageView)convertView.findViewById(R.id.child_item_4);
            final ImageView diceFive = (ImageView)convertView.findViewById(R.id.child_item_5);
            final ImageView diceSix = (ImageView)convertView.findViewById(R.id.child_item_6);

            final ChildViewHolder childViewHolder = new ChildViewHolder(diceOne, diceTwo, diceThree, diceFour, diceFive, diceSix);
            convertView.setTag(childViewHolder);
        }

        final ChildViewHolder childViewHolder = (ChildViewHolder) convertView.getTag();
        childViewHolder.diceOne.setImageResource(diceImages.get(0));
        childViewHolder.diceTwo.setImageResource(diceImages.get(1));
        childViewHolder.diceThree.setImageResource(diceImages.get(2));
        childViewHolder.diceFour.setImageResource(diceImages.get(3));
        childViewHolder.diceFive.setImageResource(diceImages.get(4));
        childViewHolder.diceSix.setImageResource(diceImages.get(5));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * This class holds necessary references for the group view to minimize memory expensive calls to findViewByID.
     */
    private class GroupViewHolder
    {
        private final TextView roundNumber;
        private final TextView roundScore;
        private final TextView roundChoice;

        public GroupViewHolder(TextView roundNumber, TextView roundScore, TextView roundChoice) {
            this.roundNumber = roundNumber;
            this.roundScore = roundScore;
            this.roundChoice = roundChoice;
        }
    }
    /**
     * This class holds necessary references for the child view to minimize memory expensive calls to findViewByID.
     */
    private class ChildViewHolder
    {
        private final ImageView diceOne;
        private final ImageView diceTwo;
        private final ImageView diceThree;
        private final ImageView diceFour;
        private final ImageView diceFive;
        private final ImageView diceSix;

        public ChildViewHolder(ImageView diceOne, ImageView diceTwo,
                               ImageView diceThree, ImageView diceFour,
                               ImageView diceFive, ImageView diceSix) {
            this.diceOne = diceOne;
            this.diceTwo = diceTwo;
            this.diceThree = diceThree;
            this.diceFour = diceFour;
            this.diceFive = diceFive;
            this.diceSix = diceSix;
        }
    }
}
