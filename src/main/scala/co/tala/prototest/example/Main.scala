package co.tala.prototest.example

import scala.concurrent.Await
import scala.concurrent.duration._

import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.PublishRequest
import co.tala.prototest.example.addressbook.{AddressBook, Person}
import co.tala.prototest.example.addressbook.Person.{PhoneNumber, PhoneType}
import redis.RedisClient

import scala.util.Random

object Main extends App {

  implicit val akkaSystem = akka.actor.ActorSystem()

  publishKeyToSNS(storeAddressBookToRedis(createSampleAddressBook()))

  akkaSystem.terminate()


  def createSampleAddressBook(): AddressBook = {
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

    AddressBook(Seq(p1, p2))
  }

  def storeAddressBookToRedis(addressBook: AddressBook): String = {
    val redis = RedisClient(
      host = "lawrence-test.dabcez.0001.usw2.cache.amazonaws.com",
      port = 6379,
      password = None,
      db = None,
      name = ""
    )

    val addressBookBinary = addressBook.toByteArray
    val redisKey = "person-" + Random.nextInt(1000)
    val redisResult = Await.result(redis.set(redisKey, addressBookBinary), 2.seconds)
    println(s"Storing $redisKey into Redis: $redisResult")
    redisKey
  }

  def publishKeyToSNS(redisKey: String): Unit = {
    val topicArn = "arn:aws:sns:us-west-2:347023968887:lawrence-test"

    val snsClient = new AmazonSNSClient()

    snsClient.setRegion(Region.getRegion(Regions.US_WEST_2))

    val publishRequest = new PublishRequest(topicArn, redisKey)

    val publishResult = snsClient.publish(publishRequest)

    println(s"Publishing $redisKey to SNS: ${publishResult.getMessageId}")
  }
}
