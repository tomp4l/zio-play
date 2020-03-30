package model

import zio.{ZIO, ZLayer}

object BeerRepository {

  sealed trait BeerId {
    def id: Int
  }

  case class UnknownBeerId(id: Int) extends BeerId

  case class KnownBeerId(id: Int) extends BeerId

  case class Beer(id: KnownBeerId, name: String)

  trait Service {
    def getBeer(beerId: BeerId): ZIO[Any, Error, Option[Beer]]

    def getKnownBeer(beerId: KnownBeerId): ZIO[Any, Error, Beer]

    def getBeers: ZIO[Any, Error, Seq[Beer]]
  }

  val nullService: ZLayer[Any, Nothing, BeerRepository] = ZLayer.succeed(
    new Service {
      override def getBeer(beerId: BeerId): ZIO[Any, Error, Option[Beer]] = ZIO.succeed(None)

      override def getKnownBeer(beerId: KnownBeerId): ZIO[Any, Error, Beer] = ZIO.fail(ServiceUnavailable)

      override def getBeers: ZIO[Any, Error, Seq[Beer]] = ZIO.succeed(Seq())
    }
  )

  def getBeer(beerId: BeerId): ZIO[BeerRepository, Error, Option[Beer]] =
    ZIO.accessM(_.get.getBeer(beerId))

  def getBeers: ZIO[BeerRepository, Error, Seq[Beer]] =
    ZIO.accessM(_.get.getBeers)

  def getKnownBeer(beerId: KnownBeerId): ZIO[BeerRepository, Error, Beer] =
    ZIO.accessM(_.get.getKnownBeer(beerId))
}
