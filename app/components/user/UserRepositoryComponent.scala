package components.user

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

/**
  * Created by Sari on 11/15/2015.
  */
trait UserRepositoryComponent {
    val userRepository: UserRepository

    trait UserRepository {

        def all() : List[User]

        def createUser(user: User): User

        def updateUser(user: User) : Option[User]

        def tryFindById(id: Long): Option[User]

        def delete(id: Long)

    }
}

trait UserRepositoryComponentImpl extends UserRepositoryComponent {
    override val userRepository = new UserRepositoryImpl

    class UserRepositoryImpl extends UserRepository {

        val idSequence = new AtomicLong(0)

        val user = {
            get[Option[Long]]("id") ~
              get[String]("email") map {
                case id~email => User(id, email)
            }
        }

        override def all(): List[User] = DB.withConnection { implicit c =>
            SQL("select * from user").as(user *)
        }

        override def createUser(user: User): User = {
            val newId = idSequence.incrementAndGet()
            val createdUser = user.copy(id = Option(newId))

            DB.withConnection { implicit c =>
                SQL("insert into user (id, email) values ({id}, {email})").on(
                    'id -> createdUser.id,
                    'email -> createdUser.email
                ).executeUpdate()
            }

            createdUser
        }

        override def updateUser(newUser: User) : Option[User] = {

            DB.withConnection { implicit c =>
                SQL("update user set email={email} where id={id}").on(
                    'email -> newUser.email,
                    'id -> newUser.id
                ).executeUpdate()
            }

            val result = DB.withConnection { implicit c =>
                SQL("select * from user where id={id}").on(
                    'id -> newUser.id
                ).as(user *)
            }

            result.lift(0)
        }

        override def tryFindById(id: Long): Option[User] = {

            val result = DB.withConnection { implicit c =>
                SQL("select * from user where id={id}").on(
                    'id -> id
                ).as(user *)
            }

            result.lift(0)
        }

        override def delete(id: Long) {

            DB.withConnection { implicit c =>
                SQL("delete from user where id={id}").on(
                    'id -> id
                ).executeUpdate()
            }
        }

    }

}
