package com.eveintel.killmail

import xml.{XML, NodeSeq}

object XmlConversions {

  def fromXml(str: String) = fromXml(XML.loadString(str))

  def fromXml(ns: NodeSeq) = {

  }
}
