/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.tasks.properties.annotations;

import com.google.common.collect.ImmutableList;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.InputArtifactDependencies;
import org.gradle.api.internal.tasks.properties.BeanPropertyContext;
import org.gradle.api.internal.tasks.properties.InputFilePropertyType;
import org.gradle.api.internal.tasks.properties.PropertyValue;
import org.gradle.api.internal.tasks.properties.PropertyVisitor;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.ClasspathNormalizer;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.SkipWhenEmpty;
import org.gradle.internal.reflect.ParameterValidationContext;
import org.gradle.internal.reflect.PropertyMetadata;
import org.gradle.work.Incremental;

import java.lang.annotation.Annotation;

public class ClasspathPropertyAnnotationHandler implements OverridingPropertyAnnotationHandler {
    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return Classpath.class;
    }

    @Override
    public boolean isPropertyRelevant() {
        return true;
    }

    @Override
    public ImmutableList<Class<? extends Annotation>> getOverriddenAnnotationTypes() {
        return ImmutableList.of(InputFiles.class, InputArtifact.class, InputArtifactDependencies.class);
    }

    @Override
    public boolean shouldVisit(PropertyVisitor visitor) {
        return !visitor.visitOutputFilePropertiesOnly();
    }

    @Override
    public void visitPropertyValue(String propertyName, PropertyValue value, PropertyMetadata propertyMetadata, PropertyVisitor visitor, BeanPropertyContext context) {
        visitor.visitInputFileProperty(
            propertyName,
            propertyMetadata.isAnnotationPresent(Optional.class),
            propertyMetadata.isAnnotationPresent(SkipWhenEmpty.class),
            propertyMetadata.isAnnotationPresent(Incremental.class),
            ClasspathNormalizer.class,
            value,
            InputFilePropertyType.FILES
        );
    }

    @Override
    public void validatePropertyMetadata(PropertyMetadata propertyMetadata, ParameterValidationContext visitor) {
    }
}