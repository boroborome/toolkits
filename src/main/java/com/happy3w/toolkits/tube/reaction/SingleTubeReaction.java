package com.happy3w.toolkits.tube.reaction;

import com.happy3w.toolkits.tube.holder.ReactDataHolder;

public interface SingleTubeReaction<T> extends TubeReaction {
    void reaction(T data, ReactDataHolder holder);
}
