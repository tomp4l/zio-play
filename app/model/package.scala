import zio.Has

package object model {
  type UserRepository = Has[UserRepository.Service]
  type BeerRepository = Has[BeerRepository.Service]

}
