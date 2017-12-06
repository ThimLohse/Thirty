package se.umu.thlo0007.dicegame_revised;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents a subset of dices and the corresponding subset sum.
 * @author Thim Lohse
 *
 */
public class SubSetAndSum {

    private ArrayList<Integer> set;
    private int setSum;

    public SubSetAndSum() {
        this.set = new ArrayList<>();
        setSum = 0;

    }

    /**
     *
     * @param element The element to be added to the subset and also the value to be added to the sum of the set.
     */
    public void addElement(int element)
    {
        set.add(element);
        setSum += element;
    }

    /**
     *
     * @return The sum of the subset.
     */
    public int getSum()
    {
        return setSum;
    }


    /**
     *
     * @return The subset without the sum.
     */
    public ArrayList<Integer> getSet() {
        return set;
    }

    /**
     *
     * A Comparator used to compare SubSetAndSum objects with each other on basis of the sum only.
     * The comparator used in a sorting algorithm will sort in decreasing order.
     *
     */
    public static Comparator<SubSetAndSum> SumComparator = new Comparator<SubSetAndSum>() {
        @Override
        public int compare(SubSetAndSum o1, SubSetAndSum o2) {
            int SubSetSum1 = o1.getSum();
            int SubSetSum2 = o2.getSum();
            return (SubSetSum2 - SubSetSum1);

        }
    };
    /**
     *
     * A Comparator used to compare SubSetAndSum objects with each other on basis of the number of elements in the subset and the sum.
     * The comparator used in a sorting algorithm will sort in decreasing order.
     *
     */
    public static Comparator<SubSetAndSum> SumAndNumDicesComparator = new Comparator<SubSetAndSum>() {
        @Override
        public int compare(SubSetAndSum o1, SubSetAndSum o2) {
            int compareSum1 = o1.getSum();
            int compareSum2 = o2.getSum();

            int compareSize1 = o1.getSet().size();
            int compareSize2 = o2.getSet().size();

            int totalCompare = ((compareSum2 - compareSum1) + (compareSize1 - compareSize2));

            return totalCompare;
        }
    };
}
