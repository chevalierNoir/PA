/**
 * Created by qli on 18/03/16.
 */
package com.example.bowenshi.intersaclay;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SkillTree {

    private SkillTreeNode root;

    public SkillTree(Context context, int id) {
        InputStream hierarchy = context.getResources().openRawResource(id);
        Reader hierarchyReader = new InputStreamReader(hierarchy);
        this.root = new Gson().fromJson(hierarchyReader, SkillTreeNode.class);
    }

    public List<CharSequence> getTopics() {
        List<CharSequence> topics = new ArrayList<>();

        for (SkillTreeNode node : root.children) {
            topics.add(node.name);
        }
        return topics;
    }


    public List<List<CharSequence>> getSubTopics() {
        List<List<CharSequence>> subTopics = new ArrayList<>();

        for (SkillTreeNode topic : root.children) {
            List<CharSequence> sub = new ArrayList<>();

            for (SkillTreeNode subTopic : topic.children) {
                sub.add(subTopic.name);
            }
            subTopics.add(sub);
        }
        return subTopics;
    }

    class SkillTreeNode {

        @SerializedName("name")
        public String name;

        @SerializedName("values")
        public List<SkillTreeNode> children = new ArrayList<SkillTreeNode>();
    }
}
