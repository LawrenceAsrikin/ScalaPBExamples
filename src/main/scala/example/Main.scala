package example

import scala.concurrent.Await
import scala.concurrent.duration._
import addressbook.{AddressBook, Person}
import example.addressbook.Person.{PhoneNumber, PhoneType}
import redis.RedisClient

import scala.util.Random

object Main extends App {

  implicit val akkaSystem = akka.actor.ActorSystem()

  val redis = RedisClient(
    host = "lawrence-test.dabcez.0001.usw2.cache.amazonaws.com",
    port = 6379,
    password = None,
    db = None,
    name = ""
  )

  val p1 = Person(
    id = 1,
    name = "Bob",
    country = "KE",
    email = Some("bob@gmail.com"),
    phones = Seq(
      PhoneNumber(
        number = "254712345678",
        `type` = Some(PhoneType.MOBILE)
      )
    )
  )

  val p2 = Person(
    id = 2,
    name = "Dan",
    country = "TZ",
    email = None,
    phones = Seq(
      PhoneNumber(
        number = "254712345677",
        `type` = Some(PhoneType.HOME)
      )
    )
  )

  val addressBook = AddressBook(
    Seq(p1, p2)
  )

  val addressBookBinary = addressBook.toByteArray

  val redisKey = "person-" + Random.nextInt(1000)

  val redisResult = Await.result(redis.set(redisKey, addressBookBinary), 2.seconds)

  println(s"Storing $redisKey into Redis: $redisResult")
}
