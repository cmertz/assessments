package de.nimel.assessment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class SpringContextVerifier {

	// this is probably dynamic
	// so adapt if the configuration changes
	private final String[] frameworkBeans = {
			"org.springframework.context.annotation.internalConfigurationAnnotationProcessor",
			"org.springframework.context.annotation.internalAutowiredAnnotationProcessor",
			"org.springframework.context.annotation.internalRequiredAnnotationProcessor",
			"org.springframework.context.annotation.internalCommonAnnotationProcessor",
			"org.springframework.context.annotation.ConfigurationClassPostProcessor$ImportAwareBeanPostProcessor#0",
			"environment", "systemProperties", "systemEnvironment",
			"importRegistry", "messageSource", "applicationEventMulticaster",
			"lifecycleProcessor" };

	private Map<String, Object> beans;

	public SpringContextVerifier(final ApplicationContext ctx) {

		beans = ctx.getBeansOfType(Object.class);

		// remove framework beans from
		// mapping
		for (String name : frameworkBeans) {
			assertBeanOfName(name);
		}
	}

	public SpringContextVerifier(final String classPathXml) {
		this(new ClassPathXmlApplicationContext(classPathXml));
	}

	public void assertNoUnusedBeans() {

		if (!beans.isEmpty()) {
			throw new AssertionError();
		}
	}

	public void assertBeansOfType(final Class<?> type, final int number) {

		Set<String> keysToRemove = new HashSet<String>();

		for (String key : beans.keySet()) {

			final Object bean = beans.get(key);

			if (type.isAssignableFrom(bean.getClass())) {
				keysToRemove.add(key);
			}
		}

		if (keysToRemove.size() != number) {
			throw new AssertionError();
		}

		for (String key : keysToRemove) {
			beans.remove(key);
		}
	}

	public void assertBeanOfName(final String name) {

		if (!beans.containsKey(name)) {
			throw new AssertionError();
		}

		beans.remove(name);
	}
}
