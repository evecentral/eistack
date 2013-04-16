/**
 * Copyright 2013 Yann Ramin
 * atrus@stackworks.net
 *
 * https://github.com/theatrus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *limitations under the License.
 */

package com.eveintel.killmail

import xml.{XML, NodeSeq}
import java.net.URL
import org.joda.time.format.DateTimeFormat

object XmlConversions {

  val sqlDate = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  def processParticipant(ns: NodeSeq): Participant = {
    val shipType = (ns \ "@shipTypeID").text.toLong
    val factionName = (ns \ "@factionName").text
    val factionID = (ns \ "@factionID").text.toLong

    val damageTaken = (ns \ "@damageTaken").text match {
      case "" => None
      case t => Some(t.toInt)
    }

    val damageDone = (ns \ "@damageDone").text match {
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

    Participant(Some(charId), Some(charName), Some(corpId), Some(corpName),
      Some(allianceId), Some(allianceName),
      Some(shipType), Some(factionID), Some(factionName),
      damageTaken, damageDone, securityStatus, finalBlow, weaponTypeId)
  }

  def processLoot(ns: NodeSeq): Loot = {
    val flag = (ns \ "@flag").text.toInt
    val qtyDestroyed = (ns \ "@qtyDestroyed").text.toInt
    val qtyDropped = (ns \ "@qtyDropped").text.toInt
    val typeId = (ns \ "@typeID").text.toLong
    Loot(typeId, flag, qtyDropped, qtyDestroyed, Seq(), Seq())
  }

  def processSingleMail(ns: NodeSeq, fromSource: KillSource): Killmail = {
    val ssId = (ns \ "@solarSystemID").text.toLong
    val moonID = (ns \ "@moonID").text match {
      case "" => None
      case n => Some(n.toLong)
    }
    val killTime = sqlDate.parseDateTime((ns \ "@killTime").text)
    val killInternalID = Some((ns \ "@killInternalID").text.toLong)
    val killID = (ns \ "@killID").text.toLong
    val victim = processParticipant(ns \ "victim")
    val attackers = (ns \ "rowset").filter(rowset => (rowset \ "@name").text == "attackers").map(rowset => (rowset \ "row").map(processParticipant(_))).flatten
    val loot = (ns \ "rowset").filter(rowset => (rowset \ "@name").text == "items").flatMap(rowset => (rowset \ "row").map(processLoot(_)))

    val tombstone = fromSource.tombstone
    val source = fromSource.copy(edkInternalId = killInternalID)

    Killmail(killID, ssId, killTime, moonID, Seq(), victim, attackers,
      loot, Seq(source), tombstone, Seq())
  }

  def apply(url: URL, fromSource: KillSource): Seq[Killmail] = {
    val ns = XML.load(url)
    apply(ns, fromSource)
  }

  def apply(str: String, fromSource: KillSource): Seq[Killmail] =
    apply(XML.loadString(str), fromSource)

  def apply(ns: NodeSeq, fromSource: KillSource): Seq[Killmail] = {
    (ns \ "result" \ "rowset" \ "row") map  { rows =>
      processSingleMail(rows, fromSource)
    }
  }
}
