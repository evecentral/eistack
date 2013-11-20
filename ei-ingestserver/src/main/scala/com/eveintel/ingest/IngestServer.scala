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

package com.eveintel.ingest

import com.eveintel.killmail.{KillHashing, BoardType, KillSource, XmlConversions}
import com.eveintel.protobuf.Eistack.Killmails
import scala.collection.JavaConversions._

trait IngestService {
  import com.eveintel.killmail.KillmailImplicitConverters._

  def ingest(source: String, contents: String) = {
    val kmsource = KillSource(false, boardType = Some(BoardType.EDK), boardUrl = Some(source))
    val kms = XmlConversions(contents, kmsource).map(KillHashing(_)).map(_.build)
    val converted = Killmails.newBuilder().addAllKillmails(kms)
    converted.build().toByteArray
  }
}

object IngestServer extends App {

}
