package com.happy3w.persistence.core.rowdata;

public interface IRdConfig {
    /**
     * 构建用于比较的Key字符串<br>
     *     如果两个Config的内容是相同的，则生成的Key相同，如果Config内容不同，则生成的Key也不同。不需要包含自己的类型信息
     * @param builder 用于内容Key的Builder
     */
    void buildContentKey(StringBuilder builder);
}
