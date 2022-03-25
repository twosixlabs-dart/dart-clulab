package com.twosixlabs.dart.odinson

import ai.lum.odinson.utils.QueryUtils
import ai.lum.odinson.{Extractor, ExtractorEngine, Mention}
import better.files.File
import com.twosixlabs.dart.odinson.config.OdinsonConfig


sealed trait QueryType

case object LEMMA extends QueryType

case object NORM extends QueryType

case object LITERAL extends QueryType

case class ExtractorQuery( ruleset : String, filename : Option[ String ] = None, variables : Map[ String, String ] = Map(), queryType : QueryType = LEMMA )


class OdinsonExtractor( odinsonConfig : OdinsonConfig ) {

    private lazy val engine : ExtractorEngine = ExtractorEngine.fromConfig( odinsonConfig.configAdapter() )

    def execute( query : ExtractorQuery ) : Seq[ Mention ] = {
        val extractors = buildExtractors( query )
        engine.extractNoState( extractors ).toSeq
    }

    private def buildExtractors( query : ExtractorQuery ) : Seq[ Extractor ] = {
        val varPatterns = query.queryType match {
            case LEMMA => query.variables.map( v => (v._1, toPattern( "lemma", v._2 )) )
            case NORM => query.variables.map( v => (v._1, toPattern( "norm", v._2 )) )
            case LITERAL => query.variables
        }

        val rules = File( s"${odinsonConfig.extractor.rulesDir}/${query.ruleset}.yml" ).contentAsString
        engine.compileRuleString( rules, varPatterns )
    }

    private def toPattern( field : String, value : String ) : String = {
        s"[${field}=${QueryUtils.maybeQuoteLabel( value )}]"
    }

}
