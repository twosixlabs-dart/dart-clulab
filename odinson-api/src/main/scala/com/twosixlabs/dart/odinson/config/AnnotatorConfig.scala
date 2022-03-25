package com.twosixlabs.dart.odinson.config

import com.typesafe.config.Config

object AnnotatorConfig {
    def fromConfig( config : Config ) : AnnotatorConfig = {
        val processor : String = config.getString( "processor" )
        val dynetMem : Int = config.getInt( "dynet.mem.mb" )
        val batch : Boolean = config.getBoolean( "dynet.autobatch" )

        AnnotatorConfig( processor = processor,
                         dynetMemMb = dynetMem,
                         dynetAutoBatch = batch )
    }
}

case class AnnotatorConfig( processor : String, dynetMemMb : Int, dynetAutoBatch : Boolean )
