package com.happy3w.persistence.core.filter;

public interface IFilter {
    /**
     * Identity of this kind filter. It's the filter type flag, like class name.
     * @return Identity
     */
    String getType();

    /**
     * Whether to add "not" to the logic.true means normal logic, no not; false means will add not to this logic.
     * @return positive flag
     */
    boolean isPositive();
}
