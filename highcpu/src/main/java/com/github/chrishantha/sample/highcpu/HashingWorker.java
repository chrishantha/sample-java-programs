/*
 * Copyright 2015 M. Isuru Tharanga Chrishantha Perera
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
package com.github.chrishantha.sample.highcpu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashingWorker implements Runnable {

    private final String algorithm;
    private final long length;

    private byte[] lastComputedHash;

    public HashingWorker(long length, String algorithm) {
        this.length = length;
        this.algorithm = algorithm;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            StringBuilder data = new StringBuilder();

            // Some random data
            for (int i = 0; i < length; i++) {
                data.append(UUID.randomUUID().toString());
            }

            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            // Hash
            digest.update(data.toString().getBytes());
            byte[] computedHash = digest.digest();
            if (lastComputedHash != null && computedHash.length != lastComputedHash.length) {
                throw new IllegalStateException("Is the hash computation correct??");
            }
            lastComputedHash = computedHash;
        }
    }
}
