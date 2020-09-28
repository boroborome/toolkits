package com.happy3w.toolkits.pipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassifyResult<DataType, TagType> {
    private DataType data;
    private TagType tag;
}
