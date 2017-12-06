package se.umu.thlo0007.dicegame_revised;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * This class/Activity is the Controller between the data models and views used during the game play of the application.
 *
 * @author Thim Lohse
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, NewGameDialogFragment.NewGameDialogListener {

    private static final String SAVED_DICES_KEY = "Dices";
    private static final String SAVED_SCORE_CALCULATOR_KEY = "ScoreCalculator";
    private static final String SAVED_SCORE_SELECTION_KEY = "ScoreSelection";
    private static final String SAVED_SCORE_ITEMS_KEY = "ScoreItems";
    public static final String SAVED_GAME_PLAY_KEY = "GamePlay";
    public static final String NEW_GAME_KEY = "NewGame";
    private ArrayList<Dice> mDiceDataModelRef;
    private GridView mGridView;
    private Animation mIndicateUsageOrChangeAnimation;
    private Button mThrowBtn;
    private Button mCalculateBtn;
    private Spinner mScoreChoices;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private ArrayList<CharSequence> mScoreItems;
    private ScoreCalculator mScoreCalculator;
    private MediaPlayer mMediaPlayer;
    private TextView mCounterText;
    private GamePlayModel mGamePlayModel;
    private DiceAdapter mDiceAdapter;
    private Toolbar mToolbar;
    private boolean mStartNewGame;

    /**
     * This enum is used to provide an easy way to notify the activity on which UI components to update.
     */
    private enum mUpdateUIComponents
    {
        THROW, ROUND, DICE_CLICK
    }


    /**
     * This method is used to link up all the view elements of the activity, load the animations used in the activity,
     * as well as link all the listeners to the activity.
     *
     * @param savedInstanceState The most recent saved state of the activity, equals NULL when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar)findViewById(R.id.actionToolbar);
        setSupportActionBar(mToolbar);
        mThrowBtn = (Button)findViewById(R.id.throw_btn);
        mCalculateBtn = (Button)findViewById(R.id.calculate_btn);
        mScoreChoices = (Spinner)findViewById(R.id.score_choices);
        mCounterText = (TextView)findViewById(R.id.throwText);
        mGridView = (GridView)findViewById(R.id.dice_gridview);
        mIndicateUsageOrChangeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);

        if(savedInstanceState == null)
        {
            initDataModels();
        }
        else if(savedInstanceState.getBoolean(NEW_GAME_KEY))
        {
            initDataModels();
        }

        mGridView.setOnItemClickListener(this);
        mScoreChoices.setOnItemSelectedListener(this);
        mThrowBtn.setOnClickListener(this);
        mCalculateBtn.setOnClickListener(this);

    }

    /**
     * This method is used to handle click events on the buttons.
     *
     * @param v The button which has been clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.throw_btn:
            {
                notifyNewThrow();
                updateUI(mUpdateUIComponents.THROW);
                break;
            }
            case R.id.calculate_btn:
            {
                notifyNewRound();
                updateUI(mUpdateUIComponents.ROUND);
                break;
            }

        }

    }

    /**
     * This method is used to handle click events from the user on the {@link MainActivity#mGridView}.
     * The user can only interact with the dices if at least 1 throw has been made.
     * This is done do avoid cheating, and saving dices between rounds.
     *
     * @param parent The GridView showing the {@link Dice}.
     * @param view unused.
     * @param position The position of the {@link Dice} in the GridView.
     * @param id unused.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.dice_gridview:
            {
                if((mGamePlayModel.getThrowCounter() > 0))
                {
                    ((Dice) parent.getItemAtPosition(position)).onClickDice();
                    updateUI(mUpdateUIComponents.DICE_CLICK);
                }
                break;
            }
            default:
            {
                break;
            }
        }

    }

    /**
     * This method handles the positive callback event from the {@link NewGameDialogFragment}.
     *
     * @param dialogFragment The dialog fragment passed back to the parent/creating activity.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {

        dialogFragment.dismiss();
        startNewGame();
    }

    /**
     * This method handles the negative callback event from the {@link NewGameDialogFragment}.
     * @param dialogFragment The dialog fragment passed back to the parent/creating activity.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }

    /**
     * This method is used to handle selections of score choices by the user.
     *
     * @param position The position of the selected score choice item.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mScoreCalculator.setScoreChoice((String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * This method is used to initialize the data models used in the activity. This is used the first time the app starts,
     * and if the user requests a new game.
     */
    private void initDataModels()
    {
        mGamePlayModel = new GamePlayModel();
        mScoreCalculator = new ScoreCalculator();
        mScoreItems = getScoreItems();
        mDiceDataModelRef = getDices();
        mStartNewGame = false;
    }

    /**
     * This method is used to notify the data models about an interaction from the user,
     * corresponding to a new throw of the dices.
     *
     * A new throw can only be invoked if the game is not over, and the user can only throw the dice 3 times per round.
     */
    private void notifyNewThrow()
    {
        if(!mGamePlayModel.isGameOver() && mGamePlayModel.getThrowCounter() < 3)
        {
            for (Dice d : mDiceDataModelRef)
            {
                d.throwDice();
            }
            mGamePlayModel.newThrow();
            mMediaPlayer.start();
        }
        else
        {
            mCalculateBtn.startAnimation(mIndicateUsageOrChangeAnimation);
        }
    }

    /**
     * This method is used to notify the data models about an interaction from the user,
     * corresponding to the end of the current round and the start of a new one.
     *
     * A new round can only be invoked if the game is not over, and if the user has thrown the dices at least once.
     * The latter is used to avoid cheating.
     *
     */
    private void notifyNewRound()
    {
        //Beräkning kan endast göras om tärningarna kastats minst en gång denna omgång. (Detta för att undvika fusk)
        if(!mGamePlayModel.isGameOver() && mGamePlayModel.getThrowCounter() > 0)
        {

            for (Dice d : mDiceDataModelRef)
            {
                d.roundRestore();
            }
            mScoreCalculator.setDiceValues(mDiceDataModelRef);
            mGamePlayModel.addScore(mScoreCalculator.getScore());
            mGamePlayModel.addScoreChoice(mScoreCalculator.getScoreChoice());
            mGamePlayModel.addDiceImageData(mDiceDataModelRef, mScoreCalculator.getScoreChoice());
            mScoreItems.remove(mScoreChoices.getSelectedItemPosition());
            mGamePlayModel.newRound();
        }
        else
        {
            mThrowBtn.startAnimation(mIndicateUsageOrChangeAnimation);
        }
        if(mGamePlayModel.isGameOver())
        {
            Intent resultsIntent = new Intent(MainActivity.this, ResultsActivity.class);
            resultsIntent.putExtra(SAVED_GAME_PLAY_KEY, mGamePlayModel);
            startActivity(resultsIntent);
        }

    }

    /**
     * This method is used to update the correct UI components, depending on the game button pressed.
     *
     * @param selector The {@link mUpdateUIComponents} value used as a selector to determine which UI components to update,
     *                 and with which parameters.
     */
    private void updateUI(mUpdateUIComponents selector)
    {
        String userMessage;
        int userMessageNumber;

        switch (selector)
        {
            case THROW:
            {
                userMessage = getString(R.string.current_throw_text);
                userMessageNumber = mGamePlayModel.getThrowCounter();
                updateCounterWithAnimation(mCounterText, userMessage, userMessageNumber);


                break;
            }
            case ROUND:
            {
                userMessage = getString(R.string.current_round_text);
                userMessageNumber = mGamePlayModel.getRoundCounter();
                updateCounterWithAnimation(mCounterText, userMessage, userMessageNumber);
                mSpinnerAdapter.notifyDataSetChanged();
                mScoreChoices.setAdapter(mSpinnerAdapter);

               break;
            }
            case DICE_CLICK:
            {
                break;
            }

        }
        mDiceAdapter.notifyDataSetChanged();

        if(mGamePlayModel.isGameOver())
        {
            userMessage = getString(R.string.game_over_text);
            userMessageNumber = -1;
            updateCounterWithAnimation(mCounterText, userMessage, userMessageNumber);
        }

    }

    /**
     * This method creates a new list of 6 {@link Dice}, to be used in the game.
     * @return A new list of {@link Dice}.
     */
    private ArrayList<Dice> getDices()
    {
        ArrayList<Dice> dices = new ArrayList<>();
        dices.add(new Dice());
        dices.add(new Dice());
        dices.add(new Dice());
        dices.add(new Dice());
        dices.add(new Dice());
        dices.add(new Dice());
        return dices;
    }

    /**
     * This method creates a new list of String score choices to be used in {@link MainActivity#mScoreChoices}.
     *
     * @return A new list of all the score choices available.
     */
    private ArrayList<CharSequence> getScoreItems()
    {
        ArrayList<CharSequence> scoreChoices = new ArrayList<>();
        scoreChoices.add("Låga");
        scoreChoices.add("4");
        scoreChoices.add("5");
        scoreChoices.add("6");
        scoreChoices.add("7");
        scoreChoices.add("8");
        scoreChoices.add("9");
        scoreChoices.add("10");
        scoreChoices.add("11");
        scoreChoices.add("12");
        return scoreChoices;
    }

    /**
     * This method is used to update the counter view element with the correct value and/or message to display to the user.
     * The animation runs on a separate thread to not block the UI.
     *
     * @param textView The textView to be updated.
     * @param msg The message to be added to the textView.
     * @param value The value to be added to the textView.
     */
    private void updateCounterWithAnimation(final TextView textView, String msg, int value)
    {
        if(value < 0)
        {
            textView.setText(msg);
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setText(msg + value);
            Thread animationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    textView.setVisibility(View.VISIBLE);
                    textView.startAnimation(mIndicateUsageOrChangeAnimation);
                    textView.setVisibility(View.INVISIBLE);
                }
            });
            animationThread.run();
        }

    }

    /**
     * This method is used to notify the activity that a new game should be started,
     * which effectively ends the previous game.
     *
     * The method uses method recreate to force the activity to destroy itself and call onCreate.
     */
    private void startNewGame()
    {
        mStartNewGame = true;
        recreate();
    }

    /**
     * This method creates a NewGameDialogFragment and displays it to the user.
     */
    public void showNewGameDialog()
    {
        DialogFragment dialogFragment = new NewGameDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "NewGameDialogFragment");
    }

    /**
     * This method is used to inflate and create a custom menu bar.
     *
     * @param menu The reference to the menu view element.
     * @return The menu view element, with the custom menu.xml inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is used to handle user selection from the menu bar.
     *
     * @param item The item selected by the user.
     * @return The boolean value representing if the selected menu item was handled successfully.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.icon_new_game:
            {
                showNewGameDialog();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method is used to allocate resources needed when the activity is visible.
     * The method also create references to the needed adapters and links them to their corresponding views.
     * This is done to have a cleaner use of {@link MainActivity#onRestoreInstanceState(Bundle)},
     * as it is called after onCreate but before onResume.
     */
    @Override
    protected void onResume() {

        mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.shake_and_roll_short);
        mMediaPlayer.setLooping(false);

        mDiceAdapter = new DiceAdapter(this, mDiceDataModelRef);
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mScoreItems);
        mGridView.setAdapter(mDiceAdapter);
        mScoreChoices.setAdapter(mSpinnerAdapter);

        super.onResume();
    }

    /**
     * This method is used to release resources used when the activity is not visible.
     */
    @Override
    protected void onPause() {

        mDiceAdapter = null;
        mSpinnerAdapter = null;

        if(mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        super.onPause();
    }

    /**
     * This method is used to save the current state of the activity at state change, such as rotation or destruction.
     *
     * @param outState The current state of the activity.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(SAVED_DICES_KEY, mDiceDataModelRef);
        outState.putParcelable(SAVED_SCORE_CALCULATOR_KEY, mScoreCalculator);
        outState.putInt(SAVED_SCORE_SELECTION_KEY, mScoreChoices.getSelectedItemPosition());
        outState.putParcelable(SAVED_GAME_PLAY_KEY, mGamePlayModel);
        outState.putCharSequenceArrayList(SAVED_SCORE_ITEMS_KEY, mScoreItems);
        outState.putBoolean(NEW_GAME_KEY, mStartNewGame);
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is used to restore the saved state of the activity at state change.
     * The restoration will only occur if the user has not asked for a new game.
     *
     * @param savedInstanceState The most recent saved state of the activity.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(!savedInstanceState.getBoolean(NEW_GAME_KEY))
        {
            mDiceDataModelRef = savedInstanceState.getParcelableArrayList(SAVED_DICES_KEY);
            mScoreCalculator = savedInstanceState.getParcelable(SAVED_SCORE_CALCULATOR_KEY);
            mScoreChoices.setSelection(savedInstanceState.getInt(SAVED_SCORE_SELECTION_KEY));
            mScoreItems = savedInstanceState.getCharSequenceArrayList(SAVED_SCORE_ITEMS_KEY);
            mGamePlayModel = savedInstanceState.getParcelable(SAVED_GAME_PLAY_KEY);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}

