package controllers

import infrastructure.HttpBeerRepository
import javax.inject.{Inject, Provider}
import play.api.libs.ws.WSClient
import zio.Runtime
import zio.internal.Platform

import scala.concurrent.ExecutionContext


class RuntimeProvider @Inject
(val client: WSClient)(implicit ec: ExecutionContext) extends Provider[ZioRuntime] {
  override def get(): ZioRuntime = {
    val beers = new HttpBeerRepository(client).service
    val allLayers = beers

    Runtime.unsafeFromLayer(allLayers, Platform.fromExecutionContext(ec))
  }
}
