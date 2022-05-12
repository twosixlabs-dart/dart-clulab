package com.twosixlabs.dart.odinson

import ai.lum.odinson.{Document => OdinsonDocument}
import better.files.File
import com.twosixlabs.dart.odinson.config.OdinsonConfig
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import org.scalatest.BeforeAndAfterEach

import java.util.concurrent.TimeUnit.MINUTES
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class OdinsonIndexerTestSuite extends OdinsonTestBase with BeforeAndAfterEach {

    private val indexerConf : OdinsonConfig = ODINSON_CONF.copy( index = ODINSON_CONF.index.copy( dir = "target/indexer_test" ) )

    override def afterEach( ) : Unit = {
        File( indexerConf.index.dir ).delete( swallowIOExceptions = true )
    }

    "Odinson Indexer" should "set up an empty index" in {
        OdinsonIndexer.setupIndexDir( indexerConf )

        val index = File( indexerConf.index.dir )
        index.exists shouldBe true
        index.isEmpty shouldBe true
    }

    "Odinson Indexer" should "overwrite an existing index" in {
        OdinsonIndexer.setupIndexDir( indexerConf )
        val index = File( indexerConf.index.dir )

        val touch = index.createChild( "test" )
        index.list.toList should contain( touch )

        OdinsonIndexer.setupIndexDir( indexerConf )
        index.exists shouldBe true
        index.isEmpty shouldBe true
    }

    "Odinson Indexer" should "index some documents" in {
        val indexer = new OdinsonIndexer( indexerConf )

        val indexerOp = indexer.indexDocuments( ODINSON_DOCS )
        val indexerResults : IndexerResult = Await.result( indexerOp, Duration( 2, MINUTES ) )
        indexer.close()

        val index = getLuceneReader( indexerConf.index.dir )
        index.numDocs() shouldBe 5

        indexerResults.corpusSize shouldBe ODINSON_DOCS.size
    }

    // This isn't really a test but just a quick way to generate an index from a directory of files...
    "Odinson Indexer" should "index demo docs" ignore {
        val indexer = new OdinsonIndexer( indexerConf )

        val files = File( "nlp_output/odinson" )
          .list
          .flatMap( file => {
              val content = file.contentAsString
              if ( content.nonEmpty ) Some( OdinsonDocument.fromJson( file.toJava ) )
              else None
          } )
          .toSeq

        val indexerOp = indexer.indexDocuments( files )
        val indexerResults : IndexerResult = Await.result( indexerOp, Duration( 120, MINUTES ) )
        println( indexerResults.corpusSize )
        indexer.close()
    }

    private def getLuceneReader( index : String ) : DirectoryReader = {
        DirectoryReader.open( FSDirectory.open( File( index ).path ) )
    }

}
