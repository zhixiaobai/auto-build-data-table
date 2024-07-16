package com.zxb.auto_build;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr.M
 * @date 2024/7/16
 * @Description
 */
public class AutoBuildDataTableRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
    private Environment environment;
    private ResourceLoader resourceLoader;
    private final DatabaseManager databaseManager = new DatabaseManager();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> basePackages = getBasePackages(importingClassMetadata);
        try {
            databaseManager.initialize();
            basePackages.forEach(this::processPackage);
            databaseManager.destroyConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error processing auto build data table", e);
        }
    }

    private void processPackage(String basePackage) {
        findCandidateComponents(basePackage).forEach(this::processCandidateComponent);
    }

    private Set<BeanDefinition> findCandidateComponents(String basePackage) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(AutoBuild.class));
        return scanner.findCandidateComponents(basePackage);
    }

    private void processCandidateComponent(BeanDefinition beanDefinition) {
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedDef = (AnnotatedBeanDefinition) beanDefinition;
            if (annotatedDef.getMetadata().isConcrete()) {
                try {
                    Class<?> clazz = Class.forName(annotatedDef.getMetadata().getClassName());
                    Map<String, Object> attributes = annotatedDef.getMetadata().getAnnotationAttributes(AutoBuild.class.getCanonicalName());
                    databaseManager.processEntityClass(clazz, attributes);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException("Failed to process entity class", e);
                }
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableAutoBuildDataTable.class.getCanonicalName());
        Set<String> basePackages = new HashSet<>();
        assert attributes != null;
        String[] values = (String[]) attributes.get("value");

        String pkg;
        for (String value : values) {
            pkg = value;
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        values = (String[]) attributes.get("basePackages");
        for (String value : values) {
            pkg = value;
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        Class<?>[] classes = (Class<?>[]) attributes.get("basePackageClasses");
        for(Class<?> clazz : classes) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }
}
