package com.twosixlabs.dart.odinson

import ai.lum.odinson.{Field, GraphField, TokensField, Document => OdinsonDocument, Sentence => OdinsonSentence}
import com.twosixlabs.dart.odinson.config.OdinsonConfig
import org.clulab.processors.clu.CluProcessor
import org.clulab.processors.fastnlp.FastNLPProcessor
import org.clulab.processors.{Processor, Document => ProcessorsDocument, Sentence => ProcessorsSentence}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Success, Try}

class OdinsonAnnotator( odinsonConfig : OdinsonConfig ) {

    private lazy val LOG : Logger = LoggerFactory.getLogger( getClass )

    private val processor : Processor = {
        odinsonConfig.annotator.processor match {
            case "clulab" => new CluProcessor
            case "fast_nlp" => new FastNLPProcessor
        }
    }

    def annotateDocument( id : String, text : String ) : Option[ OdinsonDocument ] = {
        doProcessorsAnnotation( text ) match {
            case Success( annotatedDoc ) => {
                annotatedDoc.id = Some( id )
                Some( convertToOdinsonDoc( annotatedDoc ) )
            }
            case Failure( e ) => {
                LOG.warn( s"error encountered annotating, skipping doc: ${id} - ${e.getClass.getSimpleName} : ${e.getMessage} : ${e.getCause}" )
                if ( LOG.isDebugEnabled() ) e.printStackTrace()
                None
            }
        }
    }

    private def doProcessorsAnnotation( text : String ) : Try[ ProcessorsDocument ] = {
        try {
            val doc = processor.mkDocument( text )
            processor.tagPartsOfSpeech( doc )
            processor.lemmatize( doc )
            processor.recognizeNamedEntities( doc )
            processor.parse( doc )
            processor.chunking( doc )
            processor.srl( doc )
            doc.clear()
            Success( doc )
        } catch {
            case e : Throwable => Failure( e )
        }
    }

    /** convert processors document to odinson document */
    private def convertToOdinsonDoc( doc : ProcessorsDocument ) : OdinsonDocument = {
        val metadata = makeOdinsonMetadata( doc )
        val sentences = makeOdinsonSentences( doc )
        OdinsonDocument( doc.id.get, metadata, sentences )
    }

    /** generate metadata from processors document */
    private def makeOdinsonMetadata( d : ProcessorsDocument ) : Seq[ Field ] = {
        // TODO return metadata
        Seq.empty
    }

    /** make sequence of odinson documents from processors document */
    private def makeOdinsonSentences( d : ProcessorsDocument ) : Seq[ OdinsonSentence ] = {
        d.sentences.map( convertSentence )
    }

    /** convert processors sentence to odinson sentence */
    def convertSentence( s : ProcessorsSentence ) : OdinsonSentence = {
        val raw = TokensField( odinsonConfig.fields.rawTokenField, s.raw )
        val word = TokensField( odinsonConfig.fields.wordTokenField, s.words )
        val maybeTag = s.tags.map( tags => TokensField( odinsonConfig.fields.posTagTokenField, tags ) )
        val maybeLemma = s.lemmas.map( lemmas => TokensField( odinsonConfig.fields.lemmaTokenField, lemmas ) )
        val maybeEntity = s.entities.map( entities => TokensField( odinsonConfig.fields.entityTokenField, entities ) )
        val maybeChunk = s.chunks.map( chunks => TokensField( odinsonConfig.fields.chunkTokenField, chunks ) )
        val maybeDeps = s.dependencies.map( g => GraphField( odinsonConfig.fields.dependenciesField, g.allEdges, g.roots ) )
        val fields = Some( raw ) :: Some( word ) :: List( maybeTag, maybeLemma, maybeEntity, maybeChunk, maybeDeps )
        OdinsonSentence( s.size, fields.flatten )
    }

    // loads all the processor models etc... so that we don't have to do a cold run
    def warmup( ) : Unit = {
        LOG.debug( "warmup odinson annotator" )
        annotateDocument( "_warmup_", "this is a sentence" )
    }

}
