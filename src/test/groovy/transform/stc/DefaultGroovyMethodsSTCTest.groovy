/*
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
package groovy.transform.stc

/**
 * Unit tests for static type checking : default groovy methods.
 */
class DefaultGroovyMethodsSTCTest extends StaticTypeCheckingTestCase {

    void testEach() {
        assertScript """
            ['a','b'].each { // DGM#each(Object, Closure)
                println it // DGM#println(Object,Object)
            }
        """

        assertScript """
            ['a','b'].eachWithIndex { it, i ->// DGM#eachWithIndex(Object, Closure)
                println it // DGM#println(Object,Object)
            }
        """
    }

    void testStringToInteger() {
        assertScript """
        String name = "123"
        name.toInteger() // toInteger() is defined by DGM
        """
    }

    void testVariousAssignmentsThenToInteger() {
        assertScript """
         class A {
          void foo() {}
         }
        def name = new A()
        name.foo()
        name = 1
        name = '123'
        name.toInteger() // toInteger() is defined by DGM
        """
    }

    void testMethodsOnPrimitiveTypes() {
        assertScript '''
            1.times { it }
        '''

        assertScript '''
            true.equals { it }
        '''
    }
  
    void testShouldAcceptMethodFromDefaultDateMethods() {
      assertScript '''
        def s = new Date()
        println s.year
        println s.format("yyyyMMdd")
      '''
    }

    // GROOVY-5568
    void testDGMMethodAsProperty() {
        assertScript '''
            String foo(InputStream input) {
                input.text
            }
            def text = new ByteArrayInputStream('foo'.getBytes())
            assert foo(text) == 'foo'
        '''
    }

    // GROOVY-5584
    void testEachOnMap() {
        assertScript '''import org.codehaus.groovy.transform.stc.ExtensionMethodNode
            import org.codehaus.groovy.runtime.DefaultGroovyMethods

            @ASTTest(phase=INSTRUCTION_SELECTION, value= {
                def mn = node.rightExpression.getNodeMetaData(DIRECT_METHOD_CALL_TARGET)
                assert mn
                assert mn instanceof ExtensionMethodNode
                assert mn.declaringClass == MAP_TYPE
                def en = mn.extensionMethodNode
                assert en.declaringClass == make(DefaultGroovyMethods)
                assert en.parameters[0].type == MAP_TYPE
            })
            def x = [a:1, b:3].each { k, v -> "$k$v" }
        '''
    }

    // GROOVY-6961
    void testCollectMany() {
        assertScript '''
            class ListCompilerAndReverser {
                static List<Integer> revlist(List<List<String>> list) {
                    list.collectMany { strings ->
                        strings.collect {
                            it.toInteger()
                        }
                    } sort { int it ->
                        -it
                    }
                }
            }

            assert ListCompilerAndReverser.revlist([["1", "2", "3"], ["4", "5", "6"], ["7", "8", "9"]]) == [9, 8, 7, 6, 5, 4, 3, 2, 1]
        '''
    }

    // GROOVY-7283
    void testArrayMinMaxSupportsOneAndTwoArgClosures() {
        assertScript '''
            Date now = new Date()
            Date then = now + 7
            def dates = [now, then] as Date[]
            assert dates.min() == now
            assert dates.max() == then
            assert dates.min{ d -> d.time } == now
            assert dates.max{ d1, d2 -> d2.time <=> d1.time } == now
        '''
    }

    // GROOVY-7283
    void testListWithDefaultInfersInt() {
        assertScript '''
            def list = [].withDefault{ it.longValue() }
            list[0] = list[3]
            assert list[0] == 3 && list[0].class == Long
        '''
    }

    // GROOVY-7952
    void testIsGetterMethodAsProperty() {
        assertScript '''
            assert !'abc'.allWhitespace
        '''
    }
}

