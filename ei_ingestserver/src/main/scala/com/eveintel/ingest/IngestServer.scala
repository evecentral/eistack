package com.eveintel.ingest

import akka.actor.{Actor, Props}
import spray.routing.HttpService
import spray.can.server.SprayCanHttpServerApp
import com.eveintel.killmail.{BoardType, KillSource, XmlConversions}
import com.eveintel.protobuf.Eistack.Killmails
import scala.collection.JavaConversions._

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
          val converted = Killmails.newBuilder().addAllKillmails(XmlConversions(contents, kmsource).map(_.build))
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
