import model.{BeerRepository, UserRepository}
import zio.Runtime

package object controllers {

  type ZioRuntime = Runtime[BeerRepository with UserRepository]
}
