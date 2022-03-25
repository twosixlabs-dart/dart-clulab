import Dependencies._
import sbt._

organization in ThisBuild := "com.twosixlabs.dart"
name := "dart-clulab"
scalaVersion in ThisBuild := "2.12.7"

resolvers in ThisBuild ++= Seq( "Maven Central" at "https://repo1.maven.org/maven2/",
                                "JCenter" at "https://jcenter.bintray.com",
                                ( "Clulab Artifactory" at "http://artifactory.cs.arizona.edu:8081/artifactory/sbt-release" ).withAllowInsecureProtocol( true ),
                                "Local Ivy Repository" at s"file://${System.getProperty( "user.home" )}/.ivy2/local/default" )

val skipPublish = Seq( skip.in( publish ) := true )

lazy val root = ( project in file( "." ) ).settings( skipPublish ).aggregate( odinsonApi )

lazy val odinsonApi = ( project in file( "odinson-api" ) ).settings( libraryDependencies ++= betterFiles
                                                                                             ++ odinson
                                                                                             ++ clulab
                                                                                             ++ dartCommons
                                                                                             ++ logging )

publishMavenStyle := true
sonatypeProfileName := "com.twosixlabs"
inThisBuild(
    List(
        organization := organization.value,
        homepage := Some( url( "https://github.com/twosixlabs-dart/dart-clulab" ) ),
        licenses := List( "Apache License 2.0" -> url( "https://www.apache.org/licenses/LICENSE-2.0.html" ) ),
        developers := List( Developer( "twosixlabs-dart", "Two Six Technologies", "", url( "https://github.com/twosixlabs-dart" ) ) ) ) )

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

test in publish in ThisBuild := {}
test in publishLocal in ThisBuild := {}


javacOptions in ThisBuild ++= Seq( "-source", "8", "-target", "8" )
scalacOptions in ThisBuild += "-target:jvm-1.8"
