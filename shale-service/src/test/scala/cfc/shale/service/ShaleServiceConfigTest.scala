package cfc.shale.service

import com.typesafe.config.ConfigFactory

class ShaleServiceConfigTest extends org.scalatest.FreeSpec {

  "ShaleConfig" - {
    "default parses successfully" in ShaleServiceConfig.parse(ConfigFactory.load())
  }
}
