package unimet.deli;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.ChildInfo;
import modelos.GroupInfo;

/**
 * Created by Gourav on 08-03-2016.
 */
public class CustomAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupInfo> deptList;
    private int tipo;

    public CustomAdapter(Context context, ArrayList<GroupInfo> deptList, int tipo) {
        this.context = context;
        this.deptList = deptList;
        this.tipo= tipo;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        final ChildInfo detailInfo = (ChildInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.sequence);
        sequence.setText(detailInfo.getSequence().trim() + ". ");
        final TextView childItem = (TextView) view.findViewById(R.id.childItem);
        childItem.setText(detailInfo.getName().trim());

        final CheckBox check = (CheckBox) view.findViewById(R.id.check);
        if(tipo==1) {
            check.setChecked(detailInfo.isSeleccionado());
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailInfo.isSeleccionado()) {
                        detailInfo.setSeleccionado(false);
                    } else {
                        detailInfo.setSeleccionado(true);
                    }
                    Log.d("Selecciondado:", Boolean.toString(detailInfo.isSeleccionado()));
                }
            });

            childItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detailInfo.isSeleccionado()) {
                        detailInfo.setSeleccionado(false);
                        check.setChecked(false);
                    } else {
                        detailInfo.setSeleccionado(true);
                        check.setChecked(true);
                    }

                }
            });

        }else{
            check.setEnabled(false);
            check.setVisibility(view.INVISIBLE);
        }
        return view;
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ChildInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        GroupInfo headerInfo = (GroupInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}