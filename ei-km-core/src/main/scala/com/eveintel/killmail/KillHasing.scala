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

import com.google.common.hash.{Hasher, Hashing}
import java.nio.charset.Charset


object KillHashing {

  private[this] def hashParticipant(h: Hasher, p: Participant) {
    p.productIterator.foreach {
      case Some(l: Long) => h.putLong(l)
      case Some(i: Int) => h.putInt(i)
      case Some(s: String) => h.putString(s, Charset.forName("UTF-8"))
      case Some(b: Boolean) => h.putBoolean(b)
      case Some(d: Double) => h.putDouble(d)
      case None =>
    }
  }

  private[this] def hashLoot(h: Hasher, sl: Seq[Loot]) {
    sl.sortBy(loot => (loot.typeId << 32) + (loot.qtyDestroyed << 16) + loot.containedLoot.size).foreach { l =>
      h.putLong(l.typeId)
      h.putLong(l.qtyDestroyed)
      h.putLong(l.qtyDropped)
      if (l.containedLoot.size > 0)
        hashLoot(h, l.containedLoot)
    }
  }

  def apply(km: Killmail): Killmail = {
    val hf = Hashing.murmur3_128()
    val hasher = hf.newHasher()
    hasher.putLong(km.ccpId)
    hasher.putLong(km.killTime.getMillis)
    hasher.putLong(km.solarSystemId)
    hashParticipant(hasher, km.victim)
    km.attackers.sortWith { case (p1, p2) =>
      (p1.characterId.getOrElse(p1.factionId.getOrElse(0L))
      < p2.characterId.getOrElse(p2.factionId.getOrElse(0L)))
    }.foreach(hashParticipant(hasher, _))
    hashLoot(hasher, km.loot)
    km.copy(hash = hasher.hash().asBytes())
  }

}
