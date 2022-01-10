package com.happy3w.toolkits.tube;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;
import com.happy3w.toolkits.tube.holder.SimpleDataHolder;
import com.happy3w.toolkits.tube.reaction.TubeReaction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReactDataInfo {
    private List<TubeReaction> tubeReactions;
    private ReactDataHolder dataHolder = new SimpleDataHolder();

    public void addReaction(TubeReaction tubeReaction) {
        if (tubeReactions == null) {
            tubeReactions = new ArrayList<>();
        }
        tubeReactions.add(tubeReaction);
    }

    public <T> void addDatas(List<T> values) {
        dataHolder.addValues(values);
    }
}
