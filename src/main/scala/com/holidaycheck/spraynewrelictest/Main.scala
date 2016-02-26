package com.holidaycheck.spraynewrelictest

import akka.actor.ActorSystem
import spray.http.{HttpRequest, HttpResponse}
import spray.routing.SimpleRoutingApp
import scala.concurrent.{Promise, Future}
import spray.client.pipelining._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")

  startServer(interface = "::", port = 8080) {
    path("without-future") {
      get {
        complete {
          Thread.sleep(1000)
          "waited 1s without future"
        }
      }
    } ~
    path("with-future") {
      get {
        complete {
          Future {
            Thread.sleep(1000)
            "waited 1s with future"
          }
        }
      }
    } ~
    path("with-future-onsuccess") {
      get {
        onSuccess(Future {
          Thread.sleep(1000)
          "waited 1s with future on success"
        }) {
          result => complete(result)
        }
      }
    } ~
    path("with-java-thread") {
      get {
        complete {
          val promise = Promise[String]()
          val thread = new SuccessfulJavaThread(promise)
          thread.start()
          promise.future
        }
      }
    } ~
    path("error-without-future") {
      get {
        complete {
          throw new Exception("Error without future")
        }
      }
    } ~
    path("error-with-future") {
      get {
        complete {
          Future[String] {
            throw new Exception("Error with future")
          }
        }
      }
    } ~
    path("error-with-java-thread") {
      get {
        complete {
          val promise = Promise[String]()
          val thread = new FailingJavaThread(promise)
          thread.start()
          promise.future
        }
      }
    } ~
    path("spray-client-without-future") {
      get {
        complete {
          val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
          pipeline(Get("https://www.holidaycheck.de/")).map(response => {
            "Request without future responds with: " + response.status.defaultMessage
          })
        }
      }
    } ~
    path("spray-client-with-future") {
      get {
        complete {
          Future {
            val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
            pipeline(Get("https://www.holidaycheck.de/")).map(response => {
              "Request with future responds with: " + response.status.defaultMessage
            })
          }
        }
      }
    }
  }
}