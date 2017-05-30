/*
 * Copyright (C) 2017 Ordina
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

package nl.ordina.jtech.hackadrone.net;

import java.io.IOException;

/**
 * Interface representing a decoder.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface Decoder extends Connection {

    /**
     * Reads bytes.
     *
     * @return the read bytes
     * @throws IOException if reading the bytes failed
     */
    byte[] read() throws IOException;

}
