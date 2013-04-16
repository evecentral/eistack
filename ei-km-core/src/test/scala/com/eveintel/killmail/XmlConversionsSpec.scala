package com.eveintel.killmail

import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpec

class XmlConversionsSpec extends WordSpec with MustMatchers{

  val edkMail1 = """|<?xml version="1.0" encoding="UTF-8"?>
                   |<eveapi edkapi="1.03" version="2">
                   |  <currentTime>2013-03-18 15:42:19</currentTime>
                   |  <result>
                   |    <rowset columns="killID,solarSystemID,killTime,moonID,hash,trust" key="killID" name="kills">
                   |      <row hash="3b119634f1498a98c84dbbb62ddfdbe3" killID="0" killInternalID="253739" killTime="2007-10-22 15:22:00" moonID="0" solarSystemID="30000903" trust="1">
                   |        <victim allianceID="173739862" allianceName="Veritas Immortalis" characterID="482310896" characterName="Verwoester" corporationID="360870066" corporationName="The African Contingency" damageTaken="0" factionID="0" factionName="" shipTypeID="670"/>
                   |        <rowset columns="characterID,characterName,corporationID,corporationName,allianceID,allianceName,factionID,factionName,securityStatus,damageDone,finalBlow,weaponTypeID,shipTypeID" name="attackers">
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="368882082" characterName="Black Jumper" corporationID="142114724" corporationName="Altera Odyssea" damageDone="0" factionID="0" factionName="" finalBlow="1" securityStatus="5.0" shipTypeID="22456" weaponTypeID="2889"/>
                   |        </rowset>
                   |      </row>
                   |      <row hash="934773e5759beaa781664a233207744e" killID="0" killInternalID="253740" killTime="2007-10-22 15:24:00" moonID="0" solarSystemID="30001029" trust="1">
                   |        <victim allianceID="917526329" allianceName="Alliance917526329" characterID="1448032579" characterName="AligatorKorr" corporationID="1418791931" corporationName="Risky eXplosion" damageTaken="0" factionID="0" factionName="" shipTypeID="629"/>
                   |        <rowset columns="characterID,characterName,corporationID,corporationName,allianceID,allianceName,factionID,factionName,securityStatus,damageDone,finalBlow,weaponTypeID,shipTypeID" name="attackers">
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="1516491142" characterName="cookherk" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="0" securityStatus="5.0" shipTypeID="11993" weaponTypeID="2410"/>
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="105673158" characterName="Isanoe nothwood" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="0" securityStatus="5.0" shipTypeID="16233" weaponTypeID="3520"/>
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="175351067" characterName="Lyrithe" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="0" securityStatus="0.3" shipTypeID="20125" weaponTypeID="2185"/>
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="1721241013" characterName="JUNETTE" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="1" securityStatus="5.0" shipTypeID="11999" weaponTypeID="2897"/>
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="628207801" characterName="JAYKAY thecoyote" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="0" securityStatus="1.1" shipTypeID="22456" weaponTypeID="22456"/>
                   |          <row allianceID="166439722" allianceName="Tau Ceti Federation" characterID="740334210" characterName="Batistadu" corporationID="1132466394" corporationName="Kernel of War" damageDone="0" factionID="0" factionName="" finalBlow="0" securityStatus="5.0" shipTypeID="11957" weaponTypeID="2567"/>
                   |        </rowset>
                   |      </row>
                   |    </rowset>
                   |  </result>
                   |  <cachedUntil>2013-03-18 15:42:19</cachedUntil>
                   |</eveapi>
                   |""".stripMargin

  val ks = KillSource(false, None, None, None, 0, 0)

  "xml loading" should {
    "load an EDK derived mail" in {
      val kmAll = XmlConversions(edkMail1, ks)
      val km = kmAll.head
      val km2 = kmAll.tail.head

      kmAll must have size(2)
      km.ccpId must equal(0)
      km.victim.allianceId.get must equal (173739862)
      km.victim.allianceName.get must equal ("Veritas Immortalis")
      km.victim.characterName.get must equal ("Verwoester")
      km.victim.characterId.get must equal (482310896)
      km.victim.shipTypeId.get must equal (670)
      km.victim.corporationName.get must equal ("The African Contingency")
      km.victim.corporationId.get must equal (360870066)
      km.victim.damageTaken.get must equal (0)
      km.attackers must have size(1)

      km2.attackers must have size(6)
    }

    "load an EDK derived mail from a file" in {
      val mails = XmlConversions(getClass.getResource("/test1.xml"), ks)

      mails must have size(3)

    }
  }

}
