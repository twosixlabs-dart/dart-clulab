package com.twosixlabs.dart.odinson

import org.clulab.dynet.Utils.initializeDyNet

object Odinson {
    
    def initDynet( autoBatch : Boolean = false, dynetMem : String = "" ) : Unit = initializeDyNet( autoBatch, dynetMem )

}
