package com.eveintel.ingest

import akka.actor.{Actor, Props}
import spray.routing.HttpService
import spray.can.server.SprayCanHttpServerApp
import com.eveintel.killmail.{KillHashing, BoardType, KillSource, XmlConversions}
import com.eveintel.protobuf.Eistack.Killmails
import scala.collection.JavaConversions._
import org.apache.thrift.transport.{TSocket, TFramedTransport}
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.cassandra.thrift.{ColumnParent, Cassandra}


class IngestServiceActor extends Actor with IngestService {
  def actorRefFactory = context
  def receive = runRoute(ingestRoute)
}

trait IngestService extends HttpService {
  override implicit def executionContext = actorRefFactory.dispatcher
  import com.eveintel.killmail.KillmailImplicitConverters._
  val ingestRoute = {
    post {
      path("convert") {
        formFields('xml, 'source) { case (contents: String, source: String) =>
          val kmsource = KillSource(false, boardType = Some(BoardType.EDK), boardUrl = Some(source))
          val kms = XmlConversions(contents, kmsource).map(KillHashing(_)).map(_.build)
          val converted = Killmails.newBuilder().addAllKillmails(kms)
          complete(converted.build().toByteArray)
        }
      }
    }
  }
}

object IngestServer extends App with SprayCanHttpServerApp {

  // the handler actor replies to incoming HttpRequests
  val handler = system.actorOf(Props[IngestServiceActor], name = "ingest-service")

  newHttpServer(handler) ! Bind(interface = "localhost", port = 8976)


}
