package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CombineResult<K, V> {
    /**
     * The original Key-Value result
     */
    private List<Pair<K, V>> simpleResult;

    /**
     * The default logic is the count of non-null dimension
     */
    private int score;

    /**
     * Identity which dimension is not null and what is the value.
     */
    private int mask;

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
    public boolean isOver(CombineResult<K, V> otherResult) {
        if (otherResult == null) {
            return true;
        }
        if (otherResult.mask == mask) {
            return false;
        }

        return (mask & otherResult.mask) == otherResult.mask;
    }
}
