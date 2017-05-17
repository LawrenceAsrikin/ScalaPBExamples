package example

import addressbook.{AddressBook, Person}
import example.addressbook.Person.{PhoneNumber, PhoneType}

object Main extends App {

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

  println(
    AddressBook.parseFrom(addressBookBinary)
  )
}
