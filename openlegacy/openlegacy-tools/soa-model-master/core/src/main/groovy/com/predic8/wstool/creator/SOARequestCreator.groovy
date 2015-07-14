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

package com.predic8.wstool.creator;

import groovy.xml.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.soamodel.*
import com.predic8.wsdl.AbstractSOAPBinding
import com.predic8.wsdl.BindingOperation
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.soap11.SOAPBinding as SOAP11Binding
import com.predic8.wsdl.soap11.SOAPBody as SOAP11Body
import com.predic8.wsdl.soap11.SOAPHeader as SOAP11Header
import com.predic8.wsdl.soap12.SOAPBinding as SOAP12Binding
import com.predic8.wsdl.soap12.SOAPBody as SOAP12Body
import com.predic8.wsdl.soap12.SOAPHeader as SOAP12Header
import com.predic8.wsdl.Binding

/**
 * Not threadsafe
 */

class SOARequestCreator extends AbstractCreator{

  private static final Logger log = LoggerFactory.getLogger(SOARequestCreator.class)
  
  public SOARequestCreator(Definitions definitions, def creator, MarkupBuilder builder) {
    super();
    this.definitions = definitions;
    this.creator = creator
    this.builder = builder
  }

  Definitions definitions
  def creator
  def formParams
  int maxRecursionDepth = 2

  private String operationName
  private String bindingName
  private String portTypeName

  def wrapEnvelope(body) {
    creator.builder = builder
    builder."$soapPrefix:Envelope"("xmlns:$soapPrefix":soapNamespace){
      if(isHeaderExisting()){
        buildHeader(builder)
      }
      "$soapPrefix:Body"() { yieldUnescaped(body) }
    }
  }

  def createRequest(String portTypeName, String operationName, String bindingName) { //todo remove parameter list
    this.bindingName = bindingName
    this.operationName = operationName
    log.debug "createRequest"
    log.debug "bindingName $bindingName"
    log.debug "operationName $operationName"
    log.debug "portTypeName $portTypeName"
    creator.builder = builder
    log.debug "creator class : ${creator.getClass().name}"
    builder."$soapPrefix:Envelope"("xmlns:$soapPrefix":soapNamespace){
      if(isHeaderExisting()){
        buildHeader(builder)
      }
      buildBody(builder)
    }
  }

  private buildBody(builder) {
      builder."$soapPrefix:Body"(){
          log.debug "creating body"
          if(isRPC(bindingName)){
              log.debug "isRPC"
              "ns1:$operationName"('xmlns:ns1':getNamespaceForRCPBinding()){
                  def ctx = creatorContext
                  ctx.path = "${ctx.path}${operationName}/"
                  log.debug "create body from bodyElement"
                  bodyElement.parts.each{
                      it.create(creator, ctx)
                  }
              }
          } else {
              log.debug "creating body from definitions"
              log.debug "element : ${bodyElement.parts[0].element}"
              bodyElement.parts[0].element.create(creator, creatorContext)
          }
      }
  }

  private getNamespaceForRCPBinding(){
      for (Binding bnd : definitions.getBindings()) {
          for (BindingOperation bop : bnd.getOperations()) {
             if(bnd.getBinding() instanceof AbstractSOAPBinding && bop.getName().equals(operationName)) {
                 if(bop.getInput().getBindingElements().get(0).getNamespace() != null ){
                     return bop.getInput().getBindingElements().get(0).getNamespace();
                 }else{
                     return definitions.targetNamespace;
                 }
             }
          }
      }
  }
  
  private buildHeader(builder) {
    log.debug "creating headers"
    builder."$soapPrefix:Header"(){
      getHeaderBindingElements(bindingOperation).each {
        it.part.element.create(creator, creatorContext)
      }
    }

  }
  private isHeaderExisting() {
    bindingOperation.input.bindingElements.find{it instanceof SOAP11Header || it instanceof SOAP12Header}
  }

  private getSoapNamespace() {
    if(binding.binding instanceof SOAP11Binding)
      return Consts.SOAP11_NS

    if(binding.binding instanceof SOAP12Binding)
      return Consts.SOAP12_NS
    ''
  }

  private getSoapPrefix() {
    if( binding.binding instanceof SOAP11Binding)
      return "s11"
    if(binding.binding instanceof SOAP12Binding)
      return "s12"
    ''
  }

  private getBindingOperation() {
    binding.getOperation(operationName)
  }

  private getBinding() {
    definitions.getBinding(bindingName)
  }

  private getHeaderBindingElements(bindingOperation){
    bindingOperation.input.bindingElements.findAll{it instanceof SOAP11Header || it instanceof SOAP12Header}
  }

  private getBodyElement() {
    bindingOperation.input.bindingElements.find{it instanceof SOAP11Body || it instanceof SOAP12Body }
  }

  private isRPC(bindingName){
    'rpc'.equalsIgnoreCase(definitions.getBinding(bindingName).binding.style)
  }

  private getCreatorContext(){
    if(creator instanceof RequestCreator){
      return new RequestCreatorContext(formParams:formParams ?: [:])
    }
    new RequestTemplateCreatorContext(maxRecursionDepth: maxRecursionDepth)
  }

  private yieldUnescaped(s) {
    new MarkupBuilderHelper(builder).yieldUnescaped(s)
  }
}
