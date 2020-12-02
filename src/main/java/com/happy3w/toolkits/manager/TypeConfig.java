package com.happy3w.toolkits.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeConfig<DT, CT> {
    private Class<? extends DT> dataType;
    private CT config;
}
