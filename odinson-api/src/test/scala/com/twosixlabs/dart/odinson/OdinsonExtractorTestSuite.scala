package com.twosixlabs.dart.odinson

import ai.lum.odinson.DataGatherer.VerboseLevels
import ai.lum.odinson.Mention
import better.files.File
import com.twosixlabs.dart.odinson.config.OdinsonConfig
import org.scalatest.BeforeAndAfterAll

import java.util.concurrent.TimeUnit.SECONDS
import scala.concurrent.Await
import scala.concurrent.duration.Duration

//@Ignore // extractor code needs to be refactored first...
class OdinsonExtractorTestSuite extends OdinsonTestBase with BeforeAndAfterAll {

    private val extractorConf : OdinsonConfig = ODINSON_CONF.copy( index = ODINSON_CONF.index.copy( dir = "target/extractor_test" ) )

    override def beforeAll( ) : Unit = {
        val indexer = new OdinsonIndexer( extractorConf )
        Await.result( indexer.indexDocuments( ODINSON_DOCS ), Duration( 10, SECONDS ) )
        indexer.close()
    }

    override def afterAll( ) : Unit = {
        val index = File( extractorConf.index.dir )
        index.delete()
    }

    "Odinson Extractor" should "get extractions from a given ruleset" in {
        val extractor = new OdinsonExtractor( odinsonConfig = extractorConf )
        val query = ExtractorQuery( ruleset = "test-rules" )
        val actualNouns =
            extractor.execute( query )
              .toList
              .flatMap( m => {
                  m.populateFields( VerboseLevels.All )
                  extractArgValue( m, "result" )
              } )

        val expectedNouns = Seq( "Michael", "Lightning", "Bolt", "band", "time" )
        actualNouns.toSet shouldBe expectedNouns.toSet
    }

    "Odinson Extractor" should "execute the a ruleset that includes query variables" in {
        val extractor = new OdinsonExtractor( odinsonConfig = extractorConf )
        val query = ExtractorQuery( ruleset = "query-vars", variables = Map( "query" -> "Michael" ), queryType = NORM )

        val result =
            extractor
              .execute( query )
              .toList
              .flatMap( m => {
                  m.populateFields( VerboseLevels.All )
                  extractArgValue( m, "result" )
              } )

        result should contain( "Michael" )
    }

    private def extractArgValue( mention : Mention, key : String ) : Option[ String ] = {
        mention.arguments.get( key ) match {
            case Some( resultMatch ) => {
                if ( resultMatch.isEmpty ) None
                else resultMatch.head.mentionFields.get( "raw" ) match {
                    case Some( lemmas ) => lemmas.headOption
                    case None => None
                }
            }
            case None => None
        }
    }

}
