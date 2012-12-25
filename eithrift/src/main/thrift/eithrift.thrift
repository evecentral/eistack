namespace java com.eveintel.thrift

struct Participant {
	   1: optional i64 characterId,
	   2: optional string characterName,
	   3: optional i64 coporationId,
	   4: optional string corporationName,
	   5: optional i64 allianceId,
	   6: optional string allianceName,
	   7: optional i64 shipTypeId,
	   8: optional i64 factionId,
	   9: optional string factionName,
	   10: optional i32 damageTaken,
	   11: optional double securityStatus,
	   12: optional bool finalBlow,
	   13: optional i64 weaponTypeId
}

struct Loot {
	   1: i64 typeId,
	   2: bool flag,
	   3: i32 qtyDropped,
	   4: i32 qtyDestroyed,
	   5: optional list<Loot>
}

struct KillSource {
	   1: bool directApi,
	   2: optional string boardType,
	   3: optional string boardUrl,
	   4: optional i64 edkInternalKillId
}

struct Killmail {
	   1: i64 ccpId,
	   2: i64 solarSystemId,
	   3: i32 killTime,
	   4: i64 moonId,
	   5: binary hash,
	   6: Participant victim,
	   7: list<Participant> attackers,
	   8: list<Loot> loot,
	   9: list<KillSource> sources
}