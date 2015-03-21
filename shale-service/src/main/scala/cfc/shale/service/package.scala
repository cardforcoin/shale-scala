package cfc.shale

import java.nio.file.Paths

import edu.gatech.gtri.typesafeconfigextensions.factory.ConfigFactory._

package object service {

  def loadConfig = emptyConfigFactory
    .bindDefaults()
    .withSources(
      classpathResource("reference"),
      classpathResource("application"),
      configFile().byPath(Paths.get("local.conf")),
      configFile().byKey("shale.config.file"),
      systemProperties()
    ).fromLowestToHighestPrecedence()
    .load()
}
