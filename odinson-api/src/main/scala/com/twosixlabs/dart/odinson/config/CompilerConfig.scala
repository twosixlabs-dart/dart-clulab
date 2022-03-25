package com.twosixlabs.dart.odinson.config

import com.typesafe.config.Config


object CompilerConfig {
    def fromConfig( config : Config, fields : FieldMappings ) : CompilerConfig = {
        CompilerConfig(
            allTokenFields = List( fields.rawTokenField,
                                   fields.wordTokenField,
                                   fields.normalizedTokenField,
                                   fields.lemmaTokenField,
                                   fields.posTagTokenField,
                                   fields.chunkTokenField,
                                   fields.entityTokenField,
                                   fields.outgoingTokenField ),
            defaultTokenField = fields.normalizedTokenField,
            sentenceLengthField = fields.sentenceLengthField,
            dependenciesField = fields.dependenciesField,
            incomingTokenField = fields.incomingTokenField,
            outgoingTokenField = fields.outgoingTokenField,
            aggressiveNormalization = config.getBoolean( "aggressiveNormalization" ) )
    }
}

case class CompilerConfig( allTokenFields : List[ String ],
                           defaultTokenField : String,
                           sentenceLengthField : String,
                           dependenciesField : String,
                           incomingTokenField : String,
                           outgoingTokenField : String,
                           aggressiveNormalization : Boolean )
