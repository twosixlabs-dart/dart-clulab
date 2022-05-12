package com.twosixlabs.dart.odinson

import ai.lum.odinson.digraph.Vocabulary
import ai.lum.odinson.lucene.index.{IncrementalOdinsonIndex, OdinsonIndex}
import ai.lum.odinson.utils.IndexSettings
import ai.lum.odinson.{Document => OdinsonDocument}
import better.files.File
import com.twosixlabs.dart.odinson.config.OdinsonConfig
import org.apache.lucene.store.FSDirectory
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


case class IndexerResult( corpusSize : Int )


class OdinsonIndexer( config : OdinsonConfig ) {

    private val LOG : Logger = LoggerFactory.getLogger( getClass )

    implicit private val ec : ExecutionContext = ExecutionContext.global

    // TODO - @michael - find out what `dependencies.txt` is from the original repo
    private val vocab : Vocabulary = Vocabulary.empty

    private val index : OdinsonIndex = {
        val indexDir = FSDirectory.open( File( config.index.dir ).path )
        Vocabulary.fromDirectory( indexDir )
        new IncrementalOdinsonIndex( directory = indexDir,
                                     settings = IndexSettings( config.fields.storedFields ),
                                     computeTotalHits = true,
                                     displayField = config.fields.displayField,
                                     normalizedTokenField = config.fields.normalizedTokenField,
                                     addToNormalizedField = config.fields.addToNormalizedField.toSet,
                                     incomingTokenField = config.fields.incomingTokenField,
                                     outgoingTokenField = config.fields.outgoingTokenField,
                                     maxNumberOfTokensPerSentence = config.index.maxNumberOfTokensPerSentence,
                                     invalidCharacterReplacement = config.index.invalidCharacterReplacement,
                                     refreshMs = config.index.refreshMs )
    }

    def indexDocuments( docs : Seq[ OdinsonDocument ] ) : Future[ IndexerResult ] = {
        val sourceDocCount : Int = docs.size
        val indexingOp : Seq[ Future[ Unit ] ] = docs.map( indexDocument )

        Future.sequence( indexingOp ) transform {
            case Success( _ ) => Success( IndexerResult( corpusSize = sourceDocCount ) )
            case Failure( e ) => Failure( e )
        }
    }

    def indexDocument( doc : OdinsonDocument ) : Future[ Unit ] = {
        Future( index.indexOdinsonDoc( doc ) )
    }

    def close( ) : Unit = index.close()

}

object OdinsonIndexer {
    private lazy val LOG : Logger = LoggerFactory.getLogger( getClass )

    def setupIndexDir( config : OdinsonConfig ) : Try[ Unit ] = {
        Try {
            val dir = File( config.index.dir )
            if ( !dir.exists() ) dir.createDirectories()
            else {
                if ( !dir.isEmpty ) {
                    LOG.warn( s"${dir.canonicalPath} is not empty, the data in this directory will be lost..." )
                    dir.delete()
                    dir.createDirectories()
                    dir.createDirectories()
                }
            }
        }
    }
}
