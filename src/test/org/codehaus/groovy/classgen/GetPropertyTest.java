/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.classgen;

import groovy.lang.GroovyObject;

/**
 * Tests using the GroovyObject API from Java on a groovy object
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 */
public class GetPropertyTest extends TestSupport {

    public void testProperty() throws Exception {
        GroovyObject object = compile("src/test/org/codehaus/groovy/classgen/MyBean.groovy");
        System.out.println("Got object: " + object);

        Object value = object.getProperty("name");
        assertEquals("name property", "James", value);

        object.setProperty("name", "Bob");
        assertEquals("name property", "Bob", object.getProperty("name"));
    }
}
