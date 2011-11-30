package org.openlegacy.loaders.support;

import org.openlegacy.loaders.ClassAnnotationsLoader;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

public abstract class AbstractClassAnnotationLoader implements ClassAnnotationsLoader {

	public int compareTo(ClassAnnotationsLoader o) {
		Order order1 = AnnotationUtils.findAnnotation(getClass(), Order.class);
		Order order2 = AnnotationUtils.findAnnotation(o.getClass(), Order.class);
		int order1Value = order1 != null ? order1.value() : Ordered.LOWEST_PRECEDENCE;
		int order2Value = order2 != null ? order2.value() : Ordered.LOWEST_PRECEDENCE;

		// Spring order is opposite
		return order2Value - order1Value;
	}
}
