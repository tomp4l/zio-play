package model

import zio._
import zio.test.mock._

object MockUserRepository {
  private val builder: URLayer[Has[Proxy], UserRepository] = ZLayer.fromService(invoke =>
    new UserRepository.Service {
      override def getUser(userId: UserRepository.UserId): ZIO[Any, Error, Option[UserRepository.User]] = invoke(GetUser, userId)

      override def save(user: UserRepository.User): ZIO[Any, Error, Unit] = invoke(Save, user)
    })

  sealed trait Tag[I, A] extends Method[UserRepository, I, A] {
    def envBuilder: URLayer[Has[Proxy], UserRepository] = builder
  }

  object GetUser extends Tag[UserRepository.UserId, Option[UserRepository.User]]

  object Save extends Tag[UserRepository.User, Unit]

}
