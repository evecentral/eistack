package com.eveintel.killmail

import scala.collection.JavaConversions._
import org.joda.time.DateTime
import com.eveintel.protobuf.Eistack
import com.google.protobuf.ByteString


object KillmailImplicitConverters {

  /**
   * Reduce matching to make using protobuf builders easier
   * @param o
   * @tparam T
   */
  class SideeffectOption[T](o: Option[T]) {
    def ifDefined(f: (T) => Unit) = if (o.isDefined) f(o.get)
  }

  implicit def optionToSideeffect[T](o: Option[T]) = new SideeffectOption[T](o)

  implicit def fromProtoToPrice(pl: Eistack.Killmail.PricingLookup): PricingLookup = {
    PricingLookup(pl.getPrice, new DateTime(pl.getPricedAt),
      pl.hasSource match { case true => Some(PricingSource(pl.getSource.getNumber)) case false => None },
      pl.hasRegionId match { case true => Some(pl.getRegionId) case false => None },
      pl.hasSolarSystemId match { case true => Some(pl.getSolarSystemId) case false => None} )
  }

  implicit def fromProtoToSource(s: Eistack.Killmail.KillSource): KillSource = {
    KillSource(s.getDirectApi,
      s.hasBoardType match { case true => Some(BoardType(s.getBoardType.getNumber)) case false => None },
      s.hasBoardUrl match { case true => Some(s.getBoardUrl) case false => None },
      s.hasEdkInternalKillId match { case true => Some(s.getEdkInternalKillId) case false => None },
      s.getLevelsOfIndirection, s.getTombstone)
  }

  implicit def fromProtoToParticipant(p: Eistack.Killmail.Participant): Participant = {
    Participant(p.hasCharacterId match { case true => Some(p.getCharacterId) case false => None },
      p.hasCharacterName match { case true => Some(p.getCharacterName) case false => None },
      p.hasCorporationId match { case true => Some(p.getCorporationId) case false => None },
      p.hasCorporationName match { case true => Some(p.getCorporationName) case false => None },
      p.hasAllianceId match { case true => Some(p.getAllianceId) case false => None },
      p.hasAllianceName match { case true => Some(p.getAllianceName) case false => None },
      p.hasShipTypeId match { case true => Some(p.getShipTypeId) case false => None },
      p.hasFactionId match { case true => Some(p.getFactionId) case false => None },
      p.hasFactionName match { case true => Some(p.getFactionName) case false => None },
      p.hasDamageTaken match { case true => Some(p.getDamageTaken) case false => None },
      p.hasSecurityStatus match { case true => Some(p.getSecurityStatus) case false => None },
      p.hasFinalBlow match { case true => Some(p.getFinalBlow) case false => None },
      p.hasWeaponTypeId match { case true => Some(p.getWeaponTypeId) case false => None }
    )
  }

  implicit def fromProtoToLoot(l: Eistack.Killmail.Loot): Loot = {
    Loot(l.getTypeId, l.getFlag, l.getQtyDropped, l.getQtyDestroyed,
      l.getContainedLootList.toSeq.map(t => fromProtoToLoot(t)),
      l.getPricingList.toSeq.map(t => fromProtoToPrice(t)))
  }

  implicit def fromProtoToKillmail(message: Eistack.Killmail) : Killmail = {
    Killmail(message.getCcpId,
      message.getSolarSystemId,
      new DateTime(message.getKillTime),
      message.hasMoonId match { case true => Some(message.getMoonId) case false => None },
      message.getHash.toByteArray.toSeq,
      message.getVictim,
      message.getAttackersList.toSeq.map { p => fromProtoToParticipant(p) },
      message.getLootList.toSeq.map { l => fromProtoToLoot(l)},
      message.getSourcesList.toSeq.map { s => fromProtoToSource(s) },
      message.getTombstone,
      message.getPriceList.toSeq.map { p => fromProtoToPrice(p) })
  }

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
