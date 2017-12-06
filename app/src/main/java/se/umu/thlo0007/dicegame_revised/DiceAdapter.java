package se.umu.thlo0007.dicegame_revised;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * This adapter class is the Controller between the Model representing the dices, and the View.
 * Responsible for populating the view with the use of the image resource information in the Dice objects.
 * Also responsible for animating a shake to each view item when appropriate.
 *
 * @author Thim Lohse
 *
 * **/
public class DiceAdapter extends BaseAdapter {

    private final ArrayList<Dice> mDices;
    private final LayoutInflater mInflater;
    private final Animation shake_animation;

    /**
     *
     * @param parent The context of the activity the adapter was created.
     * @param dices The data source used by the adapter to populate the view.
     */
    public DiceAdapter(Context parent, ArrayList<Dice> dices) {
        this.mDices = dices;
        this.mInflater = (LayoutInflater) parent.getSystemService(parent.LAYOUT_INFLATER_SERVICE);
        this.shake_animation = AnimationUtils.loadAnimation(parent, R.anim.shake_animation);
    }

    /**
     *
     * @return The length/size of the data arraylist
     */
    @Override
    public int getCount() {
        return mDices.size();
    }

    /**
     *
     * @param position The position in the arraylist
     * @return The Dice at the given position.
     */
    @Override
    public Object getItem(int position) {
        return mDices.get(position);
    }

    /**
     *
     * @param position The position of the Dice in the arraylist.
     * @return The integer value of the Dice position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * The method creates a reference to the {@link Dice} specified by the position.
     * If the view has not already been created, the view-cell layout is inflated and the references to the view elements are hooked up.
     * The {@link ViewHolder} class ensures that calls to findViewById is kept at a minimum to save unnecessary memory usage.
     *
     * @param position The position of the Dice-object in the source-array.
     * @param convertView The view representing the content of the Dice at the given position.
     * @param parent The ViewGroup holding the views.
     * @return The view-cell to add to the view-layer.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Dice dice = (Dice)getItem(position);

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.dice_viewcell_layout, parent, false);
            final ImageView diceImage = (ImageView)convertView.findViewById(R.id.dice_imageview_cell);
            final ViewHolder viewHolder = new ViewHolder(diceImage);
            convertView.setTag(viewHolder);

        }
        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.diceImage.setImageResource(dice.getDiceFace());


        if(dice.isAnimate())
        {
            animateView(convertView);
        }
        dice.setAnimate(false);

        return convertView;

    }

    /**
     * This class holds necessary references for the view to minimize memory expensive calls to findViewByID.
     *
     */
    private class ViewHolder
    {
        private final ImageView diceImage;

        public ViewHolder(ImageView diceImage) {
            this.diceImage = diceImage;

        }
    }

    /**
     * Animates a shake to the parameter view source.
     * @param source The view to be animated.
     */
    private void animateView(View source)
    {
        source.startAnimation(shake_animation);
    }

}
