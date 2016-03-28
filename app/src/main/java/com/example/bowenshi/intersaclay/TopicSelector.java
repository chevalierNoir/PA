package com.example.bowenshi.intersaclay;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qli on 18/03/16.
 */
public class TopicSelector {

    final private List<CharSequence> topics;
    final private List<List<CharSequence>> subTopics;

    private Context context;
    private LinearLayout container;

    Spinner topicSpinner = null;
    Spinner subTopicSpinner = null;

    public TopicSelector(Context ctx, LinearLayout ctn,
                         List<CharSequence> topics, List<List<CharSequence>> subTopics) {
        this.context = ctx;
        this.container = ctn;

        this.topics = topics;
        this.subTopics = subTopics;

        this.topicSpinner = createSpinner(this.topics);
        container.addView(topicSpinner);
    }


    private Spinner createSpinner(List<CharSequence> items) {
        Spinner spinner = new Spinner(this.context);

        //StringBuilder logMsg = new StringBuilder();
        //for (CharSequence item : items) logMsg.append(item);
        //Log.d("TopicSelector", logMsg.toString());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this.context, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ItemSelectedListener());

        return spinner;
    }


    public List<CharSequence> getResult() {
        List<CharSequence> results = new ArrayList<>();

        int topic = -1;

        if (topicSpinner != null) {
            int pos = topicSpinner.getSelectedItemPosition();

            if (pos != 0) {
                results.add(topics.get(pos));
                if (pos != topics.size() - 1) topic = pos;
            }
        }

        if (topic != -1 && subTopicSpinner != null) {
            int pos = subTopicSpinner.getSelectedItemPosition();

            if (pos != 0) {
                results.add(subTopics.get(topic).get(pos));
            }
        }

        return results;
    }

    public String toString() {
        List<CharSequence> chain = this.getResult();
        StringBuilder builder = new StringBuilder();
        builder.append(chain.get(0))
                .append('>')
                .append(chain.get(1));
        return builder.toString();
    }

    public boolean isTopicSelected() {
        return topicSpinner != null && topicSpinner.getSelectedItemPosition() != 0;
    }

    public boolean isSubTopicSelected() {
        return subTopicSpinner != null && subTopicSpinner.getSelectedItemPosition() != 0;
    }

    public void defaultSubTopic() {
        if (!isTopicSelected()) return;
        else {
            int pos = topicSpinner.getSelectedItemPosition();
            subTopicSpinner.setSelection(subTopics.get(pos).size() - 1);
        }
    }

    class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Spinner selectedSpinner = (Spinner) parent;

            if (selectedSpinner == topicSpinner) {
                if (subTopicSpinner != null) container.removeView(subTopicSpinner);

                if (pos != 0) {
                    subTopicSpinner = createSpinner(subTopics.get(pos));
                    container.addView(subTopicSpinner);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            return;
        }
    }
}


///**
// * Created by qli on 18/03/16.
// */
//package com.example.bowenshi.intersaclay;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TopicSelector {
//
//    final private List<CharSequence> topics;
//    final private List<List<CharSequence>> subTopics;
//
//    private Context context;
//    private LinearLayout container;
//
//    Spinner topicSpinner = null;
//    Spinner subTopicSpinner = null;
//
//    public TopicSelector(Context ctx, LinearLayout ctn,
//                         List<CharSequence> topics, List<List<CharSequence>> subTopics) {
//        this.context = ctx;
//        this.container = ctn;
//
//        this.topics = topics;
//        this.subTopics = subTopics;
//
//        this.topicSpinner = createSpinner(this.topics);
//        container.addView(topicSpinner);
//    }
//
//
//    private Spinner createSpinner(List<CharSequence> items) {
//        Spinner spinner = new Spinner(this.context);
//
//        StringBuilder logMsg = new StringBuilder();
//        for (CharSequence item : items) logMsg.append(item);
//        Log.d("TopicSelector", logMsg.toString());
//
//        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
//                this.context, android.R.layout.simple_spinner_dropdown_item, items);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new ItemSelectedListener());
//
//        return spinner;
//    }
//
//
//    public List<CharSequence> getResult() {
//        List<CharSequence> results = new ArrayList<>();
//
//        int topic = -1;
//
//        if (topicSpinner != null) {
//            int pos = topicSpinner.getSelectedItemPosition();
//
//            if (pos != 0) {
//                results.add(topics.get(pos));
//                if (pos != topics.size() - 1) topic = pos;
//            }
//        }
//
//        if (topic != -1 && subTopicSpinner != null) {
//            int pos = subTopicSpinner.getSelectedItemPosition();
//
//            if (pos != 0) {
//                results.add(subTopics.get(topic).get(pos));
//            }
//        }
//
//        return results;
//    }
//
//    public String toString() {
//        List<CharSequence> chain = this.getResult();
//        StringBuilder builder = new StringBuilder();
//        builder.append(chain.get(0))
//                .append('>')
//                .append(chain.get(1));
//        return builder.toString();
//    }
//
//    class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
//
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            Spinner selectedSpinner = (Spinner) parent;
//
//            if (selectedSpinner == topicSpinner) {
//                if (subTopicSpinner != null) container.removeView(subTopicSpinner);
//
//                if (pos != 0 && pos != topics.size() - 1) {
//                    subTopicSpinner = createSpinner(subTopics.get(pos));
//                    container.addView(subTopicSpinner);
//                }
//            }
//        }
//
//        public void onNothingSelected(AdapterView<?> parent) {
//            return;
//        }
//    }
//}
