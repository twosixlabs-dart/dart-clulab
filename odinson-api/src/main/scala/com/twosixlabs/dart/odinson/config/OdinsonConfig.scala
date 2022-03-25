package com.twosixlabs.dart.odinson.config

import com.typesafe.config.{Config, ConfigFactory}

object OdinsonConfig {
    def fromConfig( config : Config ) : OdinsonConfig = {

        val annotator : AnnotatorConfig = AnnotatorConfig.fromConfig( config.getConfig( "annotator" ) )
        val indexer : IndexConfig = IndexConfig.fromConfig( config.getConfig( "index" ) )
        val fields : FieldMappings = FieldMappings.fromConfig( config.getConfig( "fields" ) )
        val extractor : ExtractorConfig = ExtractorConfig.fromConfig( config.getConfig( "extractor" ) )
        val compilerConfig : CompilerConfig = CompilerConfig.fromConfig( config.getConfig( "compiler" ), fields )

        OdinsonConfig( annotator, indexer, compilerConfig, extractor, fields )
    }

}

case class OdinsonConfig( annotator : AnnotatorConfig,
                          index : IndexConfig,
                          compiler : CompilerConfig,
                          extractor : ExtractorConfig,
                          fields : FieldMappings ) {

    /**
     * Odinson OSS currently does not have a standardized configuration structure,
     * this is an adapter to the odinson version
     *
     * @return
     */
    def configAdapter( ) : Config = {
        val shimHocon =
            s"""odinson {
               |
               |  indexDir: ${index.dir}
               |
               |  computeTotalHits: true
               |  displayField: ${fields.displayField}
               |  index.documentIdField: ${fields.documentIdField}
               |
               |  index {
               |    documentIdField: ${fields.documentIdField}
               |  }
               |
               |  compiler {
               |
               |    # fields available per token
               |    allTokenFields = [
               |      ${fields.rawTokenField},
               |      ${fields.wordTokenField},
               |      ${fields.normalizedTokenField},
               |      ${fields.lemmaTokenField},
               |      ${fields.posTagTokenField},
               |      ${fields.chunkTokenField},
               |      ${fields.entityTokenField},
               |      ${fields.incomingTokenField},
               |      ${fields.outgoingTokenField},
               |    ]
               |
               |    # the token field to be used when none is specified
               |    defaultTokenField = ${fields.normalizedTokenField}
               |
               |    sentenceLengthField = ${fields.sentenceLengthField}
               |
               |    dependenciesField = ${fields.dependenciesField}
               |
               |    incomingTokenField = ${fields.incomingTokenField}
               |
               |    outgoingTokenField = ${fields.outgoingTokenField}
               |
               |    aggressiveNormalizationToDefaultField = ${compiler.aggressiveNormalization}
               |
               |  }
               |
               |
               |  state {
               |    provider: memory
               |    memory.persistOnClose: false
               |    memory.stateDir: /opt/app/data
               |  }
               |
               |  compiler {
               |    allTokenFields: [ ${compiler.allTokenFields.mkString( ", " )} ]
               |    defaultTokenField: ${compiler.defaultTokenField}
               |    sentenceLengthField: ${compiler.sentenceLengthField}
               |    dependenciesField: ${compiler.dependenciesField}
               |    incomingTokenField: ${compiler.incomingTokenField}
               |    outgoingTokenField: ${compiler.outgoingTokenField}
               |    aggressiveNormalizationToDefaultField: ${compiler.aggressiveNormalization}
               |  }
               |}
               |""".stripMargin

        ConfigFactory.parseString( shimHocon )
    }

}
