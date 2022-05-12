import sbt._

object Dependencies {

    val slf4jVersion = "1.7.36"
    val logbackVersion = "1.2.11"
    val betterFilesVersion = "3.9.1"

    val odinsonVersion = "0.6.1"
    val clulabVersion = "8.2.3"

    val dartCommonsVersion = "3.0.30"

    val logging = Seq( "org.slf4j" % "slf4j-api" % slf4jVersion,
                       "ch.qos.logback" % "logback-classic" % logbackVersion )

    val betterFiles = Seq( "com.github.pathikrit" %% "better-files" % betterFilesVersion )

    val odinson = Seq( "ai.lum" %% "odinson-core" % odinsonVersion )

    val clulab = Seq( "org.clulab" %% "processors-main" % clulabVersion,
                      "org.clulab" %% "processors-corenlp" % clulabVersion )

    val dartCommons = Seq( "com.twosixlabs.dart" %% "dart-test-base" % dartCommonsVersion % Test )

}
