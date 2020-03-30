package model

import zio._
import zio.test.mock._

object MockBeerRepository {
  private val builder: URLayer[Has[Proxy], BeerRepository] = ZLayer.fromService(invoke =>
    new BeerRepository.Service {
      override def getBeer(beerId: BeerRepository.BeerId): ZIO[Any, Error, Option[BeerRepository.Beer]] = invoke(GetBeer, beerId)

      override def getKnownBeer(beerId: BeerRepository.KnownBeerId): ZIO[Any, Error, BeerRepository.Beer] = invoke(GetKnownBeer, beerId)

      override def getBeers: ZIO[Any, Error, Seq[BeerRepository.Beer]] = invoke(GetBeers)
    })

  sealed trait Tag[I, A] extends Method[BeerRepository, I, A] {
    def envBuilder: URLayer[Has[Proxy], BeerRepository] = builder
  }

  object GetBeer extends Tag[BeerRepository.BeerId, Option[BeerRepository.Beer]]

  object GetKnownBeer extends Tag[BeerRepository.KnownBeerId, BeerRepository.Beer]

  object GetBeers extends Tag[Unit, Seq[BeerRepository.Beer]]

}
