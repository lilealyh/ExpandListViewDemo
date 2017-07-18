package com.example.expandViewDemo;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.ExpandableListView.getPackedPositionChild;
import static android.widget.ExpandableListView.getPackedPositionGroup;

public class ViewActivity extends Activity {
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private int ExpandHeight = 0;
    private int visiableHeight = 384;
    MyLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 4; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;
            if (i == 1) {
                for (int j = 1; j < 8/*Math.round(Math.random()*10+10)*/; j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }
            if (i == 2) {
                for (int j = 1; j < 9/*Math.round(Math.random()*20+5)*/; j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }
            if (i == 3) {
                for (int j = 1; j < Math.round(Math.random() * 30 + 5); j++) {
                    ChildItem child = new ChildItem();
                    child.title = "Awesome item " + j;
                    child.hint = "Too awesome";
                    item.items.add(child);
                }
            }

            items.add(item);
        }

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);

        adapter = new ExampleAdapter(this, listView);

        myLayout = (MyLayout) findViewById(R.id.mLayout);
        adapter.setData(items);

        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.v("lilea", "onScrollStateChanged===" + scrollState);
                if (scrollState == 0) {
//                    listView.smoothScrollByOffset(20);
//                    listView.smoothScrollToPosition(10);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.v("lilea", "onScroll firstVisibleItem===" + firstVisibleItem);

            }
        });


        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
             /*   ViewGroup.LayoutParams p=listView.getLayoutParams();
                Scroller scroller=myLayout.getmScroller();
                int expandHeight=adapter.getRealChildrenCount(groupPosition)*128;
//                if(groupPosition==1&&listView.isGroupExpanded(0)){
//                    listView.collapseGroupWithAnimation(0);
//                }
                if (listView.isGroupExpanded(groupPosition)) {
                    visiableHeight-=expandHeight;
                    if(visiableHeight==384){
//                        myLayout.beginScroll(0,expandHeight);
//                        myLayout.setMyFinalY(-840);
//                        myLayout.beginScroll(0,-512);
                        scroller.startScroll(0,scroller.getFinalY(),0,-840-scroller.getFinalY(),0);

                    }
                    if(visiableHeight>384&&visiableHeight<896){
//                        myLayout.beginScroll(0,-(896-visiableHeight));
                        scroller.startScroll(0,scroller.getFinalY(),0,-(896-visiableHeight),0);
                    }
                    listView.collapseGroupWithAnimation(groupPosition);
                }
                else {
                    visiableHeight+=expandHeight;
                    myLayout.beginScroll(0,expandHeight);
                    if(expandHeight>512||visiableHeight>896){
                        p.height=896;
                        listView.setLayoutParams(p);
                    }
                    listView.expandGroupWithAnimation(groupPosition);
                }
                Log.v("lilea","visiableHeight==="+visiableHeight+" p.height==="+p.height+" expandHeight==="+expandHeight);*/

                ViewGroup.LayoutParams p = listView.getLayoutParams();
                Scroller scroller = myLayout.getmScroller();
                int expandHeight = adapter.getRealChildrenCount(groupPosition) * 128;
                Log.v("lilea", "firtvisiable==" + listView.getFirstVisiblePosition());
                if (listView.isGroupExpanded(groupPosition)) {
                    visiableHeight -= expandHeight;
                    if (visiableHeight == 384) {
                        scroller.startScroll(0, scroller.getFinalY(), 0, -840 - scroller.getFinalY(), 500);

                    }
                    if (visiableHeight > 384 && visiableHeight < 896) {
                        scroller.startScroll(0, scroller.getFinalY(), 0, -(896 - visiableHeight), 500);
                    }
                    parent.collapseGroup(groupPosition);
                } else {
                    visiableHeight += expandHeight;
                    if (expandHeight > 512 || visiableHeight > 896) {
                        p.height = 896;
                        listView.setLayoutParams(p);
                    }
                    myLayout.beginScroll(0, expandHeight);
                    parent.expandGroup(groupPosition);
                    Log.v("lilea", "expandHeight==" + expandHeight + " visiableHeight==" + visiableHeight);

                    View view = listView.getChildAt(0);
                    Log.v("lilea", "top===" + view.getTop() + " bottom===" + view.getBottom());
                    int preGroupCount;
                    if (groupPosition > 0) {
                        preGroupCount = adapter.getRealChildrenCount(groupPosition - 1);
                    } else {
                        preGroupCount = adapter.getRealChildrenCount(0);
                    }
                    int firstVisiblePos = listView.getFirstVisiblePosition();
                    int remainCount = preGroupCount - firstVisiblePos;
                    if (remainCount < 0) {
                        remainCount = preGroupCount + adapter.getRealChildrenCount(0) + 1 - firstVisiblePos;
                    }
                    int remainCountHeight = remainCount * 128;
                    int scrollDistance = remainCountHeight + listView.getChildAt(0).getBottom();

                    if (firstVisiblePos == 0 && groupPosition == 1) {//第0组没有展开，直接展开第1组位置
                        scrollDistance = 128;
                        if (listView.getChildAt(0).getBottom() != 0) {//第0组的位置不完全可见时，第一组的滑动位置直接是第0组getBottom底部的位置
                            scrollDistance = listView.getChildAt(0).getBottom();
                        }
                    }
                    if (firstVisiblePos == 0 && groupPosition == 2) {//第0和第1组都没有展开，直接展开第2组
                        scrollDistance = 128 * 2;
                    }
                    long packedPos = listView.getExpandableListPosition(firstVisiblePos);
                    int firstChildPos = getPackedPositionChild(packedPos);
                    int firstGroupPos = getPackedPositionGroup(packedPos);
                    if (firstGroupPos == 0 && groupPosition == 2 && firstVisiblePos > 1) {//0组展开1组没展开，直接点击2组
                        scrollDistance = (adapter.getRealChildrenCount(firstGroupPos) - firstChildPos) * 128 + listView.getChildAt(0).getBottom() + 6;//4为滑动误差
                    }
                    if (firstGroupPos == 1 && groupPosition == 2) {
                        scrollDistance = (adapter.getRealChildrenCount(firstGroupPos) - firstChildPos-1) * 128 + listView.getChildAt(0).getBottom() + 6;//4为滑动误差
                    }
                    if (groupPosition != 0) {

//                    listView.smoothScrollByOffset(scrollDistance);
                        listView.smoothScrollBy(scrollDistance, 1000);
                    }
                }



                /*Log.v("lilea","expandHeight=="+expandHeight+" visiableHeight=="+visiableHeight);
                View view=listView.getChildAt(0);
                int preGroupCount;
                if(groupPosition>0){
                    preGroupCount=adapter.getRealChildrenCount(groupPosition-1);
                }
                else {
                    preGroupCount=adapter.getRealChildrenCount(0);
                }
                int firstVisiablePos=listView.getFirstVisiblePosition();
                int remainCount=preGroupCount-firstVisiablePos;
                if(remainCount<0){
                    remainCount=preGroupCount+adapter.getRealChildrenCount(0)+1-firstVisiablePos;
                }
                int remainCountHeight=remainCount*128;
                int scrollDistance=remainCountHeight+listView.getChildAt(0).getBottom();
                Log.v("lilea","firtvisiable=="+listView.getFirstVisiblePosition());
                Log.v("lilea","top==="+view.getTop()+" bottom==="+view.getBottom());
                if(groupPosition!=0){

//                    listView.smoothScrollByOffset(scrollDistance);
                    listView.smoothScrollBy(scrollDistance,1000);
                }*/
                return true;
            }

        });
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

        AnimatedExpandableListView mview;

        private ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        private ExampleAdapter(Context context, AnimatedExpandableListView view) {
            this.mview = view;
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

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {

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
        Log.v("lilea", "params.height==" + params.height);

        listView.setLayoutParams(params);
    }


}

