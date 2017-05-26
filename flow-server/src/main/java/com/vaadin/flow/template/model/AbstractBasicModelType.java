/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.template.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.util.ReflectTools;

import elemental.json.Json;
import elemental.json.JsonValue;

/**
 * Common abstract class with generic functionality for basic mode type.
 * 
 * @param <T>
 *            the (basic) Java type used by this model type
 * @author Vaadin Ltd
 *
 */
public abstract class AbstractBasicModelType<T> implements ModelType {

    private final Class<T> type;

    protected AbstractBasicModelType(Class<T> type) {
        this.type = type;
    }

    @Override
    public boolean accepts(Type applicationType) {
        if (type.isPrimitive()
                && ReflectTools.convertPrimitiveType(type) == applicationType) {
            return true;
        }
        return type == applicationType;
    }

    @Override
    public Class<T> getJavaType() {
        return type;
    }

    @Override
    public JsonValue toJson() {
        return Json.create(type.getSimpleName());
    }

    protected static <M> Map<Class<?>, M> loadBasicTypes(
            Function<Class<?>, M> factory) {
        Map<Class<?>, M> map = Stream
                .of(int.class, Integer.class, boolean.class, Boolean.class,
                        double.class, Double.class, String.class)
                .collect(Collectors.toMap(Function.identity(), factory));

        // Make sure each type has a unique getSimpleName value since it's used
        // as an identifier in JSON messages
        assert map.keySet().stream().map(Class::getSimpleName).distinct()
                .count() == map.size();
        return Collections.unmodifiableMap(map);
    }
}