package com.twosixlabs.dart.odinson

import ai.lum.odinson.{Document => OdinsonDocument}
import org.scalatest.BeforeAndAfterAll


class OdinsonAnnotatorTestSuite extends OdinsonTestBase with BeforeAndAfterAll {

    private val SOURCE_DOC : String = "This is a unit test written in Scala."
    private val VALIDATION_DOC : String = """{"id":"123abc","metadata":[],"sentences":[{"numTokens":9,"fields":[{"$type":"ai.lum.odinson.TokensField","name":"raw","tokens":["This","is","a","unit","test","written","in","Scala","."]},{"$type":"ai.lum.odinson.TokensField","name":"word","tokens":["This","is","a","unit","test","written","in","Scala","."]},{"$type":"ai.lum.odinson.TokensField","name":"tag","tokens":["DT","VBZ","DT","NN","NN","VBN","IN","NNP","."]},{"$type":"ai.lum.odinson.TokensField","name":"lemma","tokens":["this","be","a","unit","test","write","in","Scala","."]},{"$type":"ai.lum.odinson.TokensField","name":"entity","tokens":["O","O","O","O","O","O","O","LOCATION","O"]},{"$type":"ai.lum.odinson.TokensField","name":"chunk","tokens":["B-NP","B-VP","B-NP","I-NP","I-NP","B-VP","B-PP","B-NP","O"]},{"$type":"ai.lum.odinson.GraphField","name":"dependencies","edges":[[4,5,"acl"],[4,0,"nsubj"],[5,7,"nmod_in"],[4,1,"cop"],[4,8,"punct"],[4,3,"compound"],[7,6,"case"],[4,2,"det"]],"roots":[4]}]}]}"""

    override def beforeAll( ) : Unit = Odinson.initDynet()

    "Odinson Annotator" should "annotate a document" in {
        val annotator : OdinsonAnnotator = new OdinsonAnnotator( ODINSON_CONF )
        annotator.warmup()

        val docId = "123abc"
        val text = SOURCE_DOC

        annotator.annotateDocument( docId, text ) match {
            case Some( actual ) => {
                OdinsonDocument.fromJson( VALIDATION_DOC ) shouldBe actual
            }
            case None => fail( "this test document should not cause this situation to happen..." )
        }
    }

}
