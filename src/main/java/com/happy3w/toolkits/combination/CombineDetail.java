package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CombineDetail<K, V> {
    /**
     * The original Key-Value result
     */
    private List<Pair<K, V>> normalResult;

    /**
     * The results which is overed by this result
     */
    private List<CombineDetail<K, V>> overedCombines;

    /**
     * The default logic is the count of non-null dimension
     */
    private int score;

    /**
     * Identity which dimension is not null and what is the value.
     */
    private int mask;

    public CombineDetail(List<Pair<K, V>> normalResult) {
        this.normalResult = normalResult;
    }

    /**
     * Check whether this result dimension values contains all the other result dimension values
     *
     * For example, there are results
     * <ul>
     *     <li>a: [{'key': 'd1', 'value': 'd1v1'}, {'key': 'd2', 'value': 'd2v1'}]</li>
     *     <li>b: [{'key': 'd1', 'value': 'd1v1'}, {'key': 'd2', 'value': null}]</li>
     *     <li>c: [{'key': 'd1', 'value': 'd1v2'}, {'key': 'd2', 'value': null}]</li>
     * </ul>
     * a.isOver(b) == true;<br>
     * a.isOver(c) == false;<br>
     * a.isOver(a) == false;<br>
     * @param otherResult The other result to check with
     * @return Result
     */
    public boolean isOver(CombineDetail<K, V> otherResult) {
        if (otherResult == null) {
            return true;
        }
        if (otherResult.mask == mask) {
            return false;
        }

        return (mask & otherResult.mask) == otherResult.mask;
    }

    public void addOveredCombine(CombineDetail<K, V> overedCombine) {
        if (this.overedCombines == null) {
            this.overedCombines = new ArrayList<>();
        }
        overedCombines.add(overedCombine);
    }
}
