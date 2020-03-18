import com.google.inject.{AbstractModule, TypeLiteral}
import controllers.RuntimeProvider

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(new TypeLiteral[controllers.ZioRuntime] {}).toProvider(new TypeLiteral[RuntimeProvider] {})
  }
}
