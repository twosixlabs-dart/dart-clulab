package com.twosixlabs.dart.odinson.config

import com.typesafe.config.Config

object IndexConfig {

    def fromConfig( config : Config ) : IndexConfig = {
        val indexDir : String = config.getString( "dir" )
        val maxTokens : Int = config.getInt( "maxNumberOfTokensPerSentence" )
        val invalidCharReplacement : String = config.getString( "invalidCharacterReplacement" )

        IndexConfig( dir = indexDir,
                     maxNumberOfTokensPerSentence = maxTokens,
                     invalidCharacterReplacement = invalidCharReplacement )
    }
}

case class IndexConfig( dir : String,
                        maxNumberOfTokensPerSentence : Int,
                        invalidCharacterReplacement : String )
