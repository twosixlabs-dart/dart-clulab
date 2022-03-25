package com.twosixlabs.dart.odinson.config

import com.twosixlabs.dart.odinson.OdinsonTestBase

class OdinsonConfigParsingTestSuite extends OdinsonTestBase {

    "Odinson Config Parsing" should "just work™" in {
        ODINSON_CONF.annotator.processor shouldBe "fast_nlp"
        ODINSON_CONF.index.dir shouldBe "./target/test_index"
        ODINSON_CONF.fields.rawTokenField shouldBe "raw"
        ODINSON_CONF.fields.addToNormalizedField shouldBe Seq( "raw", "word" )
        ODINSON_CONF.compiler.aggressiveNormalization shouldBe true
        ODINSON_CONF.compiler.incomingTokenField shouldBe ODINSON_CONF.fields.incomingTokenField
        ODINSON_CONF.extractor.rulesDir shouldBe "./odinson-api/src/test/resources/rules"
    }

}
