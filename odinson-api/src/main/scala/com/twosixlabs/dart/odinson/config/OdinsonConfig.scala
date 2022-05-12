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
               |
               |  index {
               |    documentIdField: ${fields.documentIdField}
               |
               |    storedFields = [
               |      ${this.fields.displayField},
               |      ${this.fields.lemmaTokenField},
               |      ${this.fields.rawTokenField}
               |    ]
               |
               |    addToNormalizedField = [
               |      ${this.fields.rawTokenField},
               |      ${this.fields.wordTokenField}
               |    ]
               |
               |    normalizedTokenField: ${this.fields.normalizedTokenField}
               |
               |    incomingTokenField: ${this.fields.incomingTokenField}
               |    outgoingTokenField: ${this.fields.outgoingTokenField}
               |
               |    incremental: ${this.index.incremental}
               |    refreshMs: ${this.index.refreshMs}
               |
               |    maxNumberOfTokensPerSentence: ${this.index.maxNumberOfTokensPerSentence}
               |    invalidCharacterReplacement: ${this.index.invalidCharacterReplacement}
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
               |
               |
               |  state {
               |    provider: memory
               |    memory.persistOnClose: false
               |    memory.stateDir: ./target
               |  }
               |}
               |""".stripMargin

        ConfigFactory.parseString( shimHocon )
    }

}
