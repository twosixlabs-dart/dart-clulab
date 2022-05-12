import better.files.File
import com.twosixlabs.dart.odinson.OdinsonIndexer
import com.twosixlabs.dart.odinson.config.OdinsonConfig
import com.typesafe.config.ConfigFactory

object Main {

    def main( args : Array[ String ] ) : Unit = {
        val conf = ConfigFactory.load( "local.conf" ).getConfig( "odinson" )
        val config = OdinsonConfig.fromConfig( conf )
        OdinsonIndexer.setupIndexDir( config )

        println( File( "nlp_output/odinson" ).list.toList.size )

    }

}
