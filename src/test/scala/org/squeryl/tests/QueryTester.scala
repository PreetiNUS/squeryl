package org.squeryl.tests

import org.squeryl.Query


trait QueryTester {

  var logQueries = false

  var validateFirstAndExit = -1

  var dumpAst = false

  var doNotExecute = false

  def activateWorkbenchMode = {
    logQueries = true
    dumpAst = true
    validateFirstAndExit = 0
  }


  def log(queryName: Symbol, query:Query[_]) = {

    println(queryName + " :")
    println(query)

    for(r <- query)
      println("-->" + r)
  }

  def assertEquals(expected:Any, actual:Any, msg:String) =
    if(expected != actual)
      error("expected " + expected + " got " + actual)

  def validateQuery[R,S](name: Symbol, q:Query[R], mapFunc: R=>S, expected: List[S]): Unit =
    validateQuery[R,S](logQueries, name, q, mapFunc, expected)

  def validateQuery[R,S](logFirst: Boolean, name: Symbol, q:Query[R], mapFunc: R=>S, expected: List[S]): Unit = {

    if(validateFirstAndExit >= 1)
      return

//    if(dumpAst)
//      println(q.dumpAst)
    
    if(logFirst || logQueries)
      log(name, q)

    if(doNotExecute)
      return

    val r = q.toList.map(mapFunc)

    if(r == expected)
      println("query " + name + " passed.")
    else {
      val msg =
        "query : " + name + " failed,\n" +
        "expected " + expected + " got " + r + " \n query " + name +
        " was : \n" + q
      error(msg)
    }
    
    if(validateFirstAndExit >= 0)
      validateFirstAndExit += 1
  }

  def passed(s: Symbol) = println(s + " passed.")
}
