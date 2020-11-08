package com.happy3w.toolkits.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeItem<T, D> implements ITypeItem<T> {
    private Class<T> type;
    private D data;
}
