package se.umu.thlo0007.dicegame_revised;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This activity is the Controller between the Model holding the data and the View holding the view elements concerning the results of the game.
 * This activity is always started from {@link MainActivity}, and all data needed is collected from the starting Intent.
 * The activity implements the interface {@link se.umu.thlo0007.dicegame_revised.NewGameDialogFragment.NewGameDialogListener},
 * enabling it to handle events passed back from the NewGameDialogFragment.
 *
 * @author Thim Lohse
 **/
public class ResultsActivity extends AppCompatActivity implements NewGameDialogFragment.NewGameDialogListener{

    private TextView mTotalScore;
    private ExpandableListView mResultsListView;
    private ArrayList<Integer> mResultsArrayList;
    private ArrayList<String> mResultsChoiceList;
    private GamePlayModel mGamePlayModel;
    private HashMap<String, ArrayList<Integer>> mResultsDiceImageListMap;
    private ExpandableResultListAdapter mResultListAdapter;
    private Intent mIntent;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mToolbar = (Toolbar)findViewById(R.id.actionToolbar);
        setSupportActionBar(mToolbar);

        mIntent = getIntent();
        mGamePlayModel = mIntent.getParcelableExtra(MainActivity.SAVED_GAME_PLAY_KEY);
        mResultsArrayList = mGamePlayModel.getScores();
        mResultsChoiceList = mGamePlayModel.getScoreChoices();
        mResultsDiceImageListMap = mGamePlayModel.getRoundDiceImageData();

        mTotalScore = (TextView)findViewById(R.id.results_total_score);

        mTotalScore.setText(String.valueOf(mGamePlayModel.getScoreTotal()));

        mResultsListView = (ExpandableListView)findViewById(R.id.results_listView);

        mResultListAdapter = new ExpandableResultListAdapter(this, mResultsArrayList, mResultsChoiceList, mResultsDiceImageListMap);

        mResultsListView.setAdapter(mResultListAdapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method handles click actions on the menu bar.
     *
     * @param item The item clicked.
     * @return The boolean value representing if the selected menu item was handled successfully.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.icon_new_game:
            {
                showNewGameFragment();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    /**
     * This method creates a NewGameDialogFragment and displays it to the user.
     */
    public void showNewGameFragment()
    {
        DialogFragment dialogFragment = new NewGameDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "NewGameDialogFragment");
    }

    /**
     * This method handles the positive callback event from the {@link NewGameDialogFragment}.
     * The method creates a new intent and sets flags to clear the back stack,
     * before a new game is started to reduce memory usage. It then destroys the current instance of {@link ResultsActivity}.
     *
     * @param dialogFragment The dialog fragment passed back to the parent/creating activity.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
        Intent newGameIntent = new Intent(ResultsActivity.this, MainActivity.class);
        //Flaggorna i detta intent meddelar att en ny instans av main-aktivitet skall startas och att backstack är okej att rensas.
        newGameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newGameIntent);
        finish();

    }

    /**
     * This method handles the negative callback event from the {@link NewGameDialogFragment}.
     * @param dialogFragment The dialog fragment passed back to the parent/creating activity.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
    }
}
