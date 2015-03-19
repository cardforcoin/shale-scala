package cfc.shale

import com.typesafe.config.ConfigFactory

class ShaleConfigTest extends org.scalatest.FreeSpec {

  "ShaleConfig" - {
    "default parses successfully" in ShaleConfig.parse(ConfigFactory.load())
  }
}
