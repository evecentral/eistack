package com.eveintel.killmail

import xml.{XML, NodeSeq}
import java.net.URL

object XmlConversions {

  def processParticipant(ns: NodeSeq): Participant = {
    println(ns)
    val shipType = (ns \ "@shipTypeID").text.toLong
    val factionName = (ns \ "@factionName").text
    val factionID = (ns \ "@factionID").text.toLong
    val damageTaken = (ns \ "@damageTaken").text match {
      case "" => None
      case t => Some(t.toInt)
    }
    val corpName = (ns \ "@corporationName").text
    val corpId = (ns \ "@corporationID").text.toLong
    val charName = (ns \ "@characterName").text
    val charId = (ns \ "@characterID").text.toLong
    val allianceName = (ns \ "@allianceName").text
    val allianceId = (ns \ "@allianceID").text.toLong
    val weaponTypeId = (ns \ "@weaponTypeID").text match {
      case "" => None
      case t => Some(t.toLong)
    }

    val securityStatus = (ns \ "@securityStatus").text match {
      case "" => None
      case t => Some(t.toDouble)
    }

    val finalBlow = (ns \ "@finalBlow").text match {
      case "" => None
      case "1" => Some(true)
      case "0" => Some(false)
    }
    Participant(Some(charId), Some(charName), Some(corpId), Some(corpName), Some(allianceId), Some(allianceName),
      Some(shipType), Some(factionID), Some(factionName), damageTaken, securityStatus, finalBlow, weaponTypeId)
  }

  def processLoot(ns: NodeSeq): Loot = {
    val flag = (ns \ "@flag").text.toInt
    val qtyDestroyed = (ns \ "@qtyDestroyed").text.toInt
    val qtyDropped = (ns \ "@qtyDropped").text.toInt
    val typeId = (ns \ "@typeID").text.toLong
    Loot(typeId, flag, qtyDropped, qtyDestroyed, Seq(), Seq())
  }

  def processSingleMail(ns: NodeSeq, fromSource: Seq[KillSource]): Unit = {
    val ssId = (ns \ "@solarSystemID").text.toLong
    val moonID = (ns \ "@moonID").text.toLong
    val killTime = (ns \ "@killTime").text
    val killInternalID = Some((ns \ "@killInternalID").text)
    val killID = (ns \ "@killID").text.toLong
    val victim = processParticipant(ns \ "victim")
    val attackers = (ns \ "rowset").filter(rowset => (rowset \ "@name").text == "attackers").map(rowset => (rowset \ "row").map(processParticipant(_))).flatten
    val loot = (ns \ "rowset").filter(rowset => (rowset \ "@name").text == "items").flatMap(rowset => (rowset \ "row").map(processLoot(_)))
    println(attackers)
    println(loot)
    //println(ns)
    //Killmail()
  }

  def fromXml(url: URL, fromSource: Seq[KillSource]): Seq[Killmail] = {
    val ns = XML.load(url)
    fromXml(ns, fromSource)
  }

  def fromXml(str: String, fromSource: Seq[KillSource]): Seq[Killmail] =
    fromXml(XML.loadString(str), fromSource)

  def fromXml(ns: NodeSeq, fromSource: Seq[KillSource]): Seq[Killmail] = {
    (ns \ "result" \ "rowset" \ "row") map  { rows =>
      processSingleMail(rows, fromSource)
    }
    Seq()
  }
}
