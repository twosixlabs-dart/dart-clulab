package com.twosixlabs.dart.odinson.config

import com.typesafe.config.Config

import scala.collection.JavaConverters._


object FieldMappings {
    def fromConfig( config : Config ) : FieldMappings = {
        val rawTokenField : String = config.getString( "rawTokenField" )
        val wordTokenField : String = config.getString( "wordTokenField" )
        val lemmaTokenField : String = config.getString( "lemmaTokenField" )
        val posTagTokenField : String = config.getString( "posTagTokenField" )
        val chunkTokenField : String = config.getString( "chunkTokenField" )
        val entityTokenField : String = config.getString( "entityTokenField" )
        val dependenciesField : String = config.getString( "dependenciesField" )
        val incomingTokenField : String = config.getString( "incomingTokenField" )
        val outgoingTokenField : String = config.getString( "outgoingTokenField" )
        val documentIdField : String = config.getString( "documentIdField" )
        val sentenceIdField : String = config.getString( "sentenceIdField" )
        val sentenceLengthField : String = config.getString( "sentenceLengthField" )
        val displayField : String = config.getString( "displayField" )
        val normalizedTokenFields : String = config.getString( "normalizedTokenField" )
        val storedFields : Seq[ String ] = config.getStringList( "storedFields" ).asScala

        val addToNormalizedField : Seq[ String ] = config.getStringList( "addToNormalizedField" ).asScala

        FieldMappings( rawTokenField = rawTokenField,
                       wordTokenField = wordTokenField,
                       lemmaTokenField = lemmaTokenField,
                       posTagTokenField = posTagTokenField,
                       chunkTokenField = chunkTokenField,
                       entityTokenField = entityTokenField,
                       dependenciesField = dependenciesField,
                       incomingTokenField = incomingTokenField,
                       outgoingTokenField = outgoingTokenField,
                       documentIdField = documentIdField,
                       sentenceIdField = sentenceIdField,
                       sentenceLengthField = sentenceLengthField,
                       displayField = displayField,
                       normalizedTokenField = normalizedTokenFields,
                       storedFields = storedFields,
                       addToNormalizedField = addToNormalizedField )
    }
}

case class FieldMappings( rawTokenField : String,
                          wordTokenField : String,
                          lemmaTokenField : String,
                          posTagTokenField : String,
                          chunkTokenField : String,
                          entityTokenField : String,
                          dependenciesField : String,
                          incomingTokenField : String,
                          outgoingTokenField : String,
                          documentIdField : String,
                          sentenceIdField : String,
                          sentenceLengthField : String,
                          displayField : String,
                          normalizedTokenField : String,
                          storedFields : Seq[ String ],
                          addToNormalizedField : Seq[ String ] )
