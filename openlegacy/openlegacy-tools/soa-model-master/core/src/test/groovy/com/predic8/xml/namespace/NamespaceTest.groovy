/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.xml.namespace

import javax.xml.stream.*

import com.predic8.soamodel.*
import com.predic8.wsdl.WSDLParserContext

class A extends XMLElement {
  def b
  def c
	static final String NAMESPACE = 'uri:a'
  
  protected parseChildren(token, child, params) {
    switch ( child ) {
      case 'b' :
      b = new B()
      b.parse(token,params)
      break
    }
    switch ( child ) {
      case 'c' :
      c = new C()
      c.parse(token,params)
      break
    }
  }  
  def getElementName() {
    'a'
  }
  public String getNamespaceUri() {
		'uri:a'
  }
  public String getPrefix() { null }
}

class B extends XMLElement {
	static final String NAMESPACE = 'uri:a'
  def getElementName() {
    'b'
  }
  public String getNamespaceUri() {
	 'uri:a'
  }
  public String getPrefix() { null }
}

class C extends XMLElement {
	static final String NAMESPACE = 'uri:a'
  def d
  def e
  
  protected parseChildren(token, child, params) {
    switch ( child ) {
      case 'd' :
      d = new D()
      d.parse(token,params)
      break
      case 'e' :
      e = new E()
      e.parse(token,params)
      break
    }
  }  
  def getElementName() {
    'c'
  }
  public String getNamespaceUri() {
	  'uri:a'
  }
  public String getPrefix() { null }
}

class D extends XMLElement {
	static final String NAMESPACE = 'uri:d'
  def getElementName() {
    'd'
  }
  public String getNamespaceUri() {
	 'uri:d'
  }
  public String getPrefix() { null }
}

class E extends XMLElement {
	static final String NAMESPACE = 'uri:e'
  def getElementName() {
    'e'
  }
  public String getNamespaceUri() {
		'uri:e'
  }
  public String getPrefix() { null }
}


class NamespaceTest extends GroovyTestCase {
  def a
  
  void setUp() {
    def token = XMLInputFactory.newInstance().createXMLStreamReader(this.class.getResourceAsStream("/namespaces/a.xml"))    
    a = new A()
    token.nextTag()
    a.parse(token, new WSDLParserContext())
  }
  void testA(){    
    assertEquals('ns1',a.getPrefix("uri:a"))
    assertEquals('uri:a',a.getNamespace("ns1"))
  }
  
  void testB() {    
    assertEquals('ns1',a.b.getPrefix("uri:a"))
    assertEquals('uri:a',a.b.getNamespace("ns1"))
  }

  void testD() {  
    assertEquals('ns2',a.c.d.getPrefix("uri:d"))
    assertEquals('uri:d',a.c.d.getNamespace("ns2"))
  }
  
  void testE() {
    assertEquals('ns2',a.c.e.getPrefix("uri:e"))
    assertEquals('uri:e',a.c.e.getNamespace("ns2"))
  }
  
}
