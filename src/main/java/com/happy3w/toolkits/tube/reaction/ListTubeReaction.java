package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

import java.util.List;

public interface ListTubeReaction<T> extends TubeReaction {
    void reaction(List<T> datas, ReactDataHolder holder);
}
