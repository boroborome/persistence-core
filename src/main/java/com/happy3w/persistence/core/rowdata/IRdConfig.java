package com.happy3w.persistence.core.rowdata;

public interface IRdConfig<T extends IRdConfig<T>> {
    default T newCopy() {
        try {
            IRdConfig instance = this.getClass().newInstance();
            instance.merge(this);
            return (T) instance;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Failed to create instance:" + this.getClass(), e);
        }
    }

    void merge(T other);
}
