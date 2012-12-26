package com.eveintel.killmail

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
                       securityStatus: Option[Double],
                       finalBlow: Option[Boolean],
                       weaponTypeId: Option[Long])

case class KillSource(directApi: Boolean,
                      boardType: Option[BoardType.BoardType],
                      boardUrl: Option[String],
                      edkInternalId: Option[Long],
                      levelsOfIndirection: Int,
                      tombstone: Int)

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

object KillmailConverters {

  /**
   * Reduce matching to make using protobuf builders easier
   * @param o
   * @tparam T
   */
  class SideeffectOption[T](o: Option[T]) {
    def ifDefined(f: (T) => Unit) = if (o.isDefined) f(o.get)
  }

  implicit def optionToSideeffect[T](o: Option[T]) = new SideeffectOption[T](o)

  implicit def toProtoPricingLookup(pl: PricingLookup) = {
    val plb = Eistack.Killmail.PricingLookup.newBuilder()
    plb.setPrice(pl.price).setPricedAt(pl.pricedAt.getMillis)
    pl.regionId.ifDefined { rid => plb.setRegionId(rid) }
    pl.solarSystemId.ifDefined { ssid => plb.setSolarSystemId(ssid) }
    pl.source.ifDefined { source => plb.setSource(Eistack.Killmail.PricingSource.valueOf(source.id)) }
    plb
  }

  implicit def toProtoLoot(l: Loot) = {
    val builder = Eistack.Killmail.Loot.newBuilder()
    builder.setTypeId(l.typeId)
    builder.setFlag(l.flag)
    builder.setQtyDestroyed(l.qtyDestroyed)
    builder.setQtyDropped(l.qtyDropped)
    l.pricing.foreach { price => builder.addPricing(price) }
    builder
  }

  implicit def toProtoKillsource(ks: KillSource) = {
    val builder = Eistack.Killmail.KillSource.newBuilder()
    builder.setDirectApi(ks.directApi)
    ks.boardType.ifDefined { bt => builder.setBoardType(Eistack.Killmail.BoardType.valueOf(bt.id)) }
    ks.boardUrl.ifDefined { builder.setBoardUrl(_) }
    ks.edkInternalId.ifDefined { builder.setEdkInternalKillId(_) }
    builder.setLevelsOfIndirection(ks.levelsOfIndirection)
    builder.setTombstone(ks.tombstone)
    builder
  }



  implicit def toProtoParticipant(pa: Participant) = {
    val builder = Eistack.Killmail.Participant.newBuilder()
    pa.allianceId.ifDefined { builder.setAllianceId(_) }
    pa.allianceName.ifDefined { builder.setAllianceName(_) }
    pa.characterId.ifDefined { builder.setCharacterId(_) }
    pa.characterName.ifDefined { builder.setCharacterName(_) }
    pa.shipTypeId.ifDefined { builder.setShipTypeId(_) }
    pa.factionId.ifDefined { builder.setFactionId(_) }
    pa.factionName.ifDefined { builder.setFactionName(_) }
    pa.damageTaken.ifDefined { builder.setDamageTaken(_) }
    pa.securityStatus.ifDefined { builder.setSecurityStatus(_) }
    pa.finalBlow.ifDefined { builder.setFinalBlow(_) }
    pa.weaponTypeId.ifDefined { builder.setWeaponTypeId(_) }
    builder
  }

  implicit def toProtoKm(km: Killmail) = {
    val kmbuilder = Eistack.Killmail.newBuilder()
    kmbuilder.setCcpId(km.ccpId).setSolarSystemId(km.solarSystemId)

    km.moonId.ifDefined { moonId => kmbuilder.setMoonId(moonId) }
    kmbuilder.setHash(ByteString.copyFrom(km.hash.toArray))
    kmbuilder.setVictim(km.victim)

    km.attackers.foreach { pa => kmbuilder.addAttackers(pa) }
    km.loot.foreach { loot => kmbuilder.addLoot(loot) }
    km.source.foreach { source => kmbuilder.addSources(source) }
    kmbuilder.setTombstone(km.tombstone)
    km.price.foreach(kmbuilder.addPrice(_))
    kmbuilder
  }

}