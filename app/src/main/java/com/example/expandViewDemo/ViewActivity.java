package com.example.expandViewDemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends Activity {
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private int ExpandHeight=0;
    private int visiableHeight=384;
    MyLayout myLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for(int i = 1; i < 4; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;
            if(i==1){
                for(int j = 0; j < 2; j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }
            if(i==2){
                for(int j = 0; j < 10; j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }
            if(i==3){
                for(int j = 0; j < 4; j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }

            items.add(item);
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        myLayout=(MyLayout) findViewById(R.id.mLayout);
        listView.setAdapter(adapter);


        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                Log.v("lilea","listViewHeight==="+listView.getLayoutParams().height);
                ExpandHeight=adapter.getRealChildrenCount(groupPosition)*128;
//                Log.v("lilea","expandheight=="+ExpandHeight+" currentViewHeight==="+getCurrentViewHeight());
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                    if(ExpandHeight>896&&listView.getLayoutParams().height!=384){
                        myLayout.beginScroll(0,-512);
//                        myLayout.setMyFinalY();
                    }
                    else {
                        if(myLayout.getFinalY()!=-840){
                            myLayout.beginScroll(0,-ExpandHeight);
                        }
                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        if(params.height!=328){
                            params.height -= ExpandHeight;
                        }
                        if(myLayout.getFinalY()==-840){
                            params.height=384;
                        }
                        listView.setLayoutParams(params);
                    }
                }

                else {
                    listView.expandGroupWithAnimation(groupPosition);
                    if(ExpandHeight>896){
//                        myLayout.beginScroll(0,512);
//                        myLayout.setMyFinalY();
                        if(myLayout.getFinalY()!=-328){
//                            myLayout.beginScroll(0,512);
                            myLayout.setMyFinalY();
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            params.height = 896;
                            listView.setLayoutParams(params);
                        }
                    }
                    else{
                        if(ExpandHeight+myLayout.getFinalY()>-328){
                            myLayout.setMyFinalY();
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            params.height = 896;
                            listView.setLayoutParams(params);
                        }
                        else {
                            myLayout.beginScroll(0,ExpandHeight);
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            if(myLayout.getFinalY()!=-328){
                                if(params.height==-1){
                                    params.height=384;
                                }
                                params.height += ExpandHeight;
                            }
                            else {
                                params.height = 896;
                            }
                            listView.setLayoutParams(params);
                        }
                    }
                }
                Log.v("lilea","visiableHeight=="+visiableHeight);
                return true;
            }

        });
    }
    public int getCurrentViewHeight(){
        int groupCount=adapter.getGroupCount();
        int expandChildrenCount=0;
        int height;
        for (int j = 0; j <groupCount ; j++) {
           if(listView.isGroupExpanded(j)){
               expandChildrenCount+=adapter.getRealChildrenCount(j);
           }
        }
        height=(expandChildrenCount+groupCount)*128;
        return height;
    }
    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        private ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.item_clean, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
    public void setListHeight(ExpandableListView listView, AnimatedExpandableListView.AnimatedExpandableListAdapter listAdapter) {

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int total = 0;

        View listItem;

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            listItem = listAdapter.getGroupView(i, false, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
//            total += (listAdapter.getChildrenCount(0) - 1);
        }

        for(int i = 0; i < listAdapter.getGroupCount() ; i++) {

            if (listView.isGroupExpanded(i))
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
//                    total += (listAdapter.getChildrenCount(i) - 1);
                }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * total);
        Log.v("lilea","params.height=="+params.height);

        listView.setLayoutParams(params);
    }
}