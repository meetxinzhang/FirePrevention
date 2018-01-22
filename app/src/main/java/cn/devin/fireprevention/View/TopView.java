package cn.devin.fireprevention.View;

/**
 * Created by Devin on 2018/1/22.
 */

public interface TopView {
    /**
     * Initialize the view.
     *
     * e.g. the facade-pattern method for handling all Actionbar settings
     */
    void initViews();

    /**
     * Open
     */
    void openDatePickerDialog();

    /**
     * Start ListActivity
     */
    void startListActivity();

}
