package com.happy3w.toolkits.convert;

/**
 * 实现某个数据类型和字符串之间的互相转换逻辑
 * @param <T> converter负责处理的数据类型
 * @param <S> 子类类型，用于返回自身的时候不用cast
 */
public interface ISimpleConverter<T, S extends ISimpleConverter<T, S>> {
    /**
     * 返回所有支持的数据类型。注册的时候会使用这个类型注册，如果类型重复后一个类型对应的Converter会被注册进去
     * @return 所有支持的数据类型
     */
    Class[] dataTypes();

    /**
     * 将提供的数据转换为字符串形式。这个字符串应该包含数据的所有信息，以供解析使用
     * @param value 需要转换的值
     * @return 转换后的字符串
     */
    String convertToString(T value);

    /**
     * 从字符串解析一个数据。
     * @param str 用于解析的字符串，应该符合固定的格式
     * @return 转换后结果
     */
    T convertFromString(String str);

    default S appendConfig(String config) {
        throw new UnsupportedOperationException();
    }

    default S defaultConfig(String config) {
        throw new UnsupportedOperationException();
    }
}
