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

import scala.collection.JavaConversions._
import org.joda.time.DateTime
import com.eveintel.protobuf.Eistack
import com.google.protobuf.ByteString

object PricingSource extends Enumeration {
  type PricingSource = Value
  val EVECENTRAL, MANUAL = Value
}

object BoardType extends Enumeration {
  type BoardType = Value
  val EDK, ZKB, CUSTOM = Value
}

case class PricingLookup(price: Double,
                         pricedAt: DateTime,
                         source: Option[PricingSource.PricingSource],
                         regionId: Option[Long],
                         solarSystemId: Option[Long])

case class Loot(typeId: Long,
                flag: Int,
                qtyDropped: Int,
                qtyDestroyed: Int,
                containedLoot: Seq[Loot],
                pricing: Seq[PricingLookup])

case class Participant(characterId: Option[Long],
                       characterName: Option[String],
                       corporationId: Option[Long],
                       corporationName: Option[String],
                       allianceId: Option[Long],
                       allianceName: Option[String],
                       shipTypeId: Option[Long],
                       factionId: Option[Long],
                       factionName: Option[String],
                       damageTaken: Option[Int],
                       damageDone: Option[Int],
                       securityStatus: Option[Double],
                       finalBlow: Option[Boolean],
                       weaponTypeId: Option[Long])

case class KillSource(directApi: Boolean = false,
                      boardType: Option[BoardType.BoardType] = None,
                      boardUrl: Option[String] = None,
                      edkInternalId: Option[Long] = None,
                      levelsOfIndirection: Int = 0,
                      tombstone: Int = 0)

case class Killmail(ccpId: Long, solarSystemId: Long,
                    killTime: DateTime,
                    moonId: Option[Long],
                    hash: Seq[Byte],
                    victim: Participant,
                    attackers: Seq[Participant],
                    loot: Seq[Loot],
                    source: Seq[KillSource],
                    tombstone: Int,
                    price: Seq[PricingLookup])

