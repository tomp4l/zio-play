package model

import zio.{IO, ZIO}

object BeerRepository {

  case class BeerId(id: Int) extends AnyVal

  case class Beer(name: String)

  sealed trait Error

  case object ServiceUnavailable extends Error

  trait Service {
    def getBeer(beerId: BeerId): IO[Error, Option[Beer]]

    def getBeers: IO[Error, Seq[Beer]]
  }

  def getBeer(beerId: BeerId): ZIO[BeerRepository, Error, Option[Beer]] =
    ZIO.accessM(_.get.getBeer(beerId))

  def getBeers: ZIO[BeerRepository, Error, Seq[Beer]] =
    ZIO.accessM(_.get.getBeers)
}
