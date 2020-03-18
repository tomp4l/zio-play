import model.BeerRepository
import zio.Runtime

package object controllers {

  type ZioRuntime = Runtime[BeerRepository]
}
