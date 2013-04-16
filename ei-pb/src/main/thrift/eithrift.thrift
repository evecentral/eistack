namespace java com.eveintel.thrift

enum PricingSource {
       EVECENTRAL = 1,
       MANUAL = 2
}

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

struct PricingLookup {
       1: double price,
       // Milliseconds sinch epoch, Jan 1 1970 UTC
       2: i64 pricedAt,
       3: optional PricingSource source,
       4: i64 regionId,
       5: optional i64 systemId
}

struct LootContents {
       1: list<Loot> loot
}

struct Loot {
	   1: i64 typeId,
	   2: bool flag,
	   3: i32 qtyDropped,
	   4: i32 qtyDestroyed,
	   5: optional LootContents containedLoot,
	   6: optional list<PricingLookup> price
}

struct KillSource {
	   1: bool directApi,
	   2: optional string boardType,
	   3: optional string boardUrl,
	   4: optional i64 edkInternalKillId,
	   5: i32 levelsOfIndirection,
	   6: i32 tombstone
}

struct Killmail {
       // The CCP Kill-ID
	   1: i64 ccpId,
	   // Where the kill occurred
	   2: i64 solarSystemId,
	   // Milliseconds since epoch, Jan-1 1970, UTC
	   3: i64 killTime,
	   // If the kill occurred at a moon
	   4: optional i64 moonId,
	   // The stable SHA1 hash of key fields in this kill, for synchronization purposes
	   5: binary hash,
	   // Who the victim (loss) of the kill
	   6: Participant victim,
	   // All participants which show on the killmail
	   7: list<Participant> attackers,
	   // Loot that dropped from the victim
	   8: list<LootContents> loot,
	   // Killboards or APIs where this kill originated from, along with a levelOfIndirection
	   9: list<KillSource> sources,
	   // A counter to indicate deletions of this kill. More than 0 indicates that the kill has been removed somewhere
	   10: i32 tombstone,
	   // An aggregate price lookup giving a source, date and time
	   11: optional list<PricingLookup> price
}