/*
 * Copyright 2018 M. Isuru Tharanga Chrishantha Perera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.chrishantha.sample.memoryleak;

import java.util.Objects;

public abstract class Key {

    protected final String KEY;

    public Key(final long NUMBER, final long LENGTH) {
        String binaryString = Long.toBinaryString(NUMBER);
        StringBuilder keyBuilder = new StringBuilder();
        long limit = LENGTH - binaryString.length();
        for (long i = 0; i < limit; i++) {
            keyBuilder.append('0');
        }
        keyBuilder.append(binaryString);
        KEY = keyBuilder.toString();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(KEY);
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(" [key=");
        builder.append(KEY);
        builder.append("]");
        return builder.toString();
    }
}
