package org.linphone.ui;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @SuppressWarnings("unused")
    private static final String TAG = SelectableAdapter.class.getSimpleName();
    private SparseBooleanArray mSelectedItems;
    private boolean mIsEditionEnabled=false;
    private SelectableHelper mListHelper;

    public SelectableAdapter(SelectableHelper helper) {
        mSelectedItems = new SparseBooleanArray();
        mListHelper = helper;
    }

    private CompoundButton.OnCheckedChangeListener mDeleteCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            Integer position = (Integer)compoundButton.getTag();

            mListHelper.updateSelectionButtons(mSelectedItems.size() == 0, mSelectedItems.size() == getItemCount());
        }
    };
    public CompoundButton.OnCheckedChangeListener getDeleteListener() {
        return mDeleteCheckboxListener;
    }
    public boolean isEditionEnabled() {
        return mIsEditionEnabled;
    }

    public void enableEdition(boolean set) {
        mIsEditionEnabled = set;

        mSelectedItems.clear();
        notifyDataSetChanged();
    }
    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        mListHelper.updateSelectionButtons(getSelectedItemCount() == 0, getSelectedItemCount() == getItemCount());

        notifyItemChanged(position);
    }

    /**
     * Clear the selection status for all items
     */
//    public void clearSelection() {
//        List<Integer> selection = getSelectedItems();
//        mSelectedItems.clear();
//        for (Integer i : selection) {
//            notifyItemChanged(i);
//        }
//    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); ++i) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    public void selectAll() {
        for (Integer i = 0; i < getItemCount(); i++) {
            mSelectedItems.put(i, true);
            notifyItemChanged(i);
        }
        mListHelper.updateSelectionButtons(false, true);
//        notifyDataSetChanged();
    }

    public void deselectAll() {
        mSelectedItems.clear();
        mListHelper.updateSelectionButtons(true, false);
        notifyDataSetChanged();
    }


    public abstract Object getItem(int position);
}
