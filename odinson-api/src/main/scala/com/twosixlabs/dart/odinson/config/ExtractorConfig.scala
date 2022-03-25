package com.twosixlabs.dart.odinson.config

import com.typesafe.config.Config

object ExtractorConfig {
    def fromConfig( config : Config ) : ExtractorConfig = {
        val rules = config.getString( "rules.dir" )
        val state = config.getString( "state" )
        val lemmatize = config.getBoolean( "lemmatize" )
        ExtractorConfig( rules, state, lemmatize )
    }
}

case class ExtractorConfig( rulesDir : String, state : String, lemmatize : Boolean )
