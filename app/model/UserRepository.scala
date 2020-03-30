package model

import java.util.UUID

import model.BeerRepository.KnownBeerId
import zio.{ZIO, ZLayer}


object UserRepository {

  case class UserId(uuid: UUID) extends AnyRef

  case class User(id: UserId, name: String, favouriteBeer: Option[KnownBeerId] = None) {

    def withNewFavouriteBeer(beerId: KnownBeerId): User = copy(favouriteBeer = Some(beerId))
  }

  trait Service {
    def getUser(userId: UserId): ZIO[Any, Error, Option[User]]

    def save(user: User): ZIO[Any, Error, Unit]
  }

  def getUser(userId: UserId): ZIO[UserRepository, Error, Option[User]] = ZIO.accessM(_.get.getUser(userId))

  def save(user: User): ZIO[UserRepository, Error, Unit] = ZIO.accessM(_.get.save(user))

  val inMemory: ZLayer[Any, Nothing, UserRepository] =
    ZLayer.succeed(new Service {
      private var map = {
        val uuid = UUID.fromString("4b5abbeb-406c-4a48-91a5-18a72b9de2a5")
        Map(
          UserId(uuid) -> User(UserId(uuid), "bobby")
        )
      }

      override def getUser(userId: UserId): ZIO[Any, Error, Option[User]] =
        ZIO.succeed {
          map.get(userId)
        }

      override def save(user: User): ZIO[Any, Error, Unit] =
        ZIO.succeed {
          map = map + (user.id -> user)
        }
    })

}
